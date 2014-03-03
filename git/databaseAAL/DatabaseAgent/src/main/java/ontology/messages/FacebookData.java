package ontology.messages;

import java.sql.Timestamp;
import java.util.Date;

import ontology.Message;
import ontology.MessageType;
import facebook4j.User;

public class FacebookData extends Message{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7387841624375299862L;
	private long id;
	private long fbid;
	private String username;
	private String name;
	private String middleName;
	private String lastName;
	private String birthday;
	private String email;
	private String gender;
	private String picture;
	private Timestamp timestamp;
	private User me;
	private String accessToken;
	
	public FacebookData(String senderID, String receiverID, long id){
		super(senderID, receiverID, MessageType.SOCIAL_DATA);
		this.id = id;
	}
	
	public void setFbid(long fbid){
		this.fbid = fbid;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setMiddleName(String middleName){
		this.middleName = middleName;
	}
	
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	
	public void setGender(String gender){
		this.gender = gender;
	}
	
	public void setPicture(String url){
		this.picture = url;
	}
	
	public void setTimestamp(Timestamp timestamp){
		this.timestamp = timestamp;
	}
	
	public void setMe(User user){
		this.me = user;
	}
	
	public void setAccessToken(String access){
		this.accessToken = access;
	}
	
	public long getId(){
		return id;
	}
	
	public long getFbid(){
		return fbid;
	}
	
	public String getName(){
		return name;
	}
	
	public String getMiddleName(){
		return middleName;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public String getGender(){
		return gender;
	}
	
	public String getPicture(){
		return picture;
	}
	
	public Date getTimestamp(){
		return timestamp;
	}
	
	public User getMe(){
		return me;
	}
	
	public String getAccessToken(){
		return accessToken;
	}

}
