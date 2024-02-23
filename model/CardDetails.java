package com.example.concertticketbuy.model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardDetails {
    private String cardNumber;
    private String expirationDate;
    private String cvc;
    private int user_id;

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public int getUserId() {
        return user_id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCvc() {
        return cvc;
    }

    public CardDetails(String cardNumber, String expirationDate, String cvc, int user_id) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvc = cvc;
        this.user_id = user_id;
    }

    public boolean addUserCard(CardDetails newCard, int userID) {
        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();


        String insertCard = "INSERT INTO card_details (user_id, card_number, expiration_date, cvc) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(insertCard);
            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, newCard.getCardNumber());
            preparedStatement.setDate(3, java.sql.Date.valueOf(newCard.getExpirationDate()));
            preparedStatement.setString(4, newCard.getCvc());

            int rowsAffected = preparedStatement.executeUpdate();


            if (rowsAffected > 0) {
                return true;
            }

        } catch (Exception e) {
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

    public boolean checkForUserCard(int userId) {
        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();

        String selectCard = "SELECT * FROM card_details WHERE user_id = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(selectCard);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeConnection(connectDB);
        }

        return false;
    }

    public boolean deleteUserCard(int userId) {
        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();

        String deleteCard = "DELETE FROM card_details WHERE user_id = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(deleteCard);
            preparedStatement.setInt(1, userId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return true;
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

        return false;
    }
    public CardDetails getUserCardDetails(int userId) {
        DBUtils userConnection = new DBUtils();
        Connection connectDB = userConnection.getConnection();
        CardDetails userCardDetails = new CardDetails("", "", "", userId);

        String selectCard = "SELECT * FROM card_details WHERE user_id = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(selectCard);
            preparedStatement.setInt(1, userCardDetails.getUserId());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userCardDetails.setCardNumber(resultSet.getString("card_number"));
                userCardDetails.setExpirationDate(resultSet.getString("expiration_date"));
                userCardDetails.setCvc(resultSet.getString("cvc"));
                return userCardDetails;
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeConnection(connectDB);
        }

        return null;
    }
}

