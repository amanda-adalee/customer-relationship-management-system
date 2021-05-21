package Util;

import Model.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**This class contains the functions to create and write to the login_activity.txt file. The purpose of this class
 is to log to date and time of any successful or unsuccessful user login. */
public class LoginLogger {

    public static File loginActivity = new File("login_activity.txt");
    public static Path logFile = Paths.get("login_activity.txt");

    /**This function checks whether the text file has been created or not. It will create a new
     text file if one doesn't already exist. */
    public static void createFileLogger() {
        try {
            if (loginActivity.createNewFile()) {
                //System.out.println("File created: " + loginActivity.getName());
            } else {
               // System.out.println("File already exists.");
            }
        } catch (IOException e) {
            //System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**This function writes to the text file. It will write the username, date, time, and time zone.
     @param isLoginSuccessful this boolean determines which write function will be executed.
     @param user username is written to the text file*/
    public static void writeFileLogger(boolean isLoginSuccessful, User user){
        try {
            BufferedWriter writer = Files.newBufferedWriter(logFile, StandardOpenOption.APPEND);
            if(isLoginSuccessful){
                writer.write(user.getUserName() + " successfully logged in on "
                        + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z")) + "\n");
            } else {
                writer.write("Unsuccessful log in at "
                        + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z")) + "\n");
            }
            writer.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
           // System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    /**This is the method that gets called if a user successfully logs in. If the text file exist then the
     write method will be called. Otherwise the text file must first be created and then written to.
     @param user - the user is passed in so that the name can get logged.
     */
    public static void logValidSignIn(User user){

        if (loginActivity.exists()){
            writeFileLogger(true, user);
        }else {
            createFileLogger();
            writeFileLogger(true, user);
        }
    }

    /**This is the method that gets called if a user unsuccessfully logs in. If the text file exist then the
     write method will be called. Otherwise the text file must first be created and then written to. */
    public static void logInvalidSignIn(){

        if (loginActivity.exists()){
            writeFileLogger(false, new User());
        } else {
            createFileLogger();
            writeFileLogger(false, new User());
        }
    }



}
