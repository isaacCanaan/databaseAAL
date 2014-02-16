package ontology.messages;

import objects.LinkedInUser;
import ontology.Message;
import ontology.MessageType;
import de.dailab.jiactng.agentcore.knowledge.IFact;

public class LinkedInData extends Message{
	
	private LinkedInUser user;
	
	public LinkedInData(String senderID, String receiverID){
		super(senderID, receiverID, MessageType.SOCIAL_DATA);
	}
	
	public LinkedInUser getMe(){
		return this.user;
	}
	
	public void setMe(LinkedInUser user){
		this.user = user;
	}

}
