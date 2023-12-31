package fr.pantheonsorbonne.miage.game.monopoly.plateau;

import fr.pantheonsorbonne.miage.game.monopoly.jeu.Pioche;
import fr.pantheonsorbonne.miage.game.monopoly.joueur.Joueur;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.cartes.Carte;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.cartes.CartePayerOuChance;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.cartes.CartePrison;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.cartes.avancer.CarteAvancerCaseNormale;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.cartes.avancer.CarteAvancerGare;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.cartes.payer.CartePayerFixe;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.cartes.recevoir.CarteRecevoirFixe;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.cartes.recevoir.CarteRecevoirJoueurs;
import fr.pantheonsorbonne.miage.game.monopoly.plateau.cartes.reculer.CarteReculerNom;

public class CaisseDeCommunaute extends Case {

    private static Pioche piocheCaisseCommunaute = new Pioche();

    public CaisseDeCommunaute(String name) {
        super(name);
    }

    static {

        //On ajoute les cartes Caisse de communauté
        piocheCaisseCommunaute.ajouterCarte(new CarteAvancerCaseNormale("Placez-vous sur la case départ.","Case départ"));
        piocheCaisseCommunaute.ajouterCarte(new CarteRecevoirFixe("Erreur de la banque en votre faveur. Recevez 200€.", 200));
        piocheCaisseCommunaute.ajouterCarte(new CartePayerFixe("Payez la note du médecin 50€.", 50));
        piocheCaisseCommunaute.ajouterCarte(new CarteRecevoirFixe("La vente de votre stock vous rapporte 50€.", 50));
        piocheCaisseCommunaute.ajouterCarte(new CartePrison("Aller en prison. Rendez-vous directement à la prison. Ne franchissez pas par la case départ, ne touchez pas 200€."));
        piocheCaisseCommunaute.ajouterCarte(new CarteReculerNom("Retournez à Belleville", "Boulevard de BelleVille"));
        piocheCaisseCommunaute.ajouterCarte(new CarteRecevoirFixe("Recevez votre revenu annuel 100€.", 100));
        piocheCaisseCommunaute.ajouterCarte(new CarteRecevoirJoueurs("C’est votre anniversaire, Happy Birthday Champion ! Chaque joueur doit vous donner 10€.", 10));
        piocheCaisseCommunaute.ajouterCarte(new CarteRecevoirFixe("Les contributions vous remboursent la somme de 20€.", 20));
        piocheCaisseCommunaute.ajouterCarte(new CarteRecevoirFixe("Recevez votre intérêt sur l’emprunt à 7% 25€.", 25));
        piocheCaisseCommunaute.ajouterCarte(new CartePayerFixe("Payez votre Police d’Assurance 50€.", 50));
        piocheCaisseCommunaute.ajouterCarte(new CartePayerOuChance("Payez une amende de 10€ ou bien tirez une carte « CHANCE »", 10));
        piocheCaisseCommunaute.ajouterCarte(new CarteAvancerGare("Rendez-vous à la gare la plus proche. Si vous passez par la case départ, recevez 200€."));
        piocheCaisseCommunaute.ajouterCarte(new CarteRecevoirFixe("Vous avez gagné le deuxième Prix de Beauté. Recevez 10€.", 10));
        piocheCaisseCommunaute.ajouterCarte(new CarteRecevoirFixe("Vous héritez 100€", 100));

    }

    public void appliquerEffetCase(Joueur joueur) {
        Carte carteTiree = piocheCaisseCommunaute.piocherCarte();
        System.out.println(joueur.getName() + " a tiré une carte Caissé de Communauté : " + carteTiree.getDescription());
        try {
            carteTiree.appliquerEffet(joueur);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
