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
	  
	  public TodoAccess(Connection connect){
		  this.connect = connect;
	  }
	  
	  // Method to save a TodoItem by UserId, Priority, Text, Date
	  public void saveNewTodoItem(int userId, String prio, String text, Date date) throws Exception{
		  
		  
		  System.out.println("Save Test.");
		  try {
			  
			  // SQL Statement to insert the TodoItem in the TODO-Table
			  preparedStatement = connect.prepareStatement("insert into  AAL.TODO values (?, default, ?, ? , ?)");
			  // insert values into the SQL Statement
			  preparedStatement.setInt(1, userId);
		          preparedStatement.setString(2, prio);
		          preparedStatement.setString(3, text);
		          preparedStatement.setDate(4, new java.sql.Date(date.getTime()));
		          // execute the SQL-Statement
		          preparedStatement.executeUpdate();  
		    	      
		  } 
		  catch (Exception e) {
		      throw e;
	      } 
	  }
	  // update a TodoItem by its Id
	  public void updateTodoItem(int todoId, TodoData.TodoItem item) throws Exception{
		  
		  try {
			  // SQL Statement for update
			  preparedStatement = connect.prepareStatement("update AAL.TODO set prio=?, text=? where todoId= ? ; ");
			  preparedStatement.setString(1, item.prio);
			  preparedStatement.setString(2, item.text);
			  preparedStatement.setInt(3, todoId);
		          preparedStatement.executeUpdate();
		      
		  } 
		  catch (Exception e) {
		      throw e;
		  } 
		  
	  }
	  // delte a TodoItem by its Id
	  public void deleteTodo(int todoId) throws Exception{
		  
		  try {
			  // SQL Statement for delete
			  preparedStatement = connect.prepareStatement("delete from AAL.TODO where todoId= ? ; ");
		          preparedStatement.setString(1, String.valueOf(todoId));
		          preparedStatement.executeUpdate();
		      
		  } 
		  catch (Exception e) {
		      throw e;
		  } 
	  }
	  // Method to return a TodoItem by its Id
	  public TodoData.TodoItem readTodoItem(int todoId) throws Exception{
		  // prepare new TodoItem
		  TodoData data = new TodoData();
		  TodoData.TodoItem item = data.new TodoItem(todoId, null, null, null);
		  
		  try {
			  // SQL Statement to select a Item from TODO-Table
			  preparedStatement = connect.prepareStatement("SELECT userId, todoId, prio, text, date from AAL.TODO where todoId = ?");
	                  preparedStatement.setString(1, String.valueOf(todoId));
	                  resultSet = preparedStatement.executeQuery();
	          
	          
	          while (resultSet.next()) {
	          	  // get Information of a Todo 
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
	  // Method to return a list of TodoItems of a User
	  public  ArrayList<TodoData.TodoItem> readTodoItemList(int userId) throws Exception{
	  	  // prepare a list of TodoItems
		  ArrayList<TodoData.TodoItem> items = new ArrayList<TodoData.TodoItem>();
		  // TodoItem
		  TodoData data = new TodoData();
		  
		  try {
			  // SQL Statement for all TodoItems of a User
			  preparedStatement = connect.prepareStatement("SELECT todoId, prio, text, date from AAL.TODO where userId=?");
	                  preparedStatement.setInt(1, userId);
	                  resultSet = preparedStatement.executeQuery();
	          
	          // get all Items of the prepared Table
	          while (resultSet.next()) {
	                  // get TodoItem Information and add them to the Object
	        	  TodoData.TodoItem item = data.new TodoItem(0, null, null, null);
	        	  item.id = resultSet.getInt("todoId");
	    	          item.prio = resultSet.getString("prio");
	    	          item.text = resultSet.getString("text");
	    	          item.created = resultSet.getDate("date");
	    	          // add Item to the List
	    	          items.add(item);
	    	     
	    	    }
		  } 
		  catch (Exception e) {
		      throw e;
		  } 
		  
		  return items;
		  
	  }
	
}
