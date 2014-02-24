package agents.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import newsfeeds.FetchRSSFeed;
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

public class NewsfeedBean extends AbstractAgentBean{
	
	private IActionDescription sendAction = null;
	
	@Override
	public void doStart() throws Exception{
		super.doStart();
		log.info("InformationAgent started.");
		
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
				
				ArrayList<NewsFeedData.NewsFeedMessage> news = null;
				
				if(obj instanceof GetNewsData){
					
					log.info("NewsfeedAgent - message received");
									
					try {
						news = new FetchRSSFeed().getRSSFeedWeltDE();
						
						List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

						for(IAgentDescription agent : agentDescriptions){
							if(agent.getName().equals("InformationAgent")){

								IMessageBoxAddress receiver = agent.getMessageBoxAddress();
								
								JiacMessage newMessage = new JiacMessage(new NewsFeedData(thisAgent.getAgentId(), agent.getAid(), news));

								invoke(sendAction, new Serializable[] {newMessage, receiver});
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					memory.remove(wce.getObject());
					log.info("Newsfeed sent");
				}
			}
		}
	}

}
