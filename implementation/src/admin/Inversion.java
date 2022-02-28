package admin;

import java.io.IOException;
import java.net.Socket;
import java.net.URLClassLoader;

import bri.Connexion;
import bri.serveur.service.IServiceBRI;

public class Inversion implements IServiceBRI
{
    static
    {
        // Fermeture du chargement depuis l'URL, indispensable pour un rechargement du service.
        try { ((URLClassLoader)Inversion.class.getClassLoader()).close(); }
        catch (IOException e) {}
    }

    private Connexion connexion;

    public Inversion(Socket connexion) throws IOException
    {
        this.connexion = new Connexion(connexion);
    }

    @Override
    public final void run()
    {
        try
        {
            while (true)
            {
                String message = this.connexion.demander("Entrez un message à inverser : ");
                this.connexion.ecrire(Connexion.VRAI);
                if (message.length() == 0) break;
                message = (new StringBuilder(message)).reverse().toString();
                this.connexion.ecrire("Message inversé : " + message);
            }
        }
        catch (IOException|NullPointerException e) {}
    }   
}
