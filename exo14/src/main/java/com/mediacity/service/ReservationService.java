package com.mediacity.service;

import com.mediacity.model.Adherent;
import com.mediacity.model.Ouvrage;

import java.util.*;

public class ReservationService {

    // File d'attente par ouvrage
    private final Map<Ouvrage, LinkedList<Adherent>> filesAttente = new HashMap<>();

    public void reserver(Adherent adherent, Ouvrage ouvrage) {
        if (adherent.isSuspendu()) {
            throw new IllegalStateException("Adhérent suspendu");
        }
        if (ouvrage.isDisponible()) {
            throw new IllegalStateException("L'ouvrage est disponible, il peut être emprunté directement.");
        }

        filesAttente.putIfAbsent(ouvrage, new LinkedList<>());
        LinkedList<Adherent> file = filesAttente.get(ouvrage);
        
        if (!file.contains(adherent)) {
            file.add(adherent);
        }
    }

    public int getPositionFileAttente(Adherent adherent, Ouvrage ouvrage) {
        LinkedList<Adherent> file = filesAttente.get(ouvrage);
        if (file != null && file.contains(adherent)) {
            return file.indexOf(adherent) + 1; // 1-based index
        }
        return -1;
    }

    public void annulerReservation(Adherent adherent, Ouvrage ouvrage) {
        LinkedList<Adherent> file = filesAttente.get(ouvrage);
        if (file != null) {
            file.remove(adherent);
        }
    }

    public Adherent enregistrerRetour(Ouvrage ouvrage) {
        LinkedList<Adherent> file = filesAttente.get(ouvrage);
        if (file != null && !file.isEmpty()) {
            ouvrage.setDisponible(false); // Mis de côté pour le premier de la file
            return file.getFirst(); // Retourne l'adhérent à notifier
        }
        ouvrage.setDisponible(true);
        return null;
    }
}