package agents.beans;

import java.io.Serializable;
import java.util.List;

import ontology.messages.GetFacebookData;

import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import de.dailab.jiactng.agentcore.AbstractAgentBean;
import de.dailab.jiactng.agentcore.action.Action;
import de.dailab.jiactng.agentcore.comm.ICommunicationBean;
import de.dailab.jiactng.agentcore.comm.IMessageBoxAddress;
import de.dailab.jiactng.agentcore.comm.message.JiacMessage;
import de.dailab.jiactng.agentcore.ontology.AgentDescription;
import de.dailab.jiactng.agentcore.ontology.IActionDescription;
import de.dailab.jiactng.agentcore.ontology.IAgentDescription;

public class TestAgentBean extends AbstractAgentBean{
	
	private IActionDescription sendAction = null;
	private int id = 30;
	private String accessToken = "CAACEdEose0cBADDH8beNaES7WziXrcRZAURnhuaZBmUQ2I0a0Jkk76U65UtK2RsA7G2nnnT8fqP1zPKfkx0LDiBGfTVvnUHv2dtBo02QpuS0cJwoh9v8cgE8INvM33szrLINTsZCgrC3lqVZA31bcxxZBV4nYTpGQZBuObdvQPZCWFUq7kzvPxL7zKgHAvSzpIZD";
	
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
		
//		memory.attach(new MessageObserver(), new JiacMessage());
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
				
				JiacMessage message =  new JiacMessage();

				IMessageBoxAddress receiver = agent.getMessageBoxAddress();

				
				log.info("TestAgent - sending UserID to: " + receiver);
				invoke(sendAction, new Serializable[] {message, receiver});
			}
		}
	}
	
//	private class MessageObserver implements SpaceObserver<IFact>{
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
//				log.info("FacebookAgent - message received");
//				
//				IJiacMessage message = memory.remove(wce.getObject());
//
//				
//				if(message != null){
//					IFact obj = message.getPayload();
//					 
//					if(obj instanceof FBMessage){
//						try {
//							
//							log.info("Success: " +  ((FBMessage) obj).getfbUser().getMe().getName());
//
//							
//							
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//					else{
//						memory.write(wce.getObject());
//					}
//					
//				}
//			}
//			
//		}
//		
//	}

}
