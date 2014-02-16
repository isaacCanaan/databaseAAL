package ontology.messages;

import java.util.ArrayList;

import ontology.Message;
import ontology.MessageType;
import objects.Mail;
import de.dailab.jiactng.agentcore.knowledge.IFact;

public class MailData extends Message{
	
	private ArrayList<Mail> mails;
	
	public MailData(String senderID, String receiverID, ArrayList<Mail> mails){
		super(senderID, receiverID, MessageType.COMM_DATA);
		this.mails = mails;
	}
	
	public ArrayList<Mail> getMails(){
		return mails;
	}
	
	public void setMails(ArrayList<Mail> mails){
		this.mails = mails;
	}

}
