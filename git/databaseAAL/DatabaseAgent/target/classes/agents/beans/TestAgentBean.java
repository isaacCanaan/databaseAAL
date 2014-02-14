package agents.beans;

import java.io.Serializable;
import java.util.List;

import messages.FBData;
import messages.LinkedInData;
import access.MySQLAccess;
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
	private long id = 30;
	private String accessToken = "CAACEdEose0cBAKfZBadiovURZBG218avCAxiep4xjbYJrX5gTJ5fGQfhTpo9R7Ibmmceg3hZBcFm6fHV8m3le2lbffYGZB7FA4gx3cy8xpGT8lxe81eEf8MzkrHRVpwwMufz5vFtoBeOL9o4zNXjbStdg9vfShYdtbJy6FXHUyoKUZCJZC8YEUZAVzbWhlETRQZD";
	
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
	}
	
	@Override
	public void execute(){

		List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

		for(IAgentDescription agent : agentDescriptions){
			if(agent.getName().equals("SocialMediaAgent")){

				JiacMessage message =  new JiacMessage(new FBData(id, accessToken));
//				JiacMessage message =  new JiacMessage(new LinkedInData(accessTokenValue, tokenSecretValue));
				IMessageBoxAddress receiver = agent.getMessageBoxAddress();

				
				log.info("TestAgent - sending UserID to: " + receiver);
				invoke(sendAction, new Serializable[] {message, receiver});
			}
		}
	}

}
