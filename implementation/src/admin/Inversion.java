package admin;

import java.io.IOException;
import java.net.Socket;
import java.net.URLClassLoader;

import bri.Connexion;
import bri.serveur.service.IServiceBRI;

/**
 * Service BRI d'inversion de chaînes de caractères.
 */
public class Inversion implements IServiceBRI
{
    /** Version du service BRI. */
    public static final String VERSION = "alpha";

    static
    {
        // Fermeture du chargement depuis l'URL, indispensable pour un rechargement du service.
        try { ((URLClassLoader)Inversion.class.getClassLoader()).close(); }
        catch (IOException e) {}
    }

    /** Connexion cliente. */
    private Connexion connexion;

    /**
     * Construction d'une instance du service BRI.
     * @param connexion Socket client ouvert et connecté.
     * @throws IOException Impossible d'ouvrir la connexion depuis le socket client.
     */
    public Inversion(Socket connexion) throws IOException
    {
        this.connexion = new Connexion(connexion);
    }

    @Override
    public final void run()
    {
        this.connexion.ecrire("Inversion - version " + VERSION);
        try
        {
            // Boucle de question-réponse.
            while (true)
            {
                String message = this.connexion.demander("Entrez un message à inverser : ");
                if (message == null) break;
                this.connexion.ecrire(Connexion.VRAI);
                if (message.length() == 0) break;
                message = (new StringBuilder(message)).reverse().toString();
                this.connexion.ecrire("Message inversé : " + message);
            }
        }
        catch (Exception e) {}
    }   
}
