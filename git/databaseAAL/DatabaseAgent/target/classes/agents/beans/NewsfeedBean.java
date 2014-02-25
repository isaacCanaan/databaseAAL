package agents.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import newsfeeds.FetchRSSFeed;
import ontology.Message;
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

public class NewsfeedBean extends AbstractCommunicatingBean{
	
	private IActionDescription sendAction = null;
	
	@Override
	public void doStart() throws Exception{
		super.doStart();
		
		sendAction = retrieveAction(ICommunicationBean.ACTION_SEND);
		log.info("InformationAgent started.");
	}
	
	@Override
	public void execute(){
		
	}

	@Override
	protected void receiveMessage(Message message) {
		ArrayList<NewsFeedData.NewsFeedMessage> news = null;
		
		if(message instanceof GetNewsData){
			
			log.info("NewsfeedAgent - message received");
							
			try {
				news = new FetchRSSFeed().getRSSFeedWeltDE();
				
				List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

				for(IAgentDescription agent : agentDescriptions){
					if(agent.getName().equals("InformationAgent")){

						log.info("Found InformationAgent");
						IMessageBoxAddress receiver = agent.getMessageBoxAddress();
						
						JiacMessage newMessage = new JiacMessage(new NewsFeedData(thisAgent.getAgentId(), agent.getAid(), news));

						invoke(sendAction, new Serializable[] {newMessage, receiver});
						log.info("Newsfeed sent");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}
