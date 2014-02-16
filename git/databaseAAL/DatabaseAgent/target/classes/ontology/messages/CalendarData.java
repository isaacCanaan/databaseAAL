package ontology.messages;

import java.util.ArrayList;

import ontology.Message;
import ontology.MessageType;
import objects.Entry;

public class CalendarData extends Message{
	
	ArrayList<Entry> entries;
	
	public CalendarData(String senderID, String receiverID, ArrayList<Entry> entries){
		super(senderID, receiverID, MessageType.INFO_DATA);
		this.entries = entries;
	}
	
	public void setEntries(ArrayList<Entry> entries){
		this.entries = entries;
	}
	
	public ArrayList<Entry> getEntries(){
		return this.entries;
	}

}
