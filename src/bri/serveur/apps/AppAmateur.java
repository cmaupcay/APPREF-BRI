package bri.serveur.apps;

import java.net.Socket;

public class AppAmateur extends App
{
    public static final String NOM = "Amateur";
    public static final int PORT = 7001;
    @Override
    public final String nom() { return NOM; }
    @Override
    public final int port() { return PORT; }

    @Override
    public final void nouvelle_connexion(Socket connexion)
    {
        
    }
}
