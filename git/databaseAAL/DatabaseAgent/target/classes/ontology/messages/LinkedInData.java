package ontology.messages;

import de.dailab.jiactng.agentcore.knowledge.IFact;

public class LinkedInData implements IFact{
	
	private String accessToken;
	private String tokenSecret;
	
	public LinkedInData(String accessToken, String tokenSecret){
		this.accessToken = accessToken;
		this.tokenSecret = tokenSecret;
	}
	
	public String getAccessToken(){
		return accessToken;
	}
	
	public String getTokenSecret(){
		return tokenSecret;
	}

}
