package com.example.demo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RechercheVille {
    
    private List<String> villes = Arrays.asList(
        "Paris", "Budapest", "Skopje", "Rotterdam", "Valence", "Vancouver", 
        "Amsterdam", "Vienne", "Sydney", "New York", "Londres", "Bangkok", 
        "Hong Kong", "Dubaï", "Rome", "Istanbul"
    );
   
    public List<String> Rechercher(String mot) {
        if ("*".equals(mot)) {
            return villes;
        }

        if (mot == null || mot.length() < 2) {
            throw new NotFoundException("Le texte de recherche doit contenir au moins 2 caractères.");
        }

        String motMinuscule = mot.toLowerCase();

        return villes.stream()
                .filter(ville -> ville.toLowerCase().contains(motMinuscule))
                .collect(Collectors.toList());
    }
}