package admin.messagerie.menus;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;

import admin.messagerie.IMenu;
import admin.messagerie.Message;
import bri.Connexion;
import bri.serveur.IUtilisateur;
import bri.serveur.Utilisateurs;

public class Ecriture implements IMenu
{
    /** Constructeur des messages. */
    private static Constructor<? extends Message> MESSAGES_CONSTRUCTEUR;

    static
    {
        try
        {
            // Définition du constructeur de message.
            MESSAGES_CONSTRUCTEUR = Message.class.getConstructor(IUtilisateur.class, IUtilisateur.class, String.class);
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public final void executer(final List<Message> messages, final Connexion connexion, final IUtilisateur utilisateur) throws IOException
    {
        // Choix du destinataire dans la liste des utilisateurs.
        final List<IUtilisateur> utilisateurs = Utilisateurs.liste();
        final int destinataire = connexion.demander_choix(utilisateurs.toArray(), "A qui voulez vous envoyer un message ?");
        connexion.ecrire(Connexion.VRAI);
        if (destinataire == utilisateurs.size()) return;
        
        // Demande du contenu du message.
        final String contenu = connexion.demander("Ecrivez votre message : ");
        connexion.ecrire(Connexion.VRAI);

        // Création et envoi du message.
        try 
        { 
            synchronized (messages) 
            { messages.add(MESSAGES_CONSTRUCTEUR.newInstance(utilisateur, utilisateurs.get(destinataire), contenu)); }
            connexion.ecrire("Message envoyé !");
        }
        catch (Exception e) 
        { 
            connexion.ecrire("| ERREUR | Impossible d'envoyer le message : " + e.getMessage());
            e.printStackTrace();
        }
    } 

    @Override
    public final String toString() { return "Ecrire un message"; }
}
