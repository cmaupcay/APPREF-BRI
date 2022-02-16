package bri.serveur.apps;

public class AppProgrammeur extends App
{
    public static final String NOM = "Programmeur";
    public static final int PORT = 7000;
    @Override
    public final String nom() { return NOM; }
    @Override
    public final int port() { return PORT; }
}
