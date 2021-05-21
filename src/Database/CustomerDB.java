package Database;

import Model.Country;
import Model.Customer;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**This class contains functions to get, add, update, and delete from the customers table in the database.
 Each function starts a connection with the database, executes a query, and lastly close the connection
 with the database. */
public class CustomerDB {


    /**This function creates an array list of all customers from the database.
     @return allCustomers - First a connection is established with the database.
     Then a select query is executed. Customer objects are created and then added
     to the allCustomers list. The connection to the database is closed. Lastly the
     allCustomers list is returned.*/
    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        // Connect to database and create statement object
        MainDB.setStatement(MainDB.startConnection());
        // Get statement reference
        Statement statement = MainDB.getStatement();


        // Select and execute statement
        String dbQuery = "SELECT *"
                        + " FROM customers"
                        + " INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID"
                        + " INNER JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID"
                        + " ORDER BY Customer_ID ASC";
        statement.execute(dbQuery);
        // Get ResultSet
        ResultSet rs = statement.getResultSet();

        // Initialize empty list for all customers
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

        // Loop through ResultSet until null, next() is a boolean
        while (rs.next()) {
            // Initialize temp customer, sets variables
            Customer tempCustomer = new Customer();
            Country tempCountry = new Country();
            Division tempDivision = new Division();

            tempCustomer.setCustomerId(rs.getInt("Customer_ID"));
            tempCustomer.setCustomerName(rs.getString("Customer_Name"));
            tempCustomer.setCustomerAddress(rs.getString("Address"));
            tempCustomer.setCustomerPostalCode(rs.getString("Postal_Code"));
            tempCustomer.setCustomerPhoneNumber(rs.getString("Phone"));
            tempDivision.setDivisionName(rs.getString("Division"));
            tempDivision.setDivisionId(rs.getInt("Division_ID"));
            tempCountry.setCountryName(rs.getString("Country"));
            tempCountry.setCountryId(rs.getInt("Country_ID"));

            // Set customer country with temCountry values
            tempCustomer.setCustomerCountry(tempCountry);
            // Set customer country with temCountry values
            tempCustomer.setCustomerDivision(tempDivision);
            // Adds temp customer to all customer list
            allCustomers.add(tempCustomer);

        }
        //close DB connection
        MainDB.closeConnection();
        return allCustomers;
    }

    /**This function passes in a customer object and adds it into the database.
     @param customer this object is added to the database. The object's values are
     added to the respective columns in the database.
     @return boolean - true or false. If the object is successfully added to the database true is
     returned, if an error occurs and the insert is unsuccessful false will be returned.
     */
    public static boolean addCustomer (Customer customer) {
        try {

            String insertQuery = "INSERT INTO customers (Customer_Name, Address, Postal_Code,"
                    + "Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)"
                    + "VALUES ( ? , ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?, ?)";

            // Connect to database and create prepared statement object
            MainDB.setPreparedStatement(MainDB.startConnection(), insertQuery);

            // Get prepared statement reference
            PreparedStatement ps = MainDB.getPreparedStatement();

            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getCustomerAddress());
            ps.setString(3, customer.getCustomerPostalCode());
            ps.setString(4, customer.getCustomerPhoneNumber());
            ps.setString(5, UserDB.sessionUser.getUserName());
            ps.setString(6, UserDB.sessionUser.getUserName());
            ps.setInt(7, customer.getCustomerDivision().getDivisionId());

            // Execute prepared statement
            ps.execute();
            MainDB.closeConnection();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MainDB.closeConnection();
        return false;
    }

    /**This function passes in a customerId and deletes from the database the row that has that customerID.
    @param customerId this integer will be looked for in the database so that any row whose customerId matches
    this integer will be deleted.
    @return boolean - true or false. If the object is successfully deleted from the database true is
    returned, if an error occurs and the delete is unsuccessful false will be returned.*/
    public static  boolean deleteCustomer(int customerId) {
        try {
            String deleteQuery = "DELETE FROM customers WHERE Customer_ID = ?";

            // Connect to database and create prepared statement object
            MainDB.setPreparedStatement(MainDB.startConnection(), deleteQuery);
            // Get prepared statement reference
            PreparedStatement ps = MainDB.getPreparedStatement();

            ps.setInt(1, customerId);

            // Execute prepared statement
            ps.execute();
            MainDB.closeConnection();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        MainDB.closeConnection();
        return false;
    }

    /**This function passes in a customer object and updates it into the database.
     @param customer this object is updates in the database. The object's values are
     updated to the respective columns in the database.
     @return boolean - true or false. If the object is successfully updated in the database true is
     returned, if an error occurs and the insert is unsuccessful false will be returned.
     */
    public static boolean updateCustomer(Customer customer) {
        try {

            String updateQuery = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?,"
                    + " Phone = ?, Last_Update = CURRENT_TIMESTAMP, Last_Updated_By = ?, Division_ID = ?"
                    + " WHERE Customer_ID = ?";

            // Connect to database and create prepared statement object
            MainDB.setPreparedStatement(MainDB.startConnection(), updateQuery);

            // Get prepared statement reference
            PreparedStatement ps = MainDB.getPreparedStatement();

            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getCustomerAddress());
            ps.setString(3, customer.getCustomerPostalCode());
            ps.setString(4, customer.getCustomerPhoneNumber());
            ps.setString(5, UserDB.sessionUser.getUserName());
            ps.setInt(6, customer.getCustomerDivision().getDivisionId());
            ps.setInt(7, customer.getCustomerId());

            // Execute prepared statement
            ps.executeUpdate();
            MainDB.closeConnection();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();

        }
        MainDB.closeConnection();
        return false;
    }

}
