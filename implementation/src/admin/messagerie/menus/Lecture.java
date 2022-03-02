package admin.messagerie.menus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import admin.messagerie.IMenu;
import admin.messagerie.Message;
import bri.Connexion;
import bri.serveur.IUtilisateur;

/**
 * Menu de lecture des messages reçus.
 */
public class Lecture implements IMenu
{
    @Override
    public final void ouvrir(final List<String> messages, final Connexion connexion, final IUtilisateur utilisateur) throws IOException
    {
        // Récupération des messages reçus depuis la liste passée en paramètre.
        List<Message> messages_recus = new ArrayList<>();
        synchronized (messages)
        {
            Message m = null;
            for (String s : messages)
            {
                try 
                { 
                    // Récupération du message depuis sa forme sérialisée.
                    m = new Message(s);
                    // Ajout à la liste si le destinataire est l'utilisateur authentifié.
                    if (m.destinataire().equals(utilisateur))
                        messages_recus.add(m);
                }
                catch (Exception e)
                {
                    // Echec de récupération du message (sérialisation éronnée).
                    e.printStackTrace();
                    connexion.ecrire("| ERREUR | Impossible de lire le message : " + e.getMessage());
                }
            }
        }
        // Demande de séléction du message à lire.
        final int selection = connexion.demander_choix(messages_recus.toArray(), "Quel message voulez-vous consulter ?");
        connexion.ecrire(Connexion.VRAI);
        if (selection == messages_recus.size()) return;
        final Message message = messages_recus.get(selection);
        // Affichage du message.
        connexion.ecrire(message.afficher());
        // Changement de la valeur du drapeau de lecture.
        if (!message.lu())
        {
            message.lire();
            // Modification du message sérialisé dans la liste originale des messages.
            messages.set(selection, message.exporter());
        }
        // Attente d'une réponse pour retourner au menu.
        connexion.demander(Connexion.RETOUR);
        connexion.ecrire(Connexion.VRAI);
    } 

    @Override
    public final String toString() { return "Lire vos messages"; }
}
