package Controller;

import Database.CustomerDB;
import Model.Country;
import Model.Customer;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**In this class the user can create/add a customer. To add a new customer the user must fill in the customer
 details then save. */
public class CustomerAddController implements Initializable {
    Stage stage;
    Parent scene;

    ObservableList<Division> selectedDivisions = FXCollections.observableArrayList();

    //region FXML Variables
    @FXML
    private TextField customerIdText;

    @FXML
    private TextField customerNameText;

    @FXML
    private TextField addressText;

    @FXML
    private TextField postalCodeText;

    @FXML
    private TextField phoneNumberText;

    @FXML
    private ComboBox<Country> countryComboBox;

    @FXML
    private ComboBox<Division> divisionComboBox;

    //endregion

    /**This button saves the new customer to the database. Before saving, an input validating function is called.
     If the database is updated with the new customer then the customer menu scene will load. If the update is not
     successful, the user will receive an error to try again.*/
    @FXML
    void addNewCustomerButton(ActionEvent event) throws IOException {

        String customerName = customerNameText.getText();
        String address = addressText.getText();
        String postalCode = postalCodeText.getText();
        String phoneNumber = phoneNumberText.getText();
        Country country = countryComboBox.getValue();
        Division division = divisionComboBox.getValue();

        if (isInputValid()) {

            Customer newCustomer = new Customer();
            newCustomer.setCustomerName(customerName);
            newCustomer.setCustomerAddress(address);
            newCustomer.setCustomerPostalCode(postalCode);
            newCustomer.setCustomerPhoneNumber(phoneNumber);
            newCustomer.setCustomerCountry(country);
            newCustomer.setCustomerDivision(division);
            boolean result = CustomerDB.addCustomer(newCustomer);
            if (result)
            {
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/CustomerMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Error");
                errorAlert.setContentText("There was an error updating the database. Please try again.");
                errorAlert.showAndWait();
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("One or more text fields are empty.");
            errorAlert.showAndWait();
        }
    }

    /**This function validates user input. It will validate that none of the text fields or combo boxes are empty.
     @return  isInputValid - true or false */
    private boolean isInputValid(){
        boolean isInputValid = true;
        if (customerNameText.getLength() == 0 )
            isInputValid = false;
        if (addressText.getLength() == 0 )
            isInputValid = false;
        if (postalCodeText.getLength() == 0 )
            isInputValid = false;
        if (phoneNumberText.getLength() == 0)
            isInputValid = false;
        if (countryComboBox.getSelectionModel().isEmpty())
            isInputValid = false;
        if (divisionComboBox.getSelectionModel().isEmpty())
            isInputValid = false;
        return isInputValid;
    }

    /**This is a back button. When the back button is pressed the user will be taken to the customer menu scene that
     will load. */
    @FXML
    void backNewCustomerButton(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomerMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**This combo box calls the populateDivisionComboBox function.*/
    @FXML
    void onActionCountryButton(ActionEvent event) {
        populateDivisionComboBox();
    }

    /**This function sets the items for the division combo box. A variable for the country id is created using
        the value that the user selected for the country combo box. A for each loop will go through the master division
        list and add any divisions, whose country ids match the country id selected, to a selected divisions list.
        The division combo box items will be set to this list. */
    private void populateDivisionComboBox() {
        selectedDivisions.clear();
        int countryID = countryComboBox.getValue().getCountryId();
        for (Division division : Division.masterDivisionList) {
            if (division.getCountryId() == countryID) {
                selectedDivisions.add(division);
            }
        }
        divisionComboBox.setItems(selectedDivisions);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryComboBox.setItems(Country.masterCountryList);
    }

}
