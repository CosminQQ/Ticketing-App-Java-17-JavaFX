package com.example.concertticketbuy.controller;

import com.example.concertticketbuy.model.DBUtils;
import com.example.concertticketbuy.model.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Controller {
    private SessionManager sessionManager = SessionManager.getInstance();
    @FXML
    private Button cancelButton;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;

    public void cancerButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    public void loginButtonAction(ActionEvent e) {
        loginMessageLabel.setText("Please enter username\n and password");
        if (!usernameTextField.getText().isBlank() && !passwordPasswordField.getText().isBlank()) {
            if (validateLogin()) {
                try {
                    sessionManager.setUsername(usernameTextField.getText());

                    changeScene(loginButton, "/com/example/concertticketbuy/MainUserMeniu.fxml");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else {
                loginMessageLabel.setText("Invalid login.\n Please try again.");
            }
        } else {
            loginMessageLabel.setText("Please enter username and password.");
        }
    }

    public boolean validateLogin() {
        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();

        String verifyLogin = "SELECT COUNT(1) FROM users WHERE username = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(verifyLogin);
            preparedStatement.setString(1, usernameTextField.getText());
            preparedStatement.setString(2, passwordPasswordField.getText());

            ResultSet queryResult = preparedStatement.executeQuery();

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    loginMessageLabel.setText("Welcome!");
                    return true;
                } else {
                    loginMessageLabel.setText("Invalid login.\n Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void registerButtonAction(ActionEvent e) throws Exception {
        loginMessageLabel.setText("you're going to register");
        changeScene(registerButton, "/com/example/concertticketbuy/SingUp.fxml");

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