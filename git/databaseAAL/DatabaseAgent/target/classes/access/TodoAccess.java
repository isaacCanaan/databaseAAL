package access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import ontology.messages.TodoData;


public class TodoAccess {

	private Connection connect = null;
	  private PreparedStatement preparedStatement = null;
	  private ResultSet resultSet = null;
	  
	  
	  public void saveNewTodoItem(int userId, int todoId, String prio, String text, Date date) throws Exception{
		  	  
		  MySQLAccess access = new MySQLAccess();
		  
		  try {
			  
			  connect = access.connectDriver();
			  

			  
			  preparedStatement = connect.prepareStatement("insert into  AAL.TODO values (default, ?, ?, ?, ? , ?)");
			  preparedStatement.setInt(1, userId);
		      preparedStatement.setInt(2, todoId);
		      preparedStatement.setString(3, prio);
		      preparedStatement.setString(4, text);
		      preparedStatement.setDate(5, (java.sql.Date) date);
		      preparedStatement.executeUpdate();  
		     
		      
		  } 
		  catch (Exception e) {
		      throw e;
	      } 
	  }
	  
	  public void updateTodoItem(int todoId, TodoData.TodoItem item) throws Exception{
		  
		  MySQLAccess access = new MySQLAccess();
		  
		  try {
			  
			  connect = access.connectDriver();
			  
			  preparedStatement = connect.prepareStatement("update AAL.TODO set  prio=?, text=? where todoId= ? ; ");
			  preparedStatement.setString(3, item.prio);
			  preparedStatement.setString(4, item.text);
		      preparedStatement.executeUpdate();
		      
		  } 
		  catch (Exception e) {
		      throw e;
		  } 
		  
	  }
	  
	  public void deleteUser(int todoId) throws Exception{
		  
		  MySQLAccess access = new MySQLAccess();
		  
		  try {
			  
			  connect = access.connectDriver();
			 
			  preparedStatement = connect.prepareStatement("delete from AAL.TODO where todoId= ? ; ");
		      preparedStatement.setString(2, String.valueOf(todoId));
		      preparedStatement.executeUpdate();
		      
		  } 
		  catch (Exception e) {
		      throw e;
		  } 
	  }
	  
	  public TodoData.TodoItem readTodoItem(int todoId) throws Exception{
		  
		  TodoData data = new TodoData();
		  TodoData.TodoItem item = data.new TodoItem(todoId, null, null, null);
		  MySQLAccess access = new MySQLAccess();
		  
		  try {
			 
			  connect = access.connectDriver();
			  
			  preparedStatement = connect.prepareStatement("SELECT userId, todoId, prio, text, date from AAL.TODO where todoId = ?");
	          preparedStatement.setString(2, String.valueOf(todoId));
	          resultSet = preparedStatement.executeQuery();
	          
	          
	          while (resultSet.next()) {
	        	  item.id = resultSet.getInt("todoId");
	    	      item.prio = resultSet.getString("prio");
	    	      item.text = resultSet.getString("text");
	    	      item.created = resultSet.getDate("date");
	    	      

	    	    }
		  } 
		  catch (Exception e) {
		      throw e;
		  } 
		  
		  return item;
		  
	  }
	  
	  public  ArrayList<TodoData.TodoItem> readTodoItemList(int userId) throws Exception{
		  ArrayList<TodoData.TodoItem> items = new ArrayList<TodoData.TodoItem>();
		  TodoData data = new TodoData();
		  TodoData.TodoItem item = data.new TodoItem(0, null, null, null);
		  MySQLAccess access = new MySQLAccess();
		  
		  try {
				 
			  connect = access.connectDriver();
			  
			  preparedStatement = connect.prepareStatement("SELECT todoId, prio, text, date from AAL.TODO where userId=?");
	          preparedStatement.setString(1, String.valueOf(userId));
	          resultSet = preparedStatement.executeQuery();
	          
	          
	          while (resultSet.next()) {
	        	  item.id = resultSet.getInt("todoId");
	    	      item.prio = resultSet.getString("prio");
	    	      item.text = resultSet.getString("text");
	    	      item.created = resultSet.getDate("date");
	    	      
	    	      items.add(item);
	    	      

	    	    }
		  } 
		  catch (Exception e) {
		      throw e;
		  } 
		  
		  return items;
		  
	  }
	
}
