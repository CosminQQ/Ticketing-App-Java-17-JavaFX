package com.example.concertticketbuy.controller;

import com.example.concertticketbuy.model.PurchaseBagData;
import com.example.concertticketbuy.model.SessionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import java.util.List;

public class ShoppingCartMenuController {

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
    private TableView<PurchaseBagData> tableView;

    @FXML
    private TableColumn<PurchaseBagData, String> bandColumn;

    @FXML
    private TableColumn<PurchaseBagData, String> dateColumn;

    @FXML
    private TableColumn<PurchaseBagData, Integer> ticketsBoughtColumn;

    @FXML
    private TableColumn<PurchaseBagData, String> ticketZoneColumn;

    @FXML
    private TableColumn<PurchaseBagData, Integer> pricePaidColumn;

    private SessionManager sessionManager = SessionManager.getInstance();
    private int currentUser = sessionManager.getUserId();
    private PurchaseBagData purchaseBagDataList = new PurchaseBagData();
    private List<PurchaseBagData> purchaseBagData = purchaseBagDataList.endPurchasedTickets(currentUser);

    public void initialize() {
        bandColumn.setCellValueFactory(new PropertyValueFactory<>("band"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        ticketsBoughtColumn.setCellValueFactory(new PropertyValueFactory<>("ticketsBought"));
        ticketZoneColumn.setCellValueFactory(new PropertyValueFactory<>("ticketZone"));
        pricePaidColumn.setCellValueFactory(new PropertyValueFactory<>("pricePaidPerTicket"));
        populateTableView();

    }

    private void populateTableView() {

        ObservableList<PurchaseBagData> observableList = FXCollections.observableArrayList(purchaseBagData);
        tableView.setItems(observableList);
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
    public void printShoppingCart(){
        int userId = SessionManager.getInstance().getUserId();
        PurchaseBagData purchaseBagData = new PurchaseBagData();
        List<PurchaseBagData> purchasedTickets = purchaseBagData.endPurchasedTickets(userId);
        if(purchasedTickets.size() == 0){
            System.out.println("You have no tickets in your shopping cart!");
        }
        else{
        purchaseBagData.printToConsoleTicketsBoughtByUser(userId);
    }}


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
