package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RechercheVilleTest {

    private RechercheVille rechercheVille;

    @BeforeEach
    public void setUp() {
        rechercheVille = new RechercheVille();
    }

    @Test
    public void testRechercher_MoinsDeDeuxCaracteres_LeveNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            rechercheVille.Rechercher("a");
        });
    }

    @Test
    public void testRechercher_TexteExact_RetourneVillesCommencantParLeTexte() {
        List<String> resultat = rechercheVille.Rechercher("Va");
        
        assertEquals(2, resultat.size());
        assertTrue(resultat.contains("Valence"));
        assertTrue(resultat.contains("Vancouver"));
    }

    @Test
    public void testRechercher_InsensibleALaCasse() {
        List<String> resultat = rechercheVille.Rechercher("va");
        
        assertEquals(2, resultat.size());
        assertTrue(resultat.contains("Valence"));
        assertTrue(resultat.contains("Vancouver"));
    }

    @Test
    public void testRechercher_PartieDuNom_RetourneVillesContenantLeTexte() {
        List<String> resultat = rechercheVille.Rechercher("ape");
        
        assertEquals(1, resultat.size());
        assertTrue(resultat.contains("Budapest"));
    }

    @Test
    public void testRechercher_Asterisque_RetourneToutesLesVilles() {
        List<String> resultat = rechercheVille.Rechercher("*");
        
        assertEquals(16, resultat.size());
        assertTrue(resultat.contains("Paris"));
        assertTrue(resultat.contains("Istanbul"));
    }
}