package bri.serveur;

public abstract class Console 
{
    private static final String PREFIX = " [BRI] ";

    public static void afficher(final String message, final boolean ligne)
    {
        if (ligne) System.out.println(PREFIX + message);
        else System.out.print(PREFIX + message);
    }
    public static void afficher(final String message) { afficher(message, true); }
    public static void afficher(final IApp app, final String message)
    { afficher("<" + app.nom() + ">\t" + message); }
}
