package agents.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import jiac.Settings;
import ontology.Message;
import ontology.messages.GetUserKeys;
import ontology.messages.GetUserPreferences;
import ontology.messages.ResultQrIdMessage;
import ontology.messages.SaveMessage;
import ontology.messages.UpdateUserKeys;
import ontology.messages.UpdateUserPreferences;
import ontology.messages.UserKeysData;
import ontology.messages.UserPreferencesData;

import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import access.MySQLAccess;
import access.RecognitionAccess;
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

public class ProfileBean extends AbstractCommunicatingBean{
	
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
		
		access = new MySQLAccess();
		connect = access.connectDriver();
		userAccess = new UserAccess(connect);
		
		sendAction = retrieveAction(ICommunicationBean.ACTION_SEND);
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
				
				if(obj instanceof GetUserKeys){
					
					int id = ((GetUserKeys) obj).getUserID();
					
					List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

					for(IAgentDescription agent : agentDescriptions){
						if(agent.getName().equals("FacebookAgent")){

							IMessageBoxAddress receiver = agent.getMessageBoxAddress();
							
							UserKeysData keys = null;
							
							try {
								keys = new UserKeysData(thisAgent.getAgentId(), agent.getAid(), id, userAccess.getKeys(id));
							} catch (SQLException e) {
								
								e.printStackTrace();
							}

							JiacMessage newMessage = new JiacMessage(keys);

							invoke(sendAction, new Serializable[] {newMessage, receiver});
						}
					}
					
				}
				
				if(obj instanceof UpdateUserKeys){
					
					int id = ((UpdateUserKeys) obj).getUserID();
					HashMap<String,String> keys = ((UpdateUserKeys) obj).getKeys();
					
					for(Entry<String, String> e : keys.entrySet()){
						
						try {
							userAccess.saveKey(id, e.getKey(), e.getValue());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					
				}
				
				if(obj instanceof GetUserPreferences){
					
					int id = ((GetUserPreferences) obj).getUserID();
					
					List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

					for(IAgentDescription agent : agentDescriptions){
						if(agent.getName().equals("FacebookAgent")){

							IMessageBoxAddress receiver = agent.getMessageBoxAddress();
							
							UserPreferencesData prefs = null;
							
							try {
								prefs = new UserPreferencesData(thisAgent.getAgentId(), agent.getAid(), id, userAccess.getPreferences(id));
							} catch (SQLException e) {
								
								e.printStackTrace();
							}

							JiacMessage newMessage = new JiacMessage(prefs);

							invoke(sendAction, new Serializable[] {newMessage, receiver});
						}
					}
					
				}
				
				if(obj instanceof UpdateUserPreferences){
					
					int id = ((UpdateUserPreferences) obj).getUserID();
					Settings sets = ((UpdateUserPreferences) obj).getPreferences();
						
						try {
							userAccess.savePreferences(id, sets);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
				}
			}
		}
	}


	@Override
	protected void receiveMessage(Message message) {
		
		if(message instanceof GetUserKeys){
			
			int id = ((GetUserKeys) message).getUserID();
			
			List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

			for(IAgentDescription agent : agentDescriptions){
				if(agent.getName().equals("FacebookAgent")){

					IMessageBoxAddress receiver = agent.getMessageBoxAddress();
					
					UserKeysData keys = null;
					
					try {
						keys = new UserKeysData(thisAgent.getAgentId(), agent.getAid(), id, userAccess.getKeys(id));
					} catch (SQLException e) {
						
						e.printStackTrace();
					}

					JiacMessage newMessage = new JiacMessage(keys);

					invoke(sendAction, new Serializable[] {newMessage, receiver});
				}
			}
			
		}
		
		if(message instanceof UpdateUserKeys){
			
			int id = ((UpdateUserKeys) message).getUserID();
			HashMap<String,String> keys = ((UpdateUserKeys) message).getKeys();
			
			for(Entry<String, String> e : keys.entrySet()){
				
				try {
					userAccess.saveKey(id, e.getKey(), e.getValue());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
		}
		
		if(message instanceof GetUserPreferences){
			
			int id = ((GetUserPreferences) message).getUserID();
			
			List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

			for(IAgentDescription agent : agentDescriptions){
				if(agent.getName().equals("FacebookAgent")){

					IMessageBoxAddress receiver = agent.getMessageBoxAddress();
					
					UserPreferencesData prefs = null;
					
					try {
						prefs = new UserPreferencesData(thisAgent.getAgentId(), agent.getAid(), id, userAccess.getPreferences(id));
					} catch (SQLException e) {
						
						e.printStackTrace();
					}

					JiacMessage newMessage = new JiacMessage(prefs);

					invoke(sendAction, new Serializable[] {newMessage, receiver});
				}
			}
			
		}
		
		if(message instanceof UpdateUserPreferences){
			
			int id = ((UpdateUserPreferences) message).getUserID();
			Settings sets = ((UpdateUserPreferences) message).getPreferences();
				
				try {
					userAccess.savePreferences(id, sets);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		}
		
	}

}
