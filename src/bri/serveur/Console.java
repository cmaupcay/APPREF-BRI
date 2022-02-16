package bri.serveur;

public abstract class Console 
{
    private static final String PREFIX = "[BRI] ";

    public static void afficher(final String message, final boolean ligne)
    {
        if (ligne) System.out.println(PREFIX + message);
        else System.out.print(PREFIX + message);
    }
}
