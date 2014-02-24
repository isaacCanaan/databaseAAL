package agents.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ontology.messages.CalendarData;
import ontology.messages.GetCalendarData;
import ontology.messages.GetTodoData;

import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import calendar.GoogleCalendarFetcher;
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

public class TodoBean extends AbstractAgentBean{

private IActionDescription sendAction = null;
	
	@Override
	public void doStart() throws Exception{
		super.doStart();
		log.info("InformationAgent started.");
		
		IActionDescription template = new Action(ICommunicationBean.ACTION_SEND);
		sendAction = memory.read(template);

		if(sendAction == null){
			sendAction = thisAgent.searchAction(template);
		}
		
		if(sendAction == null){
			throw new RuntimeException("Send action not found.");
		}
		
		memory.attach(new MessageObserver(), new JiacMessage());
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
			
			ArrayList<CalendarData.Entry> entries = null;
			
			if(event instanceof WriteCallEvent<?>){
				WriteCallEvent<IJiacMessage> wce = (WriteCallEvent<IJiacMessage>) event;
				
				log.info("TodoAgent - message received");
				
				IJiacMessage message = memory.remove(wce.getObject());
				IFact obj = message.getPayload();
				
				if(obj instanceof GetTodoData){
									
					try {

						
						List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

						for(IAgentDescription agent : agentDescriptions){
							if(agent.getName().equals("InformationAgent")){

								IMessageBoxAddress receiver = agent.getMessageBoxAddress();
								
								JiacMessage newMessage = new JiacMessage(new CalendarData(thisAgent.getAgentId(), agent.getAid(), ((GetCalendarData) obj).getUserID(), entries));

								invoke(sendAction, new Serializable[] {newMessage, receiver});
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				if(entries == null){
					throw new RuntimeException("No Calendar Entries found.");
				}
			}
			
		}
		
	}
}
