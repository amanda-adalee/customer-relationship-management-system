package Database;

import java.sql.*;

public class MainDB {

    // Parts for JDBC
    private static String protocol = "jdbc";
    private static String vendorName =":mysql:";
    private static String ipAddress = "//wgudb.ucertify.com/WJ06Ah5";

    // JDBC Url
    private static String jdbcUrl = protocol + vendorName + ipAddress;

    // Driver and Connection Interface Reference
    private static String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    // Username & Password
    private static String userName ="U06Ah5";
    private static String passWord ="53688706629";

    // This function establishes a connection with the database, or throws an error
    public static Connection startConnection ()
    {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection) DriverManager.getConnection(jdbcUrl, userName, passWord);
            //System.out.println("Connection was established.");
        }
        catch(ClassNotFoundException e){
            System.out.println("Error:" + e.getMessage());
        }
        catch(SQLException e){
            System.out.println("Error:" + e.getMessage());
        }

        return conn;
    }

    // This method closes the connection with the database
    public static void closeConnection() {
        try {
            conn.close();
            //System.out.println("Connection was closed.");
        }
        catch(SQLException e){
            System.out.println("Error:" + e.getMessage());
        }
    }

    // Statement reference
    public static Statement statement;
    public static PreparedStatement preparedStatement;

    // Create statement object
    public static void setStatement(Connection conn) throws SQLException {
        statement = conn.createStatement();
    }

    // Return statement object
    public static Statement getStatement() {
        return statement;
    }

    // Create prepared statement object
    public static void setPreparedStatement(Connection conn, String query) throws  SQLException{
        preparedStatement = conn.prepareStatement(query);
    }

    // Return prepared statement object
    public static PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

}

