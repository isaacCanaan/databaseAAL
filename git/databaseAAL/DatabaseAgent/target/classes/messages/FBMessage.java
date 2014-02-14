package messages;

import objects.FacebookUser;
import de.dailab.jiactng.agentcore.knowledge.IFact;

public class FBMessage implements IFact{
	
	FacebookUser fbUser;
	
	public FBMessage(){
	}
	
	public FacebookUser getfbUser(){
		return this.fbUser;
	}
	
	public void setfbUser(FacebookUser fbUser){
		this.fbUser = fbUser;
	}

}
