package bri.client;

import bri.Connexion;
import bri.IBRI;

/**
 * Mode de connexion au serveur BRI.
 */
public interface IMode extends IBRI
{
    /**
     * Procédure d'acceptation de la connexion au serveur.
     * @param connexion Connexion au serveur BRI.
     * @return Acceptation de la connexion.
     */
    public boolean accepter_connexion(Connexion connexion);
}
