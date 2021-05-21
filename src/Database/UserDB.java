package Database;

import Model.User;

import java.sql.*;


public class UserDB {

    public static User sessionUser;

    public static Boolean validateUser(String username, String password) throws SQLException {

       // Connect to database and create statement object
       MainDB.setStatement(MainDB.startConnection());
       // Get statement reference
       Statement statement = MainDB.getStatement();


       // Select and execute statement
       String dbQuery = "SELECT * FROM users WHERE User_Name='" + username + "' AND Password='" + password + "'";
       statement.execute(dbQuery);
       // Get ResultSet
       ResultSet rs = statement.getResultSet();

       // Loop through ResultSet until null, next() is a boolean
       while (rs.next()) {
           sessionUser = new User();
           sessionUser.setUserName(rs.getString("User_Name"));
           sessionUser.setUserId(rs.getInt("User_ID"));

            /** If statement checks whether the information in the named columns
            equal to the arguments that were passed in, both condition statements must to be true.*/
           if (rs.getString("User_Name").equals(username) && rs.getString("Password").equals(password)) {

               MainDB.closeConnection(); // close DB connection
               return true;
           }
       }
       //close DB connection
       MainDB.closeConnection();
       return false;
   }

}