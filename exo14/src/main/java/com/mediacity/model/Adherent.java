package com.mediacity.model;

public class Adherent {
    private Long id;
    private String nom;
    private boolean suspendu;
    private int nombreRetardsImportants;

    public Adherent(Long id, String nom, boolean suspendu, int nombreRetardsImportants) {
        this.id = id;
        this.nom = nom;
        this.suspendu = suspendu;
        this.nombreRetardsImportants = nombreRetardsImportants;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public boolean isSuspendu() { return suspendu || nombreRetardsImportants >= 3; }
    
    public void setSuspendu(boolean suspendu) { this.suspendu = suspendu; }
    public int getNombreRetardsImportants() { return nombreRetardsImportants; }
    public void setNombreRetardsImportants(int nombreRetardsImportants) { this.nombreRetardsImportants = nombreRetardsImportants; }
}