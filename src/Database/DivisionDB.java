package Database;

import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**This class contains a function to get all divisions from the first_level_divisions table in the database.*/
public class DivisionDB {

        /**This function creates an array list of all divisions from the database.
        @return allDivisions - First a connection is established with the database.
        Then a select query is executed. Division objects are created and then added
        to the allDivisions list. The connection to the database is closed. Lastly the
        allDivisions list is returned.*/
        public static ObservableList<Division> getAllDivisions() throws SQLException {

            // Connect to database and create statement object
            MainDB.setStatement(MainDB.startConnection());
            // Get statement reference
            Statement statement = MainDB.getStatement();

            // Select and execute statement
            String dbQuery = "SELECT first_level_divisions.Division_ID, first_level_divisions.Division, first_level_divisions.COUNTRY_ID "
                            + "FROM first_level_divisions";

            statement.execute(dbQuery);
            // Get ResultSet
            ResultSet rs = statement.getResultSet();

            // Initialize empty list for all divisions
            ObservableList<Division> allDivisions = FXCollections.observableArrayList();

            // Loop through ResultSet until null, next() is a boolean
            while (rs.next()) {
                // Initialize temp division, sets variables
                Division tempDivision = new Division();
                tempDivision.setDivisionId(rs.getInt("Division_ID"));
                tempDivision.setDivisionName(rs.getString("Division"));
                tempDivision.setCountryId(rs.getInt("COUNTRY_ID"));

                // Adds temp division to all divisions list
                allDivisions.add(tempDivision);
            }
            //close DB connection
            MainDB.closeConnection();
            return allDivisions;
        }
}
