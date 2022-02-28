import java.io.IOException;
import java.net.Socket;
import java.net.URLClassLoader;

import bri.Connexion;
import bri.serveur.service.IServiceBRI;

public class BaseServiceBRI implements IServiceBRI
{
    static
    {
        // Fermeture du chargement depuis l'URL, indispensable pour un rechargement du service.
        try { ((URLClassLoader)BaseServiceBRI.class.getClassLoader()).close(); }
        catch (IOException e) {}
    }

    private Connexion connexion;

    public BaseServiceBRI(Socket connexion) throws IOException
    {
        this.connexion = new Connexion(connexion);
    }

    @Override
    public final void run()
    {
        // Programme d'une nouvelle instance du service.
    }
}
