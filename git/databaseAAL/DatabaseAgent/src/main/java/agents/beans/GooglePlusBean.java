package agents.beans;

import com.googlecode.googleplus.GooglePlusFactory;
import com.googlecode.googleplus.Plus;

import de.dailab.jiactng.agentcore.AbstractAgentBean;

public class GooglePlusBean extends AbstractAgentBean{
	
	private String clientId;
	private String clientSecret;
	
	@Override
	public void doStart(){
		GooglePlusFactory factory = new GooglePlusFactory(clientId, clientSecret);
		@SuppressWarnings("deprecation")
		Plus api = factory.getApi("");
		
		api.getMe();
	}

}
