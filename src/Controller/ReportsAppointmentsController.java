package Controller;

import Database.AppointmentDB;
import Database.ContactDB;
import Model.Appointment;
import Model.Report;
import Util.Utilities;
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
import java.util.ResourceBundle;

/** This class populates a table view report of the appointments types and amount for each month. A user can
    select a month from a combo box, once the month is selected the table view will populate the appointment
    types and amount during that month. */
public class ReportsAppointmentsController implements Initializable {

    Stage stage;
    Parent scene;

    private ObservableList<Report> allAppointmentsByType = FXCollections.observableArrayList();
    private ObservableList<String> allMonths = FXCollections.observableArrayList();


    //region FXML Variables
    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private TableView<Report> appointmentTableView;

    @FXML
    private TableColumn<String, Report> typeColumn;

    @FXML
    private TableColumn<Integer, Report> amountColumn;
    //endregion

    /**This button will load the AppointmentMenu scene.*/
    @FXML
    void appointmentsButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**This button will load the ReportsSchedules scene.*/
    @FXML
    void appointmentsFilteredByContactsButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportsSchedules.fxml"));
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

    /**This combo box sets the items for the appointment table view. A for each loop will go through the all
     appointmentsByType list and add to a new list the appointments whose month names match the value from the
     month combo box. The appointment table view items will be set to this list. */
    @FXML
    void onActionMonthComboBox(ActionEvent event) {

        String selectedMonth = monthComboBox.getValue();
        ObservableList<Report> selectedMonthAppointments = FXCollections.observableArrayList();
        for (Report report : allAppointmentsByType) {
            if (selectedMonth.equals(report.getMonth())) {
                selectedMonthAppointments.add(report);
            }
        }
        appointmentTableView.setItems(selectedMonthAppointments);
    }

    private void populateMonthComboBox() {
        allMonths = Utilities.getAllMonths();
        monthComboBox.setItems(allMonths);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            allAppointmentsByType = AppointmentDB.getAllAppointmentsByType();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        populateMonthComboBox();

        //binding
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
    }

}
