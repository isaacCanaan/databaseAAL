package agents.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import objects.LinkedInUser;
import ontology.Message;
import ontology.messages.LinkedInData;

import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.schema.Connections;
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

public class LinkedInBean extends AbstractCommunicatingBean{
	
	private final String consumerKeyValue = "77ja7axi3e2bp5";
	private final String consumerSecretValue = "7l6kDAp2pb6eU7fK";
	private String accessTokenValue;
	private String tokenSecretValue;
	
	private IActionDescription sendAction = null;
	
	private MySQLAccess access = null;
	private UserAccess userAccess = null;
	private Connection connect = null;
	
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
		
		sendAction = retrieveAction(ICommunicationBean.ACTION_SEND);
	}

	@Override
	protected void receiveMessage(Message message) {
		if(message != null){
			
			if(message instanceof GetLinkedInData){
				
				try {
					client = factory.createLinkedInApiClient(message.getAccessToken(), ((LinkedInData) message).getTokenSecret());
					Person profile = client.getProfileForCurrentUser();
					log.info("Name:" + profile.getFirstName() + " " + profile.getLastName());
					log.info("Headline:" + profile.getHeadline());
					log.info("Summary:" + profile.getSummary());
					log.info("Industry:" + profile.getIndustry());
					log.info("Picture:" + profile.getPictureUrl());
					
					Connections connections = client.getConnectionsForCurrentUser();
					log.info("Total connections fetched:" + connections.getTotal());
					for (Person person : connections.getPersonList()) {
					       log.info(person.getId() + ":" + person.getFirstName() + " " + person.getLastName() + ":" + person.getHeadline());
					}	
					
					LinkedInUser user = new LinkedInUser();
					
					List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

					for(IAgentDescription agent : agentDescriptions){
						if(agent.getName().equals("LinkedInAgent")){

							IMessageBoxAddress receiver = agent.getMessageBoxAddress();
							
							LinkedInData data = new LinkedInData(message.getID(), thisAgent.getAgentId(), agent.getAid());
							data.setMe(user);
							JiacMessage newMessage = new JiacMessage(data);

							invoke(sendAction, new Serializable[] {newMessage, receiver});
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
