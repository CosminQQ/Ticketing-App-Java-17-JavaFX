package com.example.concertticketbuy.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketType {
    private int ticketId;
    private String zoneName;
    private int priceIncrement;

    public TicketType(int ticket_type_id, String ticket_type, int ticket_price) {
        this.ticketId = ticket_type_id;
        this.zoneName = ticket_type;
        this.priceIncrement = ticket_price;
    }

    public int getTicketTypeId() {
        return ticketId;
    }

    public String getTicketType() {
        return zoneName;
    }

    public int getTicketPrice() {
        return priceIncrement;
    }

    public static List<TicketType> getTicketTypes() {
        List<TicketType> ticketTypes = new ArrayList<>();

        try (Connection connection = new DBUtils().getConnection()) {
            String selectQuery = "SELECT * FROM ticket_type";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int ticketId = resultSet.getInt("ticket_id");
                    String zoneName = resultSet.getString("zone_name");
                    int priceIncrement = resultSet.getInt("price_increment");

                    TicketType ticketType = new TicketType(ticketId, zoneName, priceIncrement);
                    ticketTypes.add(ticketType);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        return ticketTypes;
    }

    public String getTicketZoneFromDatabase(int ticketId) {
        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();

        String getTicketZone = "SELECT zone_name FROM ticket_type WHERE ticket_id = ?";
        String ticketZone = null;

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(getTicketZone);
            preparedStatement.setInt(1, ticketId);

            ResultSet queryResult = preparedStatement.executeQuery();

            while (queryResult.next()) {
                ticketZone = queryResult.getString("zone_name");
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

        return ticketZone;
    }


}
