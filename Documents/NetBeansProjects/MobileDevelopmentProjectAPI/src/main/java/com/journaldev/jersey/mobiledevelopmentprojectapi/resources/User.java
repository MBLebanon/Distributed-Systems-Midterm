/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.journaldev.jersey.mobiledevelopmentprojectapi.resources;

import com.journaldev.jersey.mobiledevelopmentprojectapi.DataBaseConnection;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MBLebanon
 */
public class User {

    public static String register(String username, String password) throws SQLException, NoSuchAlgorithmException {
        int count = 0;

        try (java.sql.Connection connection = DataBaseConnection.connect()) {
            if (connection != null) {
                String sqlQuery = "SELECT COUNT(*) FROM users WHERE username = ?;";
                try (java.sql.PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                    preparedStatement.setString(1, username);
                    try (java.sql.ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next())
                            count = resultSet.getInt(1);
                    }

                    if (count > 0)
                        return "Username already exists!";

                    try {
                        java.security.SecureRandom random = java.security.SecureRandom.getInstanceStrong();
                        byte[] salt = new byte[32];
                        random.nextBytes(salt);

                        String base64Salt = java.util.Base64.getEncoder().encodeToString(salt)
                                .replaceAll("[^a-zA-Z0-9]", ""); // Remove non-alphanumeric characters

                        salt = base64Salt.getBytes(java.nio.charset.StandardCharsets.UTF_8);

                        byte[] inputBytes = password.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                        byte[] saltedDataBytes = new byte[salt.length + inputBytes.length];
                        System.arraycopy(inputBytes, 0, saltedDataBytes, 0, inputBytes.length);
                        System.arraycopy(salt, 0, saltedDataBytes, inputBytes.length, salt.length);

                        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
                        byte[] hashBytes = digest.digest(saltedDataBytes);

                        StringBuilder hexString = new StringBuilder();
                        for (byte hashByte : hashBytes)
                            hexString.append(String.format("%02x", hashByte));

                        String hashedData = hexString.toString();
                        String sqlQuery2 = "INSERT INTO users (username, password_hashed, salt) VALUES (?, ?, ?)";
                        try (java.sql.PreparedStatement preparedStatement2 = connection.prepareStatement(sqlQuery2)) {
                            preparedStatement2.setString(1, username);
                            preparedStatement2.setString(2, hashedData);
                            preparedStatement2.setBytes(3, salt);

                            preparedStatement2.executeUpdate();

                            return "Success!";
                        } catch (SQLException ex) {
                            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                            throw ex; // Rethrow the exception
                        }

                    } catch (Exception e) {
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            } else
                return "No data provided!";
        }
        return "Failed";
    }

    public static String login(String username, String password) throws SQLException, NoSuchAlgorithmException {
        int userID;
        try (java.sql.Connection connection = DataBaseConnection.connect()) {

            String selectQuery = "SELECT id, username, password_hashed, salt, active FROM users WHERE username = ?;";
            try (java.sql.PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setString(1, username.toLowerCase());
                try (java.sql.ResultSet resultSet = selectStatement.executeQuery()) {
                    if (!resultSet.next())
                        return "Username or Password Wrong!";
                    userID = resultSet.getInt("id");
                    boolean active = resultSet.getBoolean("active");

                    if (!active)
                        return "Username or Password Wrong!";

                    String storedHashedPassword = resultSet.getString("password_hashed");
                    byte[] storedSalt = resultSet.getBytes("salt");

                    byte[] inputBytes = password.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                    byte[] saltedDataBytes = new byte[storedSalt.length + inputBytes.length];
                    System.arraycopy(inputBytes, 0, saltedDataBytes, 0, inputBytes.length);
                    System.arraycopy(storedSalt, 0, saltedDataBytes, inputBytes.length, storedSalt.length);

                    java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
                    byte[] hashBytes = digest.digest(saltedDataBytes);

                    StringBuilder hexString = new StringBuilder();
                    String hex;
                    for (byte hashByte : hashBytes) {
                        hex = Integer.toHexString(0xff & hashByte);
                        if (hex.length() == 1)
                            hexString.append('0');
                        hexString.append(hex);
                    }

                    String hashedData = hexString.toString();
                    String uniqueValue = java.util.UUID.randomUUID().toString();

                    if (hashedData.equals(storedHashedPassword)) {
                        return "Access Granted!";
                    } else
                        return "Access Denied!";
                }
            }

        }
    }
}
