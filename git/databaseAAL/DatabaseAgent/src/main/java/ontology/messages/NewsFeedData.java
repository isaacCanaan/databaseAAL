package ontology.messages;

import java.util.List;

import ontology.Message;
import ontology.MessageType;
import objects.NewsFeedMessage;
import de.dailab.jiactng.agentcore.knowledge.IFact;

public class NewsFeedData extends Message{
	
	List<NewsFeedMessage> news;
	
	public NewsFeedData(String senderID, String receiverID, List<NewsFeedMessage> news){
		super(senderID, receiverID, MessageType.INFO_DATA);
		this.news = news;
	}
	
	public List<NewsFeedMessage> getNewsFeed(){
		return news;
	}
	
	public void setNewsFeed(List<NewsFeedMessage> news){
		this.news = news;
	}

}
