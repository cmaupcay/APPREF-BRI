package admin;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import bri.Connexion;
import bri.serveur.IUtilisateur;
import bri.serveur.Utilisateurs;
import bri.serveur.service.IServiceBRI;

public class Messagerie implements IServiceBRI
{
    // static
    // {
    //     Messagerie.class.getClassLoader().loadClass("admin.Message");
    // }

    private Connexion connexion;

    public Messagerie(Socket connexion) throws IOException
    {
        this.connexion = new Connexion(connexion);
    }

    private static ArrayList<Message> MESSAGES = new ArrayList<>();
    private static final String[] MODES = {
        "Lire vos messages",
        "Envoyer un message",
        "Quitter"
    };
    
    private static final int TENTATIVES_MAX = 3;
    private IUtilisateur utilisateur;

    private final boolean verifier_pseudo(final String pseudo)
    {
        ArrayList<IUtilisateur> utilisateurs = Utilisateurs.liste();
        for (IUtilisateur u : utilisateurs)
        {
            if (u.pseudo().equals(pseudo))
            {
                this.utilisateur = u;
                return true;
            }
        }
        return false;
    }
    private final boolean verifier_mdp(final String mdp)
    { return this.utilisateur.mdp().equals(mdp); }

    private final boolean connexion() throws IOException
    {
        final String pseudo = this.connexion.demander("Utilisateur : ");
        if (this.verifier_pseudo(pseudo))
        {
            this.connexion.ecrire(Connexion.VRAI);
            int tentatives = 0;
            while (tentatives++ < TENTATIVES_MAX)
            {
                if (this.verifier_mdp(this.connexion.demander("Mot de passe : ")))
                {
                    this.connexion.ecrire(Connexion.VRAI);
                    return true;
                }
                else this.connexion.ecrire(Connexion.FAUX);
            }
        }
        else this.connexion.ecrire(Connexion.FAUX); // Le pseudo n'existe pas
        return false;
    }

    @Override
    public final void run()
    {
        try
        {
            if (this.connexion())
            {
                this.connexion.ecrire("Bienvenue sur votre espace messagerie !");
                int mode = -1;
                while ((mode = this.connexion.demander_choix(MODES, "Que voulez-vous faire ?")) < MODES.length - 1)
                {
                    this.connexion.ecrire(Connexion.VRAI);
                    if (mode == 0)          // Lecture
                    {
                        ArrayList<Message> messages = new ArrayList<>();
                        synchronized (MESSAGES)
                        {
                            for (Message m : MESSAGES)
                            if (m.destinataire().equals(this.utilisateur))
                                messages.add(m);
                        }
                        final int selection = this.connexion.demander_choix(messages.toArray(), "Quel message voulez-vous consulter ?");
                        this.connexion.ecrire(Connexion.VRAI);
                        final Message message = messages.get(selection);
                        this.connexion.ecrire(message.afficher());
                        message.lire();
                        this.connexion.demander("-- Appuyez sur une touche pour quitter --");
                        this.connexion.ecrire(Connexion.VRAI);
                    }
                    else if (mode == 1)     // Ecriture
                    {
                        final int destinataire = this.connexion.demander_choix(Utilisateurs.liste().toArray(), "A qui voulez vous envoyer un message ?");
                        this.connexion.ecrire(Connexion.VRAI);
                        final String contenu = this.connexion.demander("Ecrivez votre message : ");
                        this.connexion.ecrire(Connexion.VRAI);
                        synchronized (MESSAGES)
                        { MESSAGES.add(new Message(this.utilisateur, Utilisateurs.liste().get(destinataire), contenu)); }
                        this.connexion.ecrire("Message envoyé !");
                    }
                }
                this.connexion.ecrire(Connexion.VRAI);
                this.connexion.ecrire("Au revoir !");
            }
            this.connexion.fermer();
        }
        catch (IOException e) {}
    }
}
