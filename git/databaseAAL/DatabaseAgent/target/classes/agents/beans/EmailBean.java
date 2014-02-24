package agents.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mails.MailReceiver;
import mails.eMailAcc;
import objects.GmailData;
import ontology.messages.GetCalendarData;
import ontology.messages.GetMailData;
import ontology.messages.MailData;

import org.apache.commons.codec.binary.Base64;
import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import access.MySQLAccess;
import access.UserAccess;
import de.dailab.jiactng.agentcore.AbstractAgentBean;
import de.dailab.jiactng.agentcore.action.Action;
import de.dailab.jiactng.agentcore.comm.ICommunicationBean;
import de.dailab.jiactng.agentcore.comm.IMessageBoxAddress;
import de.dailab.jiactng.agentcore.comm.message.IJiacMessage;
import de.dailab.jiactng.agentcore.comm.message.JiacMessage;
import de.dailab.jiactng.agentcore.knowledge.IFact;
import de.dailab.jiactng.agentcore.ontology.AgentDescription;
import de.dailab.jiactng.agentcore.ontology.IActionDescription;
import de.dailab.jiactng.agentcore.ontology.IAgentDescription;

public class EmailBean extends AbstractAgentBean{
	
	private IActionDescription sendAction = null;
	private MySQLAccess access;
	private Connection connect;
	private UserAccess userAccess;
	
	@Override
	public void doStart() throws Exception{
		super.doStart();
		log.info("CommunicationAgent started.");
		
		access = new MySQLAccess();
		connect = access.connectDriver();
		userAccess = new UserAccess(connect);
		
		IActionDescription template = new Action(ICommunicationBean.ACTION_SEND);
		sendAction = memory.read(template);

		if(sendAction == null){
			sendAction = thisAgent.searchAction(template);
		}
		
		if(sendAction == null){
			throw new RuntimeException("Send action not found.");
		}
		
		memory.attach(new MessageObserver(), new JiacMessage());
	}
	
	@Override
	public void execute(){
		
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
				
				IJiacMessage message = memory.read(wce.getObject());
				IFact obj = message.getPayload();
				
				ArrayList<eMailAcc> accs = new ArrayList<eMailAcc>();
				ArrayList<MailData.Mail> mails = null;

				if(obj instanceof GetMailData){
					
					log.info("MailAgent - Get message received");
					
					GmailData data = null;
					
					try {
						data = userAccess.getGmailData(((GetMailData) obj).getUserID());
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					String mail = data.getMail();
					String pw = data.getPassword();
					Base64 decoder = new Base64();
					byte[] test = decoder.decodeBase64(pw);

					String password = new String(test);
									
					try {
						eMailAcc acc = new eMailAcc(mail, password);
						accs.add(acc);
						MailReceiver mrec = new MailReceiver(accs);
						mails = mrec.receiveMails();
						
						List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

						for(IAgentDescription agent : agentDescriptions){
							if(agent.getName().equals("CommunicationAgent")){

								IMessageBoxAddress receiver = agent.getMessageBoxAddress();
								
								JiacMessage newMessage = new JiacMessage(new MailData(thisAgent.getAgentId(), agent.getAid(), ((GetMailData) obj).getUserID(), mails));

								invoke(sendAction, new Serializable[] {newMessage, receiver});
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					memory.remove(wce.getObject());
					log.info("Mails sent");
						
				}

				
			}
			
		}
		
	}

}
