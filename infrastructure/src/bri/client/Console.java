package bri.client;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Sur-couche d'affichage console pour le client BRI.
 */
public abstract class Console
{
    /** Préfixe d'affichage des messages. */
    private static final String PREFIX = "BRI > ";
    
    /** Nom par défaut de l'utilisateur courant du client. */
    private static final String UTILISATEUR_PAR_DEFAUT = "anonyme";
    /** Nom de l'utilisateur courant du client. */
    private static String utilisateur = UTILISATEUR_PAR_DEFAUT;
    /**
     * Modification du nom de l'utilisateur courant du client.
     * @param pseudo Nouvel utilisateur courant.
     */
    public static void modifier_utilisateur(final String pseudo) { utilisateur = pseudo; }

    /** Flux de sortie de la console. */
    public static PrintStream sortie = System.out;
    /** Flux d'entrée de la console. */
    public static Scanner entree = new Scanner(System.in);

    /**
     * Affichage d'un message sur la console du client.
     * @param message Message à afficher.
     * @param ligne Drapeau de saut de ligne.
     */
    public static void afficher(final String message, final boolean ligne)
    {
        if (ligne) sortie.println(PREFIX + message);
        else sortie.print(PREFIX + message);
    }
    /**
     * Affichage d'un message sur la console du client avec un saut de ligne.
     * @param message Message à afficher.
     */
    public static void afficher(final String message) { afficher(message, true); }

    /**
     * Affichage du préfixe de demande.
     * @param message Message associé à la demande.
     */
    public static void afficher_message_demande(final String message)
    { sortie.print(utilisateur + '@' + PREFIX + message); }

    /**
     * Affichage du préfixe de demande et lecture de la réponse.
     * @param message Message associé à la demande.
     * @param ligne Drapeau de saut de ligne.
     * @return Réponse lue sur l'entrée.
     */
    public static final String demander(final String message, final boolean ligne)
    {
        afficher_message_demande(message);
        if (ligne) return entree.nextLine();
        return entree.next();
    }

    /**
     * Demande de séléction dans un tableau.
     * @param tableau Tableau dans lequel demander la séléction.
     * @param message Message associé à la demande.
     * @return Index du choix dans le tableau.
     */
    public static final int choisir(Object[] tableau, final String message)
    {
        Console.afficher(message);
        for (int e = 0; e < tableau.length; e++) 
            Console.sortie.println("\t# " + e + " - " + tableau[e]);
        Console.sortie.println("");
        Console.sortie.flush();
        int i = 0;
        boolean valide = false;
        while (!valide)
        {
            Console.afficher_message_demande("");
            try 
            {
                String ligne = ""; // Evite un rebouclage causé par une erreur de buffer d'entrée.
                while (ligne.equals("")) ligne = Console.entree.nextLine();
                i = Integer.parseInt(ligne);
                valide = (i < tableau.length);
            }
            catch (NumberFormatException e) {}
        }
        return i;
    }
}
