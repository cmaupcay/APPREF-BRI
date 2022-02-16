package bri.serveur.apps;

public class AppAmateur extends App
{
    public static final String NOM = "Amateur";
    public static final int PORT = 7001;
    @Override
    public final String nom() { return NOM; }
    @Override
    public final int port() { return PORT; }
}
