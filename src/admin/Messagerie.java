package admin;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import bri.Connexion;
import bri.serveur.IUtilisateur;
import bri.serveur.Utilisateurs;
import bri.serveur.service.IServiceBRI;

public class Messagerie implements IServiceBRI
{
    private Connexion connexion;

    public Messagerie(Socket connexion) throws IOException
    {
        this.connexion = new Connexion(connexion);
    }

    private class Message
    {
        private IUtilisateur emissaire;
        private IUtilisateur destinataire;
        private String contenu;
        private LocalDateTime creation;
        private boolean lu;
        
        public final IUtilisateur emissaire() { return this.emissaire; }
        public final IUtilisateur destinataire() { return this.destinataire; }
        public final String contenu() { return this.contenu; }
        public final boolean lu() { return this.lu; }
        public final void lire() { this.lu = true; }

        public Message(final IUtilisateur emissaire, final IUtilisateur destinataire, final String contenu)
        {
            this.emissaire = emissaire;
            this.destinataire = destinataire;
            this.contenu = contenu;
            this.lu = false;
            this.creation = LocalDateTime.now();
        }

        @Override
        public final String toString()
        { 
            return "[" + (this.lu ? ' ' : '*') + "] De " + this.emissaire.pseudo() + 
            " le " + (DateTimeFormatter.ofPattern("dd-MM-yyyy à HH:mm:ss")).format(this.creation);
        }

        public final String afficher()
        { return "De : " + this.emissaire.pseudo() + "\nA : " + this.destinataire.pseudo() + "\n" + this.contenu; }
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
                        for (Message m : MESSAGES)
                            if (m.destinataire.equals(this.utilisateur))
                                messages.add(m);
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
                        final String contenu = this.connexion.demander("Ecrivez votre message :\n");
                        this.connexion.ecrire(Connexion.VRAI);
                        MESSAGES.add(new Message(this.utilisateur, Utilisateurs.liste().get(destinataire), contenu));
                        this.connexion.ecrire("Message envoyé !");
                    }
                }
                this.connexion.ecrire("Au revoir !");
            }
            this.connexion.fermer();
        }
        catch (IOException e) {}
    }
}
