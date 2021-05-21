package Database;

import Model.Country;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**This class contains a function to get all countries from the countries table in the database.*/
public class CountryDB {

        /**This function creates an array list of all countries from the database.
        @return allCountries - First a connection is established with the database.
        Then a select query is executed. Country objects are created and then added
        to the allCountries list. The connection to the database is closed. Lastly the
        allCountries list is returned.*/
        public static ObservableList<Country> getAllCountries() throws SQLException {

            // Connect to database and create statement object
            MainDB.setStatement(MainDB.startConnection());
            // Get statement reference
            Statement statement = MainDB.getStatement();


            // Select and execute statement
            String dbQuery = "SELECT countries.Country_ID, countries.Country FROM countries";

            statement.execute(dbQuery);
            // Get ResultSet
            ResultSet rs = statement.getResultSet();

            // Initialize empty list for all countries
            ObservableList<Country> allCountries = FXCollections.observableArrayList();

            // Loop through ResultSet until null, next() is a boolean
            while (rs.next()) {
                // Initialize temp customer, sets variables
                Country tempCountry = new Country();
                tempCountry.setCountryId(rs.getInt("Country_ID"));
                tempCountry.setCountryName(rs.getString("Country"));

                // Adds temp country to all country list
                allCountries.add(tempCountry);
            }
            //close DB connection
            MainDB.closeConnection();
            return allCountries;
        }

}
