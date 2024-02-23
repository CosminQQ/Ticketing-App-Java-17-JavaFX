package com.example.concertticketbuy.controller;

import com.example.concertticketbuy.model.ConcertData;
import com.example.concertticketbuy.model.DBUtils;
import com.example.concertticketbuy.model.SessionManager;
import com.example.concertticketbuy.model.TicketType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class StoreMenuController {
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
    private TableView<ConcertData> tableView;

    @FXML
    private TableColumn<ConcertData, String> bandColumn;

    @FXML
    private TableColumn<ConcertData, String> dateColumn;

    @FXML
    private TableColumn<ConcertData, String> locationColumn;

    @FXML
    private TableColumn<ConcertData, Integer> ticketPriceColumn;

    @FXML
    private Label availableTicketTypeLabel;

    @FXML
    private Label errorTicketBuyLabel;

    @FXML
    private TextField ticketTypeTextBox;

    @FXML
    private TextField numberOfTicketsTextBox;

    @FXML
    private Button buyticketsButton;
    @FXML
    private Button calculateTotalForUser;
    private List<TicketType> ticketTypes = TicketType.getTicketTypes();

    public void showTotalOnLabel(ActionEvent e) {
        errorTicketBuyLabel.setText("");
        int total = 0;
        ConcertData thisConcert = SessionManager.getInstance().getCurrentConcertData();
        if (Integer.parseInt(numberOfTicketsTextBox.getText()) < 10 && Integer.parseInt(numberOfTicketsTextBox.getText()) > 0) {
            if (!thisConcert.getLocation().matches("Arenele Romane")) {
                if (ticketTypeTextBox.getText().matches("1")) {
                    total = thisConcert.getTicket_price() * Integer.parseInt(numberOfTicketsTextBox.getText());
                    availableTicketTypeLabel.setText("Total: " + total + "RON");
                    return;
                } else {
                    errorTicketBuyLabel.setText("Please enter a valid zone.\n");
                }
            } else {
                for (TicketType ticketType : ticketTypes)
                    if (ticketType.getTicketTypeId() == Integer.parseInt(ticketTypeTextBox.getText())) {
                        total = (thisConcert.getTicket_price() + ticketType.getTicketPrice()) * Integer.parseInt(numberOfTicketsTextBox.getText());
                        availableTicketTypeLabel.setText("Total: " + total + "RON");
                        return;
                    }else{errorTicketBuyLabel.setText("Please enter a valid zone.\n");}

            }
        }
        errorTicketBuyLabel.setText("Please enter a valid number of tickets.\nYou can buy up to 9 tickets.\n");

    }

    public void buyTicketsActionButton(ActionEvent e) {
        if (ticketTypeTextBox.getText().isEmpty() || numberOfTicketsTextBox.getText().isEmpty()) {
            errorTicketBuyLabel.setText("Please enter the zone and\nthe number of tickets\nyou want to buy.\n");
            return;
        }
        if (numberOfTicketsTextBox.getText().matches("[1-9]") == false) {
            errorTicketBuyLabel.setText("Please enter a valid\nnumber of tickets.\nYou can buy up to 9\ntickets.\n");
            return;
        }

        SessionManager sessionManager = SessionManager.getInstance();

        if (Objects.equals(sessionManager.getCurrentConcertData().getLocation(), "Arenele Romane")) {
            for (TicketType ticketType : ticketTypes) {
                if (!ticketType.getTicketType().matches("General Access Ticket") &&
                        Objects.equals(ticketType.getTicketTypeId(), Integer.parseInt(ticketTypeTextBox.getText()))) {
                    if (!sessionManager.checkForUserCard(sessionManager.getCurrentUserData().getUserId())) {
                        errorTicketBuyLabel.setText("You don't have a card\nregistered.\nPlease register a card\nin the Account menu.\n");
                        return;
                    } else {
                        if(checkTicketsSoldValid(sessionManager.getCurrentConcertData().getConcert_id(), Integer.parseInt(numberOfTicketsTextBox.getText())) >  3000){
                            errorTicketBuyLabel.setText("There are not enough tickets\navailable for this concert.\n");
                            return;
                        }

                        sessionManager.addTicket(ticketType, Integer.parseInt(numberOfTicketsTextBox.getText()), sessionManager.getCurrentConcertData().getConcert_id());
                        errorTicketBuyLabel.setText("Ticket Bought at " + sessionManager.getCurrentConcertData().getBand() +
                                "\n in" + ticketType.getTicketType() + " zone!");
                        sessionManager.storeUserPurchases(sessionManager.getCurrentUserData().getUserId(),
                                ticketType.getTicketPrice() + sessionManager.getCurrentConcertData().getTicket_price(),Integer.parseInt(numberOfTicketsTextBox.getText()));
                        numberOfTicketsTextBox.setText("");
                        ticketTypeTextBox.setText("");
                        return;
                    }
                }
            }
        } else {
            if (ticketTypeTextBox.getText().matches("1")) {
                if (!sessionManager.checkForUserCard(sessionManager.getCurrentUserData().getUserId())) {
                    errorTicketBuyLabel.setText("You don't have a card\nregistered.\nPlease register a card\nin the Account menu.\n");
                    return;
                } else {

                    TicketType ticketType = new TicketType(4, "General access ticket", 0);
                    sessionManager.addTicket(ticketType, Integer.parseInt(numberOfTicketsTextBox.getText()),
                            sessionManager.getCurrentConcertData().getConcert_id());
                    errorTicketBuyLabel.setText("Ticket Bought at " + sessionManager.getCurrentConcertData().getBand() + "!");
                    sessionManager.storeUserPurchases(sessionManager.getCurrentUserData().getUserId(),
                            ticketType.getTicketPrice() + sessionManager.getCurrentConcertData().getTicket_price(),Integer.parseInt(numberOfTicketsTextBox.getText()));
                    numberOfTicketsTextBox.setText("");
                    ticketTypeTextBox.setText("");
                    return;
                }
            }
        }

        errorTicketBuyLabel.setText("Please enter a valid zone.\n");
    }


    private void handleBuyButtonClick(ConcertData concertData) {
        availableTicketTypeLabel.setText("Available zones for " + concertData.getBand() + ":\n");
        errorTicketBuyLabel.setText("");
        SessionManager.getInstance().setCurrentConcertData(concertData);
        //System.out.println(SessionManager.getInstance().getCurrentConcertData().getConcert_id());
        if (Objects.equals(concertData.getLocation(), "Arenele Romane")) {

            errorTicketBuyLabel.setText("WARNING:\n   You cannot buy tickets\nseated in different zones.\n");

            for (TicketType ticketType : ticketTypes)
                if (!ticketType.getTicketType().matches("General Access Ticket")) {
                    availableTicketTypeLabel.setText(availableTicketTypeLabel.getText() + ticketType.getTicketTypeId() + ". " +
                            ticketType.getTicketType() + ":  " + (ticketType.getTicketPrice() + concertData.getTicket_price()) + "RON\n");
                }
        } else {
            errorTicketBuyLabel.setText("");
            availableTicketTypeLabel.setText(availableTicketTypeLabel.getText() + "1. General acces ticket:  " + concertData.getTicket_price() + "RON\n");
        }
    }

    private void setupBuyColumn() {
        TableColumn<ConcertData, Void> buyColumn = new TableColumn<>("Buy");
        buyColumn.setCellFactory(param -> new TableCell<ConcertData, Void>() {
            private final Button buyButton = new Button("Details");

            {
                buyButton.setOnAction(event -> handleBuyButtonClick(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buyButton);
                }
            }
        });

        tableView.getColumns().add(buyColumn);
    }

    public void initialize() {
        errorTicketBuyLabel.setText("");
        availableTicketTypeLabel.setText("");
        bandColumn.setCellValueFactory(new PropertyValueFactory<>("band"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        ticketPriceColumn.setCellValueFactory(new PropertyValueFactory<>("ticket_price"));
        populateTableView();
        setupBuyColumn();
    }

    private void populateTableView() {
        ObservableList<ConcertData> concertDataList = FXCollections.observableArrayList();

        try (Connection connection = new DBUtils().getConnection()) {
            System.out.println("Connected to the database");
            String selectQuery = "SELECT * FROM concerts";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ConcertData concertData = new ConcertData();
                    concertData.setBand(resultSet.getString("band"));
                    concertData.setDate(resultSet.getString("date"));
                    concertData.setLocation(resultSet.getString("location"));
                    concertData.setTicket_price(resultSet.getInt("ticket_price"));
                    concertData.setConcert_id(resultSet.getInt("concert_id"));
                    concertDataList.add(concertData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setItems(concertDataList);
    }
    public int checkTicketsSoldValid(int concertId, int ticketsBought){
        int ticketsAvailable = 0;
        try (Connection connection = new DBUtils().getConnection()) {
            String selectQuery = "SELECT nb_tickets FROM tickets_sold WHERE concert_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setInt(1, concertId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        ticketsAvailable += resultSet.getInt("nb_tickets");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return ticketsAvailable + ticketsBought;
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
        SessionManager.getInstance().printUserTickets();
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
