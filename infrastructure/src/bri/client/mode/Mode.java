package bri.client.mode;

import java.io.IOException;

import bri.Connexion;
import bri.client.Console;
import bri.client.IMode;

/**
 * Classe de base des modes de connexion au serveur BRI.
 */
public abstract class Mode implements IMode
{
    /** Nom du mode. */
    protected final String nom;
    /** Port associé au mode sur le serveur. */
    protected final int port;

    /**
     * Création du mode.
     * @param nom Nom du mode.
     * @param port Port associé.
     */
    protected Mode(final String nom, final int port)
    {
        this.nom = nom;
        this.port = port;
    }

    /**
     * Procédure de réponse à une demande du serveur.
     * @param connexion Connexion au serveur.
     * @return Réponse envoyée au serveur.
     * @throws IOException Impossible de procéder à la réponse.
     */
    public static final String repondre_a_demande(Connexion connexion) throws IOException
    {
        String tmp = connexion.lire();
        if (tmp != null && tmp.equals(Connexion.DEMANDE))
        {
            tmp = Console.demander(connexion.lire(), false);
            connexion.ecrire(tmp);
            return tmp;
        }
        throw new IOException("Le message n'est pas une demande : " + tmp);
    }

    @Override
    public final String nom() { return this.nom; }
    @Override
    public final String toString() { return this.nom; }

    @Override
    public final int port() { return this.port; }

}
