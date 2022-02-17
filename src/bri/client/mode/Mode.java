package bri.client.mode;

import java.io.IOException;

import bri.client.Connexion;
import bri.client.Console;
import bri.client.IMode;

public abstract class Mode implements IMode
{
    protected final String nom;
    protected final int port;

    protected Mode(final String nom, final int port)
    {
        this.nom = nom;
        this.port = port;
    }

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
