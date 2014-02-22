package ontology.messages;

import java.util.HashMap;

import ontology.Message;
import ontology.MessageType;

public class UserKeysData extends Message{
	
	private int userID;
	HashMap<String, String> keys;
	
	public UserKeysData(String senderID, String receiverID, int userID, HashMap<String,String> keys) {
    	super(senderID, receiverID, MessageType.GET_INFO);
    	this.userID = userID;
    	this.keys = keys;
    }
	
	public int getId(){
		return this.userID;
	}
	
	public HashMap<String,String> getKeys(){
		return this.keys;
	}

}
