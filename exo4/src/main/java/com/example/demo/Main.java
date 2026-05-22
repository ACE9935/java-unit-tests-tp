package com.example.demo;

public class Main {
    public static void main(String[] args) {
        // Instanciation de la classe PasswordValidator
        PasswordValidator validator = new PasswordValidator();
        
        // Définition d'un mot de passe de test
        String testPassword = "Password1!";
        
        // Affichage des résultats
        System.out.println("Password to test: " + testPassword);
        System.out.println("Is Valid: " + validator.isValid(testPassword));
        System.out.println("Validation Message: " + validator.getErrorMessage(testPassword));
    }
}