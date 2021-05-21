package Model;

/**This class encapsulates the instance variables for the contact object. The access points for this class are
 through the public getters and setters. */
public class Contact {

    // Private instance variables
    private int contactId;
    private String contactName;
    private String contactEmail;

    // Constructor
    public Contact() { }

    // Getters and Setters
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }


    @Override
    public String toString() { return this.getContactName(); }

}
