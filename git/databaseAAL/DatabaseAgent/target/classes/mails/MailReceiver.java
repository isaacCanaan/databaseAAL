package mails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import ontology.messages.MailData;

public class MailReceiver {
	
	private ArrayList<eMailAcc> emailAccs;
	private ArrayList<MailData.Mail> emails;
	
	public MailReceiver (ArrayList<eMailAcc> emailAccs){
		this.emailAccs = emailAccs;
	}
	
	//method to recieve mails from multiple email accounts
	public ArrayList<MailData.Mail> receiveMails(){
		ArrayList<MailData.Mail> received = new ArrayList<MailData.Mail>();
		
		// for each email account we extract all email in "INBOX"
		for (int i=0; i<emailAccs.size(); i++){
			eMailAcc current = emailAccs.get(i);
			// set the provider 
			String provider = getProvider(current.getEmailAdd());
			// set the properties, e.g. SLL port...
			Properties props = new Properties();
			switch(provider){
				case "aol.com":
					props.put("mail.imaps.host", "imap.de.aol.com");
					received.addAll(getMails(current, props));break;
				case "aim.com": case "aol.de":
					props.put("mail.imaps.host", "imap.aim.com");
					received.addAll(getMails(current, props));break;
				case "gmail.com": case "googlemail.com":
					props.put("mail.imaps.host", "imap.gmail.com");
					props.put("mail.imaps.port", "993");
					received.addAll(getMails(current, props));break;
				case "gmx.net": case "gmx.de":
					props.put("mail.imaps.host", "imap.gmx.net");
					props.put("mail.imaps.port", "993");
					received.addAll(getMails(current, props));break;
				case "campus.tu-berlin.de": case "tu-berlin.de":
					props.put("mail.imaps.port", "993");
					props.put("mail.imaps.host", "mail.tu-berlin.de");
					received.addAll(getMails(current , props));break;
				case "web.de":
					props.put("mail.pop3s.host", "pop3.web.de");
					received.addAll(getMails(current, props));break;
				case "hotmail.de":
					props.put("mail.pop3s.host", "pop3.live.com");
					props.put("mail.pop3s.port", "995");
					received.addAll(getMails(current, props));break;
				default:
					System.err.println("Provider is not supported!");;
			}
		}
		// sort all emails by date
		Collections.sort(received, new MailComparator());

		return received;
		
	}
	
	// method to extract the provider (server address)
	private String getProvider(String addr){
		if (addr.contains("@")){
			return addr.substring(addr.indexOf("@")+1);
		} else {
			System.err.println("This is no E-Mail address!!");
			return addr;
		}
	}
	
	// method which extract the emails from the mailservers
	private ArrayList<MailData.Mail> getMails(eMailAcc acc,Properties properties){
		ArrayList<MailData.Mail> mails = new ArrayList<MailData.Mail>();
		try {  
			   Session session = Session.getInstance(properties, null);
			   Store store = null;
			   
			   // imap and pop3, both with ssl transcription are supported
			   if (properties.getProperty("mail.imaps.host") != null){
					   store = session.getStore("imaps");
					   if (properties.getProperty("mail.imaps.port") == null){
						   store.connect(properties.getProperty("mail.imaps.host"), acc.getUserName(), acc.getPassword());
					   }else{
						   store.connect(properties.getProperty("mail.imaps.host"),
								   Integer.parseInt(properties.getProperty("mail.imaps.port")),acc.getUserName(), acc.getPassword());
					   }
				} else {
					store = session.getStore("pop3s");
					if (properties.getProperty("mail.pop3s.port") == null){
						   store.connect(properties.getProperty("mail.pop3s.host"), acc.getUserName(), acc.getPassword());
					   }else{
						   store.connect(properties.getProperty("mail.pop3s.host"),
								   Integer.parseInt(properties.getProperty("mail.pop3s.port")),acc.getUserName(), acc.getPassword());
					   }
				}
			   
			  
			   // create the folder object and open it  
			   Folder emailFolder = store.getFolder("INBOX"); 
			   emailFolder.open(Folder.READ_ONLY);  
			  
			   // retrieve the messages from the folder in an array
			   Message[] messages = emailFolder.getMessages();  
			   for (int i = 0; i < messages.length; i++) {  
			    	Message message = messages[i];  
			    	String msg = getText(message);
			    	String type = message.getContentType();
			    	MailData mailData = new MailData();
			    	mails.add(mailData.new Mail(message.getSubject(), msg, type,
			    		message.getFrom()[0].toString(),new Date(message.getSentDate().getTime())));
			   }  
			  
			   // close the store and folder objects  
			   emailFolder.close(false);  
			   store.close(); 
			  
			  } catch (NoSuchProviderException e) {e.printStackTrace();}   
			  catch (MessagingException e) {e.printStackTrace();}  	   
		return mails;
	}

	// Getter and setter
	public ArrayList<MailData.Mail> getEmails() {
		return emails;
	}

	public void setEmails(ArrayList<MailData.Mail> emails) {
		this.emails = emails;
	}
	
	// method to get the plain text from the message
	private String getText(Part p){
		try {
			if (p.isMimeType("text/plain")) {
				return (String)p.getContent();
			}
			else if (p.isMimeType("text/html")) {
			     return (String)p.getContent();
			 } else if (p.isMimeType("multipart/alternative")) {
				 Multipart mp = (Multipart)p.getContent();
				 String text = null;
				 for (int i = 0; i < mp.getCount(); i++) {
		             Part bp = mp.getBodyPart(i);
		             if (bp.isMimeType("text/html")) {
		                 if (text == null)
		                     text = getText(bp);
		                 continue;
		             } else if (bp.isMimeType("text/plain")) {
		                 String s = getText(bp);
		                 if (s != null)
		                     return s;
		             } else {
		                 return getText(bp);
		             }
		         }
			 }
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
