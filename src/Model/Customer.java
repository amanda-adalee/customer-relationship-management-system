package Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

/**This class encapsulates the instance variables for the customer object. The access points for this class are
 through the public getters and setters. */
public class Customer {

    // Private instance variables
    private int customerId;
    private String customerName;
    private String customerAddress;
    private Country customerCountry;
    private Division customerDivision;
    private String customerPostalCode;
    private String customerPhoneNumber;
    private ZonedDateTime dateCreated;
    private String createdBy;
    private ZonedDateTime lastUpdated;
    private String lastUpdatedBy;


    // Customer constructor
    public Customer(){ }

    // Setters
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public void setCustomerCountry(Country customerCountry) { this.customerCountry = customerCountry; }

    public void setCustomerDivision(Division customerDivision) { this.customerDivision = customerDivision; }

    public void setCustomerPostalCode(String customerPostalCode) { this.customerPostalCode = customerPostalCode; }

    public void setCustomerPhoneNumber(String customerPhoneNumber) { this.customerPhoneNumber = customerPhoneNumber; }

    public void setDateCreated(ZonedDateTime dateCreated) { this.dateCreated = dateCreated; }

    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public void setLastUpdated(ZonedDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }


    // Getters
    public int getCustomerId() { return customerId; }

    public String getCustomerName() { return customerName; }

    public String getCustomerAddress() { return customerAddress; }

    public Country getCustomerCountry() { return customerCountry; }

    public Division getCustomerDivision() { return customerDivision; }

    public String getCustomerPostalCode() { return customerPostalCode; }

    public String getCustomerPhoneNumber() { return customerPhoneNumber; }

    public ZonedDateTime getDateCreated() { return dateCreated; }

    public String getCreatedBy() { return createdBy; }

    public ZonedDateTime getLastUpdated() { return lastUpdated; }

    public String getLastUpdatedBy() { return lastUpdatedBy; }


    @Override
    public String toString() { return this.getCustomerName(); }

}
