package com.example.concertticketbuy.model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    public Connection databaseLink;
    public Connection getConnection(){

        String databaseUser = "postgres";
        String databasePassword = "ceausescu";
        String url = "jdbc:postgresql://localhost:5432/postgres";

        try{

            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e){
            e.printStackTrace();
        }
        return databaseLink;
    }
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
