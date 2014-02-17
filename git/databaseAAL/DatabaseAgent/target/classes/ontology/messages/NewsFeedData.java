package ontology.messages;

import java.util.List;

import ontology.Message;
import ontology.MessageType;
import objects.NewsFeedMessage;
import de.dailab.jiactng.agentcore.knowledge.IFact;

public class NewsFeedData extends Message{
	
	private long id;
	List<NewsFeedMessage> news;
	
	public NewsFeedData(long id, String senderID, String receiverID, List<NewsFeedMessage> news){
		super(senderID, receiverID, MessageType.INFO_DATA);
		this.id = id;
		this.news = news;
	}
	
	public long getID(){
		return id;
	}
	
	public void setID(long id){
		this.id = id;
	}
	
	public List<NewsFeedMessage> getNewsFeed(){
		return news;
	}
	
	public void setNewsFeed(List<NewsFeedMessage> news){
		this.news = news;
	}

}
