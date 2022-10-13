package com.example.freemoneynoscam.services;

public class ValidateEmailService {

    // Metode der validerer om ens email overholder logikken for en email, tager imod en String email og sender en boolean tilbage
    public boolean isEmailValid(String email){
        // Gemmer i en boolean variabel om en indsendt email indeholder, contains(), et @ og et .
        boolean isContainingAtSymbol = email.contains("@");
        boolean isContainingPeriod = email.contains(".");

        // Hvis emailen har begge tegn i sig, så finder vi ud af hvilket tegn kommer i hvilken rækkefølge
        if (isContainingAtSymbol && isContainingPeriod) {
            int indexOfAtSymbol = email.indexOf("@");
            int indexOfPeriod = email.lastIndexOf(".");
            // Hvis @ kommer før . , så vil der sendes true tilbage, som i at emailen overholder logikken
            if (indexOfAtSymbol < indexOfPeriod) {
                return true;
            }
        } // Ellers sendes der false tilbage, da emailen ikke overholdet logikken
        return false;
    }
}
