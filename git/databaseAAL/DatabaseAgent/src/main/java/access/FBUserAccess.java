package access;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import ontology.messages.FacebookData;

import com.restfb.types.FacebookType;

// not tested yet

public class FBUserAccess {
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public FBUserAccess(Connection connect){
		this.connect = connect;
	}
	
	public void saveUserInfo(long id, FacebookData user) throws Exception{
		  
		  try {
			  
			  // get current date	  
			  Calendar cal = Calendar.getInstance();
			  
			  // check whether there is an instance for the facebook-datas in the database or not
			  int count = 0;
			  preparedStatement = connect.prepareStatement("SELECT COUNT(*) from AAL.FBUSER where id= ? ; ");
			  preparedStatement.setLong(1, id);
			  resultSet = preparedStatement.executeQuery();
			  resultSet.next();
			  count = resultSet.getInt(1);
			  
			  if(count == 1){
				  updateFBUser(id, user); 
			  }
			  else{
				  preparedStatement = connect.prepareStatement("insert into  AAL.FBUSER values (?, ?, ?)");
				  preparedStatement.setLong(1, id);
			      preparedStatement.setLong(2, user.getFbid());
			      preparedStatement.setString(3, user.getPicture());

			      preparedStatement.executeUpdate();  
			      		
			  }
		  } 
		  catch (Exception e) {
		      throw e;
	      } 
	  }
	
	public FacebookData readFBUser(long id) throws Exception{
		  
		  FacebookData user = new FacebookData("", "");
		  
		  try {
			  
		      preparedStatement = connect.prepareStatement("SELECT fb_id, picture FBUSER from AAL.FBUSER where id= ? ; ");
		      preparedStatement.setString(1, String.valueOf(id));
	          resultSet = preparedStatement.executeQuery();
	          
	          // put all datas into an user-object
	          while (resultSet.next()) {

	        	  user.setFbid(0);
	        	  user.setFbid(resultSet.getLong("fb_id"));
	    	      if(resultSet.getString("picture") != null){
	    	    	  user.setGender(resultSet.getString("picture"));
	    	      }
	    	    }
		  } 
		  catch (Exception e) {
		      throw e;
		  } 
		  
		  // no user found with given id
		  if(user.getFbid()==0){
			  return null;
		  }
		  
		  return user; 
	  }
	
	public void deleteFBUser(long id) throws Exception{
		  
		  try {
			 
			  preparedStatement = connect.prepareStatement("delete from AAL.FBUSER where id= ? ; ");
		      preparedStatement.setString(1, String.valueOf(id));
		      preparedStatement.executeUpdate();
		      
		  } 
		  catch (Exception e) {
		      throw e;
		  } 
	  }
	
	public void updateFBUser(long id, FacebookData user) throws Exception{
		  
		  try {
			  
			  Calendar cal = Calendar.getInstance();
			 
			  preparedStatement = connect.prepareStatement("update AAL.FBUSER set fb_id =?, picture=? where id= ? ; ");
			  preparedStatement.setLong(1, user.getFbid());
		      preparedStatement.setString(2, user.getPicture());
		      preparedStatement.setLong(3, id);
		      preparedStatement.executeUpdate(); 

		  } 
		  catch (Exception e) {
		      throw e;
		  } 
	}
	
}
