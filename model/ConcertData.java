package com.example.concertticketbuy.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConcertData {
    private int concert_id;
    private String band;
    private String date;
    private String location;
    private String zone;
    private int ticket_price;

    public void setConcert_id(int concert_id) {
        this.concert_id = concert_id;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTicket_price(int ticket_price) {
        this.ticket_price = ticket_price;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public int getConcert_id() {
        return concert_id;
    }

    public int getTicket_price() {
        return ticket_price;
    }

    public String getBand() {
        return band;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getZone() {
        return zone;
    }


    public ConcertData getConcertDataFromDatabase(int concertId) {
        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();

        String getConcertData = "SELECT * FROM concerts WHERE concert_id = ?";

        ConcertData concertData = new ConcertData();

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(getConcertData);
            preparedStatement.setInt(1, concertId);

            ResultSet queryResult = preparedStatement.executeQuery();

            while (queryResult.next()) {
                concertData.setConcert_id(queryResult.getInt("concert_id"));
                concertData.setBand(queryResult.getString("band"));
                concertData.setDate(queryResult.getString("date"));
                concertData.setLocation(queryResult.getString("location"));
                concertData.setZone(queryResult.getString("zone"));
                concertData.setTicket_price(queryResult.getInt("ticket_price"));
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

        return concertData;
    }

}
