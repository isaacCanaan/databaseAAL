package ontology.messages;

import java.util.ArrayList;

import ontology.Message;
import ontology.MessageType;
import objects.Mail;
import de.dailab.jiactng.agentcore.knowledge.IFact;

public class MailData extends Message{
	
	private long id;
	private ArrayList<Mail> mails;
	
	public MailData(long id, String senderID, String receiverID, ArrayList<Mail> mails){
		super(senderID, receiverID, MessageType.COMM_DATA);
		this.id = id;
		this.mails = mails;
	}
	
	public long getID(){
		return id;
	}
	
	public void setID(long id){
		this.id = id;
	}
	
	public ArrayList<Mail> getMails(){
		return mails;
	}
	
	public void setMails(ArrayList<Mail> mails){
		this.mails = mails;
	}

}
