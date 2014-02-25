package agents.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import ontology.Message;
import ontology.messages.CalendarData;
import ontology.messages.DeleteTodo;
import ontology.messages.GetCalendarData;
import ontology.messages.GetTodoData;
import ontology.messages.SaveTodo;
import ontology.messages.TodoData;
import ontology.messages.TodoData.TodoItem;

import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import access.MySQLAccess;
import access.SocialAccess;
import access.TodoAccess;
import de.dailab.jiactng.agentcore.AbstractAgentBean;
import de.dailab.jiactng.agentcore.action.Action;
import de.dailab.jiactng.agentcore.comm.ICommunicationBean;
import de.dailab.jiactng.agentcore.comm.IMessageBoxAddress;
import de.dailab.jiactng.agentcore.comm.message.IJiacMessage;
import de.dailab.jiactng.agentcore.comm.message.JiacMessage;
import de.dailab.jiactng.agentcore.knowledge.IFact;
import de.dailab.jiactng.agentcore.ontology.AgentDescription;
import de.dailab.jiactng.agentcore.ontology.IActionDescription;
import de.dailab.jiactng.agentcore.ontology.IAgentDescription;

public class TodoBean extends AbstractCommunicatingBean{

private IActionDescription sendAction = null;
private MySQLAccess access = null;
private TodoAccess todoAccess = null;
private Connection connect = null; 
	
	@Override
	public void doStart() throws Exception{
		super.doStart();
		log.info("InformationAgent started.");
		log.info("my ID: " + this.thisAgent.getAgentId());
		
		sendAction = retrieveAction(ICommunicationBean.ACTION_SEND);
		
		access = new MySQLAccess();
		connect = access.connectDriver();
		todoAccess = new TodoAccess(connect);
	}
	
	@Override
	public void execute(){
		
	}
	
	private class MessageObserver implements SpaceObserver<IFact>{

		/**
		 * 
		 */
		private static final long serialVersionUID = -8182513339144469591L;

		@Override
		public void notify(SpaceEvent<? extends IFact> event) {
			
			if(event instanceof WriteCallEvent<?>){
				WriteCallEvent<IJiacMessage> wce = (WriteCallEvent<IJiacMessage>) event;
				
				IJiacMessage message = memory.read(wce.getObject());
				IFact obj = message.getPayload();
				
				if(obj instanceof SaveTodo){
					
					log.info("TodoAgent - Save message received");
					TodoItem item = ((SaveTodo) obj).getTodo();
					try {
						todoAccess.saveNewTodoItem(((SaveTodo) obj).getUserID(), item.getPrio(), item.getText(), item.getDate());
					} catch (Exception e) {
						e.printStackTrace();
					}
					memory.remove(wce.getObject());
				}
				
				if(obj instanceof GetTodoData){
					
					log.info("TodoAgent - Get message received");
									
					try {

						ArrayList<TodoItem> todos = todoAccess.readTodoItemList(((GetTodoData) obj).getUserID());
						
						List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

						for(IAgentDescription agent : agentDescriptions){
							if(agent.getName().equals("InformationAgent")){

								IMessageBoxAddress receiver = agent.getMessageBoxAddress();
								
								JiacMessage newMessage = new JiacMessage(new TodoData(thisAgent.getAgentId(), agent.getAid(), ((GetTodoData) obj).getUserID(), todos));

								invoke(sendAction, new Serializable[] {newMessage, receiver});
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					memory.remove(wce.getObject());
					
					log.info("Todo sent.");
				}
				
			}
			
		}
		
	}

	@Override
	protected void receiveMessage(Message message) {
		if(message instanceof SaveTodo){
			
			log.info("TodoAgent - Save message received");
			TodoItem item = ((SaveTodo) message).getTodo();
			try {
				todoAccess.saveNewTodoItem(((SaveTodo) message).getUserID(), item.getPrio(), item.getText(), item.getDate());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(message instanceof GetTodoData){
			
			log.info("TodoAgent - Get message received");
							
			try {

				ArrayList<TodoItem> todos = todoAccess.readTodoItemList(((GetTodoData) message).getUserID());
				
				List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

				for(IAgentDescription agent : agentDescriptions){
					if(agent.getName().equals("InformationAgent")){

						IMessageBoxAddress receiver = agent.getMessageBoxAddress();
						
						JiacMessage newMessage = new JiacMessage(new TodoData(thisAgent.getAgentId(), agent.getAid(), ((GetTodoData) message).getUserID(), todos));

						invoke(sendAction, new Serializable[] {newMessage, receiver});
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			log.info("Todo sent.");
		}
		
		if(message instanceof DeleteTodo){
			log.info("TodoAgent - Delete message received");
			
			try {

				int todoId = ((DeleteTodo) message).getTodoID();

				todoAccess.deleteTodo(todoId);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
