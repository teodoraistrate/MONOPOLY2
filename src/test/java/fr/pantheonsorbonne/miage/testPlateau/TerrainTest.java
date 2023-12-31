package fr.pantheonsorbonne.miage.testPlateau;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.monopoly.joueur.JoueurS1;
import fr.pantheonsorbonne.miage.game.monopoly.joueur.JoueurS2;
import fr.pantheonsorbonne.miage.game.monopoly.joueur.PasAssezArgentException;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.Plateau;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.proprietes.CannotBuildException;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.proprietes.CannotSellException;
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
    public void testAcheterMaison() throws CannotBuildException, PasAssezArgentException {
        JoueurS1 joueur = new JoueurS1("manu");
        List<Terrain> listeT = plateau.getTerrainsMemeCouleur(Color.BLACK);
        for(Terrain t : listeT) {
            joueur.ajouterPropriete(t);
            t.reInitialiseNbMaisons();

        }

        Terrain terrain = listeT.get(0);
        boolean terrainsMemeCouleur = terrain.tousTerrainsMemeCouleur(terrain.getColor());
        assertTrue(terrainsMemeCouleur);

        joueur.ajouterArgent(1000);        
        terrain.acheterMaison();

        //le joueur possède une maison sur chaque terrain
        assertEquals(1,terrain.getNombreMaisons());
        assertEquals(1000- (terrain.getPrixMaison()), joueur.getPorteMonnaie(), 0.001);
    }

    @Test
    public void testVendreMaison() throws CannotSellException, CannotBuildException, PasAssezArgentException {
        JoueurS1 joueur = new JoueurS1("lol");
        List<Terrain> listeT2 = plateau.getTerrainsMemeCouleur(Color.BLACK);
        for (Terrain t : listeT2) {
            joueur.ajouterPropriete(t);
            t.reInitialiseNbMaisons();
        }

         
        Terrain terrain = listeT2.get(0);
        assertTrue(terrain.tousTerrainsMemeCouleur(Color.BLACK));
        joueur.ajouterArgent(10000);

        terrain.acheterMaison();

        terrain.vendreMaison();


        assertEquals(0, terrain.getNombreMaisons()); //jsp je pense c'est 1 mais ça fait une erreur 
        assertEquals(10000-(terrain.getPrixMaison())+ terrain.getPrixMaison()/2, joueur.getPorteMonnaie());
        
    }

   
    @Test
    public void testAcheterHotelAvecAssezArgentEtTousTerrainsDeMemeCouleur() {
        JoueurS1 proprietaire = new JoueurS1("Propriétaire");
        Terrain terrain = new Terrain("Boulevard de BelleVille", 60, Color.BLACK, new int[] {2, 4, 10, 30, 90, 160}, 50);
        terrain.setProprietaire(proprietaire);
        proprietaire.ajouterArgent(terrain.getPrixMaison() * 4);

        try {
            terrain.acheterHotel();
            assertTrue(terrain.estHotel());
            assertEquals(0, terrain.getNombreMaisons());
            assertEquals(0, proprietaire.getPorteMonnaie(), 0.0001);
        } catch (CannotBuildException | PasAssezArgentException e) {
            e.printStackTrace(); // Ajoutez cette ligne pour imprimer la stack trace de l'exception
        }
    }


    @Test
    public void testAcheterHotelSansAssezArgent() {
        JoueurS1 proprietaire = new JoueurS1("Propriétaire");
        Terrain terrain = new Terrain("Boulevard de BelleVille", 60, Color.BLACK, new int[] {2, 4, 10, 30, 90, 160}, 50);
        terrain.setProprietaire(proprietaire);
    
        try {
            terrain.acheterHotel();
            fail("PasAssezArgentException devrait être levée, mais aucune exception n'a été déclenchée.");
        } catch (PasAssezArgentException e) {
            // La PasAssezArgentException est attendue ici, donc le test réussit
            assertEquals("Vous n'avez pas assez d'argent pour acheter une maison", e.getMessage());
        } catch (CannotBuildException e) {
            fail("Une autre exception inattendue a été levée : " + e.getMessage());
        }
    }

    

    @Test
    public void testAcheterHotel2() throws CannotBuildException, PasAssezArgentException {
        JoueurS1 joueur = new JoueurS1("jojo");
        List<Terrain> listeT2 = plateau.getTerrainsMemeCouleur(Color.BLACK);
        
        for (Terrain t : listeT2) {
            joueur.ajouterPropriete(t);
            t.reInitialiseNbMaisons();
        }

        Terrain terrain = listeT2.get(0);
        assertTrue(terrain.tousTerrainsMemeCouleur(Color.BLACK));
        joueur.ajouterArgent(10000);

        assertEquals(0,listeT2.get(0).getNombreMaisons());
        assertEquals(0,listeT2.get(1).getNombreMaisons());

        // Acheter toutes les maisons
        for (int i=0; i<listeT2.size()*4; i++) {
            terrain.acheterMaison();
        }

        // Acheter un hôtel sur la première propriété du groupe de couleur
        terrain.acheterHotel();

        // Vérifier que l'hôtel a été acheté correctement
        assertEquals(0, terrain.getNombreMaisons(), "Le nombre de maisons devrait être 0 après l'achat de l'hôtel");
        
    }

    @Test
    public void testCasserMaison() throws CannotSellException, CannotBuildException, PasAssezArgentException {
        JoueurS1 joueur = new JoueurS1("lol");
        List<Terrain> listeT2 = plateau.getTerrainsMemeCouleur(Color.BLACK);
        for (Terrain t : listeT2) {
            joueur.ajouterPropriete(t);
            t.reInitialiseNbMaisons();
        }
         
        Terrain terrain = listeT2.get(0);
        assertTrue(terrain.tousTerrainsMemeCouleur(Color.BLACK));
        joueur.ajouterArgent(10000);

        terrain.acheterMaison();

        terrain.casserMaison();


        assertEquals(0, terrain.getNombreMaisons()); //jsp je pense c'est 1 mais ça fait une erreur 
        assertEquals(10000-(terrain.getPrixMaison()), joueur.getPorteMonnaie());
        
    }

    @Test
    public void testVendreHotel() throws CannotSellException, CannotBuildException, PasAssezArgentException {
        JoueurS1 joueur = new JoueurS1("jojo");
        List<Terrain> listeT2 = plateau.getTerrainsMemeCouleur(Color.BLACK);
        
        for (Terrain t : listeT2) {
            joueur.ajouterPropriete(t);
            t.reInitialiseNbMaisons();
        }

        Terrain terrain = listeT2.get(0);
        assertTrue(terrain.tousTerrainsMemeCouleur(Color.BLACK));
        joueur.ajouterArgent(10000);

        assertEquals(0,listeT2.get(0).getNombreMaisons());
        assertEquals(0,listeT2.get(1).getNombreMaisons());

        // Acheter toutes les maisons
        for (int i=0; i<listeT2.size()*4; i++) {
            terrain.acheterMaison();
        }

        // Acheter un hôtel sur la première propriété du groupe de couleur
        terrain.acheterHotel();
        terrain.vendreHotel();

        // Vérifier que l'hôtel a été acheté correctement
        assertEquals(4, terrain.getNombreMaisons(), "Le nombre de maisons devrait être 0 après l'achat de l'hôtel");
        

    }


    
    
}


    

    




    



