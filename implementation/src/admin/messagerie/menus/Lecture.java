package admin.messagerie.menus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import admin.messagerie.IMenu;
import admin.messagerie.Message;
import bri.Connexion;
import bri.serveur.IUtilisateur;

public class Lecture implements IMenu
{
    @Override
    public final void executer(final List<Message> messages, final Connexion connexion, final IUtilisateur utilisateur) throws IOException
    {
        List<Message> messages_recus = new ArrayList<>();
        synchronized (messages)
        {
            for (Message m : messages)
            if (m.destinataire().equals(utilisateur))
                messages_recus.add(m);
        }
        final int selection = connexion.demander_choix(messages.toArray(), "Quel message voulez-vous consulter ?");
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
