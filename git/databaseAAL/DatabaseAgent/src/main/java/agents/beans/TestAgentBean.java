package agents.beans;

import java.io.Serializable;
import java.util.List;

import ontology.messages.FBMessage;
import ontology.messages.FacebookData;
import ontology.messages.GetFacebookData;
import ontology.messages.LinkedInData;

import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import access.MySQLAccess;
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
	private String accessToken = "CAACEdEose0cBAEtSJJYlAMSYPvDoM3bMDvAZCShCREz6xlJfr3rGmzBnCQwboEQwQEUdeGFRas9pGK8ZB5DrRLlJQcCE3SLZAJoPdRPgJySQDyeXsQwHzuUrLKk1zc1Ojiq5utQigZBorErpD2LBZA5wt5oHHJiZAYKA3sCDr03zAFlkPyZCSZBbxW1nfZAUDLB4ZD";
	
	private String accessTokenValue = "9f511c7f-c2a8-446f-bde8-99f6d16275ac";
	private String tokenSecretValue = "f9519a5a-902d-4ac6-88f6-e50eb0563c4b";
	
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
//				JiacMessage message =  new JiacMessage(new LinkedInData(accessTokenValue, tokenSecretValue));
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
				
				log.info("FacebookAgent - message received");
				
				IJiacMessage message = memory.remove(wce.getObject());

				
				if(message != null){
					IFact obj = message.getPayload();
					 
					if(obj instanceof FBMessage){
						try {
							
							log.info("Success: " +  ((FBMessage) obj).getfbUser().getMe().getName());

							
							
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
