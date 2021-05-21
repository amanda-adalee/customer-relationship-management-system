package Model;

/**This class encapsulates the instance variables for the user object. The access points for this class are
 through the public getters and setters. */
public class User {

    // Private instance variables
    private String userName;
    private String passWord;
    private int userId;

    // Constructor
    public User() { }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
