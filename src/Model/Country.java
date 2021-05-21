package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

import static Database.CountryDB.getAllCountries;

/**This class encapsulates the instance variables for the country object. The access points for this class are
 through the public getters and setters. */
public class Country {

    //Private instance variables
    private int countryId;
    private String countryName;
    public static ObservableList<Country> masterCountryList = FXCollections.observableArrayList();

    // Country Constructor
    public Country() { }

    // Setters
    public int getCountryId() { return countryId; }

    public void setCountryId(int countryId) { this.countryId = countryId; }

    // Getters
    public String getCountryName() { return countryName; }

    public void setCountryName(String countryName) { this.countryName = countryName; }

    // method displays country names for combo boxes
    @Override
    public String toString() { return this.getCountryName(); }

    public static void setMasterCountryList() throws SQLException {
        Country.masterCountryList = getAllCountries();
    }

}
