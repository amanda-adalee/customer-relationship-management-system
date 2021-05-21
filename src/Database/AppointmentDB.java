package Database;

import Model.Appointment;
import Model.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

/**This class contains functions to get, add, update, and delete from the appointments table in the database.
    Each function starts a connection with the database, executes a query, and lastly close the connection
    with the database. */
public class AppointmentDB {

    /**This function creates an array list of all appointments from the database.
     @return allAppointments - First a connection is established with the database.
     Then a select query is executed. Appointment objects are created and then added
     to the allAppointments list. The connection to the database is closed. Lastly the
     allAppointments list is returned.*/
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {

        // Connect to database and create statement object
        MainDB.setStatement(MainDB.startConnection());
        // Get statement reference
        Statement statement = MainDB.getStatement();

        // Select and execute statement
        String dbQuery = "SELECT * FROM appointments INNER JOIN customers ON customers.Customer_ID = appointments.Customer_ID "
                         + "INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID"
                         + " ORDER BY Appointment_ID ASC";;

        statement.execute(dbQuery);
        // Get ResultSet
        ResultSet rs = statement.getResultSet();

        // Initialize empty list for all appointments
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        // Loop through ResultSet until null, next() is a boolean
        while (rs.next()) {
            // Initialize temp appointment, sets variables
            Appointment tempAppointment = new Appointment();
            tempAppointment.setAppointmentId(rs.getInt("Appointment_ID"));
            tempAppointment.setTitle(rs.getString("Title"));
            tempAppointment.setDescription(rs.getString("Description"));
            tempAppointment.setLocation(rs.getString("Location"));
            tempAppointment.setType(rs.getString("Type"));
            tempAppointment.setCustomerId(rs.getInt("Customer_ID"));
            tempAppointment.setCustomerName(rs.getString("Customer_Name"));
            tempAppointment.setContactName(rs.getString("Contact_Name"));
            tempAppointment.setUserId(rs.getInt("User_ID"));
            tempAppointment.setEndDate(rs.getDate("End").toLocalDate());
            tempAppointment.setEndTime(rs.getTime("End").toLocalTime());
            tempAppointment.setEndDateTime(LocalDateTime.of(tempAppointment.getEndDate(),tempAppointment.getEndTime()));
            tempAppointment.setStartDate(rs.getDate("Start").toLocalDate());
            tempAppointment.setStartTime(rs.getTime("Start").toLocalTime());
            tempAppointment.setStartDateTime(LocalDateTime.of(tempAppointment.getStartDate(), tempAppointment.getStartTime()));

            // Adds temp appointment to all appointments list
            allAppointments.add(tempAppointment);
        }

        //close DB connection
        MainDB.closeConnection();

        return allAppointments;
    }

    /**This function passes in an appointment object and adds it into the database.
     @param appointment this object is added to the database. The object's values are
     added to the respective columns in the database.
     @return boolean - true or false. If the object is successfully added to the database true is
     returned, if an error occurs and the insert is unsuccessful false will be returned.
     */
    public static boolean addAppointment(Appointment appointment) {

        LocalDateTime start = LocalDateTime.of(appointment.getStartDate(), appointment.getStartTime());
        LocalDateTime end = LocalDateTime.of(appointment.getEndDate(), appointment.getEndTime());
        Timestamp startTimestamp = Timestamp.valueOf(start);
        Timestamp endTimestamp = Timestamp.valueOf(end);

        String insertQuery = "INSERT INTO appointments (Title, Description, Location, Type," +
                " Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)" +
                " VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?)";

        // Connect to database and create prepared statement object
        try {
            MainDB.setPreparedStatement(MainDB.startConnection(), insertQuery);

            // Get prepared statement reference
            PreparedStatement ps = MainDB.getPreparedStatement();

            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, startTimestamp);
            ps.setTimestamp(6, endTimestamp);
            ps.setString(7, UserDB.sessionUser.getUserName());
            ps.setString(8, UserDB.sessionUser.getUserName());
            ps.setInt(9, appointment.getCustomerId());
            ps.setInt(10, UserDB.sessionUser.getUserId());
            ps.setInt(11, appointment.getContactId());

            ps.execute();
            MainDB.closeConnection();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        MainDB.closeConnection();
        return false;



    }

    /**This function passes in an appointmentId and deletes from the database the row that has that appointmentId.
     @param appointmentId this integer will be looked for in the database so that any row whose appointmentId matches
     this integer will be deleted.
     @return boolean - true or false. If the object is successfully deleted from the database true is
     returned, if an error occurs and the delete is unsuccessful false will be returned.
     */
    public static boolean deleteAppointment(int appointmentId) {
        try {
            String deleteQuery = "DELETE FROM appointments WHERE Appointment_ID = ?";

            // Connect to database and create prepared statement object
            MainDB.setPreparedStatement(MainDB.startConnection(), deleteQuery);
            // Get prepared statement reference
            PreparedStatement ps = MainDB.getPreparedStatement();

            ps.setInt(1, appointmentId);

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

    /**This function passes in an appointment object and updates it into the database.
     @param appointment this object is updates in the database. The object's values are
     updated to the respective columns in the database.
     @return boolean - true or false. If the object is successfully updated in the database true is
     returned, if an error occurs and the insert is unsuccessful false will be returned.
     */
    public static boolean updateAppointment(Appointment appointment) {

        LocalDateTime start = LocalDateTime.of(appointment.getStartDate(), appointment.getStartTime());
        LocalDateTime end = LocalDateTime.of(appointment.getEndDate(), appointment.getEndTime());
        Timestamp startTimestamp = Timestamp.valueOf(start);
        Timestamp endTimestamp = Timestamp.valueOf(end);

        String updateQuery = "UPDATE appointments SET Title = ?, Description = ?, Location = ?," +
                " Type = ?, Start = ?, End = ?, Last_Update = CURRENT_TIMESTAMP, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?," +
                " Contact_ID = ? WHERE Appointment_ID = ?";

        // Connect to database and create prepared statement object
        try {
            MainDB.setPreparedStatement(MainDB.startConnection(), updateQuery);

            // Get prepared statement reference
            PreparedStatement ps = MainDB.getPreparedStatement();

            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, startTimestamp);
            ps.setTimestamp(6, endTimestamp);
            ps.setString(7, UserDB.sessionUser.getUserName());
            ps.setInt(8, appointment.getCustomerId());
            ps.setInt(9, UserDB.sessionUser.getUserId());
            ps.setInt(10, appointment.getContactId());
            ps.setInt(11, appointment.getAppointmentId());

            ps.executeUpdate();
            MainDB.closeConnection();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        MainDB.closeConnection();
        return false;

    }

    /**This function passes in a customerId and deletes from the database the row that has that customerId.
     @param customerId this integer will be looked for in the database so that any row whose customerId matches
     this integer will be deleted.
     @return boolean - true or false. If the object is successfully deleted from the database true is
     returned, if an error occurs and the delete is unsuccessful false will be returned.
     */
    public static boolean deleteAllAppointmentsByCustomer(int customerId) {
        try {
            String deleteQuery = "DELETE FROM appointments WHERE Customer_ID = ?";

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

    public static ObservableList<Report> getAllAppointmentsByType() throws SQLException {

        // Connect to database and create statement object
        MainDB.setStatement(MainDB.startConnection());
        // Get statement reference
        Statement statement = MainDB.getStatement();

        // Select and execute statement
        String dbQuery = "SELECT MONTHNAME(Start) AS 'Month', Type, COUNT(*) AS 'Amount' FROM appointments GROUP BY Month, Type";

        statement.execute(dbQuery);
        // Get ResultSet
        ResultSet rs = statement.getResultSet();

        // Initialize empty list for all appointments
        ObservableList<Report> allAppointmentsByTypes = FXCollections.observableArrayList();

        while (rs.next()) {
            Report tempReport = new Report();
            tempReport.setMonth(rs.getString("Month"));
            tempReport.setType(rs.getString("Type"));
            tempReport.setCount(rs.getInt("Amount"));
            allAppointmentsByTypes.add(tempReport);
        }

        //close DB connection
        MainDB.closeConnection();

        return allAppointmentsByTypes;
    }

}
