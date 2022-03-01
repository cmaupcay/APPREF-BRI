package admin;

import java.io.IOException;
import java.net.Socket;
import java.net.URLClassLoader;

import bri.Connexion;
import bri.serveur.service.IServiceBRI;

public class AnalyseXML implements IServiceBRI
{
    static
    {
        // Fermeture du chargement depuis l'URL, indispensable pour un rechargement du service.
        try { ((URLClassLoader)AnalyseXML.class.getClassLoader()).close(); }
        catch (IOException e) {}
    }

    private Connexion connexion;

    public AnalyseXML(Socket connexion) throws IOException
    {
        this.connexion = new Connexion(connexion);
    }

    @Override
    public final void run()
    {
        // TODO Implémentation
        this.connexion.ecrire("| ERREUR | Service non implémenté.");   
    }
}
