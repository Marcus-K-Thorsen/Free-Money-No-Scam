package com.example.freemoneynoscam.controllers;
import com.example.freemoneynoscam.model.User;
import com.example.freemoneynoscam.repository.EmailRepository;
import com.example.freemoneynoscam.services.ValidateEmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;

// Min ene ændring for denne branch

@Controller
public class IndexController {

    private final ValidateEmailService validEmailServ = new ValidateEmailService();
    private final EmailRepository emailRepository = new EmailRepository();

    // Starter forsiden ved at GetMapping, hente HTML filen index.html under templates
    @GetMapping("/")
    public String index(){
        return "index";
    }

    @PostMapping("/testen")
    public String testen(Model model, WebRequest dataFromHiddenForm) {
        String datoFelt = dataFromHiddenForm.getParameter("date");
        LocalDate datoen;

        if (datoFelt != null && !(datoFelt.isBlank())) {
            datoen = LocalDate.parse(datoFelt);
        } else {
            datoen = LocalDate.now().plusDays(7);
        }

        model.addAttribute("datoen", datoen);

        User userToDisplay = emailRepository.findUserFromEmail(dataFromHiddenForm);
        model.addAttribute("user", userToDisplay);
        return "test-side";
    }

    // Startes af f.eks. <form action="/test"> inde i index.html, en metode der tager imod et WebRequest og sendere brugeren videre til en ny side
    @PostMapping("/test")
    // PRG: P (PostMapping)
    public String test(WebRequest dataFromForm){
        System.out.println(dataFromForm.getParameter("email"));
        // skaffer brugerens input ved at <form> har <input name="email">, getParameter("email")
        String usersEmail = dataFromForm.getParameter("email");

        // Validerer at det er en korrekt email der tastes ind gennem class ValidateEmailService
        assert usersEmail != null;
        boolean isEmailValid = validEmailServ.isEmailValid(usersEmail);
        // Brugerens email sendes som en String til ValidateEmailService, som sender en boolean tilbage
        if (isEmailValid) {
            // Hvis emailen er valid, så sendes hele brugerens svar, WebRequest dataFromForm, videre til EmailRepository
            EmailRepository emailRepository = new EmailRepository();
            boolean isEmailPosted = emailRepository.postEmail(dataFromForm);
            // Hvis brugerens svar lægges, INSERT INTO, til tabellen, så bliver brugeren ført, redirect:/success, til en ny side, success.html
            if (isEmailPosted) {
                // PRG: R (redirect)
                return "redirect:/success";
            }
            // Ellers bliver brugeren ført, redirect:/failure, til en ny side, failure.html
        }
        return "redirect:/failure";
    }

    @GetMapping("/success")
    public String success () {
        // PRG: G (GetMapping)
        return "success";
    }
    @GetMapping("/failure")
    public String failure () {
        return "failure";
    }
}
