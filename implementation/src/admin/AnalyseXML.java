package admin;

import java.io.IOException;
import java.net.Socket;
import java.net.URLClassLoader;

import bri.Connexion;
import bri.serveur.Console;
import bri.serveur.service.IServiceBRI;

/**
 * Service BRI d'analyse de fichier XML.
 */
public class AnalyseXML implements IServiceBRI
{
    static
    {
        // Fermeture du chargement depuis l'URL, indispensable pour un rechargement du service.
        try { ((URLClassLoader)AnalyseXML.class.getClassLoader()).close(); }
        catch (IOException e) {}
    }

    /** Connexion cliente. */
    private Connexion connexion;

    /**
     * Construction d'une instance du service BRI.
     * @param connexion Socket client ouvert et connecté.
     * @throws IOException Impossible d'ouvrir la connexion depuis le socket client.
     */
    public AnalyseXML(Socket connexion) throws IOException
    {
        this.connexion = new Connexion(connexion);
    }

    @Override
    public final void run()
    {
        try
        {
            final String[] xml = this.connexion.demander_fichier("Fichier XML : ");
            this.connexion.ecrire(Connexion.VRAI);
        }
        catch (IOException e) { e.printStackTrace(); }
    }
}
