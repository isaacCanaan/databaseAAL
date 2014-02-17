package ontology.messages;

import java.util.ArrayList;

import ontology.Message;
import ontology.MessageType;
import objects.Entry;

public class CalendarData extends Message{
	
	private long id;
	ArrayList<Entry> entries;
	
	public CalendarData(long id, String senderID, String receiverID, ArrayList<Entry> entries){
		super(senderID, receiverID, MessageType.INFO_DATA);
		this.id = id;
		this.entries = entries;
	}
	
	public long getID(){
		return id;
	}
	
	public void setID(long id){
		this.id = id;
	}
	
	public void setEntries(ArrayList<Entry> entries){
		this.entries = entries;
	}
	
	public ArrayList<Entry> getEntries(){
		return this.entries;
	}

}
