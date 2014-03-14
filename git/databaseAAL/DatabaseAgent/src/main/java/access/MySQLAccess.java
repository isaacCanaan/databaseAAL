package access;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MySQLAccess {
	
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;
  
  final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  
  // method to get the current date and time
  public String now(){
	  
	Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    return sdf.format(cal.getTime());
  }
  
  // get connection to driver
  public Connection connectDriver() throws Exception{
	 
	  Connection connect = null;
	  
	  try {
		  Class.forName("com.mysql.jdbc.Driver");
		  connect = DriverManager.getConnection("jdbc:mysql://r3ach.myds.me/AAL?" + "user=AAL&password=gruppe02");
		  return connect;
	  } 
	  catch (Exception e) {
		  throw e;
	  }
  }
  
  // dummy-method
  private void writeMetaData(ResultSet resultSet) throws SQLException {
    //   Now get some metadata from the database
    // Result set get the result of the SQL query
    
    System.out.println("The columns in the table are: ");
    
    System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
    for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
      System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
    }
  }


  // method to end the connection to driver
  public void close(Connection connect) {
    try {
      if (resultSet != null) {
        resultSet.close();
      }

      if (statement != null) {
        statement.close();
      }

      if (connect != null) {
        connect.close();
      }
    } catch (Exception e) {

    }
  }

} 