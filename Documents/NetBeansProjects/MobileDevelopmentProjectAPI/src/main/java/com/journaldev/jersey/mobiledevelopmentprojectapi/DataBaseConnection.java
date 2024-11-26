package com.journaldev.jersey.mobiledevelopmentprojectapi;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author MBLebanon
 */
public class DataBaseConnection {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/mobiledevelopmentproject";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "root";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static java.sql.Connection connect() throws SQLException {
            return java.sql.DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

}
