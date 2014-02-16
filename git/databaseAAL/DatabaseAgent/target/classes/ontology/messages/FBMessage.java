package ontology.messages;

import de.dailab.jiactng.agentcore.knowledge.IFact;

public class FBMessage implements IFact{
	
	FacebookData fbUser;
	
	public FBMessage(){
	}
	
	public FacebookData getfbUser(){
		return this.fbUser;
	}
	
	public void setfbUser(FacebookData fbUser){
		this.fbUser = fbUser;
	}

}
