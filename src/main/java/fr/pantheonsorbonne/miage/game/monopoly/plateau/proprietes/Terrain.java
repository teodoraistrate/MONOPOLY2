package fr.pantheonsorbonne.miage.game.monopoly.plateau.proprietes;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.pantheonsorbonne.miage.game.monopoly.jeu.JeuLocal;
import fr.pantheonsorbonne.miage.game.monopoly.joueur.PasAssezArgentException;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.Plateau;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.Prison;

public class Terrain extends Propriete {

    Plateau plateau = Plateau.getInstance();

    private int[] tableauLoyer;
    private int nombreMaisons = 0;
    private boolean estHotel = false;
    private int prixMaison;
    private Color color;
    private boolean estSquatte;
    private int nombreToursInitialSquatteur;
    private boolean estPrisonAdditionnelle = false;
    private int loyerPrison;

    Prison prison = Prison.getInstance("Prison");

    public Terrain(String name, int price, Color color, int[] tableauLoyer, int prixMaison) {
        super(name, price);
        this.color = color;
        this.tableauLoyer = tableauLoyer;
        this.prixMaison = prixMaison;
        this.loyerPrison = tableauLoyer[0]/10 +1;
    }

    // getteurs

    public int getPrixMaison() {
        return prixMaison;
    }

    public int[] getTableauLoyer() {
        return tableauLoyer;
    }

    public int getNombreToursInitialSquatteur() {
        return nombreToursInitialSquatteur;
    }

    public int getNombreMaisons() {
        return nombreMaisons;
    }

    
    public int getLoyerPrison() {
        return loyerPrison;
    }
    
    public void augmenterNbMaisons() {
        this.nombreMaisons++;
    }

    @Override
    public int getLoyer() {
        if (this.tousTerrainsMemeCouleur(this.getColor())) {
            if (this.estHotel())
                return tableauLoyer[5];
            else if (this.getNombreMaisons() >= 1) {
                return tableauLoyer[this.getNombreMaisons()+1];
            } else {
                return tableauLoyer[0]*2;
            }
        }
        return tableauLoyer[0];
    }

    public boolean tousTerrainsMemeCouleur(Color couleur) {
        boolean resultat = true;
        Plateau p = Plateau.getInstance();
        List<Terrain> listeT = p.getTerrainsMemeCouleur(couleur);
        for (Terrain t : listeT) {
            if (t.getProprietaire() != this.getProprietaire()) {
                resultat = false;
            }
        }
        return resultat;
    }

    public Map<Terrain, Integer> getListeNombreMaisons() {
        Map<Terrain, Integer> listeNombreMaisons = new HashMap<>();
        List<Terrain> terrains = plateau.getTerrainsMemeCouleur(this.getColor());
        for (Terrain t : terrains) {
            listeNombreMaisons.put(t, t.getNombreMaisons());
        }
        return listeNombreMaisons;
    }

    public Color getColor() {
        return color;
    }

    // setteurs

    public void setNombreToursInitialSquatteur(int nbTours) {
        this.nombreToursInitialSquatteur = nbTours;
    }

    // is a

    public boolean estHotel() {
        return estHotel;
    }

    public boolean estSquatte() {
        return estSquatte;
    }

    public boolean estPrisonAdditionnelle() {
        return estPrisonAdditionnelle;
    }

    // autres méthodes

    public void acheterMaison() throws CannotBuildException, PasAssezArgentException {
        if (this.getProprietaire().getPorteMonnaie() < this.getPrixMaison()) {
            throw new PasAssezArgentException("Vous n'avez pas assez d'argent pour acheter une maison");
        } else {
            if (!this.tousTerrainsMemeCouleur(this.getColor())) {
                throw new CannotBuildException("Vous n'avez pas tous les terrains de la couleur " + this.getColor()
                        + ", donc vous ne pouvez pas construire la maison!");
            } else {
                int maximumMaisons = 4; // Le maximum de maisons par terrain
                Map<Terrain, Integer> listeNombreMaisons = this.getListeNombreMaisons();
    
                // Vérifier si le maximum de maisons est déjà atteint
                boolean toutesMaisonsPossibles = listeNombreMaisons.values().stream().allMatch(nbMaisons -> nbMaisons == maximumMaisons);
                //boolean toutesMaisonsPossibles = listeNombreMaisons.values().stream().noneMatch(nbMaisons -> nbMaisons < maximumMaisons);

                if (toutesMaisonsPossibles) {
                    throw new CannotBuildException("Vous avez construit toutes les maisons possibles!");
                }
    
                // Rechercher le nombre minimum de maisons
                int minimumNbMaisons = 5;
                for (Terrain t : listeNombreMaisons.keySet()) {
                    if (t.getNombreMaisons() < minimumNbMaisons) {
                        minimumNbMaisons = t.getNombreMaisons();
                        // même si ça ne semble pas efficace, on va calculer le minimum mais on ne va pas sauvegarder le terrain choisi
                        // on veut avoir du contrôle quand on va choisir le terrain sur lequel on va construire la maison
                        // quand on a mis terrain choisi = t dans cette boucle on a eu des erreurs dans les tests unitaires
                    }
                }

                Terrain terrainChoisi = null;

                // on va d'abord vérifier si on peut construire une maison sur le terrain qui appelle la méthode : il est prioritaire
                if (this.getNombreMaisons() == minimumNbMaisons) {
                    terrainChoisi = this;
                } else {
                    // sinon, on va construire la maison sur le premier terrain de cette couleur qui est disponible
                    for (Terrain t : listeNombreMaisons.keySet()) {
                        if (t.getNombreMaisons() == minimumNbMaisons) {
                            terrainChoisi = t;
                            break; // on ne va prendre que le premier
                        }
                    }
                }

    
                if (terrainChoisi != null) {
                    terrainChoisi.getProprietaire().payer(terrainChoisi.getPrixMaison());
                    terrainChoisi.augmenterNbMaisons();
                } else {
                    throw new CannotBuildException("Impossible de trouver un terrain pour construire une maison supplémentaire.");
                }
            }
        }
    }
    

    public void acheterHotel() throws CannotBuildException, PasAssezArgentException {
        if (this.getProprietaire().getPorteMonnaie() < this.getPrixMaison()) {
            throw new PasAssezArgentException("Vous n'avez pas assez d'argent pour acheter une maison");
        } else if (!this.tousTerrainsMemeCouleur(this.getColor())) {
            throw new CannotBuildException("Vous n'avez pas tous les terrains de la couleur " + this.getColor()
                    + ", donc vous ne pouvez pas construire l'hotel!");
        } else {
            Map<Terrain, Integer> listeNombreMaisons = this.getListeNombreMaisons();
            for (Terrain t : listeNombreMaisons.keySet()) {
                if (listeNombreMaisons.get(t) != 4 && listeNombreMaisons.get(t) != 0) {
                    throw new CannotBuildException(
                            "Vous n'avez construit le maximum de maisons sur les terrains de cette couleur!");
                }
            }
            this.getProprietaire().payer(prixMaison);
            estHotel = true;
            nombreMaisons = 0;
        }
    }

    

    public void vendreHotel() throws CannotSellException {
        if (!this.tousTerrainsMemeCouleur(color)) {
            throw new CannotSellException("Vous n'avez même pas tous les terrains de cette couleur! ");
        }
        if (!this.estHotel())
            throw new CannotSellException("Vous n'avez pas d'hotel sur ce terrain!");
        this.estHotel = false;
        this.getProprietaire().ajouterArgent(prixMaison / 2);
        this.nombreMaisons = 4;
    }

    public void vendreMaison() throws CannotSellException {
        if (!this.tousTerrainsMemeCouleur(color)) {
            throw new CannotSellException("Vous n'avez même pas tous les terrains de cette couleur! ");
        }
        Map<Terrain, Integer> listeNombreMaisons = this.getListeNombreMaisons();

        // chercher le nb maximum de maisons pour voir ce qu'on peut vendre
        int maximumNbMaisons = -1;
        for (Terrain t : listeNombreMaisons.keySet()) {
            if (t.getNombreMaisons() > maximumNbMaisons) {
                maximumNbMaisons = t.getNombreMaisons();
            }
        }

        Terrain terrainChoisi = null;

        if (this.getNombreMaisons() == maximumNbMaisons) {
            terrainChoisi = this;
        } else {
            for (Terrain t : listeNombreMaisons.keySet()) {
            if (t.getNombreMaisons() == maximumNbMaisons) {
                terrainChoisi = t;
                break;
            }
        }
        }
        if (terrainChoisi != null) {
            if (terrainChoisi.getNombreMaisons() == 0) {
                throw new CannotSellException("Vous n'avez pas de maison sur ce terrain!");
            }
            terrainChoisi.getProprietaire().ajouterArgent(prixMaison / 2);
            terrainChoisi.nombreMaisons--;
        }
    }

    public void casserMaison() {
        Map<Terrain, Integer> listeNombreMaisons = this.getListeNombreMaisons();

        // chercher le nb maximum de maisons pour voir ce qu'on peut vendre
        int maximumNbMaisons = -1;
        for (Terrain t : listeNombreMaisons.keySet()) {
            if (t.getNombreMaisons() > maximumNbMaisons) {
                maximumNbMaisons = t.getNombreMaisons();
            }
        }

        Terrain terrainChoisi = null;
        if (this.getNombreMaisons() == maximumNbMaisons) {
            terrainChoisi = this;
        } else {
            for (Terrain t : listeNombreMaisons.keySet()) {
            if (t.getNombreMaisons() == maximumNbMaisons) {
                terrainChoisi = t;
                break;
            }
        }
        }

        if (terrainChoisi != null) {
            terrainChoisi.nombreMaisons--;
        }
    }

    public void squatter() {
        this.estSquatte = true;
        System.out.println("Un squatteur a occupé " + this.getName() + " :(");
    }

    public void fairePartirSquatteur() throws PasAssezArgentException {
        if (this.getProprietaire().getPorteMonnaie() < 200) {
            throw new PasAssezArgentException("Vous n'avez pas assez d'argent pour faire le squatteur partir!");
        } else {
            this.byeSquatteur();
            this.getProprietaire().payer(200);
            double probabilite = 0.1; // une chance sur 10 d'aller en prison s'il paye les 200€
            if (JeuLocal.verifierProbabilite(probabilite)) {
                prison.mettreJoueurEnPrison(this.getProprietaire());
            }
        }
    }

    public void byeSquatteur() {
        this.estSquatte = false;
        System.out.println("Le squatteur est parti de " + this.getName() + "yay!!");
    }
    // on va vérifier si les 8 tours sont passés dans le main

    public void transformerProprieteEnPrison() {
        this.estPrisonAdditionnelle = true;
        System.out.println(this.getName() + " est maintenant une prison additionnelle!");
    }

    public void reInitialiseNbMaisons () {
        this.nombreMaisons=0;
    }
}
