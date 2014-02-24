package agents.beans;

import java.io.Serializable;
import java.util.List;

import ontology.messages.FacebookData;
import ontology.messages.GetFacebookData;
import ontology.messages.GetNewsData;
import ontology.messages.NewsFeedData;

import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

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

public class TestAgentBean extends AbstractAgentBean{
	
	private IActionDescription sendAction = null;
	private int id = 30;
	private String accessToken = "CAACEdEose0cBABo3zuLqXOwZA5pvBRMiSzJ2AJiDu4vlOmAqYdtgmORtimR9ODaE4N9OBBqXYnZCSQseZAnDPfsZBf0ZAGZACySDY5fx92rQYz2U2g48kUfOz8ZAXPRZCQCoO9ScWFlJBRCnZA4J4I6I4rvmK1yX36nm7dM3D8ug9WZA8CAluOoZAjjKUA7V5GjvqEZD";
	
	@Override
	public void doStart() throws Exception{
		super.doStart();
		log.info("TestAgent started.");
		
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

		List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

		for(IAgentDescription agent : agentDescriptions){
			if(agent.getName().equals("SocialMediaAgent")){

				JiacMessage message =  new JiacMessage(new GetFacebookData(thisAgent.getAgentId(), String.valueOf(agent.getAid()), id, accessToken));

				IMessageBoxAddress receiver = agent.getMessageBoxAddress();

				
				log.info("TestAgent - sending UserID to: " + receiver);
				invoke(sendAction, new Serializable[] {message, receiver});
			}
			
			if(agent.getName().equals("InformationAgent")){
				
				JiacMessage message =  new JiacMessage(new GetNewsData(thisAgent.getAgentId(), String.valueOf(agent.getAid())));

				IMessageBoxAddress receiver = agent.getMessageBoxAddress();

				
				log.info("TestAgent - sending UserID to: " + receiver);
				invoke(sendAction, new Serializable[] {message, receiver});
			}
		}
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
					 
					if(obj instanceof FacebookData){
						try {

							log.info("Success: " +  ((FacebookData) obj).getMe().getName());
							log.info("Success: " +  ((FacebookData) obj).getPicture());
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					if(obj instanceof NewsFeedData){
						try {

							log.info(((NewsFeedData) obj).getNewsFeed().get(0).getEnclosure());
							log.info(((NewsFeedData) obj).getNewsFeed().get(1).getEnclosure());
							log.info(((NewsFeedData) obj).getNewsFeed().get(2).getEnclosure());
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}
			}
			
		}
		
	}

}
