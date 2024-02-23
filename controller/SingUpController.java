package com.example.concertticketbuy.controller;

import com.example.concertticketbuy.model.DBUtils;
import com.example.concertticketbuy.model.UserData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class SingUpController {
    @FXML
    private Button newUserCancelButton;
    @FXML
    private Button newUserSingUpButton;
    @FXML
    private TextField newUserTextField;
    @FXML
    private PasswordField newUserPasswordField;
    @FXML
    private TextField newUserEmailField;
    @FXML
    private TextField newUserPhoneNumberField;
    @FXML
    private Label singUpTextMessageLabel;

    public void cancelButtonOnAction(ActionEvent e){
        Stage stage = (Stage) newUserCancelButton.getScene().getWindow();
        stage.close();
    }

    public void singUpButtonOnAction(ActionEvent e) throws Exception {
        validateSingUp();
    }

    public boolean validateNewUserUsername(String newUsername){
        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();

        String verifyNewUserUsername = "SELECT COUNT(1) FROM users WHERE username = ?";

        try{
            PreparedStatement preparedStatement = connectDB.prepareStatement(verifyNewUserUsername);
            preparedStatement.setString(1, newUsername);

            ResultSet queryResult = preparedStatement.executeQuery();

            while(queryResult.next()){
                if(queryResult.getInt(1) == 1){
                    singUpTextMessageLabel.setText("Username already exists!\nPlease choose another one.");
                    return false;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return true;
    }
    void validateSingUp() throws Exception {
        if(!newUserTextField.getText().isBlank() && !newUserPasswordField.getText().isBlank() && !newUserEmailField.getText().isBlank() && !newUserPhoneNumberField.getText().isBlank()){
            {
                UserData newUser = new UserData(0, newUserTextField.getText(), newUserPasswordField.getText(), newUserEmailField.getText(), newUserPhoneNumberField.getText(),1);
                if(validateNewUserUsername(newUser.getUsername()) && validateEmailAddress(newUser.getEmail()) && validatePhoneNumber(newUser.getPhoneNumber()) && validatePassword(newUser.getPassword())){
                    //System.out.println("Username is valid." + newUser.getUsername() + " " + newUser.getPassword() + " " + newUser.getEmail() + " " + newUser.getPhoneNumber());
                    singUpTextMessageLabel.setText("Account created successfully!");
                    addNewUserData(newUser);
                    changeScene(newUserSingUpButton, "/com/example/concertticketbuy/LogIn.fxml");
                }
            }
        } else {
            singUpTextMessageLabel.setText("Please enter username,\n password, email and\n phone number.");
        }
    }

    public boolean validateEmailAddress(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if(pat.matcher(email).matches()){
            return true;
        } else {
            singUpTextMessageLabel.setText("Invalid email address.\nPlease try again.");
            return false;
        }
    }

    public boolean validatePhoneNumber(String phoneNumber){
        String phoneNumberRegex = "^[0-9]{10}$";
        Pattern pat = Pattern.compile(phoneNumberRegex);
        if(pat.matcher(phoneNumber).matches()){
            return true;
        } else {
            singUpTextMessageLabel.setText("Invalid phone number.\nPlease try again.");
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
            return true;
        } else {
            singUpTextMessageLabel.setText("Invalid password.\nMust contain at least one digit,\n one lowercase and one uppercase letter,\n one special character,\n must be between 8 and 20 characters\n and have no whitespace.\nPlease try again.");
            return false;
        }
    }
    private void addNewUserData(UserData newUser){
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
            singUpTextMessageLabel.setText("Error.\nPlease try again.");
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
    private void changeScene(Button button, String fxmlFileName) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        Parent root = loader.load();

        Stage stage = (Stage) button.getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}
