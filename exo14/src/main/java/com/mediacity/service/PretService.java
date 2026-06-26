package com.mediacity.service;

import com.mediacity.model.Adherent;
import com.mediacity.model.Ouvrage;
import com.mediacity.model.Pret;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PretService {

    private static final double PENALITE_PAR_JOUR = 0.15;
    private static final int DUREE_PRET_JOURS = 21;

    public Pret emprunter(Adherent adherent, Ouvrage ouvrage) {
        if (adherent.isSuspendu()) {
            throw new IllegalStateException("L'adhérent est suspendu et ne peut plus emprunter.");
        }
        if (!ouvrage.isDisponible()) {
            throw new IllegalStateException("L'ouvrage est déjà emprunté.");
        }

        ouvrage.setDisponible(false);
        return new Pret(adherent, ouvrage, LocalDate.now());
    }

    public double calculerPenalites(Pret pret, LocalDate dateRetourEffective) {
        LocalDate datePrevue = pret.getDateRetourPrevue();
        if (dateRetourEffective.isAfter(datePrevue)) {
            long joursDeRetard = ChronoUnit.DAYS.between(datePrevue, dateRetourEffective);
            return joursDeRetard * PENALITE_PAR_JOUR;
        }
        return 0.0;
    }
}