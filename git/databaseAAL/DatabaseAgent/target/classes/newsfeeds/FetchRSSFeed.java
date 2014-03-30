package newsfeeds;

import java.util.ArrayList;
import java.util.List;

import ontology.messages.NewsFeedData;
import objects.Feed;

public class FetchRSSFeed {
	// method to return a List of NewsMessages from www.welt.de
	public ArrayList<NewsFeedData.NewsFeedMessage> getRSSFeedWeltDE(){
		// create Parser for the given URL
		RSSFeedParser parser = new RSSFeedParser("http://www.welt.de/wirtschaft/?service=Rss");
    	Feed feed = parser.readFeed();
    	
    	return feed.getMessages();	
	}
	
		
    

}
//http://news.yahoo.com/rss/
//http://www.welt.de/vermischtes/?service=Rss
