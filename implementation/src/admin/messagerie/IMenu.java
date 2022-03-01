package admin.messagerie;

import java.io.IOException;
import java.util.List;

import bri.Connexion;
import bri.serveur.IUtilisateur;

/**
 * Interface des sous menus de la messagerie.
 */
public interface IMenu
{
    public void executer(final List<Message> messages, final Connexion connexion, final IUtilisateur utilisateur) throws IOException;    
}
