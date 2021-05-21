package Controller;

import Database.AppointmentDB;
import Database.ContactDB;
import Database.CustomerDB;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Util.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

    /**In this class the user can update/modify appointments. Previous appointment details are displayed in text fields
    and combo boxes, which the user can modify to reflect the new appointment details, then save.*/
public class AppointmentModifyController implements Initializable {
    Stage stage;
    Parent scene;

    //region FXML Variables
    @FXML
    private TextField appointmentIdText;

    @FXML
    private TextField customerIdText;

    @FXML
    private TextField locationText;

    @FXML
    private TextField titleText;

    @FXML
    private TextField descriptionText;

    @FXML
    private ComboBox<Contact> contactComboBox;

    @FXML
    private ComboBox<Customer> customerNameComboBox;

    @FXML
    private DatePicker startDateText;

    @FXML
    private DatePicker endDateText;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private ComboBox<LocalTime> startTimeComboBox;

    @FXML
    private ComboBox<LocalTime> endTimeComboBox;

    //endregion

    private ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    private ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private ObservableList<LocalTime> allAppointmentTimes = FXCollections.observableArrayList();
    private ObservableList<LocalTime> allAppointmentStartTimes = FXCollections.observableArrayList();
    private ObservableList<LocalTime> availableAppointmentEndTimes = FXCollections.observableArrayList();
    private ObservableList<String> allAppointmentTypes = FXCollections.observableArrayList();

    /**This is a back button. When the back button is pressed the user will be taken to the appointment menu scene that
     will load. */
    @FXML
    void backUpdateAppointmentButton(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**This button saves the updated appointment to the database. Before saving, an input validating function is called.
     If the database is updated with the new appointment details then the appointment scene will load. If the update is
     not successful, the user will receive an error to try again.*/
    @FXML
    void updateAppointmentButton(ActionEvent event) throws IOException {

        if (!isInputValid()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("One or more text fields are empty.");
            errorAlert.showAndWait();
            return;
        }

        int appointmentId = Integer.parseInt(appointmentIdText.getText());
        int customerId = Integer.parseInt(customerIdText.getText());
        String customerName = customerNameComboBox.getValue().getCustomerName();
        String location = locationText.getText();
        String type = typeComboBox.getValue();
        String title = titleText.getText();
        String description = descriptionText.getText();
        String contactName = contactComboBox.getValue().getContactName();
        int contactId = contactComboBox.getValue().getContactId();
        LocalDate startDate = startDateText.getValue();
        LocalDate endDate = endDateText.getValue();
        LocalTime startTime = startTimeComboBox.getValue();
        LocalTime endTime = endTimeComboBox.getValue();

        Appointment tempAppointment = new Appointment();
        tempAppointment.setAppointmentId(appointmentId);
        tempAppointment.setCustomerId(customerId);
        tempAppointment.setCustomerName(customerName);
        tempAppointment.setLocation(location);
        tempAppointment.setType(type);
        tempAppointment.setTitle(title);
        tempAppointment.setDescription(description);
        tempAppointment.setContactName(contactName);
        tempAppointment.setContactId(contactId);
        tempAppointment.setStartDate(startDate);
        tempAppointment.setEndDate(endDate);
        tempAppointment.setStartTime(startTime);
        tempAppointment.setEndTime(endTime);
        tempAppointment.setStartDateTime(LocalDateTime.of(startDate, startTime));
        tempAppointment.setEndDateTime(LocalDateTime.of(endDate, endTime));

        if (Utilities.checkForAppointmentConflict(tempAppointment, true)) {
            return;
        }

        boolean result = AppointmentDB.updateAppointment(tempAppointment);

        if (result){
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("SQL Error");
            errorAlert.setContentText("There was an error updating the database. Please try again.");
            errorAlert.showAndWait();

        }

    }

    /**This function validates user input. It will validate that none of the text fields or combo boxes are empty.
     @return  isInputValid - true or false */
    private boolean isInputValid() {
        boolean isInputValid = true;

        if(customerNameComboBox.getSelectionModel().isEmpty()){
            isInputValid = false;
        }
        if(contactComboBox.getSelectionModel().isEmpty()){
            isInputValid = false;
        }
        if(startDateText.getValue() == null){
            isInputValid = false;
        }
        if(startTimeComboBox.getSelectionModel().isEmpty()){
            isInputValid = false;
        }
        if(endDateText.getValue() == null){
            isInputValid = false;
        }
        if(endTimeComboBox.getSelectionModel().isEmpty())
        {
            isInputValid = false;
        }

        if(locationText.getLength() == 0){
            isInputValid = false;
        }
        if(typeComboBox.getValue() == null){
            isInputValid = false;
        }
        if(descriptionText.getLength() == 0){
            isInputValid = false;
        }
        if(titleText.getLength() == 0){
            isInputValid = false;
        }
        return isInputValid;
    }

    /**This function sets the list, allContacts, to the contactComboBox. The user will be able to select a contact item
     from this list. */
    private void populateContactComboBox() {
        try {
            allContacts = ContactDB.getAllContacts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contactComboBox.setItems(allContacts);
    }

    /**This function sets the list, allCustomers, to the customerNameComboBox. The user will be able to select customer
     item from this list.*/
    private void populateCustomerComboBox() {
        try {
            allCustomers = CustomerDB.getAllCustomers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        customerNameComboBox.setItems(allCustomers);
    }

    /**This function sets the list, allAppointmentTimes, to the startTimeComboBox. The user will be able to select time
     item from this list.*/
    private void populateStartTimeComboBox() {
        allAppointmentTimes = Utilities.getAppointmentTimes();
        allAppointmentStartTimes = Utilities.getAppointmentTimes();
        allAppointmentStartTimes.remove(allAppointmentStartTimes.size() - 1);
        startTimeComboBox.setItems(allAppointmentStartTimes);
    }

    /**This function disables all dates prior to today. The user will not be able to select dates in the past for an
        updated appointment date. */
    private void disableDatesBeforeToday() {
        startDateText.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
    }

    /**This date picker calls the disableDatesBeforeStartTime function.*/
    @FXML
    public void onActionStartDateSelected(ActionEvent event) {
        disableDatesBeforeStartTime();
    }

    /**This function disables all dates prior to the start date so that no appointments end before they begin.
     When the user picks a start date on the date picker this function is called. The function will disable all dates
     prior to the start date on the end date picker. It also sets the end date to the value of the start date. It is
     assumed that no appointment shall last longer than a day. The user can change the end date, if they wish.*/
    private void disableDatesBeforeStartTime() {
        LocalDate startDate = startDateText.getValue();

        endDateText.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {

                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(startDate) < 0 );

            }
        });

        endDateText.setValue(startDate);
    }

    /**This combo box sets the endTimeComboBox to a list made up of times occurring after the start time selected.
     the for each statement goes through the appointment time list and adds all the times that are after the start
     time to a new list of available end times. The endTimeComboBox value is set to the first index of the new list,
     which is 30 minutes later. */
    @FXML
    public void onActionStartTimeSelected(ActionEvent event) {
        availableAppointmentEndTimes.clear();

        LocalTime appointmentStartTime = startTimeComboBox.getValue();
        for (LocalTime appointmentTime : allAppointmentTimes) {
            if (appointmentTime.isAfter(appointmentStartTime)){
                availableAppointmentEndTimes.add(appointmentTime);
            }
        }
        endTimeComboBox.setItems(availableAppointmentEndTimes);
        endTimeComboBox.setValue(availableAppointmentEndTimes.get(0));
    }

    /**This function determines the text set for the customer id field. When the user picks a customer name from
     this combo box, customer id text box is set to that selected customer's id. The customer id text field is
     disabled. The user can not change the customer's id. */
    @FXML
    public void onActionCustomerComboBoxSelected(ActionEvent event) {
        customerIdText.setText(Integer.toString(customerNameComboBox.getValue().getCustomerId()));
    }

    private void populateAvailableEndTimes() {

        LocalTime appointmentEndTime = endTimeComboBox.getValue();
        LocalTime appointmentStartTime = startTimeComboBox.getValue();

        availableAppointmentEndTimes.clear();
        for (LocalTime appointmentTime : allAppointmentTimes) {
            if (appointmentTime.isAfter(appointmentStartTime)){
                availableAppointmentEndTimes.add(appointmentTime);
            }
        }
        endTimeComboBox.setItems(availableAppointmentEndTimes);
        endTimeComboBox.setValue(appointmentEndTime);

    }

    /**This function sets the text for the text fields and for the combo boxes.
     @param appointment this appointment object was selected by the user in the appointment menu. It has been passed
     in order to make changes to the appointment.*/
    public void sentAppointment(Appointment appointment) {

        appointmentIdText.setText(Integer.toString(appointment.getAppointmentId()));
        customerIdText.setText(Integer.toString(appointment.getCustomerId()));
        titleText.setText(appointment.getTitle());
        descriptionText.setText(appointment.getDescription());
        locationText.setText(appointment.getLocation());
        typeComboBox.setValue(appointment.getType());

        startDateText.setValue(appointment.getStartDate());
        endDateText.setValue(appointment.getEndDate());
        startTimeComboBox.setValue(appointment.getStartTime());
        endTimeComboBox.setValue(appointment.getEndTime());
        disableDatesBeforeStartTime();
        populateAvailableEndTimes();

        // Populate customer combo box
        Customer tempCustomer = new Customer();
        for (Customer customer: allCustomers) {
            if (customer.getCustomerName().equals(appointment.getCustomerName())) {
                tempCustomer = customer;
                customerNameComboBox.setValue(tempCustomer);
                break;
            }
        }
        // Populate contact combo box
        Contact tempContact = new Contact();
        for (Contact contact: allContacts) {
            if (contact.getContactName().equals(appointment.getContactName())) {
                tempContact = contact;
                contactComboBox.setValue(tempContact);
                break;
            }
        }

    }

    private void populateTypeComboBox() {
        allAppointmentTypes = Utilities.getAppointmentTypes();
        typeComboBox.setItems(allAppointmentTypes);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        populateTypeComboBox();
        populateCustomerComboBox();
        populateContactComboBox();
        populateStartTimeComboBox();
        disableDatesBeforeToday();

    }

    }
