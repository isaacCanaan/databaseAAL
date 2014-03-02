package agents.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mails.MailReceiver;
import mails.eMailAcc;

import org.apache.commons.codec.binary.Base64;

import objects.GmailData;
import ontology.Message;
import ontology.messages.GetMailData;
import ontology.messages.MailData;
import access.MySQLAccess;
import access.SocialAccess;
import de.dailab.jiactng.agentcore.AbstractAgentBean;
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

	@Override
	protected void receiveMessage(Message message) {
		if(message instanceof GetMailData){
			
			log.info("GroupAgent - Get message received");
							
			try {
				
				List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

				for(IAgentDescription agent : agentDescriptions){
					if(agent.getName().equals("SocialMediaAgent")){

						IMessageBoxAddress receiver = agent.getMessageBoxAddress();
						
						JiacMessage newMessage = new JiacMessage();

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
