package com.example.concertticketbuy.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private static SessionManager instance;
    private String username;
    private UserData currentUserData;
    private CardDetails currentCardDetails = new CardDetails("", "", "", 0);
    private List<TicketSold> ticketTypes;

    private ConcertData currentConcertData = new ConcertData();

    public void setCurrentConcertData(ConcertData currentConcertData) {
        this.currentConcertData = currentConcertData;
    }

    public ConcertData getCurrentConcertData() {
        return currentConcertData;
    }


    public int getUserId() {
        return currentUserData.getUserId();
    }
    private SessionManager() {
        this.ticketTypes = new ArrayList<>();
    }

    public List<TicketSold> getTicketTypes() {
        return ticketTypes;
    }

    public void storeRepetedUserPurchases(int userId, int concertId, int ticketId, int nbTickets) {


        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();

        String insertUserPurchaseHistory = "UPDATE tickets_sold " + "SET nb_tickets = nb_tickets + ? " + "WHERE user_id = ? AND ticket_type = ? AND concert_id = ?";

        try (PreparedStatement preparedStatement = connectDB.prepareStatement(insertUserPurchaseHistory)) {
            preparedStatement.setInt(3, ticketId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(4, concertId);
            preparedStatement.setInt(1, nbTickets);


            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Ticket purchase stored successfully");
            } else {
                System.out.println("Ticket purchase failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connectDB != null) {
                    connectDB.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
    }

    public void storeUserPurchases(int userId, int ticketPrice, int nbTickets) {
        List<TicketSold> purchaseHistory = getTicketTypes();
        TicketSold ticketSold = purchaseHistory.get(purchaseHistory.size() - 1);
        System.out.println("hello!");
        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();

        String insertUserPurchaseHistory = "INSERT INTO tickets_sold (ticket_type, user_id, concert_id, price, nb_tickets) " +
                "VALUES (?, ?, ?, ?, ?) ";

        try (PreparedStatement preparedStatement = connectDB.prepareStatement(insertUserPurchaseHistory)) {
            preparedStatement.setInt(1, ticketSold.getTicketType().getTicketTypeId());
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, ticketSold.getConcertId());
            preparedStatement.setInt(4, ticketPrice);
            preparedStatement.setInt(5, nbTickets);


            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Ticket purchase stored successfully");
            } else {
                System.out.println("Ticket purchase failed");
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            storeRepetedUserPurchases(userId, ticketSold.getConcertId(), ticketSold.getTicketType().getTicketTypeId(), nbTickets);
        } finally {
            try {
                if (connectDB != null) {
                    connectDB.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void printUserTickets() {
        System.out.println("Tickets bought by user " + username + ":");
        for (TicketSold ticketSold : ticketTypes) {
            System.out.println("Concert ID: " + ticketSold.getConcertId() +
                    ", Ticket Type: " + ticketSold.getTicketType().getTicketType());
        }
    }

    public void addTicket(TicketType ticketType, int numberOfTickets, int concertId) {
        // Check if the ticketType and numberOfTickets are valid
        if (ticketType != null && numberOfTickets > 0) {
            // Add the specified number of tickets of the given type
            for (int i = 0; i < numberOfTickets; i++) {
                TicketSold ticketSold = new TicketSold(ticketType, concertId);
                ticketTypes.add(ticketSold);
            }
        }
    }

    public UserData getCurrentUserData() {
        return currentUserData;
    }

    public void setCurrentUserData(UserData currentUserData) {
        this.currentUserData = currentUserData;
    }

    public CardDetails getCurrentCardDetails() {
        return currentCardDetails;
    }

    public boolean checkForUserCard(int userId) {
        return currentCardDetails.checkForUserCard(userId);
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
        // Create and set UserData when username is set
        if (username != null && !username.isEmpty()) {
            currentUserData = addNewUserData(username);
        }
    }

    public void setCardDetails(CardDetails cardDetails) {
        this.currentCardDetails = cardDetails;
    }

    private UserData addNewUserData(String username) {
        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();
        UserData userData = null;

        String selectUserData = "SELECT * FROM users WHERE username = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(selectUserData);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String phoneNumber = resultSet.getString("phone_number");
                int userType = resultSet.getInt("user_type");

                userData = new UserData(userId, username, password, email, phoneNumber, userType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connectDB != null) {
                    connectDB.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return userData;
    }
}
