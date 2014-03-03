package access;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import ontology.messages.FacebookData;
import ontology.messages.GroupData;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient.AccessToken;
import facebook4j.Book;
import facebook4j.Game;
import facebook4j.Interest;
import facebook4j.Movie;
import facebook4j.Music;
import facebook4j.ResponseList;
import facebook4j.Television;

// not tested yet

public class SocialAccess {
	
	private static String appId = "688786967828835";
	private static String secretKey = "4761c8cec03889b8e7b8b6dd93eb91ee";
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public SocialAccess(Connection connect){
		this.connect = connect;
	}
	
	public void saveUserInfo(long id, FacebookData user) throws Exception{
		  
		  try {
			  
			  // get current date	  
			  Calendar cal = Calendar.getInstance();
			  
			  // check whether there is an instance for the facebook-datas in the database or nota
			  preparedStatement = connect.prepareStatement("SELECT * from AAL.FBUSER where id= ? ; ");
			  preparedStatement.setLong(1, id);
			  resultSet = preparedStatement.executeQuery();
			  
			  if(resultSet.next()){
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
		  
		  FacebookData user = new FacebookData("", "", 0);
		  
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
	
	public void saveInterests(long id, ResponseList<Book> books, ResponseList<Interest> interests, ResponseList<Music> music, ResponseList<Game> games, ResponseList<Movie> movies, ResponseList<Television> tv) throws Exception{
		
		try {
			 
			  for(Book book : books){
				  
				  preparedStatement = connect.prepareStatement("select * from AAL.FBBOOKS where id=? and bookid=?");
				  preparedStatement.setLong(1, id);
			      preparedStatement.setLong(2, Long.valueOf(book.getId()));	      
			      resultSet = preparedStatement.executeQuery();
		          
		          if(resultSet.next()){
		        	  continue;
		          }
		          else{
		        	  preparedStatement = connect.prepareStatement("insert into  AAL.FBBOOKS values (?, ?, ?, ?)");
					  preparedStatement.setLong(1, id);
				      preparedStatement.setLong(2, Long.valueOf(book.getId()));
				      preparedStatement.setString(3, book.getName());
				      preparedStatement.setString(4, book.getCategory());
					  
				      preparedStatement.executeUpdate(); 
		          }
			  }
			  
			  for(Interest interest : interests){
				  
				  preparedStatement = connect.prepareStatement("select * from AAL.FBINTERESTS where id=? and interestid=?");
				  preparedStatement.setLong(1, id);
			      preparedStatement.setLong(2, Long.valueOf(interest.getId()));	      
			      resultSet = preparedStatement.executeQuery();
		          
		          if(resultSet.next()){
		        	  continue;
		          }
		          else{
					  preparedStatement = connect.prepareStatement("insert into  AAL.FBINTERESTS values (?, ?, ?, ?)");
					  preparedStatement.setLong(1, id);
				      preparedStatement.setLong(2, Long.valueOf(interest.getId()));
				      preparedStatement.setString(3, interest.getName());
				      preparedStatement.setString(4, interest.getCategory());
					  
				      preparedStatement.executeUpdate(); 
		          }
			  }
			  
			  for(Music m : music){
				  
				  preparedStatement = connect.prepareStatement("select * from AAL.FBMUSICS where id=? and musicid=?");
				  preparedStatement.setLong(1, id);
			      preparedStatement.setLong(2, Long.valueOf(m.getId()));	      
			      resultSet = preparedStatement.executeQuery();
		          
		          if(resultSet.next()){
		        	  continue;
		          }
		          else{
		        	  preparedStatement = connect.prepareStatement("insert into  AAL.FBMUSICS values (?, ?, ?, ?)");
					  preparedStatement.setLong(1, id);
				      preparedStatement.setLong(2, Long.valueOf(m.getId()));
				      preparedStatement.setString(3, m.getName());
				      preparedStatement.setString(4, m.getCategory());
					  
				      preparedStatement.executeUpdate(); 
		          }
			  }
			  
			  for(Game game : games){
				  
				  preparedStatement = connect.prepareStatement("select * from AAL.FBGAMES where id=? and gameid=?");
				  preparedStatement.setLong(1, id);
			      preparedStatement.setLong(2, Long.valueOf(game.getId()));	      
			      resultSet = preparedStatement.executeQuery();
		          
		          if(resultSet.next()){
		        	  continue;
		          }
		          else{
		        	  preparedStatement = connect.prepareStatement("insert into  AAL.FBGAMES values (?, ?, ?, ?)");
					  preparedStatement.setLong(1, id);
				      preparedStatement.setLong(2, Long.valueOf(game.getId()));
				      preparedStatement.setString(3, game.getName());
				      preparedStatement.setString(4, game.getCategory());
					  
				      preparedStatement.executeUpdate(); 
		          }
			  }
			  
			  for(Movie movie : movies){
				  
				  preparedStatement = connect.prepareStatement("select * from AAL.FBMOVIES where id=? and movieid=?");
				  preparedStatement.setLong(1, id);
			      preparedStatement.setLong(2, Long.valueOf(movie.getId()));	      
			      resultSet = preparedStatement.executeQuery();
		          
		          if(resultSet.next()){
		        	  continue;
		          }
		          else{
		        	  preparedStatement = connect.prepareStatement("insert into  AAL.FBMOVIES values (?, ?, ?, ?)");
					  preparedStatement.setLong(1, id);
				      preparedStatement.setLong(2, Long.valueOf(movie.getId()));
				      preparedStatement.setString(3, movie.getName());
				      preparedStatement.setString(4, movie.getCategory());
					  
				      preparedStatement.executeUpdate();
		          } 
			  }
			  
			  for(Television t : tv){
				  
				  preparedStatement = connect.prepareStatement("select * from AAL.FBTVS where id=? and tvid=?");
				  preparedStatement.setLong(1, id);
			      preparedStatement.setLong(2, Long.valueOf(t.getId()));	      
			      resultSet = preparedStatement.executeQuery();
		          
		          if(resultSet.next()){
		        	  continue;
		          }
		          else{
		        	  preparedStatement = connect.prepareStatement("insert into  AAL.FBTVS values (?, ?, ?, ?)");
					  preparedStatement.setLong(1, id);
				      preparedStatement.setLong(2, Long.valueOf(t.getId()));
				      preparedStatement.setString(3, t.getName());
				      preparedStatement.setString(4, t.getCategory());
					  
				      preparedStatement.executeUpdate(); 
		          }
			  }

		  } 
		  catch (Exception e) {
		      throw e;
		  }
	}
	
	public void formGroup(long id) throws Exception{
		
		  try {
			  
			  formGroup(id, "book");
			  formGroup(id, "tv");
			  formGroup(id, "game");
			  formGroup(id, "movie");
			  formGroup(id, "interest");
			  formGroup(id, "music");
	          
		  } 
		  catch (Exception e) {
		      throw e;
		  } 
		
	}
	
	private void formGroup(long id, String label) throws SQLException{
		
		ArrayList<Long> ids = new ArrayList<Long>();
		ResultSet result;
		  
      	preparedStatement = connect.prepareStatement("SELECT "+label+"id from AAL.FB"+label.toUpperCase()+"S where id= ? ; ");
      	preparedStatement.setString(1, String.valueOf(id));
        resultSet = preparedStatement.executeQuery();
        
        // get all interest ids of interests that interest the user
        while (resultSet.next()) {
      	  	ids.add(resultSet.getLong(label+"id"));	        	 
  	  	}
        
        // for every interest find users with same preferences
        for(Long e : ids){
      	  
	  	  	preparedStatement = connect.prepareStatement("SELECT * from AAL.INTERESTGROUP where gid= ? ; ");
	  	  	preparedStatement.setString(1, String.valueOf(e));
	  	  	resultSet = preparedStatement.executeQuery();
	          
		      // check whether the group already exists and whether the user is already signed up.
		      if(resultSet.next()){
		    	  
		    	  
		    	  preparedStatement = connect.prepareStatement("update AAL.INTERESTGROUP set liked=? where gid= ? ; ");
				  preparedStatement.setBoolean(1, true);
				  preparedStatement.setLong(2, resultSet.getLong("gid"));
				  preparedStatement.executeUpdate();
				  
		    	  // look if the user needs to be signed up for the already existing group
		    	  preparedStatement = connect.prepareStatement("SELECT * from AAL.GROUPMEMBER where gid= ? and mid= ? ; ");
		    	  preparedStatement.setString(1, String.valueOf(resultSet.getLong("gid")));
		    	  preparedStatement.setString(2, String.valueOf(id));
		    	  result = preparedStatement.executeQuery();
		    	  
		    	  if(result.next()){
		    		  continue;
		    	  }
		    	  else{
		    		  preparedStatement = connect.prepareStatement("insert into  AAL.GROUPMEMBER values (?, ?)");
		    		  preparedStatement.setString(1, String.valueOf(resultSet.getLong("gid")));
			    	  preparedStatement.setString(2, String.valueOf(id));
			    	  preparedStatement.executeUpdate();
		    	  }
		      }
		  
		      // all users with same interest
		      preparedStatement = connect.prepareStatement("SELECT * from AAL.FB"+label.toUpperCase()+"S where "+label+"id= ? ; ");
		      preparedStatement.setString(1, String.valueOf(e));
	          resultSet = preparedStatement.executeQuery();
	    
	          // count the number of interested users
	          resultSet.last();
	          int count = resultSet.getRow();
	          resultSet.first();
	          
	          // if there are more than 1 user with same preference then form the group and make all users a member of the group
	          if(count > 1){
	        	  preparedStatement = connect.prepareStatement("insert into  AAL.INTERESTGROUP values (?, ?, ?, ?, ?)");
		  		  preparedStatement.setLong(1, resultSet.getLong(label+"id"));
		  		  preparedStatement.setString(2, resultSet.getString("name"));
		  		  preparedStatement.setString(3, resultSet.getString("category"));
		  		  preparedStatement.setString(4, "nothing");
		  		  preparedStatement.setBoolean(5, true);
		  		  preparedStatement.executeUpdate();
		  		  
		  		  resultSet.beforeFirst();
		  		  
		  		  while (resultSet.next()) {
		    		  preparedStatement = connect.prepareStatement("insert into  AAL.GROUPMEMBER values (?, ?)");
		    		  preparedStatement.setLong(1, resultSet.getLong(label+"id"));
		    		  preparedStatement.setLong(2, resultSet.getLong("id"));
		    		  preparedStatement.executeUpdate();
		          }        	 
	  		  }
        }
        
	}
	
	public ArrayList<GroupData.FBGroup> getGroup(ArrayList<Integer> members) throws SQLException{
		
		ArrayList<GroupData.FBGroup> groups = new ArrayList<GroupData.FBGroup>();

		int memCount = members.size();
		String sql1 = "SELECT * FROM ((SELECT gid, COUNT(mid) AS count from (SELECT * from AAL.GROUPMEMBER where";
		String sql2 = ") AS members GROUP BY gid HAVING count = ?) as members2 INNER JOIN AAL.INTERESTGROUP t ON t.gid = members2.gid);";
		String finalSQL = "";
		
		for(int i = 0; i < memCount; i++){
			sql1 += " mid = ?";
			if(i == memCount - 1){
				finalSQL = sql1 + sql2;
				break;
			}
			sql1 += " or";		
		}
		
		// SELECT * FROM ((SELECT gid, COUNT(mid) AS count from (SELECT * from AAL.GROUPMEMBER where mid = 23 or mid = 30) AS members GROUP BY gid HAVING count = ?) as members2 INNER JOIN AAL.INTERESTGROUP t ON t.gid = members2.gid);
		
		try{
			preparedStatement = connect.prepareStatement(finalSQL);
			for(int i = 0; i < memCount; i++){
				preparedStatement.setInt(i+1, members.get(i));
			}
			preparedStatement.setInt(memCount+1, memCount);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {

				GroupData data = new GroupData();
				GroupData.FBGroup group  = data.new FBGroup();
				group.setId(resultSet.getInt("gid"));
				group.setTopic(resultSet.getString("topic"));
				group.setMembers(members);
				
				groups.add(group);
				}
		}
		
		catch (Exception e) {
			throw e;
		} 
		return groups;
	}
	
	public String getAccessToken(int id, String accessToken, String type) throws SQLException{
		
		String aToken = accessToken;
		
		if(aToken.equals("")){
			preparedStatement = connect.prepareStatement("select * from AAL.ACCESSTOKEN where id=? and type=?");
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, type);		
			resultSet = preparedStatement.executeQuery();
	          
	          if(resultSet.next()){
	        	  aToken = resultSet.getString("token");
	        	  AccessToken token = new DefaultFacebookClient().obtainExtendedAccessToken(appId, secretKey, aToken);
	        	  aToken = token.toString();
	          }
		}
		
		saveAccessToken(id, aToken, type);
		
		return aToken;
	}
	
	private void saveAccessToken(int id, String accessToken, String type) throws SQLException{
		
		preparedStatement = connect.prepareStatement("select * from AAL.ACCESSTOKEN where id=? and type=?");
		preparedStatement.setInt(1, id);
		preparedStatement.setString(2, type);		
		resultSet = preparedStatement.executeQuery();
          
	    if(resultSet.next()){
	    	preparedStatement = connect.prepareStatement("update AAL.ACCESSTOKEN set token =? where id= ? and type=?; ");
	    	preparedStatement.setString(1, accessToken);
	    	preparedStatement.setInt(2, id);
	    	preparedStatement.setString(3, type);
	    	preparedStatement.executeUpdate(); 
	    }
	    else{
	    	 preparedStatement = connect.prepareStatement("insert into  AAL.ACCESSTOKEN values (?, ?, ?)");
	    	 preparedStatement.setLong(1, id);
	    	 preparedStatement.setString(2, accessToken);
	    	 preparedStatement.setString(3, type);
			 preparedStatement.executeUpdate();
	    }
 

	}
	
}
