package admin.messagerie;

import java.io.IOException;
import java.util.List;

import bri.Connexion;
import bri.serveur.IUtilisateur;

/**
 * Interface des menus de la messagerie.
 */
public interface IMenu
{
    /**
     * Ouverture du menu.
     * @param messages Liste des messages sérialisés.
     * @param connexion Connexion cliente ouverte et connectée.
     * @param utilisateur Utilisateur authentifié.
     * @throws IOException Impossible de lire les informations de connexion.
     */
    public void ouvrir(final List<String> messages, final Connexion connexion, final IUtilisateur utilisateur) throws IOException;    
}
