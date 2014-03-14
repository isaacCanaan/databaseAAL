package agents.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import ontology.Message;
import ontology.messages.FacebookData;
import ontology.messages.GetFacebookData;

import access.SocialAccess;
import access.MySQLAccess;
import de.dailab.jiactng.agentcore.comm.ICommunicationBean;
import de.dailab.jiactng.agentcore.comm.IMessageBoxAddress;
import de.dailab.jiactng.agentcore.comm.message.JiacMessage;
import de.dailab.jiactng.agentcore.ontology.AgentDescription;
import de.dailab.jiactng.agentcore.ontology.IActionDescription;
import de.dailab.jiactng.agentcore.ontology.IAgentDescription;
import facebook4j.Facebook;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONObject;

public class FacebookBean extends AbstractCommunicatingBean{
	
	private IActionDescription sendAction = null;
	
	private Facebook facebook;

	private String accessToken = "";
	
	private MySQLAccess access = null;
	private SocialAccess smAccess = null;
	private Connection connect = null;
	
	@Override
	public void doStart() throws Exception{
		super.doStart();
		log.info("FacebookAgent started.");
		
		access = new MySQLAccess();
		connect = access.connectDriver();
		smAccess = new SocialAccess(connect);
		
		sendAction = retrieveAction(ICommunicationBean.ACTION_SEND);
		
	}
	
	@Override
	public void execute(){
		
		
	}
	
	// method to get information and picture of the user with given id
	private FacebookData getMeInformation(long id, String receiverID) throws Exception{
		
		FacebookData fbUser;
		fbUser = new FacebookData(thisAgent.getAgentId(), receiverID, id);
		// a FQL-query to get faceboook-id and picture of the user
		String query = "SELECT uid, pic_big FROM user WHERE uid = me()";
		JSONArray jsonArray = facebook.executeFQL(query);
		
		// get and set the fetched datas into the FacebookData-Object
		for (int i = 0; i < jsonArray.length(); i++) {
		    JSONObject jsonObject = jsonArray.getJSONObject(i);
		    fbUser.setFbid(Long.valueOf(jsonObject.get("uid").toString()));
		    fbUser.setPicture(jsonObject.get("pic_big").toString());
		}
		
		// get all other information of the user and but it into the FacebookData-Object
		fbUser.setMe(facebook.getMe());
//		smAccess.saveUserInfo(id, fbUser);
		
//		log.info("Facebook-Daten von User: " + id + " wurde in die Datenbank geschrieben.");
		
		return fbUser;
		
	}

	// handles all received messages 
	@Override
	protected void receiveMessage(Message message) {
		
		// if it's a message to get the Facebook-Datas and send them
		if(message instanceof GetFacebookData){
			
			log.info("FacebookAgent - message received");
			
			// get the id out of the received message
			int id = ((GetFacebookData) message).getUserID();
			try {
				// get the access token out of the message or from the database
				accessToken = smAccess.getAccessToken(id, ((GetFacebookData) message).getAccessToken(), "facebook");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			// the instance that will be send back
			FacebookData fbUser = null;
			
			// create an instance of Facebook
			facebook = smAccess.initFacebook(accessToken);
			
			try {
				
				// find the agent the data is suppose to go to
				List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

				for(IAgentDescription agent : agentDescriptions){
					if(agent.getName().equals("SocialMediaAgent")){

						IMessageBoxAddress receiver = agent.getMessageBoxAddress();
						
						// put all the facebook information into the object and the access token as well
						fbUser = getMeInformation(id, agent.getAid());
						fbUser.setAccessToken(accessToken);
						
						// make the object into a JiacMessage, so it can be transported
						JiacMessage newMessage = new JiacMessage(fbUser);

						invoke(sendAction, new Serializable[] {newMessage, receiver});
						
						// use the current token to get all interests of the user and put them into the database
						smAccess.fetchInterests(id);
						
						log.info("FacebookData sent.");
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
