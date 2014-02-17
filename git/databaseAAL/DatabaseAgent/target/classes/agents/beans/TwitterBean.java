package agents.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import objects.LinkedInUser;
import ontology.messages.LinkedInData;
import ontology.messages.TwitterData;

import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import twitter4j.IDs;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

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

public class TwitterBean extends AbstractAgentBean{
	
	private final String consumerKeyValue = "6edjES4nEmLuDMCoRtvkw";
	private final String consumerSecretValue = "V2N8REdkCrDS0mr2msI3HJl4eRAgATM0K6BBUIdt33Y";
	private String accessToken = "395471530-VA2crkvu4pXsVlCkioDPrjzTQYrwSO870Vy5ny1j";
	private String tokenSecret = "d27UcbqfK5xNmetRFRoSG0YodpGqVIorcLfmZndPCPQgx";
	
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
		log.info("TwitterAgent started.");
		
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
		
//		memory.attach(new MessageObserver(), new JiacMessage());
	}
	
	@Override
	public void execute(){
		ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(true)
	      .setOAuthConsumerKey(consumerKeyValue)
	      .setOAuthConsumerSecret(consumerSecretValue)
	      .setOAuthAccessToken(accessToken)
	      .setOAuthAccessTokenSecret(tokenSecret);
	    TwitterFactory tf = new TwitterFactory(cb.build());
	    Twitter twitter = tf.getInstance();
	
	 try {
		 
		 User user = twitter.showUser(twitter.getId());
		 log.info(user);
//		List<Status> statuses = twitter.getHomeTimeline();
//		IDs friendsList = twitter.getFriendsIDs(20);
//		
//		
//		for(Status s : statuses){
//			log.info(s.getUser().getName() + ": " + s.getText());
//		}
	} catch (TwitterException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
//	private class MessageObserver implements SpaceObserver<IFact>{
//		
//		
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -8182513339144469591L;
//
//		@Override
//		public void notify(SpaceEvent<? extends IFact> event) {
//			if(event instanceof WriteCallEvent<?>){
//				WriteCallEvent<IJiacMessage> wce = (WriteCallEvent<IJiacMessage>) event;
//				
//				IJiacMessage message = memory.remove(wce.getObject());
//				
//				if(message != null){
//					IFact obj = message.getPayload();
//					
//					if(obj instanceof GetTwitterData){
//						
//						try {
//							
//							  ConfigurationBuilder cb = new ConfigurationBuilder();
//							    cb.setDebugEnabled(true)
//							      .setOAuthConsumerKey(consumerKeyValue)
//							      .setOAuthConsumerSecret(consumerSecretValue)
//							      .setOAuthAccessToken(obj.getAccessToken())
//							      .setOAuthAccessTokenSecret(obj.getTokenSecret());
//							    TwitterFactory tf = new TwitterFactory(cb.build());
//							    Twitter twitter = tf.getInstance();
//							
//							 List<Status> statuses = twitter.getHomeTimeline();
//					    	    IDs friendsList = twitter.getFriendsIDs(20);
//							
//							List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());
//
//							for(IAgentDescription agent : agentDescriptions){
//								if(agent.getName().equals("LinkedInAgent")){
//
//									IMessageBoxAddress receiver = agent.getMessageBoxAddress();
//									
//									TwitterData data = new TwitterData(obj.getID(), thisAgent.getAgentId(), agent.getAid());
//									JiacMessage newMessage = new JiacMessage(data);
//
//									invoke(sendAction, new Serializable[] {newMessage, receiver});
//								}
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//					else{
//						memory.write(wce.getObject());
//					}
//				}
//				
//			}
//			
//		}
//		
//	}

}
