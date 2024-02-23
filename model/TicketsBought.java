package com.example.concertticketbuy.model;

public class TicketsBought {
    private int ticket_id;
    private int user_id;
    private int concert_id;
    private int ticket_type_id;
    private int ticket_price;
    private String ticket_type;
    private String band;
    private String date;
    private String location;
    private String username;
    private String email;
    private String phone_number;

    public TicketsBought(int ticket_id, int user_id, int concert_id, int ticket_type_id, int ticket_price, String ticket_type, String band, String date, String location, String username, String email, String phone_number) {
        this.ticket_id = ticket_id;
        this.user_id = user_id;
        this.concert_id = concert_id;
        this.ticket_type_id = ticket_type_id;
        this.ticket_price = ticket_price;
        this.ticket_type = ticket_type;
        this.band = band;
        this.date = date;
        this.location = location;
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
    }

    public int getTicketId() {
        return ticket_id;
    }

    public int getUserId() {
        return user_id;
    }

    public int getConcertId() {
        return concert_id;
    }

    public int getTicketTypeId() {
        return ticket_type_id;
    }

    public int getTicketPrice() {
        return ticket_price;
    }

    public String getTicketType() {
        return ticket_type;
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

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phone_number;
    }
}
