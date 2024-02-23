package com.example.concertticketbuy.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;


public class UserData {
    private int user_id;
    private String username;
    private String password;
    private String email;
    private String phone_number;
    private SessionManager sessionManager = SessionManager.getInstance();
    private UserData currentUserData;
    private int userType;


    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getUserId() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public int getUserType() {
        return userType;
    }
    public void setUserType(int userType) {
        this.userType = userType;
    }

    public UserData(int user_id, String username, String password, String email, String phone_number, int userType) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone_number = phone_number;
        this.userType = userType;
    }

    public boolean validateEmailAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (pat.matcher(email).matches()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        String phoneNumberRegex = "^[0-9]{10}$";
        Pattern pat = Pattern.compile(phoneNumberRegex);
        if (pat.matcher(phoneNumber).matches()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validatePassword(String password) {
        String passwordRegex = "^(?=.*[0-9])"  // Must contain at least one digit
                + "(?=.*[a-z])(?=.*[A-Z])"    // Must contain at least one lowercase and one uppercase letter
                + "(?=.*[@#$%^&+=])"          // Must contain at least one special character from the specified set
                + "(?=\\S+$).{8,20}$";         // Must be between 8 and 20 characters and have no whitespace

        Pattern pat = Pattern.compile(passwordRegex);

        if (pat.matcher(password).matches()) {
            return true;  // Password meets the criteria
        } else {
            return false;
        }
    }

    public boolean checkAdminCanExist(UserData newUserData){
        if(validateNewUserUsername(newUserData.getUsername()) && validateEmailAddress(newUserData.getEmail()) && validatePhoneNumber(newUserData.getPhoneNumber()) && validatePassword(newUserData.getPassword())){
            addNewUserData(newUserData);
            return true;
        }
        return false;
    }
    public boolean validateNewUserUsername(String newUsername) {
        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();

        String verifyNewUserUsername = "SELECT COUNT(1) FROM users WHERE username = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(verifyNewUserUsername);
            preparedStatement.setString(1, newUsername);

            ResultSet queryResult = preparedStatement.executeQuery();

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return true;
    }


    public void addNewUserData(UserData newUser){
        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();

        String insertNewUser = "INSERT INTO users(username, password, email, phone_number, user_type) VALUES(?, ?, ?, ?, ?)";

        try{
            PreparedStatement preparedStatement = connectDB.prepareStatement(insertNewUser);
            preparedStatement.setString(1, newUser.getUsername());
            preparedStatement.setString(2, newUser.getPassword());
            preparedStatement.setString(3, newUser.getEmail());
            preparedStatement.setString(4, newUser.getPhoneNumber());
            preparedStatement.setInt(5, newUser.getUserType());

            preparedStatement.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }finally {
            try {
                if (connectDB != null) {
                    connectDB.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean secureUpdateUserCredentials(UserData newUserCredentials) {
        if (changeUserData(newUserCredentials)) {
            sessionManager.setCurrentUserData(newUserCredentials);
            return true;
        }
        return false;
    }
    private boolean changeUserData(UserData newUserCredentials) {
        if(newUserCredentials.getUserType() == 1)
            currentUserData = sessionManager.getCurrentUserData();
        int nm = 0;

        if (newUserCredentials.getUsername().isBlank() || !validateNewUserUsername(newUserCredentials.getUsername())) {
            newUserCredentials.setUsername(currentUserData.getUsername());
            nm++;
        }

        if (newUserCredentials.getEmail().isBlank() || !validateEmailAddress(newUserCredentials.getEmail())) {
            newUserCredentials.setEmail(currentUserData.getEmail());
            nm++;
        }

        if (newUserCredentials.getPhoneNumber().isBlank() || !validatePhoneNumber(newUserCredentials.getPhoneNumber())) {
            newUserCredentials.setPhoneNumber(currentUserData.getPhoneNumber());
            nm++;
        }

        if (newUserCredentials.getPassword().isBlank() || !validatePassword(newUserCredentials.getPassword())) {
            newUserCredentials.setPassword(currentUserData.getPassword());
            nm++;
        }
        System.out.println(nm);
        if(nm > 3){
            return false;
        }

        return changeUserCredentials(newUserCredentials);
    }

    private boolean changeUserCredentials(UserData newUserCredentials) {
        System.out.println(newUserCredentials.getUsername());
        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();

        String injectData = "UPDATE users SET username = ?, password = ?, email = ?, phone_number = ? WHERE user_id = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(injectData);
            preparedStatement.setString(1, newUserCredentials.getUsername());
            preparedStatement.setString(2, newUserCredentials.getPassword());
            preparedStatement.setString(3, newUserCredentials.getEmail());
            preparedStatement.setString(4, newUserCredentials.getPhoneNumber());
            preparedStatement.setInt(5, newUserCredentials.getUserId());
            System.out.println("hello!!");

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("coco!!");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connectDB != null) {
                    connectDB.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

}
