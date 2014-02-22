package access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

public class RecognitionAccess {

	private Connection connect = null;
	private Statement statement = null;
  	private PreparedStatement preparedStatement = null;
  	private ResultSet resultSet = null;

  	public RecognitionAccess(Connection connect){
  		this.connect = connect;
  	}
  	
  	public void saveRecData(int id, String qrString, byte[] pic) throws Exception{

		  try {
			  
			  preparedStatement = connect.prepareStatement("insert into  AAL.RECOGNITION values (?, ?, ?)");
			  preparedStatement.setInt(1, id);
		      preparedStatement.setString(2, qrString);
		      preparedStatement.setBytes(3, pic);
		      preparedStatement.executeUpdate();  
		      
		  } 
		  catch (Exception e) {
		      throw e;
	      } 
  	}
  	
  	public int findUserId(String qrString) throws Exception{
  		
  		try {
			  
			preparedStatement = connect.prepareStatement("SELECT id from AAL.RECOGNITION where qrString= ? ; ");
			preparedStatement.setString(1, String.valueOf(qrString));
			resultSet = preparedStatement.executeQuery();
			  
			// put all datas into an user-object
			while (resultSet.next()) {

				return resultSet.getInt("id");
			}
  		}	 
  		catch (Exception e) {
  			throw e;
  		} 
  	
  		return 0;
  	
  	}
}
  	

