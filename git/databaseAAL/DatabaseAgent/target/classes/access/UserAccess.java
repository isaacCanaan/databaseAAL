package access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;

import jiac.Settings;
import objects.GmailData;
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
	  
	  // method to update user profile
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
	  
	  // method to save the key of an user with given id
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
	  
	  
	  // method to get the key of an user with given id
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
	  
	  // method to save the preferences of an user with given id
	  public void savePreferences(int id, Settings sets) throws Exception{
		  try {
			  
			  // check whether the preferences exist or not
			  preparedStatement = connect.prepareStatement("SELECT * from AAL.PREFERENCES where id= ?; ");
		      preparedStatement.setInt(1, id);
	          resultSet = preparedStatement.executeQuery();
	          
	          // if they do exist then perform an update
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
	          // otherwise insert the preferences into the database
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
	  
	  // method to get the preferences
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
	  
	  // method to save the gmail-account data in the database
	  public void saveGmailData(int id, String mail, String pw) throws SQLException{
		  
		  // check whether the information exists in the database or not
		  preparedStatement = connect.prepareStatement("SELECT * from AAL.GMAILDATA where id= ?; ");
	      preparedStatement.setInt(1, id);
          resultSet = preparedStatement.executeQuery();
          
          // if it does then perform an update
          if(resultSet.next()){
        	  preparedStatement = connect.prepareStatement("update AAL.GMAILDATA set mail = ?, password = ? where id= ? ; ");
    	      preparedStatement.setString(1, mail);
    	      preparedStatement.setString(2, pw);
    	      preparedStatement.setInt(3, id);
              preparedStatement.executeUpdate();
          }
          // otherwise insert the datas
          else{
        	  preparedStatement = connect.prepareStatement("insert into AAL.GMAILDATA values (?, ?, ?)");
        	  preparedStatement.setInt(1, id);
    	      preparedStatement.setString(2, mail);
    	      preparedStatement.setString(3, pw);
    	      preparedStatement.executeUpdate();
          }
		  
	  }
	  
	  // method to get the gmail-account information
	  public GmailData getGmailData(int id) throws SQLException{
		  
		  preparedStatement = connect.prepareStatement("SELECT * from AAL.GMAILDATA where id= ?; ");
	      preparedStatement.setInt(1, id);
          resultSet = preparedStatement.executeQuery();
          
          if(resultSet.next()){
        	  GmailData data = new GmailData(id, resultSet.getString("mail"), resultSet.getString("password"));
        	  
        	  return data;
          }
          
          return null;
	  }
	  
	  // method to find the id of an user with given mail-address
	  public int findGmailUser(String mail) throws SQLException{
		  
		  int id = 0;
		  
		  preparedStatement = connect.prepareStatement("SELECT id from AAL.GMAILDATA where mail= ?; ");
	      preparedStatement.setString(1, mail);
          resultSet = preparedStatement.executeQuery();
          
          if(resultSet.next()){
        	  id = resultSet.getInt("id");
          }
          
          return id;
	  }

}
