package com.eai.openfeignservice.user.outils.enums;

public enum ClientProfil {

    PARTICULIER("Particulier résidant au Maroc"),
    MRE("Marocain résidant à l’étranger"),
    ETUDIANT("Etudiant résidant au Maroc"),
    PROFESSIONNEL("Professionnels");
    private final String label;

    ClientProfil(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
