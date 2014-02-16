package objects;

public class LinkedInUser {

	private String first;
	private String last;
	
	public LinkedInUser(){
		
	}
	
	public void setFirstname(String name){
		this.first = name;
	}
	
	public void setLastname(String name){
		this.last = name;
	}
	
	public String getFirstname(){
		return this.first;
	}
	
	public String getLastname(){
		return this.last;
	}
	
}
