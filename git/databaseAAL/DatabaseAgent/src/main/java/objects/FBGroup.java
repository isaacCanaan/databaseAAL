package objects;

import java.util.ArrayList;

public class FBGroup {

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
