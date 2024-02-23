package com.example.concertticketbuy.controller;

import com.example.concertticketbuy.model.CardDetails;
import com.example.concertticketbuy.model.SessionManager;
import com.example.concertticketbuy.model.UserData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserAccountMenuController {
    @FXML
    private Button homeButton;

    @FXML
    private Button storeButton;

    @FXML
    private Button shoppingCartButton;

    @FXML
    private Button accountButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Button exitButton;
    @FXML
    private Button changeUserDataButton;
    @FXML
    private Button addUserCardButton;
    @FXML
    private Button deleteUserDataButton;
    @FXML
    private TextField newUsernameLabel;
    @FXML
    private TextField newPasswordLabel;
    @FXML
    private TextField newEmailLabel;
    @FXML
    private TextField newPhoneNumberLabel;
    @FXML
    private TextField addCardNumberLabel;
    @FXML
    private TextField addCardExpirationDateLabel;
    @FXML
    private TextField addCardCvcLabel;
    @FXML
    private Label changeUserDataErrorLabel;
    @FXML
    private Label addCardDetailsErrorLabel;
    @FXML
    private Label usernameUserLabel;
    private String username;
    private SessionManager sessionManager = SessionManager.getInstance();
    private UserData currentUserData;
    private CardDetails currentCardDetails = new CardDetails("", "", "", 0);

    @FXML
    private void initialize() {
        // Retrieve UserData from SessionManager
        currentUserData = sessionManager.getCurrentUserData();
        currentCardDetails.setUserId(currentUserData.getUserId());
        if (currentCardDetails.checkForUserCard(currentUserData.getUserId())) {
            currentCardDetails = currentCardDetails.getUserCardDetails(currentUserData.getUserId());
            addCardCvcLabel.setText(currentCardDetails.getCvc());
            addCardExpirationDateLabel.setText(currentCardDetails.getExpirationDate());
            addCardNumberLabel.setText(currentCardDetails.getCardNumber());
        }
        sessionManager.setCardDetails(currentCardDetails);
        if (currentUserData != null) {
            String username = currentUserData.getUsername();
            usernameUserLabel.setText(username);
            newUsernameLabel.setPromptText(username);
            newEmailLabel.setPromptText(currentUserData.getEmail());
            newPhoneNumberLabel.setPromptText(currentUserData.getPhoneNumber());
            newPasswordLabel.setPromptText(currentUserData.getPassword());
            changeUserDataErrorLabel.setText("");
            addCardDetailsErrorLabel.setText("");

        }
    }

    public boolean validateCardNumber(String cardNumber) {
        if (cardNumber.matches("\\d{16}")) {
            return true;
        } else {
            addCardDetailsErrorLabel.setText("Card number must have 16 digits and contain only\n numbers! and no spaces!");
            addCardNumberLabel.setText("");
            return false;
        }
    }

    public boolean validateCVC(String cvc) {
        if (cvc.matches("\\d{3}")) {
            return true;
        } else {
            addCardDetailsErrorLabel.setText("CVC must have ONLY 3 digits and contain only\n numbers!");
            addCardCvcLabel.setText("");
            return false;
        }
    }

    public boolean validateExpirationDate(String expirationDate) {
        if (expirationDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return true;
        } else {
            addCardDetailsErrorLabel.setText("Expiration date must have the following\n format: YYYY-MM-DD");
            addCardExpirationDateLabel.setText("");
            return false;
        }
    }

    public boolean validateCardDetails(String cardNumber, String expirationDate, String cvc) {
        if (validateCardNumber(cardNumber) && validateExpirationDate(expirationDate) && validateCVC(cvc)) {
            addCardDetailsErrorLabel.setText("Card details added successfully!");
            return true;
        } else {
            return false;
        }
    }

    private void addAdminAccount(UserData newUserData) {
        newUserData.setUserType(0);
        if(newUserData.getUsername().equals("") || newUserData.getPassword().equals("") || newUserData.getEmail().equals("") || newUserData.getPhoneNumber().equals("")){
            changeUserDataErrorLabel.setText("Invalid data\n to change account\n credentials!");
            return;
        }
        if(!newUserData.checkAdminCanExist(newUserData)){
            changeUserDataErrorLabel.setText("Admin account already exists!");
            return;
        }
        changeUserDataErrorLabel.setText("New admin account created successfully!");
        newPasswordLabel.setText("");
        newUsernameLabel.setText("");
        newEmailLabel.setText("");
        newPhoneNumberLabel.setText("");
    }



    public void changeUserDataOnAction(ActionEvent event) {
        UserData oldUserData = sessionManager.getCurrentUserData();
        UserData newUserData = new UserData(oldUserData.getUserId(), "", "", "", "", oldUserData.getUserType());
        newUserData.setUsername(newUsernameLabel.getText());
        newUserData.setPassword(newPasswordLabel.getText());
        newUserData.setEmail(newEmailLabel.getText());
        newUserData.setPhoneNumber(newPhoneNumberLabel.getText());

        if (oldUserData.getUserType() == 0) {
            addAdminAccount(newUserData);
            return;
        }

        newUserData.setUserType(1);

        if (newUserData.secureUpdateUserCredentials(newUserData)) {
            changeUserDataErrorLabel.setText("User data changed\nCheck prompt text for\n confirmation!");
            usernameUserLabel.setText(newUserData.getUsername());
            newUsernameLabel.setPromptText(newUserData.getUsername());
            newEmailLabel.setPromptText(newUserData.getEmail());
            newPhoneNumberLabel.setPromptText(newUserData.getPhoneNumber());
            newPasswordLabel.setPromptText(newUserData.getPassword());
            newPasswordLabel.setText("");
            newUsernameLabel.setText("");
            newEmailLabel.setText("");
            newPhoneNumberLabel.setText("");
        } else {
            changeUserDataErrorLabel.setText("Invalid data\n to change account\n credentials!");
        }
    }


    public void addUserCardOnAction(ActionEvent event) {
        if (currentCardDetails.checkForUserCard(currentUserData.getUserId())) {
            addCardDetailsErrorLabel.setText("You can add only one card at a time!\nDelete the current card and try again!");
            return;
        }
        if (validateCardDetails(addCardNumberLabel.getText(), addCardExpirationDateLabel.getText(), addCardCvcLabel.getText())) {
            currentUserData = sessionManager.getCurrentUserData();
            CardDetails userCardDetails = new CardDetails(addCardNumberLabel.getText(), addCardExpirationDateLabel.getText(), addCardCvcLabel.getText(), currentUserData.getUserId());
            if (userCardDetails.addUserCard(userCardDetails, currentUserData.getUserId())) {
                addCardDetailsErrorLabel.setText("Card details added successfully!");
                setCardDetailsTextFieldEmpty();
                currentUserData = sessionManager.getCurrentUserData();
                currentCardDetails.setUserId(currentUserData.getUserId());
                if (currentCardDetails.checkForUserCard(currentUserData.getUserId())) {
                    currentCardDetails = currentCardDetails.getUserCardDetails(currentUserData.getUserId());
                }
                sessionManager.setCardDetails(currentCardDetails);

            } else {
                addCardDetailsErrorLabel.setText("Error!\nCard details already exists!");
                setCardDetailsTextFieldEmpty();
            }
        }

    }

    public void setCardDetailsTextFieldEmpty() {
        addCardNumberLabel.setText("");
        addCardExpirationDateLabel.setText("");
        addCardCvcLabel.setText("");
    }

    public void deleteUserCardOnAction(ActionEvent event) {
        currentUserData = sessionManager.getCurrentUserData();
        CardDetails userCardDetails = new CardDetails("", "", "", currentUserData.getUserId());
        if (userCardDetails.checkForUserCard(currentUserData.getUserId())) {
            if (userCardDetails.deleteUserCard(currentUserData.getUserId())) {
                addCardDetailsErrorLabel.setText("Card details deleted successfully!");
                currentCardDetails.setCvc("");
                currentCardDetails.setCardNumber("");
                currentCardDetails.setExpirationDate("");
                sessionManager.setCardDetails(currentCardDetails);
            }
        } else {
            addCardDetailsErrorLabel.setText("No card registered!");
        }

    }

    public void deleteUserDataOnAction(ActionEvent event) {

    }

    @FXML
    private void showHomeScreen(ActionEvent event) throws Exception {
        changeScene(homeButton, "/com/example/concertticketbuy/MainUserMeniu.fxml");
    }

    @FXML
    private void showStoreScreen(ActionEvent event) throws Exception {
        changeScene(storeButton, "/com/example/concertticketbuy/StoreMenu.fxml");
    }

    @FXML
    private void showShoppingCartScreen(ActionEvent event) throws Exception {
        changeScene(shoppingCartButton, "/com/example/concertticketbuy/ShoppingCartMenu.fxml");
    }

    @FXML
    private void showLogOutScreen(ActionEvent event) throws Exception {
        changeScene(logOutButton, "/com/example/concertticketbuy/LogIn.fxml");
    }

    @FXML
    private void showAccountScreen(ActionEvent event) throws Exception {
        changeScene(accountButton, "/com/example/concertticketbuy/UserAccountMenu.fxml");
    }

    @FXML
    private void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }


    private void changeScene(Button button, String fxmlFileName) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        Parent root = loader.load();

        Stage stage = (Stage) button.getScene().getWindow();
        Scene scene = new Scene(root);

        // Close the current stage (scene)
        stage.close();

        // Open the new scene
        stage.setScene(scene);
        stage.show();
    }
}
