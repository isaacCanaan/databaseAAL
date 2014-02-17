package ontology.messages;

import objects.LinkedInUser;
import ontology.Message;
import ontology.MessageType;
import de.dailab.jiactng.agentcore.knowledge.IFact;

public class LinkedInData extends Message{
	
	private long id;
	private LinkedInUser user;
	
	public LinkedInData(long id, String senderID, String receiverID){
		super(senderID, receiverID, MessageType.SOCIAL_DATA);
		this.id = id;
	}
	
	public long getID(){
		return id;
	}
	
	public void setID(long id){
		this.id = id;
	}
	
	public LinkedInUser getMe(){
		return this.user;
	}
	
	public void setMe(LinkedInUser user){
		this.user = user;
	}

}
