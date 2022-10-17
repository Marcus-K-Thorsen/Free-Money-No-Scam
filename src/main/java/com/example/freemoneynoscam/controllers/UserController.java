package com.example.freemoneynoscam.controllers;

import com.example.freemoneynoscam.model.User;
import com.example.freemoneynoscam.repository.EmailRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {
  private EmailRepository emailRepo = new EmailRepository();


  // Videresend bruger til en ny webside/HTML fil der hedder "user-information" når der kaldes (href) på "/user"
  @GetMapping("/user")
  public String user(Model model) {
    // Metoden fetchSingleUser() i class EmailRepository, bringer et user object og lægger det i en variabel: userToDisplay
    User userToDisplay = emailRepo.fetchSingleUser();
    // Det fundne user object: userToDisplay, sættes som en værdi til navnet "user", som sendes over til HTML filen "user-information"
    model.addAttribute("user", userToDisplay);
    return "user-information";
  }

  // Videresend bruger til en ny webside/HTML fil der hedder "user-informations" når der kaldes (href) på "/user"
  @GetMapping("/users")
  public String users(Model model) {
    // Metoden fetchFourUsers() i class EmailRepository, bringer en List<User> af 4 user objects, lægger det i en variabel: users
    List<User> users = emailRepo.fetchFourUsers();
    // Den fundne liste af user objekter bliver taget ud af listen: users.get(x) og sættes som værdi til navnet "userX", som sendes til HTML filen "user-informations"
    model.addAttribute("user1", users.get(0));
    model.addAttribute("user2", users.get(1));
    model.addAttribute("user3", users.get(2));
    model.addAttribute("user4", users.get(3));
    return "user-informations";
  }
}
