package com.example.concertticketbuy.controller;

import com.example.concertticketbuy.model.UserData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMeniuController {

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

    private UserData user;
    public static void initializeUserData(String Username){

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

        stage.close();

        stage.setScene(scene);
        stage.show();
    }

}
