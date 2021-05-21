package Database;

import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**This class contains a function to get all contacts from the contacts table in the database.*/
public class ContactDB {

    /**This function creates an array list of all contacts from the database.
     @return allContacts - First a connection is established with the database.
     Then a select query is executed. Contact objects are created and then added
     to the allContacts list. The connection to the database is closed. Lastly the
     allContacts list is returned.*/
    public static ObservableList <Contact> getAllContacts () throws SQLException {

        // Connect to database and create statement object
        MainDB.setStatement(MainDB.startConnection());
        // Get statement reference
        Statement statement = MainDB.getStatement();

        // Select and execute statement
        String dbQuery = "SELECT * FROM contacts";
        statement.execute(dbQuery);

        // Get ResultSet
        ResultSet rs = statement.getResultSet();


        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        while (rs.next()) {

            Contact tempContact = new Contact();

            tempContact.setContactId(rs.getInt("Contact_ID"));
            tempContact.setContactName(rs.getString("Contact_Name"));
            tempContact.setContactEmail(rs.getString("Email"));

            allContacts.add(tempContact);
        }
        //close DB connection
        MainDB.closeConnection();

        return allContacts;
    }

}

