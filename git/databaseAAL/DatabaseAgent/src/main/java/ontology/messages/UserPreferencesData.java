package ontology.messages;

import java.util.HashMap;

import jiac.Settings;
import ontology.Message;
import ontology.MessageType;

public class UserPreferencesData extends Message{
	
	private int userID;
	private Settings sets;
	
	public UserPreferencesData(String senderID, String receiverID, int userID, Settings sets) {
    	super(senderID, receiverID, MessageType.GET_INFO);
    	this.userID = userID;
    	this.sets = sets;
    }
	
	public int getId(){
		return this.userID;
	}
	
	public Settings getKeys(Settings sets){
		return this.sets;
	}

}
