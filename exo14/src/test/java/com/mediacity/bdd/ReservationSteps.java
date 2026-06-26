package com.mediacity.bdd;

import com.mediacity.model.Adherent;
import com.mediacity.model.Ouvrage;
import com.mediacity.service.ReservationService;
import io.cucumber.java.fr.*;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationSteps {

    private final ReservationService reservationService = new ReservationService();
    
    // Registres pour retrouver les entités via leur nom/titre dans les scénarios
    private final Map<String, Ouvrage> ouvrages = new HashMap<>();
    private final Map<String, Adherent> adherents = new HashMap<>();
    
    private Exception exceptionLevee;
    private Adherent adherentNotifie;

    @Étantdonné("un ouvrage {string} qui est actuellement emprunté")
    public void un_ouvrage_emprunte(String titre) {
        ouvrages.put(titre, new Ouvrage((long) ouvrages.size(), titre, false));
    }

    @Étantdonné("un adhérent {string}")
    public void un_adherent(String nom) {
        adherents.put(nom, new Adherent((long) adherents.size(), nom, false, 0));
    }

    @Étantdonné("un adhérent {string} qui est suspendu")
    public void un_adherent_suspendu(String nom) {
        adherents.put(nom, new Adherent((long) adherents.size(), nom, true, 3));
    }

    @Étantdonné("un adhérent {string} a déjà réservé {string}")
    public void un_adherent_a_deja_reserve(String nomAdherent, String titreOuvrage) {
        un_adherent(nomAdherent);
        reservationService.reserver(adherents.get(nomAdherent), ouvrages.get(titreOuvrage));
    }

    @Étantdonné("un ouvrage {string} qui vient d'être retourné")
    public void un_ouvrage_qui_vient_detre_retourne(String titre) {
        ouvrages.put(titre, new Ouvrage((long) ouvrages.size(), titre, false));
    }

    @Étantdonné("que {string} est première dans la file d'attente pour {string}")
    @Étantdonné("{string} est en position {int} dans la file d'attente")
    public void est_premiere_file_attente(String nom, String titre) {
        un_adherent(nom);
        reservationService.reserver(adherents.get(nom), ouvrages.get(titre));
    }

    @Quand("{string} demande à réserver {string}")
    public void demande_a_reserver(String nom, String titre) {
        try {
            reservationService.reserver(adherents.get(nom), ouvrages.get(titre));
        } catch (Exception e) {
            exceptionLevee = e;
        }
    }

    @Quand("le retour de l'ouvrage est enregistré")
    public void retour_ouvrage_enregistre() {
        // Prend le premier ouvrage de la map par simplicité pour ce test
        Ouvrage ouvrage = ouvrages.values().iterator().next();
        adherentNotifie = reservationService.enregistrerRetour(ouvrage);
    }

    @Quand("{string} annule sa réservation")
    public void annule_sa_reservation(String nom) {
        Ouvrage ouvrage = ouvrages.values().iterator().next();
        reservationService.annulerReservation(adherents.get(nom), ouvrage);
    }

    @Alors("la réservation est acceptée")
    public void reservation_acceptee() {
        assertThat(exceptionLevee).isNull();
    }

    @Alors("{string} est ajoutée à la file d'attente pour {string}")
    public void est_ajoutee_file_attente(String nom, String titre) {
        int position = reservationService.getPositionFileAttente(adherents.get(nom), ouvrages.get(titre));
        assertThat(position).isGreaterThan(0);
    }

    @Alors("{string} est placé en position {int} dans la file d'attente")
    public void est_place_en_position(String nom, int positionAttendue) {
        // Cherche l'ouvrage dans la map
        Ouvrage ouvrage = ouvrages.values().iterator().next();
        int position = reservationService.getPositionFileAttente(adherents.get(nom), ouvrage);
        assertThat(position).isEqualTo(positionAttendue);
    }

    @Alors("l'ouvrage est mis de côté pour {string}")
    public void ouvrage_mis_de_cote(String nom) {
        Ouvrage ouvrage = ouvrages.values().iterator().next();
        assertThat(ouvrage.isDisponible()).isFalse();
    }

    @Alors("{string} est notifiée de la disponibilité")
    public void notifie_disponibilite(String nom) {
        assertThat(adherentNotifie).isNotNull();
        assertThat(adherentNotifie.getNom()).isEqualTo(nom);
    }

    @Alors("la réservation est refusée avec le motif {string}")
    public void reservation_refusee_motif(String motif) {
        assertThat(exceptionLevee).isNotNull();
        assertThat(exceptionLevee.getMessage()).contains(motif);
    }

    @Alors("{string} est retirée de la file d'attente pour {string}")
    public void retiree_file_attente(String nom, String titre) {
        int position = reservationService.getPositionFileAttente(adherents.get(nom), ouvrages.get(titre));
        assertThat(position).isEqualTo(-1); // -1 indique qu'il n'est plus dans la file
    }
}