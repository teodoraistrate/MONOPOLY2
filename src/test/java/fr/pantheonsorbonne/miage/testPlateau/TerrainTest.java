package fr.pantheonsorbonne.miage.testPlateau;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.monopoly.joueur.JoueurS1;
import fr.pantheonsorbonne.miage.game.monopoly.joueur.JoueurS2;
import fr.pantheonsorbonne.miage.game.monopoly.joueur.PasAssezArgentException;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.Plateau;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.proprietes.CannotBuildException;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.proprietes.Propriete;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.proprietes.Terrain;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;
import java.util.List;

public class TerrainTest {

    private Plateau plateau;
    private JoueurS1 joueur;

    @BeforeEach
    public void setUp() {
        plateau = Plateau.getInstance();
        joueur = new JoueurS1("TestJoueur");
    }


    @Test
    public void testGetPrixMaison() {
        Terrain terrain = new Terrain("TerrainTest", 200, Color.GREEN, new int[]{10, 50, 150, 450, 625, 750, 875, 925, 975, 1025}, 100);
        assertEquals(100, terrain.getPrixMaison());
    }

    @Test
    public void testGetTableauLoyer() {
        int[] tableauLoyer = {10, 50, 150, 450, 625, 750, 875, 925, 975, 1025};
        Terrain terrain = new Terrain("TerrainTest", 200, Color.GREEN, tableauLoyer, 100);
        assertArrayEquals(tableauLoyer, terrain.getTableauLoyer());
    }

    @Test
    public void testGetNombreToursInitialSquatteur() {
        Terrain terrain = new Terrain("TerrainTest", 200, Color.GREEN, new int[]{10, 50, 150, 450, 625, 750, 875, 925, 975, 1025}, 100);
        assertEquals(0, terrain.getNombreToursInitialSquatteur());
    }

    @Test
    public void testGetNombreMaisons() {
        Terrain terrain = new Terrain("TerrainTest", 200, Color.GREEN, new int[]{10, 50, 150, 450, 625, 750, 875, 925, 975, 1025}, 100);
        assertEquals(0, terrain.getNombreMaisons());
    }

    @Test
    public void testAugmenterNbMaisons() {
        Terrain terrain = new Terrain("TerrainTest", 200, Color.GREEN, new int[]{10, 50, 150, 450, 625, 750, 875, 925, 975, 1025}, 100);
        terrain.augmenterNbMaisons();
        assertEquals(1, terrain.getNombreMaisons());
    }

    @Test
    public void testGetLoyerWithoutHotel() {
        int[] tableauLoyer = {10, 50, 150, 450, 625, 750, 875, 925, 975, 1025};
        Terrain terrain = new Terrain("TerrainTest", 200, Color.GREEN, tableauLoyer, 100);


        // Aucune maison
        assertEquals(20, terrain.getLoyer());

        // Ajout d'une maison
        terrain.augmenterNbMaisons();
        assertEquals(20, terrain.getLoyer());

        // Ajout de deux maisons
        terrain.augmenterNbMaisons();
        assertEquals(150, terrain.getLoyer());
    }


    @Test
    public void testAcheterMaison() throws CannotBuildException, PasAssezArgentException {
        JoueurS1 joueur = new JoueurS1("manu");
        List<Terrain> listeT = plateau.getTerrainsMemeCouleur(Color.BLACK);
        for(Terrain t : listeT) {
            joueur.ajouterPropriete(t);
        }

        Terrain terrain = listeT.get(0);
        boolean terrainsMemeCouleur = terrain.tousTerrainsMemeCouleur(terrain.getColor());
        assertTrue(terrainsMemeCouleur);

        joueur.ajouterArgent(1000);
        terrain.acheterMaison();
        terrain.acheterMaison();        
        
        terrain.acheterMaison();

        //le joueur possède une maison sur chaque terrain
        assertEquals(1, terrain.getNombreMaisons());
        assertEquals(1000- 3*(terrain.getPrixMaison()), joueur.getPorteMonnaie(), 0.001);
    }

    

    @Test
    public void testAcheterHotel() throws CannotBuildException, PasAssezArgentException {
        JoueurS1 joueur = new JoueurS1("jojo");
        List<Terrain> listeT2 = plateau.getTerrainsMemeCouleur(Color.BLACK);
        for (Terrain t : listeT2) {
            joueur.ajouterPropriete(t);
        }
        
        Terrain terrain = listeT2.get(0);
        assertTrue(terrain.tousTerrainsMemeCouleur(Color.BLACK));

        joueur.ajouterArgent(10000);

        // Acheter 12 maisons
        for (int i = 0; i < 12; i++) {
            terrain.acheterMaison();
        }

        Terrain avecHotel = terrain;
        for (Terrain t : listeT2) {
            if (t.getNombreMaisons() == 4) {
                avecHotel = t;
            }
        }

        avecHotel.acheterHotel();
        //c'est 0 car on a réinitialisé le nombre de maisons après l'achat de l'hotel
        assertEquals(0, avecHotel.getNombreMaisons());
        assertEquals(10000 - 13 * terrain.getPrixMaison(), joueur.getPorteMonnaie(), 0.0001);
    }

    




    
}


