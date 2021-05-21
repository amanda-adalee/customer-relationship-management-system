package Model;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;


/**This class encapsulates the instance variables for the appointment object. The access points for this class are
    through the public getters and setters. */
public class Appointment {

    // Private instance variables
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Date createDate;
    private String createdBy;
    private Date lastUpdated;
    private String lastUpdatedBy;
    private String customerName;
    private int customerId;
    private int contactId;
    private String contactName;
    private int userId;

    // appointment constructor
    public Appointment() { }


    // Setters
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public void setTitle(String title) { this.title = title; }

    public void setDescription(String description) { this.description = description; }

    public void setLocation(String location) { this.location = location; }

    public void setType(String type) { this.type = type; }

    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }

    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }

    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public void setContactId(int contactId) { this.contactId = contactId; }

    public void setContactName(String contactName) { this.contactName = contactName; }

    public void setUserId(int userId) { this.userId = userId; }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    // Getters

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }


    public int getAppointmentId() { return appointmentId; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getLocation() { return location; }

    public String getType() { return type; }

    public LocalDate getStartDate() { return startDate; }

    public LocalTime getStartTime() { return startTime; }

    public LocalDate getEndDate() { return endDate; }

    public LocalTime getEndTime() { return endTime; }

    public Date getCreateDate() { return createDate; }

    public String getCreatedBy() { return createdBy; }

    public Date getLastUpdated() { return lastUpdated; }

    public String getLastUpdatedBy() { return lastUpdatedBy; }

    public String getCustomerName() { return customerName; }

    public int getCustomerId() { return customerId; }

    public int getContactId() { return contactId; }

    public String getContactName() { return contactName; }

    public int getUserId() { return userId; }
}
