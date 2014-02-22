package access;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import ontology.messages.FacebookData;

import com.restfb.types.FacebookType;

import facebook4j.Book;
import facebook4j.Game;
import facebook4j.Interest;
import facebook4j.Movie;
import facebook4j.Music;
import facebook4j.ResponseList;
import facebook4j.Television;

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
		  
		  FacebookData user = new FacebookData(0, "", "");
		  
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
	
}
