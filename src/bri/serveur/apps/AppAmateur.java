package bri.serveur.apps;

import java.io.IOException;

import bri.client.Connexion;
import bri.serveur.apps.session.Amateur;

public class AppAmateur extends App
{
    public static final String NOM = "Amateur";
    public static final int PORT = 7001;
    @Override
    public final String nom() { return NOM; }
    @Override
    public final int port() { return PORT; }

    @Override
    public final ISession nouvelle_session(Connexion connexion) throws IOException
    {
        return new Amateur();
    }
}
