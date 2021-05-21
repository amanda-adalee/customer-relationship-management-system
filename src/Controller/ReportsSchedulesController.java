package Controller;

import Database.AppointmentDB;
import Database.ContactDB;
import Model.Appointment;
import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import java.util.ResourceBundle;

/** This class populates a table view report of the appointments for each contact. A user can select a contact from a
 combo box, once the contact is selected the table view will populate the appointments for that contact. */
public class ReportsSchedulesController implements Initializable {

    Stage stage;
    Parent scene;

    ObservableList <Appointment> allAppointments = FXCollections.observableArrayList();
    ObservableList <Appointment> currentContactAppointments = FXCollections.observableArrayList();
    ObservableList <Contact> allContacts = FXCollections.observableArrayList();

    //region FXML Variables
    @FXML
    private TableView<Appointment> appointmentsTableView;

    @FXML
    private TableColumn<Appointment, Integer> appointmentId;

    @FXML
    private TableColumn<Appointment, Integer> customerId;

    @FXML
    private TableColumn<Appointment, String> customerName;

    @FXML
    private TableColumn<Appointment, String> appointmentTitle;

    @FXML
    private TableColumn<Appointment, String> appointmentDescription;

    @FXML
    private TableColumn<Appointment, String> appointmentLocation;

    @FXML
    private TableColumn<Appointment, String> appointmentType;

    @FXML
    private TableColumn<Appointment, Date> appointmentStartDate;

    @FXML
    private TableColumn<Appointment, Time> appointmentStartTime;

    @FXML
    private TableColumn<Appointment, Date> appointmentEndDate;

    @FXML
    private TableColumn<Appointment, Time> appointmentEndTime;

    @FXML
    private ComboBox<Contact> contactComboBox;

    //endregion

    /**This button will load the AppointmentMenu scene.*/
    @FXML
    void appointmentsButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**This button will load the CustomerMenu scene.*/
    @FXML
    void customerInformationButton(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomerMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**This button will load the ReportsCustomers scene.*/
    @FXML
    void customersFilteredByCountriesButton(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportsCustomers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**This button will load the ReportsAppointments scene.*/
    @FXML
    void totalCustomerAppointmentsButton(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportsAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**This combo box sets the items for the appointment table view. A for each loop will go through the all appointments
     list and add to a  new list the appointments whose contact names match the value from the contact combo box.
     The appointment table view items will be set to this list. */
    @FXML
    void onActionContactComboBox(ActionEvent event) {

        currentContactAppointments.clear();

        for (Appointment appointment : allAppointments) {
            if (appointment.getContactName().equals(contactComboBox.getValue().getContactName())) {
                currentContactAppointments.add(appointment);
            }
        }
        appointmentsTableView.setItems(currentContactAppointments);
    }

    /**This function sets the contact combo box items to the all contacts list.*/
    private void populateContactComboBox() {
        try {
            allContacts = ContactDB.getAllContacts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contactComboBox.setItems(allContacts);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            allAppointments = AppointmentDB.getAllAppointments();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        populateContactComboBox();

        // binding
        appointmentsTableView.setItems(allAppointments);
        appointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        appointmentStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        appointmentEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        appointmentEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }

}
