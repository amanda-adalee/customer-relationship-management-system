package Controller;

import Database.AppointmentDB;
import Database.UserDB;
import Model.Appointment;
import Model.Country;
import Model.Division;
import Util.LoginLogger;
import javafx.application.Application;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import static Database.UserDB.sessionUser;
import static Database.UserDB.validateUser;

/** This class handles the login menu screen, which loads as the primary stage. It takes input for username and password from the user.
  In this class the validateUser function is called to authenticate the user.*/
public class MainMenuController extends Application implements Initializable {

    Stage stage;
    Parent scene;
    public static ResourceBundle rb;
    public static Locale currentLocale;

    @FXML
    private PasswordField passwordText;

    @FXML
    private TextField userNameText;

    @FXML
    private Button submitButton;

    @FXML
    private Button clearButton;

    @FXML
    private Text loginLabel;

    @FXML
    private Text languageLabel;

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Creates master country and division lists in two calls to the database, instead of multiple calls later in the application.
        Country.setMasterCountryList();
        Division.setMasterDivisionList();

        Parent root = FXMLLoader.load(getClass().getResource("../View/MainMenuLogin.fxml"));
        primaryStage.setTitle("Scheduling Application");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

    }

    /** This is an onAction clear button, if clicked any information that was entered into the username and password
        text fields will be deleted and reset. */
    @FXML
    void clearButton(ActionEvent event) {

        userNameText.clear();
        passwordText.clear();
    }

    /** In this function an if statements call validateUser to authenticate the user. if the credentials
        entered by the user are found and validated then the customerMenu scene will load otherwise an
        error message will notify the user that the credentials were invalid and to try again. No new scene will load. */
    @FXML
    public void submitButton(ActionEvent event) throws IOException, SQLException {

        if (validateUser(userNameText.getText(), passwordText.getText())) {
            LoginLogger.logValidSignIn(UserDB.sessionUser);
            checkForUpcomingAppointments();
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            LoginLogger.logInvalidSignIn();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText(rb.getString("error"));
            errorAlert.setContentText(rb.getString("passwordInvalidMessage"));
            errorAlert.showAndWait();
        }
    }

    /** This function generates an alert to notify the user whether there is an appointment in 15 minutes og log in time,
        or there isn't an appointment. The current time of the user's log in is compared to all appointment start times.*/
    private void checkForUpcomingAppointments () throws SQLException {
        LocalDateTime currentTime = LocalDateTime.now();
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        allAppointments = AppointmentDB.getAllAppointments();
        Appointment upcomingAppointment = new Appointment();
        boolean anyUpcomingAppointment = false;

        for (Appointment appointment: allAppointments) {
            LocalDateTime startDateTime = LocalDateTime.of(appointment.getStartDate(), appointment.getStartTime());
            if(startDateTime.isAfter(currentTime) && startDateTime.isBefore(currentTime.plusMinutes(15))) {
                upcomingAppointment = appointment;
                anyUpcomingAppointment = true;
            }

        }
        if (anyUpcomingAppointment){

            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText(rb.getString("upcomingAppointment"));
            errorAlert.setContentText( upcomingAppointment.getContactName() + rb.getString("hasappointmentid#") +
                                        upcomingAppointment.getAppointmentId() + rb.getString("with") +
                                        upcomingAppointment.getCustomerName() + rb.getString("today at") +
                                        upcomingAppointment.getStartTime() + ".");
            errorAlert.showAndWait();

        } else {
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText(rb.getString("upcomingAppointment"));
            errorAlert.setContentText(rb.getString("noUpcomingAppointmentsMessage"));
            errorAlert.showAndWait();
        }

    }

    public static void main(String[] args) {
        launch(args);

    }

    private void setLanguage() {
        currentLocale = Locale.getDefault();
        rb = ResourceBundle.getBundle("Util/lang", currentLocale);

        passwordText.setPromptText(rb.getString("passwordPrompt"));
        userNameText.setPromptText(rb.getString("usernamePrompt"));
        submitButton.setText(rb.getString("submit"));
        clearButton.setText(rb.getString("clear"));
        loginLabel.setText(rb.getString("login"));
        languageLabel.setText(rb.getString("language"));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLanguage();

        passwordText.setText("test");
        userNameText.setText("test");
    }


}
