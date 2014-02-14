package objects;

import java.util.ArrayList;

public class FBGroup {

	private long id;
	private String topic;
	private ArrayList<Long> members;
	
	public FBGroup(long id, String topic, ArrayList<Long> members){
		this.id = id;
		this.topic = topic;
		this.members = members;
	}
	
	public long getID(){
		return this.id;
	}
}
