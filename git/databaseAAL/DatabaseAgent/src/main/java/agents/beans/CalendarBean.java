package agents.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import mails.MailReceiver;
import mails.eMailAcc;
import objects.GmailData;
import ontology.messages.CalendarData;
import ontology.messages.GetCalendarData;
import ontology.messages.SaveCalendarData;
import ontology.messages.SaveGmailData;

import org.apache.commons.codec.binary.Base64;
import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import access.MySQLAccess;
import access.UserAccess;
import calendar.GoogleCalendarFetcher;
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

public class CalendarBean extends AbstractAgentBean{
	
	private IActionDescription sendAction = null;
	private MySQLAccess access;
	private Connection connect;
	private UserAccess userAccess;
	
	@Override
	public void doStart() throws Exception{
		super.doStart();
		log.info("InformationAgent started.");
		
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
			
			ArrayList<CalendarData.Entry> entries = null;
			
			if(event instanceof WriteCallEvent<?>){
				WriteCallEvent<IJiacMessage> wce = (WriteCallEvent<IJiacMessage>) event;
				
				IJiacMessage message = memory.read(wce.getObject());
				IFact obj = message.getPayload();
				
				if(obj instanceof SaveGmailData){
					
					log.info("CalendarAgent - Save message received");
					try {
						userAccess.saveGmailData(((SaveGmailData) obj).getUserID(), ((SaveGmailData) obj).getUser(), ((SaveGmailData) obj).getPassword());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					memory.remove(wce.getObject());
				}
				
				if(obj instanceof GetCalendarData){
						
					log.info("CalendarAgent - Get message received");
					
					GmailData data = null;
					
					try {
						data = userAccess.getGmailData(((GetCalendarData) obj).getUserID());
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					String mail = data.getMail();
					String pw = data.getPassword();
					Base64 decoder = new Base64();
					byte[] test = decoder.decodeBase64(pw);

					String password = new String(test);
					
					
					try {
						
						GoogleCalendarFetcher fetch = new GoogleCalendarFetcher(mail, password);
						entries = fetch.getEventEntries();
						
						List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

						for(IAgentDescription agent : agentDescriptions){
							if(agent.getName().equals("CommunicationAgent")){

								IMessageBoxAddress receiver = agent.getMessageBoxAddress();
								
								JiacMessage newMessage = new JiacMessage(new CalendarData(thisAgent.getAgentId(), agent.getAid(), ((GetCalendarData) obj).getUserID(), entries));

								invoke(sendAction, new Serializable[] {newMessage, receiver});
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					
					memory.remove(wce.getObject());
				}

			}
			
		}
		
	}

}
