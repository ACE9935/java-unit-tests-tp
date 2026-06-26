package com.mediacity.model;

import java.time.LocalDate;

public class Pret {
    private Adherent adherent;
    private Ouvrage ouvrage;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;

    public Pret(Adherent adherent, Ouvrage ouvrage, LocalDate dateEmprunt) {
        this.adherent = adherent;
        this.ouvrage = ouvrage;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateEmprunt.plusDays(21);
    }

    public Adherent getAdherent() { return adherent; }
    public Ouvrage getOuvrage() { return ouvrage; }
    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
}