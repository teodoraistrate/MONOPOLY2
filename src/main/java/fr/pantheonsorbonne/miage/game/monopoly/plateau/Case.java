package fr.pantheonsorbonne.miage.game.monopoly.plateau;

import fr.pantheonsorbonne.miage.game.monopoly.joueur.Joueur;

public abstract class Case {
    private static int nombreCases = 0;
    private int idCase;
    private String name;

    protected Case(String name) {
        this.name = name;
        this.idCase = nombreCases++;
    }

    public String getName() {
        return name;
    }

    public int getIdCase() {
        return idCase;
    }

    public abstract void appliquerEffetCase(Joueur joueur);

}
