package Controller;

import Database.AppointmentDB;
import Database.CustomerDB;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static Database.CustomerDB.getAllCustomers;

/** The customer controller class populates a table view of all customers. Here the user can view the details for customers.
    If they wish they can add, update, or delete customer information from here.*/
public class CustomerController implements Initializable {

    Stage stage;
    Parent scene;
    ResourceBundle rb;
    Locale currentLocale;

    ObservableList <Customer> allCustomers = FXCollections.observableArrayList();

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
    private TableColumn<Customer, Integer> customerPostalCode;

    @FXML
    private TableColumn<Customer, Integer> customerPhoneNumber;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button updateCustomerButton;

    @FXML
    private Button deleteCustomerButton;


    @FXML
    private Text customerInformationText;

    @FXML
    private Button appointmentsButton;

    @FXML
    private Button reportsButton;

    //endregion


    /** This addCustomer button loads the CustomerAddMenu scene when pressed.*/
    @FXML
    void addCustomer(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomerAddMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /** This appointmentsBttn button loads the AppointmentMenu scene when pressed.*/
    @FXML
    void appointmentsBttn(ActionEvent event)  throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This deleteCustomer button will delete the user selected customer from the database. First, an if statement is used to verify that the user
        has indeed selected a customer to delete. If no customer was selected an error message will pop up asking the user to select a customer.
        Next, it will ask the user to confirm the the deletion of the customer, once confirmed the customer will be deleted. Using the customerId,
        the deleteCustomer function from the CustomerDB class will delete the customer from the database. The table view will then be updated to show changes.*/
    @FXML
    void deleteCustomer(ActionEvent event) {

        if (customerTableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Customer will be deleted. Any currently scheduled appointments with this customer will also be deleted. Do you wish to continue? ");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                int customerId = customerTableView.getSelectionModel().getSelectedItem().getCustomerId();
                AppointmentDB.deleteAllAppointmentsByCustomer(customerId);
                CustomerDB.deleteCustomer(customerId);
                updateCustomerTableView();
            }

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No customer was selected. Please select a customer to delete.");
            alert.showAndWait();
        }
    }

    /** The reportsButton loads the ReportsSchedules scene when pressed.*/
    @FXML
    void reportsButton(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportsSchedules.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /** This function will load the CustomerModifyMenu scene. It consists of an if else statement. An if else
     statement is used to check whether the user has selected an item. If an item is selected from the table
     view when the user presses the update button then the CustomerModifyMenu scene will load, otherwise if
     no item was selected the user will receive an error message that they must first select a customer to update.*/
    @FXML
    void updateCustomer(ActionEvent event) throws IOException, SQLException {

        if (customerTableView.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/CustomerModifyMenu.fxml"));
            loader.load();

            CustomerModifyController CMController = loader.getController();
            CMController.sentCustomer(customerTableView.getSelectionModel().getSelectedItem(), getAllCustomers().indexOf(customerTableView.getSelectionModel().getSelectedItem()) );

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No customer was selected. Please select a customer to update.");
            alert.showAndWait();
            }

        }

    /** This function sets the allCustomers list to the getAllCustomers list from the CustomerBD class. It then
        uses the allCustomers list to set/populate the customerTableView.*/
    private void updateCustomerTableView() {
        try {
            allCustomers = getAllCustomers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        customerTableView.setItems(allCustomers);
    }

    private void setLanguage() {
        rb = MainMenuController.rb;
        currentLocale = MainMenuController.currentLocale;

        /*DELETE THIS
        appointmentsButton.setText(rb.getString("appointments"));
        reportsButton.setText(rb.getString("reports"));
        //customerInformationText.setText(rb.getString("customerInformation"));
        addCustomerButton.setText(rb.getString("add"));
        updateCustomerButton.setText(rb.getString("update"));
        deleteCustomerButton.setText(rb.getString("delete"));*/

        // columns
        customerId.setText(rb.getString("customerId"));
        customerName.setText(rb.getString("customerName"));
        customerAddress.setText(rb.getString("address"));
        customerDivision.setText(rb.getString("division"));
        customerPostalCode.setText(rb.getString("postalCode"));
        customerPhoneNumber.setText(rb.getString("phoneNumber"));
    }

    /**Lambda use explanation- These lambdas allow the combo boxes for divisions and countries to be bound properly.
    Instead of simply accessing an instance variable from a customer by passing a string to match to property name,
    the lambdas allow the access to the respective countries and divisions dynamically.*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateCustomerTableView();

        setLanguage();

        //binding
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        customerPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));

        customerDivision.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerDivision().getDivisionName()));
        customerCountry.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerCountry().getCountryName()));
    }

}
