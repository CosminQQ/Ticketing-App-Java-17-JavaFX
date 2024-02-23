package com.example.concertticketbuy.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseBagData {
    private int userId;
    private int concertId;
    private int ticketId;
    private int ticketsBought;
    private int pricePaidPerTicket;
    private String ticketZone;
    private String band;
    private String date;

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setConcertId(int concertId) {
        this.concertId = concertId;
    }
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }
    public void setTicketsBought(int ticketsBought) {
        this.ticketsBought = ticketsBought;
    }
    public void setPricePaidPerTicket(int pricePaidPerTicket) {
        this.pricePaidPerTicket = pricePaidPerTicket;
    }
    public void setTicketZone(String ticketZone) {
        this.ticketZone = ticketZone;
    }
    public void setBand(String band) {
        this.band = band;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getUserId() {
        return userId;
    }
    public int getConcertId() {
        return concertId;
    }
    public int getTicketId() {
        return ticketId;
    }
    public int getTicketsBought() {
        return ticketsBought;
    }
    public int getPricePaidPerTicket() {
        return pricePaidPerTicket;
    }
    public String getTicketZone() {
        return ticketZone;
    }
    public String getBand() {
        return band;
    }
    public String getDate() {
        return date;
    }


    public List<PurchaseBagData> endPurchasedTickets(int userId) {
        List<PurchaseBagData> purchasedTickets = new ArrayList<>();

        try (Connection connection = new DBUtils().getConnection()) {
            String selectQuery = "SELECT ts.ticket_type, ts.concert_id, ts.nb_tickets, ts.price, " +
                    "tt.zone_name, c.band, c.date " +
                    "FROM tickets_sold ts " +
                    "JOIN ticket_type tt ON ts.ticket_type= tt.ticket_id " +
                    "JOIN concerts c ON ts.concert_id = c.concert_id " +
                    "WHERE ts.user_id = ?";


            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        PurchaseBagData purchaseData = new PurchaseBagData();
                        purchaseData.setUserId(userId);
                        purchaseData.setTicketId(resultSet.getInt("ticket_type"));
                        purchaseData.setConcertId(resultSet.getInt("concert_id"));
                        purchaseData.setTicketsBought(resultSet.getInt("nb_tickets"));
                        purchaseData.setPricePaidPerTicket(resultSet.getInt("price"));
                        purchaseData.setTicketZone(resultSet.getString("zone_name"));
                        purchaseData.setBand(resultSet.getString("band"));
                        purchaseData.setDate(resultSet.getString("date"));

                        purchaseData.pricePaidPerTicket = purchaseData.pricePaidPerTicket * purchaseData.ticketsBought;
                        purchasedTickets.add(purchaseData);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return purchasedTickets;
    }

    public void printToConsoleTicketsBoughtByUser(int userId) {
        List<PurchaseBagData> purchasedTickets = endPurchasedTickets(userId);

        System.out.println("Your purchased tickets are: ");
        for (PurchaseBagData purchaseData : purchasedTickets) {
            System.out.println(purchaseData.getTicketsBought() + " tickets for " + purchaseData.getBand() +
                    " concert on " + purchaseData.getDate() + " in " + purchaseData.getTicketZone() +
                    " zone, for " + purchaseData.getPricePaidPerTicket() + " lei each.");
        }
        if(purchasedTickets.isEmpty()){
            System.out.println("You have no purchased tickets.");
        }
    }

}
