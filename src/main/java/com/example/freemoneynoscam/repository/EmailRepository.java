package com.example.freemoneynoscam.repository;

import org.springframework.web.context.request.WebRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmailRepository {

  // Skaber kontakt til en database, DatabaseConnectionManager.getConnection(), og sætter de i en variable conn, når det skabes en ny, new EmailRepository
  private Connection conn = DatabaseConnectionManager.getConnection();

  // Forsøger at indføre en bruger svar, WebRequest dataFromForm, ind i tabellen test, hvis ikke muligt sendes false tilbage ellers sendes true
  public boolean postEmail(WebRequest dataFromForm) {
    // Finder de to svar, name og email, som er brugerens svar i den WebRequest dataFromForm der sendes med metodekaldet, og lægges i String variabler
    String userName = dataFromForm.getParameter("name");
    String userEmail = dataFromForm.getParameter("email");
    try {
      // Forsøger, try, at indføre String variabler, userName og UserEmail, ind i den tilsluttede databases, conn, tabel, test,
      PreparedStatement psts = conn.prepareStatement("INSERT INTO test(name,email) VALUES(?,?)");
      // Sætter, setString(index, userSvar), de svar som brugeren gav, dataFromForm, ind i tabellen, executeUpdate()
      psts.setString(1, userName); // Index 1 henfører til den første parameter, name
      psts.setString(2, userEmail); // Index 2 henfører til den anden parameter, email
      psts.executeUpdate();
    } catch (SQLException e) {
      // Hvis der er en fejl, catch (SQLException e), som f.eks. at den email, Primary Key, allerede eksisterer i tabellen
      System.out.println("Noget gik galt SQL Exception inde i EmailRepository postEmail()");
      System.out.println(e);
      // Så hvis det ikke lykkes at sætte brugerens svar ind i tabellen, vil der blive sendt false tilbage
      return false;
    }
    // Ellers vil alt går som det skal og ingen SQLException sker, så sendes der true tilbage
    return true;
  }
}