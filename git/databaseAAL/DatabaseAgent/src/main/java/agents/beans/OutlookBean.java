package agents.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import com.moyosoft.connector.com.ComponentObjectModelException;
import com.moyosoft.connector.exception.LibraryNotFoundException;
import com.moyosoft.connector.ms.outlook.Outlook;
import com.moyosoft.connector.ms.outlook.folder.FolderType;
import com.moyosoft.connector.ms.outlook.folder.OutlookFolder;
import com.moyosoft.connector.ms.outlook.item.ItemsCollection;
import com.moyosoft.connector.ms.outlook.item.ItemsIterator;
import com.moyosoft.connector.ms.outlook.item.OutlookItem;
import com.moyosoft.connector.ms.outlook.task.OutlookTask;

import access.MySQLAccess;
import access.UserAccess;
import de.dailab.jiactng.agentcore.AbstractAgentBean;
import de.dailab.jiactng.agentcore.action.Action;
import de.dailab.jiactng.agentcore.comm.ICommunicationBean;
import de.dailab.jiactng.agentcore.comm.message.IJiacMessage;
import de.dailab.jiactng.agentcore.knowledge.IFact;
import de.dailab.jiactng.agentcore.ontology.IActionDescription;

public class OutlookBean extends AbstractAgentBean{
	
	private Outlook outlookApplication;
	
	private IActionDescription sendAction = null;
	
	private MySQLAccess access = null;
	private UserAccess userAccess = null;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	
	
	@Override
	public void doStart() throws Exception{
		super.doStart();
		log.info("OutlookAgent started.");
		
		access = new MySQLAccess();
		connect = access.connectDriver();
		userAccess = new UserAccess(connect);
		
		Outlook.setLibraryPath("/DatabaseAgent/moyocore.dll");
		
		IActionDescription template = new Action(ICommunicationBean.ACTION_SEND);
		sendAction = memory.read(template);
		
		if(sendAction == null){
			sendAction = thisAgent.searchAction(template);
		}
		
		if(sendAction == null){
			throw new RuntimeException("Send action not found.");
		}
		
//		memory.attach(new MessageObserver(), new JiacMessage());
	}
	
	@Override
	public void execute(){
		
		try {
			outlookApplication = new Outlook("yuancheng@outlook.de", "outlook2506");
		} catch (ComponentObjectModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LibraryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		OutlookFolder folder = outlookApplication.getDefaultFolder(FolderType.TASKS);
		ItemsCollection items = folder.getItems();
		
		for(ItemsIterator it = items.iterator(); it.hasNext();)
		{
		   OutlookItem item = it.nextItem();
		   
		   if(item != null && item.getType().isTask())
		   {
		      OutlookTask task = (OutlookTask) item;
		      log.info(task.getBody());
		   }
		}
	}
	
//	private class MessageObserver implements SpaceObserver<IFact>{	
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -8182513339144469591L;
//
//		@Override
//		public void notify(SpaceEvent<? extends IFact> event) {
//			if(event instanceof WriteCallEvent<?>){
//				WriteCallEvent<IJiacMessage> wce = (WriteCallEvent<IJiacMessage>) event;
//				
//				IJiacMessage message = memory.remove(wce.getObject());
//				
//				if(message != null){
//					IFact obj = message.getPayload();
//					
//					outlookApplication = new Outlook("yuancheng@outlook.de", "outlook2506");  
//					OutlookFolder folder = outlookApplication.getDefaultFolder(FolderType.TASKS);
//					ItemsCollection items = folder.getItems();
//					
//					for(ItemsIterator it = items.iterator(); it.hasNext();)
//					{
//					   OutlookItem item = it.nextItem();
//					   
//					   if(item != null && item.getType().isTask())
//					   {
//					      OutlookTask contact = (OutlookTask) item;
//					   }
//					}
//					
//					if(obj instanceof GetTwitterData){
//						
//						try {
//							
//							List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());
//
//							for(IAgentDescription agent : agentDescriptions){
//								if(agent.getName().equals("LinkedInAgent")){
//
//									IMessageBoxAddress receiver = agent.getMessageBoxAddress();
//									
//									TwitterData data = new TwitterData(obj.getID(), thisAgent.getAgentId(), agent.getAid());
//									JiacMessage newMessage = new JiacMessage(data);
//
//									invoke(sendAction, new Serializable[] {newMessage, receiver});
//								}
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//					else{
//						memory.write(wce.getObject());
//					}
//				}
//				
//			}
//			
//		}
//		
//	}

}
