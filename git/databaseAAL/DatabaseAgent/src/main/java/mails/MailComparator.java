package mails;

import java.util.Comparator;

import ontology.messages.MailData;

public class MailComparator implements Comparator<MailData.Mail>{
	@Override
	public int compare(MailData.Mail obj1, MailData.Mail obj2){
		return obj2.getReceived().compareTo(obj1.getReceived());
	}
}
