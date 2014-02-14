package messages;

import de.dailab.jiactng.agentcore.knowledge.IFact;

public class FBData implements IFact{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String accessToken;
	
	public FBData(long id, String access){
		this.id = id;
		this.accessToken = access;
	}
	
	public FBData() {
		// TODO Auto-generated constructor stub
	}

	public void setID(long id){
		this.id = id;
	}
	
	public long getID(){
		return this.id;
	}
	
	public void setAccessToken(String access){
		this.accessToken = access;
	}
	
	public String getAccessToken(){
		return this.accessToken;
	}

}
