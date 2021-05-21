package Controller;

import Database.AppointmentDB;
import Database.ContactDB;
import Database.CustomerDB;
import Model.*;
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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.spi.LocaleNameProvider;

/**In this class the user can create/add an appointment. To add a new appointment the user must fill in the appointment
    details for the appointment, including customer and contact information. */
public class AppointmentAddController implements Initializable {

    Stage stage;
    Parent scene;

    private ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    private ObservableList<LocalTime> allAppointmentTimes = FXCollections.observableArrayList();
    private ObservableList<LocalTime> allAppointmentStartTimes = FXCollections.observableArrayList();
    private ObservableList<LocalTime> availableAppointmentEndTimes = FXCollections.observableArrayList();
    private ObservableList<String> allAppointmentTypes = FXCollections.observableArrayList();



    //region FXML Variables
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
    private ComboBox<String> typeComboBox;

    @FXML
    private DatePicker startDateText;

    @FXML
    private DatePicker endDateText;

    @FXML
    private ComboBox<LocalTime> startTimeComboBox;

    @FXML
    private ComboBox<LocalTime> endTimeComboBox;


    //endregion

    /**This button saves the new appointment to the database. Before saving, an input validating function is called.
        If the database is updated with the new appointment then the appointment menu scene will load. If the update is not
        successful, the user will receive an error to try again.*/
    @FXML
    void addNewAppointmentButton(ActionEvent event) throws IOException {

        if (!isInputValid()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("One or more text fields are empty.");
            errorAlert.showAndWait();
            return;
        }

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

        if (Utilities.checkForAppointmentConflict(tempAppointment, false)) {
            return;
        }

        boolean result = AppointmentDB.addAppointment(tempAppointment);

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

    /**This is a back button. When the back button is pressed the user will be taken to the appointment menu scene that
        will load. */
    @FXML
    void backNewAppointmentButton(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionStartTimeComboBox(ActionEvent event) {

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

    /**This text field will auto populate with the customer id. When a customer name is selected in the customer
        combo box, a customer id will be auto populated for this text field.*/
    @FXML
    void populateCustomerIdTextField(ActionEvent event) {

        int customerId = customerNameComboBox.getValue().getCustomerId();
        customerIdText.setText(Integer.toString(customerId));
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

    /**This function sets the list, allAppointmentTimes, to the startTimeComboBox. The user will be able to select time
      item from this list.*/
    private void populateStartTimeComboBox() {
        allAppointmentTimes = Utilities.getAppointmentTimes();
        allAppointmentStartTimes = Utilities.getAppointmentTimes();
        allAppointmentStartTimes.remove(allAppointmentStartTimes.size() - 1);
        startTimeComboBox.setItems(allAppointmentStartTimes);

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

    /**This function disables all dates prior to today. The user will not be able to select dates in the past for a
        new appointment date. */
    private void disableDatesBeforeToday() {

        startDateText.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
    }

    /**This date picker will set the end date to the start date. When the user picks a start date, this date picker
        will set the end date the the start date. It is assumed that the appointment will not last longer than a day.
        if the user wishes, a new end date can be picked that is not the same as the start date. However, any dated prior
        to the start date are disabled. No appointments can end before the appointment starts.*/
    @FXML
    public void onActionStartDateSelected(ActionEvent event) {
        LocalDate startDate = startDateText.getValue();

        endDateText.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(startDate) < 0 );
            }
        });
        endDateText.setValue(startDate);
    }

    private void populateTypeBox() {
        allAppointmentTypes = Utilities.getAppointmentTypes();
        typeComboBox.setItems(allAppointmentTypes);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        populateTypeBox();
        populateCustomerComboBox();
        populateContactComboBox();
        populateStartTimeComboBox();
        disableDatesBeforeToday();
    }


}
