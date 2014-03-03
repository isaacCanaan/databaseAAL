package ontology.messages;

import java.util.ArrayList;

import ontology.Message;
import ontology.MessageType;

public class GetGroupData extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7379115714033272251L;
	
	private ArrayList<Integer> members;
	
	public GetGroupData(String senderID, String receiverID, ArrayList<Integer> members){
        super(senderID, receiverID, MessageType.GET_SOCIAL);
        this.members = members;
    }
	
	public ArrayList<Integer> getMembers(){
		return members;
	}

}
