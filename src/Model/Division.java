package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

import static Database.DivisionDB.getAllDivisions;

/**This class encapsulates the instance variables for the division object. The access points for this class are
 through the public getters and setters. */
public class Division {

    //Private instance variables
    private int divisionId;
    private String divisionName;
    private int countryId;
    public static ObservableList<Division> masterDivisionList = FXCollections.observableArrayList();

    // Division Constructor
    public Division() { }

    // Setters
    public void setDivisionId(int divisionId) { this.divisionId = divisionId; }

    public void setDivisionName(String divisionName) { this.divisionName = divisionName; }

    public void setCountryId(int countryId) { this.countryId = countryId; }

    // Getters
    public int getDivisionId() { return divisionId; }

    public String getDivisionName() { return divisionName; }

    public int getCountryId() { return countryId; }

    // method displays division names for combo boxes
    @Override
    public String toString() { return this.getDivisionName(); }

    public static void setMasterDivisionList() throws SQLException {
        masterDivisionList = getAllDivisions();
    }
}

