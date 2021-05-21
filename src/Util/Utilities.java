package Util;

import Database.AppointmentDB;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

public class Utilities {

    /**This function creates a list of local times. The working hours are specified in utc time.
     The utc start time is then converted to the users local time. The utc end time is also converted to the users
     local times. The new local times are used as constraints to create appointments times in the available working hours.
     @return allAppointmentTimes this list is made up of working hours relative to the time zone. This list used in a
     combo box where the user can select appointment times. */
    public static ObservableList<LocalTime> getAppointmentTimes(){

        ObservableList<LocalTime> allAppointmentTimes = FXCollections.observableArrayList();

        ZonedDateTime startTimeUTC = ZonedDateTime.of(LocalDate.now(), LocalTime.of(13, 0), ZoneId.of("UTC"));
        ZonedDateTime startTimeLocal = startTimeUTC.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime endTimeUTC = ZonedDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(3, 0), ZoneId.of("UTC"));
        ZonedDateTime endTimeLocal = endTimeUTC.withZoneSameInstant(ZoneId.systemDefault());

        LocalTime startTime = LocalTime.of(startTimeLocal.getHour(), startTimeLocal.getMinute());
        LocalTime endTime = LocalTime.of(endTimeLocal.getHour(), endTimeLocal.getMinute());

        while (!startTime.equals(endTime.plusMinutes(30))){
            allAppointmentTimes.add(startTime);
            startTime = startTime.plusMinutes(30);
       }

        return allAppointmentTimes;
    }

    /** Creates a string list of all appointment types.
     @return allAppointmentTypes this list is created to be used in a combo box
     */
    public static ObservableList<String> getAppointmentTypes (){

        ObservableList<String> allAppointmentTypes = FXCollections.observableArrayList();

        allAppointmentTypes.add("Consultation");
        allAppointmentTypes.add("Planning");
        allAppointmentTypes.add("De-Briefing");
        allAppointmentTypes.add("Other");

        return allAppointmentTypes;
    }

    /**Lambda use explanation- The following lambdas in this function all serve similar roles. Instead of using a for
     or foreach loop for each sort, the stream and filter functions are used to parse the data, filter according to the
     expressions passed in, then collect the results that passed in the list. The lists are created in a single line of
     code, instead of multiple lines.*/
    public static boolean checkForAppointmentConflict(Appointment newAppointment, boolean isUpdating) {
        boolean conflict = false;
        String errorMessage = "This meeting conflicts with the following appointments : \n";
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        try {
            allAppointments = AppointmentDB.getAllAppointments();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Generates list of appointments belonging to the customerID.
        List<Appointment> customerAppointments = allAppointments
                .stream()
                .filter(appointment -> appointment.getCustomerId() == newAppointment.getCustomerId())
                .collect(Collectors.toList());

        LocalDateTime newStartTime = newAppointment.getStartDateTime();
        LocalDateTime newEndTime = newAppointment.getEndDateTime();

        // Searches for conflicts
        List <Appointment> appointmentConflicts = customerAppointments
                .stream()
                .filter(oldAppointment -> (newStartTime.isBefore(oldAppointment.getStartDateTime()) && newEndTime.isAfter(oldAppointment.getEndDateTime()))
                        || (newStartTime.isAfter(oldAppointment.getStartDateTime()) && newEndTime.isBefore(oldAppointment.getEndDateTime()))
                        || (newStartTime.isBefore(oldAppointment.getStartDateTime()) && newEndTime.isAfter(oldAppointment.getStartDateTime()))
                        || (newEndTime.isAfter(oldAppointment.getEndDateTime()) && newStartTime.isBefore(oldAppointment.getEndDateTime()))
                        || (newStartTime.equals(oldAppointment.getStartDateTime()) || newEndTime.equals(oldAppointment.getEndDateTime())))
                .collect(Collectors.toList());

        // If modifying an appointment, excludes the original appointment from conflicting list
        if (isUpdating) {
            appointmentConflicts.removeIf(appointment -> appointment.getAppointmentId() == newAppointment.getAppointmentId());
        }

        // If conflicts exist, alerts the user and sets conflict to false
        if (appointmentConflicts.size() != 0) {
            conflict = true;
            for (Appointment appointment: appointmentConflicts) {
                errorMessage += "Appointment with " + appointment.getCustomerName() + " at " + appointment.getStartDateTime() + ". \n";
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Error");
                errorAlert.setContentText(errorMessage);
                errorAlert.showAndWait();
            }

        }
        return conflict;
    }

    /** Creates a string list of all the months in a year.
    @return allMonths this list is created to be used in a combo box
     */
    public static ObservableList<String> getAllMonths(){

        ObservableList<String> allMonths = FXCollections.observableArrayList("January","February", "March", "April",
                "May", "June", "July", "August", "September", "October", "November", "December");

        return allMonths;
    }


}
