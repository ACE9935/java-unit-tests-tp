package com.mediacity.model;

public class Ouvrage {
    private Long id;
    private String titre;
    private boolean disponible;

    public Ouvrage(Long id, String titre, boolean disponible) {
        this.id = id;
        this.titre = titre;
        this.disponible = disponible;
    }

    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}