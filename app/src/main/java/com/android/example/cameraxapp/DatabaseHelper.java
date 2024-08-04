package com.android.example.cameraxapp;

import android.util.Log;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper {

    private static final String URL = "jdbc:postgresql://sidar.cz0gkmqg6q9x.eu-west-2.rds.amazonaws.com:5432/Sidar";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Password1";

    public static boolean checkDatabaseConnection() {
        try {
            // Load the PostgreSQL driver class
            Class.forName("org.postgresql.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection != null) {
                System.out.println("Connected to the database!");
                return true;
            } else {
                System.out.println("Failed to make connection!");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
            return false;
        }

    }

    public static boolean checkIfTableExists(String tableName) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getTables(null, null, tableName, new String[]{"TABLE"})) {
                if (resultSet.next()) {
                    System.out.println("Table " + tableName + " exists.");
                    return true;
                } else {
                    System.out.println("Table " + tableName + " does not exist.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Table check failed: " + e.getMessage());
            return false;
        }
    }

    public static  void insertVisitorDetails(String tableName, String DocumentType, String fullName, String IdentificationNumber,
                                             String mobileNumber, String OrganizationName, String floor) throws SQLException {
        String sqlInsert = "INSERT INTO "+ tableName + " (signInTime, documentType, fullName, IdentificationNo, PhoneNo, OrganizationName, FloorNo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sqlInsert)){
            String signInTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            pstmt.setString(1, signInTime);
            pstmt.setString(2, DocumentType);
            pstmt.setString(3, fullName);
            pstmt.setString(4, IdentificationNumber);
            pstmt.setString(5, mobileNumber);
            pstmt.setString(6, OrganizationName);
            pstmt.setString(7, floor);

            pstmt.executeUpdate();
            Log.d("Success", fullName+ " saved successfully");
        } catch (SQLException e) {
            Log.e("Err", "Insert failed "+ e.getMessage());
        }
    }

    public static void main(String[] args) {
        String tableName = "Visitor_Details";

        if (checkDatabaseConnection()) {
            checkIfTableExists(tableName);
        }
    }
}
