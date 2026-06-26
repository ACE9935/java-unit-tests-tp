package com.mediacity.service;

import com.mediacity.model.Adherent;
import com.mediacity.model.Ouvrage;
import com.mediacity.model.Pret;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PretServiceTest {

    @InjectMocks
    private PretService pretService;

    private Adherent adherent;
    private Ouvrage ouvrage;

    @BeforeEach
    void setUp() {
        adherent = new Adherent(1L, "Dupont", false, 0);
        ouvrage = new Ouvrage(100L, "Le Petit Prince", true);
    }

    @Test
    void devrait_creer_un_pret_avec_retour_dans_21_jours() {
        // Act
        Pret pret = pretService.emprunter(adherent, ouvrage);

        // Assert
        assertThat(pret).isNotNull();
        assertThat(pret.getOuvrage()).isEqualTo(ouvrage);
        assertThat(pret.getDateRetourPrevue()).isEqualTo(LocalDate.now().plusDays(21));
        assertThat(ouvrage.isDisponible()).isFalse(); // L'ouvrage n'est plus dispo
    }

    @Test
    void ne_devrait_pas_preter_un_ouvrage_indisponible() {
        // Arrange
        ouvrage.setDisponible(false);

        // Act & Assert
        assertThatThrownBy(() -> pretService.emprunter(adherent, ouvrage))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("L'ouvrage est déjà emprunté.");
    }

    @Test
    void devrait_calculer_penalites_de_0_15_euros_par_jour_de_retard() {
        // Arrange
        Pret pret = new Pret(adherent, ouvrage, LocalDate.now().minusDays(31)); // Devait être rendu il y a 10 jours (21 + 10)
        
        // Act
        double penalite = pretService.calculerPenalites(pret, LocalDate.now());

        // Assert
        assertThat(penalite).isEqualTo(1.50); // 10 jours * 0.15€
    }

    @Test
    void devrait_suspendre_adherent_apres_trois_retards_importants() {
        // Arrange
        adherent.setNombreRetardsImportants(3);

        // Act & Assert
        assertThatThrownBy(() -> pretService.emprunter(adherent, ouvrage))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("L'adhérent est suspendu et ne peut plus emprunter.");
    }
}