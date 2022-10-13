package com.example.freemoneynoscam.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Skaber kontakt til en lokal database, når der bliver kaldt på metoden getConnection() på DatabaseConnectionManager
public class DatabaseConnectionManager {
  private static String hostname;
  private static String username;
  private static String password;
  private static Connection conn;

  public static Connection getConnection(){
    if(conn != null){
      return conn;
    }

    hostname = "jdbc:mysql://localhost:3306/clbotest";
    username = "root";
    password = "87654321";
    try {
      conn = DriverManager.getConnection(hostname, username, password);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return conn;
  }
}
