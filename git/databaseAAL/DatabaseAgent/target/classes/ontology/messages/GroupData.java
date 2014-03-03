package ontology.messages;

import java.io.Serializable;
import java.util.ArrayList;

import ontology.Message;
import ontology.MessageType;

public class GroupData extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4689493966588206928L;
	
	private ArrayList<FBGroup> interests;
	
	public GroupData(){
		
	}
	
	public GroupData(String senderID, String receiverID, ArrayList<FBGroup> interests) {
		super(senderID, receiverID, MessageType.SOCIAL_DATA);
		this.interests = interests;
	}
	
	public ArrayList<FBGroup> getInterests(){
		return interests;
	}
	
	public class FBGroup implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 5207003142337598839L;
		
		private int id;
		private String topic;
		private ArrayList<Integer> members;
		
		public FBGroup(){
			
		}
		
		public FBGroup(int id, String topic, ArrayList<Integer> members){
			this.id = id;
			this.topic = topic;
			this.members = members;
		}
		
		public int getId(){
			return this.id;
		}
		
		public String getTopic(){
			return this.topic;
		}
		
		public ArrayList<Integer> getMembers(){
			return this.members;
		}
		
		public void setId(int id){
			this.id = id;
		}
		
		public void setTopic(String topic){
			this.topic = topic;
		}
		
		public void setMembers(ArrayList<Integer> members){
			this.members = members;
		}
	}


}
