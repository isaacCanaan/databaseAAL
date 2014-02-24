package agents.beans;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import ontology.TransportFrame;
import ontology.messages.FBMessage;
import ontology.messages.FindUserIdMessage;
import ontology.messages.ResultQrIdMessage;
import ontology.messages.SaveMessage;

import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import access.MySQLAccess;
import access.RecognitionAccess;
import access.UserAccess;
import de.dailab.jiactng.agentcore.AbstractAgentBean;
import de.dailab.jiactng.agentcore.action.Action;
import de.dailab.jiactng.agentcore.comm.ICommunicationBean;
import static de.dailab.jiactng.agentcore.comm.ICommunicationBean.ACTION_JOIN_GROUP;
import static de.dailab.jiactng.agentcore.comm.CommunicationAddressFactory.*;
import de.dailab.jiactng.agentcore.comm.IGroupAddress;
import de.dailab.jiactng.agentcore.comm.IMessageBoxAddress;
import de.dailab.jiactng.agentcore.comm.message.IJiacMessage;
import de.dailab.jiactng.agentcore.comm.message.JiacMessage;
import de.dailab.jiactng.agentcore.knowledge.IFact;
import de.dailab.jiactng.agentcore.ontology.AgentDescription;
import de.dailab.jiactng.agentcore.ontology.IActionDescription;
import de.dailab.jiactng.agentcore.ontology.IAgentDescription;
import de.dailab.jiactng.agentcore.action.AbstractMethodExposingBean;

public class RecognitionBean extends AbstractMethodExposingBean{

	public static final String FACEDATAGROUPNAME = "FaceDataGroup";
	private IGroupAddress groupAddress;
	
	private IActionDescription sendAction = null;
	private Action join;
	
	private MySQLAccess access = null;
	private RecognitionAccess recAccess = null;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	@Override
	public void doInit() throws Exception{
		super.doInit();
		groupAddress = createGroupAddress(FACEDATAGROUPNAME);
	}
	
	@Override
	public void doStart() throws Exception{
		super.doStart();
		
		access = new MySQLAccess();
		connect = access.connectDriver();
		recAccess = new RecognitionAccess(connect);
		
		IActionDescription template = new Action(ICommunicationBean.ACTION_SEND);
		IActionDescription sendAction = memory.read(template);
		
		join = retrieveAction(ACTION_JOIN_GROUP);
		
		if(sendAction == null){
			sendAction = thisAgent.searchAction(template);
		}
		
		if(sendAction == null){
			throw new RuntimeException("Send action not found.");
		}
		
		invoke(join, new Serializable[] {groupAddress}, this);
		
		memory.attach(new MessageObserver(), new JiacMessage());
	}
	
	private class MessageObserver implements SpaceObserver<IFact>{
		
		

		/**
		 * 
		 */
		private static final long serialVersionUID = -8182513339144469591L;

		@Override
		public void notify(SpaceEvent<? extends IFact> event) {
			if(event instanceof WriteCallEvent<?>){
				WriteCallEvent<IJiacMessage> wce = (WriteCallEvent<IJiacMessage>) event;
				
				log.info("RecognitionAgent - message received");
				
				IJiacMessage message = memory.remove(wce.getObject());
				IFact obj = message.getPayload();
				
				if(obj instanceof SaveMessage){
					
					try {
						
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						
						int id = ((SaveMessage) obj).getId();
						String qrString = ((SaveMessage) obj).getQrString();
						LinkedList<TransportFrame> datas = ((SaveMessage) obj).getTrainingData();
						
						for(TransportFrame tFrame : datas){
							
							ImageIO.write(tFrame.getFrame(), "png", baos);
							recAccess.saveRecData(id, qrString, baos.toByteArray());
							baos.flush();
						}
						
						baos.close();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
//				
//				if(obj instanceof FindUserIdMessage){
//					
//					int id = 0;
//					try {
//						id = recAccess.findUserId(((FindUserIdMessage) obj).getQrString());
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//					
//					ResultQrIdMessage idResult = new ResultQrIdMessage(thisAgent.getAgentId(), null, id);
//
//					JiacMessage newMessage = new JiacMessage(idResult);
//
//					invoke(sendAction, new Serializable[] {newMessage, groupAddress});
//				}

			}
			
		}
		
	}
}
