package agents.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ontology.messages.FacebookData;
import ontology.messages.GetFacebookData;

import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import access.SocialAccess;
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
import facebook4j.Book;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Game;
import facebook4j.Interest;
import facebook4j.Movie;
import facebook4j.Music;
import facebook4j.ResponseList;
import facebook4j.Television;
import facebook4j.auth.AccessToken;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

public class FacebookBean extends AbstractAgentBean{
	
	private IActionDescription sendAction = null;
	
	private Facebook facebook;
	private static String appId = "688786967828835";
	private static String secretKey = "4761c8cec03889b8e7b8b6dd93eb91ee";
	
	private int id = 0;
	private String accessToken = "fail";
	private long expirationTimeMillis = 5178246;
	
	private MySQLAccess access = null;
	private SocialAccess smAccess = null;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	private long userID;
	
	@Override
	public void doStart() throws Exception{
		super.doStart();
		log.info("FacebookAgent started.");
		
		access = new MySQLAccess();
		connect = access.connectDriver();
		smAccess = new SocialAccess(connect);
		
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
	
	private void initFacebook(){
		facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId(appId, secretKey);
		facebook.setOAuthPermissions("publish_stream,offline_access,create_event,read_stream,read_friendlists,user_interests");
		AccessToken access = new AccessToken(accessToken,expirationTimeMillis);
		facebook.setOAuthAccessToken(access);
	}
	
	private FacebookData getMeInformation(long id, String receiverID) throws Exception{
		
		FacebookData fbUser;
		fbUser = new FacebookData(thisAgent.getAgentId(), receiverID, id);
		String query = "SELECT uid, pic_big FROM user WHERE uid = me()";
		JSONArray jsonArray = facebook.executeFQL(query);
		
		for (int i = 0; i < jsonArray.length(); i++) {
		    JSONObject jsonObject = jsonArray.getJSONObject(i);
		    fbUser.setFbid(Long.valueOf(jsonObject.get("uid").toString()));
		    fbUser.setPicture(jsonObject.get("pic_big").toString());
		}
		
		fbUser.setMe(facebook.getMe());
//		fbAccess.saveUserInfo(id, fbUser);
		
//		log.info("Facebook-Daten von User: " + id + " wurde in die Datenbank geschrieben.");
		
		return fbUser;
		
	}
	
	private void fetchInterests(long id) throws Exception{
			
			ResponseList<Book> books = null;
			ResponseList<Interest> interests = null;
			ResponseList<Music> music = null;
			ResponseList<Game> games = null;
			ResponseList<Movie> movies = null;
			ResponseList<Television> tv = null;
			
			try {
				books = facebook.getBooks();
				interests = facebook.getInterests();
				music = facebook.getMusic();
				games = facebook.getGames();
				movies = facebook.getMovies();
				tv = facebook.getTelevision();
			} catch (FacebookException e) {
	
			}
			
			smAccess.saveInterests(id, books, interests, music, games, movies, tv);
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
				
				if(message != null){
					IFact obj = message.getPayload();
					
					if(obj instanceof GetFacebookData){
						
						log.info("FacebookAgent - message received");
						
						id = ((GetFacebookData) obj).getUserID();
						try {
							accessToken = smAccess.getAccessToken(id, ((GetFacebookData) obj).getAccessToken(), "facebook");
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						
						FacebookData fbUser = null;
						
						initFacebook();
						
						try {
							
							List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

							for(IAgentDescription agent : agentDescriptions){
								if(agent.getName().equals("SocialMediaAgent")){

									IMessageBoxAddress receiver = agent.getMessageBoxAddress();
									
									fbUser = getMeInformation(id, agent.getAid());
									
//									fetchInterests(id);
									
									JiacMessage newMessage = new JiacMessage(fbUser);

									invoke(sendAction, new Serializable[] {newMessage, receiver});
								}
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						memory.remove(wce.getObject());
					}
					
				}
			}
			
		}
		
	}

}
