package admin.messagerie.menus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import admin.messagerie.IMenu;
import admin.messagerie.Message;
import bri.Connexion;
import bri.serveur.IUtilisateur;

// TOCOMMENT
public class Lecture implements IMenu
{
    @Override
    public final void executer(final List<String> messages, final Connexion connexion, final IUtilisateur utilisateur) throws IOException
    {
        List<Message> messages_recus = new ArrayList<>();
        synchronized (messages)
        {
            Message m = null;
            for (String s : messages)
            {
                try 
                { 
                    m = new Message(s);
                    if (m.destinataire().equals(utilisateur))
                    messages_recus.add(m);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    connexion.ecrire("| ERREUR | Impossible de lire le message : " + e.getMessage());
                }
            }
        }
        final int selection = connexion.demander_choix(messages_recus.toArray(), "Quel message voulez-vous consulter ?");
        connexion.ecrire(Connexion.VRAI);
        if (selection == messages_recus.size()) return;
        final Message message = messages_recus.get(selection);
        connexion.ecrire(message.afficher());
        message.lire();
        connexion.demander(Connexion.RETOUR);
        connexion.ecrire(Connexion.VRAI);
    } 

    @Override
    public final String toString() { return "Lire vos messages"; }
}
