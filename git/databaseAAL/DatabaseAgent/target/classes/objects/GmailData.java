package objects;

public class GmailData {

	private int id;
	private String mail;
	private String pw;
	
	public GmailData(int id, String mail, String pw){
		this.id = id;
		this.mail = mail;
		this.pw = pw;
	}
	
	public int getId(){
		return id;
	}
	
	public String getMail(){
		return mail;
	}
	
	public String getPassword(){
		return pw;
	}
	
}
