package agents.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import ontology.messages.FBData;
import ontology.messages.FBMessage;

import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import access.MySQLAccess;
import access.UserAccess;
import de.dailab.jiactng.agentcore.AbstractAgentBean;
import de.dailab.jiactng.agentcore.action.Action;
import de.dailab.jiactng.agentcore.comm.ICommunicationBean;
import de.dailab.jiactng.agentcore.comm.message.IJiacMessage;
import de.dailab.jiactng.agentcore.comm.message.JiacMessage;
import de.dailab.jiactng.agentcore.knowledge.IFact;
import de.dailab.jiactng.agentcore.ontology.IActionDescription;

public class UserRegisterBean extends AbstractAgentBean{

	private IActionDescription sendAction = null;
	
	private MySQLAccess access = null;
	private UserAccess userAccess = null;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	@Override
	public void doStart() throws Exception{
		super.doStart();
		log.info("RegistzerandLoginAgent started.");
		
		access = new MySQLAccess();
		connect = access.connectDriver();
		userAccess = new UserAccess(connect);
		
		IActionDescription template = new Action(ICommunicationBean.ACTION_SEND);
		IActionDescription sendAction = memory.read(template);
		
		if(sendAction == null){
			sendAction = thisAgent.searchAction(template);
		}
		
		if(sendAction == null){
			throw new RuntimeException("Send action not found.");
		}
		
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
				
				log.info("RegisterandLoginAgent - message received");
				
				IJiacMessage message = memory.remove(wce.getObject());
				IFact obj = message.getPayload();
				
				FBMessage newMessage = new FBMessage(); 
				
				if(obj instanceof ){
					
					try {
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
			
		}
		
	}
}
