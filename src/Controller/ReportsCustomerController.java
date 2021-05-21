package Controller;


import Database.CountryDB;
import Database.CustomerDB;
import Model.Country;
import Model.Customer;
import javafx.beans.property.SimpleStringProperty;
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

/** This class populates a table view report of the customers for each country. A user can select a country from a
    combo box, once the country is selected the table view will populate the customers in that country. */
public class ReportsCustomerController implements Initializable {

    Stage stage;
    Parent scene;

    ObservableList <Customer> allCustomers = FXCollections.observableArrayList();
    ObservableList <Customer> allCustomerByCountry = FXCollections.observableArrayList();
    ObservableList <Country> allCountries = FXCollections.observableArrayList();

    //region FXML Variables
    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, Integer> customerId;

    @FXML
    private TableColumn<Customer, String> customerName;

    @FXML
    private TableColumn<Customer, String> customerAddress;

    @FXML
    private TableColumn<Customer, String> customerDivision;

    @FXML
    private TableColumn<Customer, String> customerCountry;

    @FXML
    private TableColumn<Customer, String> customerPostalCode;

    @FXML
    private TableColumn<Customer, String> customerPhoneNumber;

    @FXML
    private ComboBox<Country> countryComboBox;

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

    /**This button will load the ReportsAppointments scene.*/
    @FXML
    void totalCustomerAppointmentsButton(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportsAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**This combo box sets the items for the customer table view. A for each loop will go through the all customers
     list and add to a new list the customers whose country names match the value from the country combo box.
     The customer table view items will be set to this list. */
    @FXML
    void onActionCountryComboBox(ActionEvent event) {
        allCustomerByCountry.clear();

        for (Customer customer : allCustomers) {
            if (customer.getCustomerCountry().getCountryName().equals(countryComboBox.getValue().getCountryName())) {
                allCustomerByCountry.add(customer);
            }
        }
        customerTableView.setItems(allCustomerByCountry);

    }

    /**This function sets the country combo box items to the all countries list.*/
    private void populateCountryComboBox() {
        try {
            allCountries = CountryDB.getAllCountries();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        countryComboBox.setItems(allCountries);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            allCustomers = CustomerDB.getAllCustomers();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        populateCountryComboBox();

        //binding
        customerTableView.setItems(allCustomers);
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        customerPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
        customerDivision.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerDivision().getDivisionName()));
        customerCountry.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerCountry().getCountryName()));

    }

}
