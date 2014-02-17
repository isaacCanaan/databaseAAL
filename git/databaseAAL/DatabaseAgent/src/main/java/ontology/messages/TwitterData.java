package ontology.messages;

import ontology.Message;
import ontology.MessageType;

public class TwitterData extends Message{
	
	private long id;

	public TwitterData(long id, String agentId, String aid) {
		super(agentId, aid, MessageType.SOCIAL_DATA);
		this.id = id;
	}

}
