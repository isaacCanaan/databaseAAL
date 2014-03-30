package calendar;

import java.awt.List;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import ontology.messages.CalendarData;
import access.MySQLAccess;
import access.UserAccess;

import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.EventWho;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class GoogleCalendarFetcher {
	
	private String emailAddress;
	private String pword;
	
	private MySQLAccess access;
	private Connection connect;
	private UserAccess userAccess;

	//contructor
	public GoogleCalendarFetcher(String emailAddress, String pword){
		this.emailAddress = emailAddress;
		this.pword = pword;
	}
	
	//method to extract all calendar entires 
	public ArrayList<CalendarData.Entry> getEventEntries() throws Exception{
		
		access = new MySQLAccess();
		connect = access.connectDriver();
		userAccess = new UserAccess(connect);
		
		// creates the service and logs in with specified user data
		CalendarService myService = new CalendarService("LivingWall GoogleCalendarAccess2");
		try {
			myService.setUserCredentials(this.emailAddress, this.pword);
		} catch (AuthenticationException e) {
			System.err.println("Authentification failed!");
		}

		// Send the request and print the response
		URL feedUrl = null;
		try {
			feedUrl = new URL("https://www.google.com/calendar/feeds/default/private/full");
		} catch (MalformedURLException e) {
			System.err.println("Calendar page could not be retrieved");
		}
		CalendarEventFeed feed = null;
		try {
			feed = myService.getFeed(feedUrl, CalendarEventFeed.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkedList<CalendarEventEntry> events = (LinkedList<CalendarEventEntry>) feed.getEntries();
		
		ArrayList<CalendarData.Entry> entries = new ArrayList<CalendarData.Entry>();
		
		//parse all events 
		for (CalendarEventEntry e : events){
			
			ArrayList<Integer> partIds = new ArrayList<Integer>();
			java.util.List<EventWho> participants = e.getParticipants();
			
			for(EventWho p : participants){
				String mail = p.getEmail();
				partIds.add(userAccess.findGmailUser(mail));
			}
			
			String content = e.getPlainTextContent();
			String name = e.getTitle().getPlainText();
			Date stime = null;
			Date etime = null;
			if (!e.getTimes().isEmpty()){
				stime = new Date(e.getTimes().get(0).getStartTime().getValue());
				etime = new Date(e.getTimes().get(0).getEndTime().getValue());
			}
			String location = e.getLocations().get(0).getValueString();
			CalendarData cal = new CalendarData();
			CalendarData.Entry currentEntry = cal.new Entry(content, name, stime, etime, location);
			entries.add(currentEntry);
		}
		return entries;
	}
}
