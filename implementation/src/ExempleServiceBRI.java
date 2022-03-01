import java.io.IOException;
import java.net.Socket;
import java.net.URLClassLoader;

import bri.Connexion;
import bri.serveur.service.IServiceBRI;

/**
 * Classe d'exemple d'un service BRI.
 */
public class ExempleServiceBRI implements IServiceBRI
{
    static
    {
        // Fermeture du chargement depuis l'URL, indispensable pour un rechargement du service.
        try { ((URLClassLoader)ExempleServiceBRI.class.getClassLoader()).close(); }
        catch (IOException e) {}
    }

    /** Connexion cliente. */
    private Connexion connexion;

    /**
     * Construction d'une instance du service BRI.
     * @param connexion Socket client ouvert et connecté.
     * @throws IOException Impossible d'ouvrir la connexion depuis le socket client.
     */
    public ExempleServiceBRI(Socket connexion) throws IOException
    {
        this.connexion = new Connexion(connexion);
    }

    @Override
    public final void run()
    {
        // Programme d'une nouvelle instance du service.
    }
}
