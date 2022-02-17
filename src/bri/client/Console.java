package bri.client;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class Console 
{
    private static final String PREFIX = "BRI > ";
    private static final String UTILISATEUR_PAR_DEFAUT = "anonyme";

    public static PrintStream sortie = System.out;
    public static Scanner entree = new Scanner(System.in);

    public static void afficher(final String message, final boolean ligne)
    {
        if (ligne) sortie.println(PREFIX + message);
        else sortie.print(PREFIX + message);
    }
    public static void afficher(final String message) { afficher(message, true); }

    public static void afficher_message_demande(final String utilisateur_courant, final String message)
    {
        sortie.print((utilisateur_courant == "" ? UTILISATEUR_PAR_DEFAUT : utilisateur_courant) + '@' + PREFIX + message);
    }

    public static final String demander(final String utilisateur_courant, final String message, final boolean ligne)
    {
        afficher_message_demande(utilisateur_courant, message);
        if (ligne) return entree.nextLine();
        return entree.next();
    }

    public static final int choisir(Object[] tableau, final String message)
    {
        Console.afficher(message);
        for (int e = 0; e < tableau.length; e++) 
            Console.sortie.println("\t# " + e + " - " + tableau[e]);
        Console.sortie.println("");
        Console.afficher_message_demande("", "");
        // TODO Bug quand on rentre n'importe quoi
        try 
        { 
            final int i = Console.entree.nextInt();
            if (i >= tableau.length) return choisir(tableau, message);
            return i;
        }
        catch (InputMismatchException e) { return choisir(tableau, message); }
    }
}
