package fr.pantheonsorbonne.miage.game.monopoly.plateau.cartes.recevoir;

import java.util.List;

import fr.pantheonsorbonne.miage.game.monopoly.jeu.JeuLocal;
import fr.pantheonsorbonne.miage.game.monopoly.joueur.Joueur;
import fr.pantheonsorbonne.miage.game.monopoly.joueur.PasAssezArgentException;

public class CarteRecevoirJoueurs extends CarteRecevoir {

    private int montant;

    public CarteRecevoirJoueurs(String description, int montant) {
        super(description);
        this.montant = montant;
    }

    int nombreJoueurs = 1;

    public void joueursDonnentArgent(Joueur joueur) throws PasAssezArgentException {
        JeuLocal jeu = JeuLocal.getInstance();
        List<Joueur> listeJoueurs = jeu.getListeJoueurs();
        if (listeJoueurs == null) {
            JeuLocal.initialiserListeJoueurs();
        }
        nombreJoueurs = listeJoueurs.size();

        for (Joueur j : listeJoueurs) {
            if (!j.equals(joueur)) {
                if (j.getPorteMonnaie() < montant) {
                    j.declarerPerte();
                    throw new PasAssezArgentException(j.getName()+" n'a pas assez d'argent pour payer donc il a perdu!");
                }
                j.payer(montant);
            }
        }
    }

    @Override
    protected int montantARecevoir() {
        return montant*(nombreJoueurs - 1); // tous les joueurs sauf celui qui tire la carte vont payer
    }
    
}