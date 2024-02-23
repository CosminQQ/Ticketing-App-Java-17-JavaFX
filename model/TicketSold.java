package com.example.concertticketbuy.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TicketSold {
    private TicketType ticketType;
    private int concertId;

    public TicketSold(TicketType ticketType, int concertId) {
        this.ticketType = ticketType;
        this.concertId = concertId;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public int getConcertId() {
        return concertId;
    }
    public boolean storeUserPurchase(TicketSold ticketSold, int userId, int price) {
        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();

        String insertUserPurchaseHistory = "INSERT INTO tickets_sold (ticket_type, user_id, concert_id, price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connectDB.prepareStatement(insertUserPurchaseHistory)) {
            preparedStatement.setInt(1, ticketSold.getTicketType().getTicketTypeId());
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, ticketSold.getConcertId());
            preparedStatement.setInt(4, price);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
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

        return false;
    }

}
