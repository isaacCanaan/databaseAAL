package access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;

import jiac.Settings;
import objects.LivingUser;

public class UserAccess {
	
	  private Connection connect = null;
	  private Statement statement = null;
	  private PreparedStatement preparedStatement = null;
	  private ResultSet resultSet = null;
	
	  public UserAccess(Connection connect){
		  this.connect = connect;
	  }
	  
	// a method for creating a new user
	  public void saveNewUser(String name, String lastname, String gender, String email, String website) throws Exception{

		  try {

			  // get current date	  
			  Calendar cal = Calendar.getInstance();
			  
			  preparedStatement = connect.prepareStatement("insert into  AAL.USER values (default, ?, ?, ?, ? , ?, ?)");
			  preparedStatement.setString(1, name);
		      preparedStatement.setString(2, lastname);
		      preparedStatement.setString(3, gender);
		      preparedStatement.setString(4, email);
		      preparedStatement.setString(5, website);
		      preparedStatement.setTimestamp(6, new java.sql.Timestamp(cal.getTimeInMillis()));
		      preparedStatement.executeUpdate();  
		      
		  } 
		  catch (Exception e) {
		      throw e;
	      } 

	  }
	  
	  // methode to update user profile
	  // works flawless, but more efficient way to save data is needed
	  public void updateUser(long id, LivingUser user) throws Exception{
		  
		  try {
			  
			  Calendar cal = Calendar.getInstance();
			 
			  preparedStatement = connect.prepareStatement("update AAL.USER set name=?, last_name=?, gender=?, email=?, webpage=?, timestamp=? where id= ? ; ");
			  preparedStatement.setString(1, user.getName());
			  preparedStatement.setString(2, user.getLastame());
			  preparedStatement.setString(3, user.getGender());
			  preparedStatement.setString(4, user.getEmail());
			  preparedStatement.setString(5, user.getWebpage());
			  preparedStatement.setTimestamp(6, new java.sql.Timestamp(cal.getTimeInMillis()));
		      preparedStatement.setLong(7, id);
		      preparedStatement.executeUpdate();
		  } 
		  catch (Exception e) {
		      throw e;
		  } 
		  
	  }
	  
	  
	  // method to delete an user with given id
	  // works flawless
	  public void deleteUser(long id) throws Exception{

		  try {
			 
			  preparedStatement = connect.prepareStatement("delete from AAL.USER where id= ? ; ");
		      preparedStatement.setString(1, String.valueOf(id));
		      preparedStatement.executeUpdate();
	          
	          // deleting the profile results in deleting of the facebook-datas as well
	          SocialAccess fbaccess = new SocialAccess(connect);
	          fbaccess.deleteFBUser(id);
	          
		  } 
		  catch (Exception e) {
		      throw e;
		  } 
	  }
	  
	  // get all datas of an user of given id
	  // works flawless
	  public LivingUser readUser(long id) throws Exception{
		  
		  LivingUser user = new LivingUser();
		  
		  try {
			  
		      preparedStatement = connect.prepareStatement("SELECT id, name, last_name, gender, email, webpage, timestamp from AAL.USER where id= ? ; ");
		      preparedStatement.setString(1, String.valueOf(id));
	          resultSet = preparedStatement.executeQuery();
	          
	          // put all datas into an user-object
	          while (resultSet.next()) {

	    	      user.setName(resultSet.getString("name"));
	    	      user.setLastname(resultSet.getString("last_name"));
	    	      user.setGender(resultSet.getString("gender"));
	    	      user.setEmail(resultSet.getString("email"));
	    	      user.setWebsite(resultSet.getString("webpage"));
	    	      user.setTimestamp(resultSet.getTimestamp(7));

	    	    }
		  } 
		  catch (Exception e) {
		      throw e;
		  } 
		  
		  // no user found with given id
		  if(user.getName().equals("")){
			  return null;
		  }
		  
		  return user;
		  
	  }
	  
	  public void saveKey(int id, String key, String value) throws Exception{
		  
		  try {
			  
			  preparedStatement = connect.prepareStatement("SELECT * from AAL.KEY where id= ? and key = ?; ");
		      preparedStatement.setInt(1, id);
		      preparedStatement.setString(1, key);
	          resultSet = preparedStatement.executeQuery();
	          
	          if(resultSet.next()) {
	        	  preparedStatement = connect.prepareStatement("update AAL.KEY set value = ? where id= ? and key = ?; ");
				  preparedStatement.setString(1, value);
				  preparedStatement.setInt(2, id);
			      preparedStatement.setString(3, key);
			      preparedStatement.executeUpdate();
	    	  }
	          else{
	        	  preparedStatement = connect.prepareStatement("insert into AAL.KEY values (? , ?, ?)");
				  preparedStatement.setInt(1, id);
			      preparedStatement.setString(2, key);
			      preparedStatement.setString(3, value);
			      preparedStatement.executeUpdate();  
	          }
		  } 
		  catch (Exception e) {
		      throw e;
		  }
	  }
	  
	  public HashMap<String,String> getKeys(int id) throws SQLException{
		  
		  HashMap<String,String> map = new HashMap<String,String>();
		  
		  preparedStatement = connect.prepareStatement("SELECT * from AAL.KEY where id= ?; ");
	      preparedStatement.setInt(1, id);
          resultSet = preparedStatement.executeQuery();
          
          while(resultSet.next()){
        	  map.put(resultSet.getString("key"), resultSet.getString("value"));
          }
		  
		  return map;
	  }
	  
	  public void savePreferences(int id, Settings sets) throws Exception{
		  try {
			  
			  preparedStatement = connect.prepareStatement("SELECT * from AAL.PREFERENCES where id= ?; ");
		      preparedStatement.setInt(1, id);
	          resultSet = preparedStatement.executeQuery();
	          
	          if(resultSet.next()) {
	        	  preparedStatement = connect.prepareStatement("update AAL.PREFERENCES set news = ?, todo = ?, calendar = ?, social =?, info = ? where id= ?; ");
				  preparedStatement.setBoolean(1, sets.isNewsPrivate());
				  preparedStatement.setBoolean(2, sets.isTodosPrivate());
				  preparedStatement.setBoolean(3, sets.isCalendarPrivate());
				  preparedStatement.setBoolean(4, sets.isSocialPrivate());
				  preparedStatement.setBoolean(5, sets.isInfoPrivate());
				  preparedStatement.setInt(6, id);
			      preparedStatement.executeUpdate();
	    	  }
	          else{
	        	  preparedStatement = connect.prepareStatement("insert into AAL.PREFERENCES values (?, ?, ?, ?, ?, ?)");
				  preparedStatement.setInt(1, id);
				  preparedStatement.setBoolean(2, sets.isNewsPrivate());
				  preparedStatement.setBoolean(3, sets.isTodosPrivate());
				  preparedStatement.setBoolean(4, sets.isCalendarPrivate());
				  preparedStatement.setBoolean(5, sets.isSocialPrivate());
				  preparedStatement.setBoolean(6, sets.isInfoPrivate());
			      preparedStatement.executeUpdate();  
	          }
		  } 
		  catch (Exception e) {
		      throw e;
		  }
	  }
	  
	  public Settings getPreferences(int id) throws SQLException{
		  
		  Settings sets = new Settings();
		  
		  preparedStatement = connect.prepareStatement("SELECT * from AAL.PREFERENCES where id= ?; ");
	      preparedStatement.setInt(1, id);
          resultSet = preparedStatement.executeQuery();
          
          while(resultSet.next()){
        	  sets.setNewsPrivacy(resultSet.getBoolean("news"));
        	  sets.setTodosPrivacy(resultSet.getBoolean("todo"));	
        	  sets.setCalendarPrivacy(resultSet.getBoolean("calendar"));       		
        	  sets.setSocialPrivacy(resultSet.getBoolean("social"));      		
        	  sets.setInfoPrivacy(resultSet.getBoolean("info"));
          }
		  
		  return sets;
	  }

}
