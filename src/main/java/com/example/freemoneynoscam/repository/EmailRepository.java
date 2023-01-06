package com.example.freemoneynoscam.repository;

import com.example.freemoneynoscam.model.User;
import org.springframework.web.context.request.WebRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmailRepository {

  // Skaber kontakt til en database, DatabaseConnectionManager.getConnection(), og sætter de i en variable conn, når det skabes en ny, new EmailRepository
  private Connection conn = DatabaseConnectionManager.getConnection();

  public User findUserFromEmail (WebRequest dataFromForm) {
    String userEmail = dataFromForm.getParameter("userEmail");
    try {
      PreparedStatement psts = conn.prepareStatement("SELECT * FROM clbotest.test WHERE email=?");
      psts.setString(1, userEmail);
      ResultSet resultSet = psts.executeQuery();
      resultSet.next();
      String name = resultSet.getString("name");
      String email = resultSet.getString("email");
      return new User(name, email);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  // Forsøger at indføre en bruger svar, WebRequest dataFromForm, ind i tabellen test, hvis ikke muligt sendes false tilbage ellers sendes true
  public boolean postEmail(WebRequest dataFromForm) {
    // Finder de to svar, name og email, som er brugerens svar i den WebRequest dataFromForm der sendes med metodekaldet, og lægges i String variabler
    String userName = dataFromForm.getParameter("name");
    String userEmail = dataFromForm.getParameter("email");
    try {
      // Forsøger, try, at indføre String variabler, userName og UserEmail, ind i den tilsluttede databases, conn, tabel, test,
      PreparedStatement psts = conn.prepareStatement("INSERT INTO clbotest.test(name,email) VALUES(?,?)");
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


  // Metode der finder 4 tilfældige user objekter og sørger for de er 4 unikke user objekter eller at den har forsøgt 100 gange, så loppet i det mindste stopper hvis der ikke er 4 unikke users
  public List<User> fetchFourUsers() {
    // Skaber en tom liste der kan holder user objekter (List<User>)
    List<User> users = new ArrayList<>();
    // Sætter en counter og en maxCounter der fortæller hvornår vi har forsøgt nok gange at finde 4 unikke user objekter
    int counter = 1;
    int maxCount = 100;
    // En boolean laves der skal fortælle hvornår vi har nået vores maxCount forsøg, hvilket er false i starten
    boolean isLimitReached = false;
    // Starter et while loop, som fortsætter så længe at der er mindre end 4 user objekter i listen users
    while (users.size() < 4) {
      // Finder et user objekt fra metoden fetchingSingleUser()
      User user = fetchSingleUser();
      // Sætter en boolean ud efter en metode isUniqUser(List<User>, User) der bringer enten true: ingen user objekter i listen er den samme som den fundne user
      boolean isUniqUser = isUserUniq(users, user);
      // Hvis user objektet er unik, så vil vi tilføre (users.add(user)) til listen List<User> users
      if (isUniqUser) {
        users.add(user);
      }
      // Forøger counter med en og hvis den har nået maxCount, så sætter vi boolean isLimitReached til true og bryder (break) ud af while loopet
      counter++;
      if (counter == maxCount) {
        isLimitReached = true;
        break;
      }
    }
    // Hvis vores limit blev nået, så finder vi ud af hvor mange user objekter der mangler for at vi har 4 og finder dem der mangler uden at spørge om det er et unik user objekt i forhold til listen users
    if (isLimitReached) {
      int amountOfUsersMissing = 4 - users.size();
      for (int i = 0; i < amountOfUsersMissing; i++) {
        users.add(fetchSingleUser());
      }
    }
    // Til sidst bringer (return) vi listen af user objekter
    return users;
  }

  // En metoder, modtager en liste a user objekter og et user objekt, der går igennem til en liste af user objekter og tjekker om der er nogen af de user i listen som har den samme email som det user objekt der sendes med metode kaldet
  private boolean isUserUniq(List<User> users, User user) {
    for (User userFromList: users) {
      String userFromListEmail = userFromList.getEmail();
      String userEmail = user.getEmail();
      if (userFromListEmail.equals(userEmail)) {
        return false;
      }
    }
    return true;
  }


  // Metoder der bringer et tilfældigt user objekt ud fra den connected database som attributten (conn) er tilknyttet til
  public User fetchSingleUser() {
    // Bruger metoden getRandomIndex() for at finde et tilfældigt id for en af de rows/brugere ud fra den tilknyttede database (conn)
      int randomID = getRandomIndex();
      // sætter to strings til at være tomme klar til at modtage en navn og email fra en bruger/row i databasen
      String name = "";
      String email = "";
      // Forsøger (try) at gå igennem databasens table "test" ud fra en SQL statement: "SELECT name, email FROM test WHERE id = ?"
      try {
        PreparedStatement psts2 = conn.prepareStatement("SELECT name, email FROM clbotest.test WHERE id = ?");
        // Sætter ?, som skal være 1, det første og eneste ?, til at være randomID
        psts2.setInt(1, randomID);
        // Udfører, psts2.executeQuery(), SQL statementet og lægger det resultat (row/bruger for name,email/koloner) der har id=randomID i en variable: resultatSet2
        ResultSet resultSet2 = psts2.executeQuery();

        // Gør sådan at den kan få fat i den første row vi ønsker for den fundne info om bruger/row ved at bruge .next() på den. Nok fordi den række den originalt har fat i er bare for name og email, mens vi ønsker værdierne, som den holder
        resultSet2.next();
        // Gemmer de værdier vi har fundet for en bruger med id=randomID og gemmer det i variablerne: name og email
        name = resultSet2.getString("name");
        email = resultSet2.getString("email");


      } catch (SQLException sqlException) {
        System.out.println("Det gik galt inde i fetchSingleUSer SQLEx");
        System.err.println(sqlException);
      }

      // Laver et user objekt der ikke er sat til noget
      User databaseUser;
      // hvis string variablerne name og email er stadig tomme, så sættes user objektet (databaseUser) til at være null
      if (name.isEmpty() && email.isEmpty()) {
        databaseUser = null;
        // Ellers hvis det kun er navnet, som stadig er tomt, fordi den bruger ikke gav sit navn til database, så sættes variablen email til at være brugeren fra databasens email
      } else if (name.isEmpty()){
        databaseUser = new User(email);
        // Ellers så må både name og email variablerne være sat til at være noget og det nye user objekt til at have både navn og email fra den fundne bruger i databasen
      } else {
        databaseUser = new User(name, email);
      }
      // Til sidst returneres user objektet tilbage, enten som et null, kun email eller både med email og navn
      return databaseUser;
    }



    // Finder et random index ud fra antallet af brugere/rækker i databasen som attributten conn er tilsluttet til
  public int getRandomIndex() {
    try {
      // Laver en SQL statement der finder id for alle brugere i den tilknyttede databases table "test"
      PreparedStatement psts = conn.prepareStatement("SELECT id FROM clbotest.test");
      // Udfører(executeQuery()) SQL statementet og gemmer det i variablen resultSet
      ResultSet resultSet = psts.executeQuery();
      // Laver en liste der kan holde tal (List<Integer>), for at holde alle de bruger id's, som er i databasens table test
      List<Integer> userIDs = new ArrayList<>();
      // Så længe der er en række af id'er (resultSet.getInt("id")), så skal det id lægges til (userIDs.add(id)) listen userIDs
      while (resultSet.next()) {
        Integer id = resultSet.getInt("id");
        userIDs.add(id);
      }
      // Vi finder ud af hvor mange id'er vi har fundet inde i databasens table test
      int amountOfIds = userIDs.size();
      // Finder et random nummer, fra en metode (randomNum(int)), der er mellem 0 og op til og ikke med antal id's (amountOfIds)
      int randomIndex = randomNum(amountOfIds);
      // Returnerer en tilfældig brugers id ud fra et random tal (randomIndex) ud af listen userIDs
      return userIDs.get(randomIndex);
    } catch (SQLException sqlE) {
      throw new RuntimeException(sqlE);
    }
  }

  // Metode der finder et random tal fra 0 til men ikke med et givet max tal
  public int randomNum(int maxNum) {
    Random random = new Random();
    return random.nextInt(maxNum);
  }
  // Metode der finder et random tal fra et minimum tal (minNum burde sættes som 1) til et givet maximum tal plus det minimum tal (maxNum)
  public int randomNum(int maxNum, int minNum) {
    Random random = new Random();
    return random.nextInt(maxNum) + minNum;
  }
}