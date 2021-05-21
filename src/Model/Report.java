package Model;

/**This class encapsulates the instance variables for the report object. The access points for this class are
 through the public getters and setters. */
public class Report {

    // Private instance variables
    private String month;
    private String type;
    private int count;

    // Getters and setters
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() { return this.getMonth(); }


}
