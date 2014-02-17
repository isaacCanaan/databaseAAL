package agents.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import objects.LinkedInUser;
import ontology.messages.LinkedInData;

import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.schema.Person;

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

public class LinkedInBean extends AbstractAgentBean{
	
	private final String consumerKeyValue = "77ja7axi3e2bp5";
	private final String consumerSecretValue = "7l6kDAp2pb6eU7fK";
	private String accessTokenValue;
	private String tokenSecretValue;
	
	private IActionDescription sendAction = null;
	
	private MySQLAccess access = null;
	private UserAccess userAccess = null;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	private LinkedInApiClientFactory factory;
	private LinkedInApiClient client;
	
	@Override
	public void doStart() throws Exception{
		super.doStart();
		log.info("LinkedInAgent started.");
		
		access = new MySQLAccess();
		connect = access.connectDriver();
		userAccess = new UserAccess(connect);
		
		factory = LinkedInApiClientFactory.newInstance(consumerKeyValue, consumerSecretValue);
		
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
	
	private class MessageObserver implements SpaceObserver<IFact>{
		
		

		/**
		 * 
		 */
		private static final long serialVersionUID = -8182513339144469591L;

		@Override
		public void notify(SpaceEvent<? extends IFact> event) {
			if(event instanceof WriteCallEvent<?>){
				WriteCallEvent<IJiacMessage> wce = (WriteCallEvent<IJiacMessage>) event;
				
				IJiacMessage message = memory.remove(wce.getObject());
				
				if(message != null){
					IFact obj = message.getPayload();
					
					if(obj instanceof LinkedInData){
						
						try {
							client = factory.createLinkedInApiClient(obj.getAccessToken(), ((LinkedInData) obj).getTokenSecret());
							Person profile = client.getProfileForCurrentUser();
							log.info("Name:" + profile.getFirstName() + " " + profile.getLastName());
							log.info("Headline:" + profile.getHeadline());
							log.info("Summary:" + profile.getSummary());
							log.info("Industry:" + profile.getIndustry());
							log.info("Picture:" + profile.getPictureUrl());
							
							LinkedInUser user = new LinkedInUser();
							
							List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

							for(IAgentDescription agent : agentDescriptions){
								if(agent.getName().equals("LinkedInAgent")){

									IMessageBoxAddress receiver = agent.getMessageBoxAddress();
									
									LinkedInData data = new LinkedInData(obj.getID(), thisAgent.getAgentId(), agent.getAid());
									data.setMe(user);
									JiacMessage newMessage = new JiacMessage(data);

									invoke(sendAction, new Serializable[] {newMessage, receiver});
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					else{
						memory.write(wce.getObject());
					}
				}
				
			}
			
		}
		
	}

}
