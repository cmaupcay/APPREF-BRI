package bri.client;

import java.io.PrintStream;
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

    public static void afficher_message_demande(final String utilisateur_courant)
    { sortie.print((utilisateur_courant == "" ? UTILISATEUR_PAR_DEFAUT : utilisateur_courant) + '@' + PREFIX); }

    public static final String demander(final String utilisateur_courant, final boolean ligne)
    {
        afficher_message_demande(utilisateur_courant);
        if (ligne) return entree.nextLine();
        return entree.next();
    }
}
