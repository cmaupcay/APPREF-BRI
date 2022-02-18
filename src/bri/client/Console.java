package bri.client;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class Console
{
    private static final String PREFIX = "BRI > ";
    
    private static final String UTILISATEUR_PAR_DEFAUT = "anonyme";
    private static String utilisateur = UTILISATEUR_PAR_DEFAUT;
    public static void modifier_utilisateur(final String pseudo) { utilisateur = pseudo; }

    public static PrintStream sortie = System.out;
    public static Scanner entree = new Scanner(System.in);

    public static void afficher(final String message, final boolean ligne)
    {
        if (ligne) sortie.println(PREFIX + message);
        else sortie.print(PREFIX + message);
    }
    public static void afficher(final String message) { afficher(message, true); }

    public static void afficher_message_demande(final String message)
    {
        sortie.print(utilisateur + '@' + PREFIX + message);
    }

    public static final String demander(final String message, final boolean ligne)
    {
        afficher_message_demande(message);
        if (ligne) return entree.nextLine();
        return entree.next();
    }

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
