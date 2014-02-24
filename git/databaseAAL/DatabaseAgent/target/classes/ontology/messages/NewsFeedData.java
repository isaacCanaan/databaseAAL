package ontology.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ontology.Message;
import ontology.MessageType;
import de.dailab.jiactng.agentcore.knowledge.IFact;

public class NewsFeedData extends Message{

	private static final long serialVersionUID = 4382748121224794885L;
	private ArrayList<NewsFeedMessage> news;
	
	public NewsFeedData(String senderID, String receiverID, ArrayList<NewsFeedMessage> news){
		super(senderID, receiverID, MessageType.INFO_DATA);
		this.news = news;
	}
	
	public NewsFeedData() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<NewsFeedMessage> getNewsFeed(){
		return news;
	}
	
	public void setNewsFeed(ArrayList<NewsFeedMessage> news){
		this.news = news;
	}
	
	public class NewsFeedMessage implements Serializable{
		String title;
		String description;
		String link;
		String author;
		String guid;
		String enclosure;
	    
		
		
		public String getTitle() {
		    return title;
		  }

		  public void setTitle(String title) {
		    this.title = title;
		  }

		  public String getDescription() {
		    return description;
		  }

		  public void setDescription(String description) {
		    this.description = description;
		  }

		  public String getLink() {
		    return link;
		  }

		  public void setLink(String link) {
		    this.link = link;
		  }
		  
//		  public String getAuthor() {
//			    return author;
//			  }
	//
//		  public void setAuthor(String author) {
//			    this.author = author;
//			  }
	//
//		  public String getGuid() {
//			    return guid;
//	        }
	//
//		  public void setGuid(String guid) {
//			    this.guid = guid;
//			  }
//			  
		  public String getEnclosure() {
		        return enclosure;
		    }

		    public void setEnclosure(String enclosure) {
		        this.enclosure = enclosure;
		    }		

			  @Override
			  public String toString() {
			    return "FeedMessage [TITLE=" + title + ", DESCRIPTION=" + description
			        + ", LINK=" + link + ", ENCLOSURE="+ enclosure + "]";
			  }


	}

}
