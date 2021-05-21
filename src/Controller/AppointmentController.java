package Controller;

import Database.AppointmentDB;
import Model.Appointment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static Database.AppointmentDB.*;


/**In this class the user can view appointments. Appointment details are displayed in a table view. The user can choose
    to view either monthly or weekly appointments. */
public class AppointmentController implements Initializable {

    Stage stage;
    Parent scene;
    private LocalDate currentDate;
    private LocalDate monthlyEndDate;
    private LocalDate weeklyEndDate;
    private boolean isOnMonthlyView;
    private boolean isOnWeeklyView;
    private ResourceBundle rb;
    private Locale currentLocale;

    ObservableList<Appointment> allMonthlyAppointments = FXCollections.observableArrayList();
    ObservableList<Appointment> allWeeklyAppointments = FXCollections.observableArrayList();
    ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

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
    private TableColumn<Appointment, String> appointmentContact;

    @FXML
    private TableColumn<Appointment, Date> appointmentStartDate;

    @FXML
    private TableColumn<Appointment, Time> appointmentStartTime;

    @FXML
    private TableColumn<Appointment, Date> appointmentEndDate;

    @FXML
    private TableColumn<Appointment, Time> appointmentEndTime;


    @FXML
    private RadioButton monthRadioSelected;

    @FXML
    private ToggleGroup apptViewToggle;

    @FXML
    private RadioButton weekRadioSelected;
    //
    @FXML
    private Text appointmentsText;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private Button updateAppointmentButton;

    @FXML
    private Button deleteAppointmentButton;

    @FXML
    private Button customerInformationButton;

    @FXML
    private Button reportsButton;

    //
    //endregion

    /**This button will load the AppointmentAddMenu scene.*/
    @FXML
    void addAppointment(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentAddMenu.fxml"));
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

    /**This button will load the ReportsSchedules scene.*/
    @FXML
    void reportsButton(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportsSchedules.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This deleteAppointments button will delete the user selected appointment from the database. First, an if statement is used to verify that the user
     has indeed selected an appointment to delete. If no appointment was selected an error message will pop up asking the user to select an appointment.
     Next, it will ask the user to confirm the the deletion of the appointment, once confirmed the appointment will be deleted. Using the appointmentId,
     the deleteAppointment function from the AppointmentDB class will delete the appointment from the database. The table view will then be updated to show changes.*/
    @FXML
    void deleteAppointments(ActionEvent event) {

        if (appointmentsTableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, appointmentsTableView.getSelectionModel().getSelectedItem().getType() +" appointment #" +
                    appointmentsTableView.getSelectionModel().getSelectedItem().getAppointmentId() + " will be deleted. Do you wish to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                int appointmentId = appointmentsTableView.getSelectionModel().getSelectedItem().getAppointmentId();
                AppointmentDB.deleteAppointment(appointmentId);
                updateAppointmentTableView();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No appointment was selected. Please select an appointment to delete.");
            alert.showAndWait();
        }
    }

    private void updateAppointmentTableView() {
        try {
            allAppointments = getAllAppointments();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        populateTableView();
    }

    /**This function populates the table view. An if else statement will check which radio button is selected.
        If the monthly radio button is set to true, the monthly table view is populated. Otherwise if the weekly
        radio button is set to true, the weekly table view will populate.*/
    private void populateTableView() {
        if (isOnMonthlyView) {
            populateMonthlyTableView();
        }
        else if (isOnWeeklyView) {
            populateWeeklyTableView();
        }
    }

    /**This button sets the isOnMonthlyView boolean to true, and calls the populateMonthlyTableView function.*/
    @FXML
    void onActionMonthButton(ActionEvent event) {
        isOnMonthlyView = true;
        isOnWeeklyView = false;
        populateMonthlyTableView();
    }

    /**This function sets the appointmentsTableView to the allMonthlyAppointments list. An end date variable is created
        using the currentDate plus 1 month. A for each statement will loop through the allAppointments list. If the
        start date for the appointment is after the current date and is before the monthly end date variable, then the
        appointment will be added to the allMonthlyAppointments list. Both conditions must be met in order for the
        appointment to be added to the list. The appointmentsTableView is then set to the allMonthlyAppointments list.*/
    private void populateMonthlyTableView (){
        monthlyEndDate = currentDate.plusMonths(1);
        allMonthlyAppointments.clear();
        for (Appointment appointment: allAppointments) {
            if (appointment.getStartDate().isAfter(currentDate) && appointment.getStartDate().isBefore(monthlyEndDate)) {
                allMonthlyAppointments.add(appointment);
            }
        }
        appointmentsTableView.setItems(allMonthlyAppointments);
    }

    /**This button sets the isOnWeeklyView boolean to true, and calls the populateWeeklyTableView function.*/
    @FXML
    void onActionWeekButton(ActionEvent event) {
        isOnWeeklyView = true;
        isOnMonthlyView = false;
        populateWeeklyTableView();
    }

    /**This function sets the appointmentsTableView to the allWeeklyAppointments list. An end date variable is created
     using the currentDate plus 1 month. A for each statement will loop through the allAppointments list. If the
     start date for the appointment is after the current date and is before the weekly end date variable, then the
     appointment will be added to the allWeeklyAppointments list. Both conditions must be met in order for the
     appointment to be added to the list. The appointmentsTableView is then set to the allWeeklyAppointments list.*/
    private void populateWeeklyTableView() {
        weeklyEndDate = currentDate.plusWeeks(1);
        allWeeklyAppointments.clear();
        for (Appointment appointment: allAppointments) {
            if (appointment.getStartDate().isAfter(currentDate) && appointment.getStartDate().isBefore(weeklyEndDate)) {
                allWeeklyAppointments.add(appointment);
            }
        }
        appointmentsTableView.setItems(allWeeklyAppointments);
    }

    /** This function will load the AppointmentModifyMenu scene. It consists of an if else statement. An if else
        statement is used to check whether the user has selected an item. If an item is selected from the table
        view when the user presses the update button then the AppointmentModifyMenu scene will load, otherwise if
        no item was selected the user will receive an error message that they must first select an appointment to update.*/
    @FXML
    void updateAppointment(ActionEvent event) throws IOException {

        if (appointmentsTableView.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AppointmentModifyMenu.fxml"));
            loader.load();

            AppointmentModifyController AMController = loader.getController();
            AMController.sentAppointment(appointmentsTableView.getSelectionModel().getSelectedItem());

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No appointment was selected. Please select an appointment to update.");
            alert.showAndWait();
        }

    }

    private void setLanguage() {
        rb = MainMenuController.rb;
        currentLocale = MainMenuController.currentLocale;

        /*DELETE THIS
        //appointmentsText.setText(rb.getString("appointments"));
        addAppointmentButton.setText(rb.getString("add"));
        updateAppointmentButton.setText(rb.getString("update"));
        deleteAppointmentButton.setText(rb.getString("delete"));
        customerInformationButton.setText(rb.getString("customerInformation"));
        reportsButton.setText(rb.getString("reports"));*/

        // Set columns
        appointmentId.setText(rb.getString("appointmentId"));
        customerId.setText(rb.getString("customerId"));
        customerName.setText(rb.getString("customerName"));
        appointmentTitle.setText(rb.getString("title"));
        appointmentDescription.setText(rb.getString("description"));
        appointmentLocation.setText(rb.getString("location"));
        appointmentType.setText(rb.getString("type"));
        appointmentContact.setText(rb.getString("contact"));
        appointmentStartDate.setText(rb.getString("startDate"));
        appointmentStartTime.setText(rb.getString("startTime"));
        appointmentEndDate.setText(rb.getString("endDate"));
        appointmentEndTime.setText(rb.getString("endTime"));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        currentDate = LocalDate.now();

        setLanguage();

        // Initialize booleans for sorting table view
        isOnMonthlyView = monthRadioSelected.isSelected();
        isOnWeeklyView = weekRadioSelected.isSelected();

        try {
             allAppointments = getAllAppointments();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        populateTableView();

        //binding
        appointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentContact.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        appointmentEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        appointmentEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        appointmentStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        appointmentStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));


    }

}
