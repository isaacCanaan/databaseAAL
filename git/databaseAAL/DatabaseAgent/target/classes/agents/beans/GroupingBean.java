package agents.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import ontology.Message;
import ontology.messages.GetGroupData;
import ontology.messages.GroupData;
import access.MySQLAccess;
import access.SocialAccess;
import de.dailab.jiactng.agentcore.comm.ICommunicationBean;
import de.dailab.jiactng.agentcore.comm.IMessageBoxAddress;
import de.dailab.jiactng.agentcore.comm.message.JiacMessage;
import de.dailab.jiactng.agentcore.ontology.AgentDescription;
import de.dailab.jiactng.agentcore.ontology.IActionDescription;
import de.dailab.jiactng.agentcore.ontology.IAgentDescription;

public class GroupingBean extends AbstractCommunicatingBean{
	
	private IActionDescription sendAction = null;
	
	private Connection connect = null;
	private MySQLAccess access = null;
	private SocialAccess sAccess = null;
	
	@Override
	public void doStart() throws Exception{
		super.doStart();
		access = new MySQLAccess();
		connect = access.connectDriver();
		sAccess = new SocialAccess(connect);
		
		sendAction = retrieveAction(ICommunicationBean.ACTION_SEND);	
	}

	// method to handle the received message
	@Override
	protected void receiveMessage(Message message) {
		
		if(message instanceof GetGroupData){
			
			log.info("GroupAgent - Get message received");
			
			// retrieve the list of ids of users from the message
			ArrayList<Integer> members = ((GetGroupData) message).getMembers();
							
			try {
				// get the common groups of the users
				ArrayList<GroupData.FBGroup> common = sAccess.getGroup(members);
				
				// find the agent that shall receive the list of groups
				List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

				for(IAgentDescription agent : agentDescriptions){
					if(agent.getName().equals("SocialMediaAgent")){

						IMessageBoxAddress receiver = agent.getMessageBoxAddress();
						
						GroupData groupData = new GroupData(thisAgent.getAgentId(), agent.getAid(), common);
						JiacMessage newMessage = new JiacMessage(groupData);

						invoke(sendAction, new Serializable[] {newMessage, receiver});
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			log.info("Groups sent");
		}
	}
}
