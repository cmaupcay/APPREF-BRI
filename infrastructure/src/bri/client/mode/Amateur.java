package bri.client.mode;

import bri.Connexion;
import bri.serveur.app.AppAmateur;

/**
 * Mode de connexion à l'application Amateur du serveur BRI.
 */
public class Amateur extends Mode
{
    /**
     * Création du mode Amateur.
     */
    public Amateur()
    { super(AppAmateur.NOM, AppAmateur.PORT); }

    @Override
    public final boolean accepter_connexion(Connexion connexion) 
    { return true; } // Pas besoin d'authentification.
}
