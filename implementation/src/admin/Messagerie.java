package admin;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.net.URLClassLoader;
import java.util.ArrayList;

import bri.Connexion;
import bri.serveur.IUtilisateur;
import bri.serveur.Utilisateurs;
import bri.serveur.service.IServiceBRI;
import bri.serveur.service.Ressources;

public class Messagerie implements IServiceBRI
{
    public static final String VERSION = "1.0.0";
    private static ArrayList<Message> MESSAGES;
    private static final String MESSAGES_NOM_RESSOURCE = "msg";
    private static Constructor<? extends Message> MESSAGES_CONSTRUCTEUR;
    static
    {
        // Importation des dépendances
        try
        {
            Class<?> message = Messagerie.class.getClassLoader().loadClass("admin.Message");
            MESSAGES_CONSTRUCTEUR = message.asSubclass(Message.class).getConstructor(IUtilisateur.class, IUtilisateur.class, String.class);

            // Fermeture du chargement depuis l'URL, indispensable pour un rechargement du service.
            ((URLClassLoader)Messagerie.class.getClassLoader()).close();
        }
        catch (ClassNotFoundException|NoSuchMethodException|IOException e) 
        { e.printStackTrace(); }

        // Liste des messages en ressource partagée
        try { MESSAGES = (ArrayList<Message>)Ressources.ressource(MESSAGES_NOM_RESSOURCE); }
        catch (ClassCastException e) { e.printStackTrace(); }
        if (MESSAGES == null && Ressources.ajouter(MESSAGES_NOM_RESSOURCE, new ArrayList<>()))
        {
            try { MESSAGES = (ArrayList<Message>)Ressources.ressource(MESSAGES_NOM_RESSOURCE); }
            catch (ClassCastException e) { e.printStackTrace(); }
        }
            
    }

    private Connexion connexion;

    public Messagerie(Socket connexion) throws IOException
    {
        this.connexion = new Connexion(connexion);
    }

    private static final String[] MODES = 
    {
        "Lire vos messages",
        "Envoyer un message"
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
        else // Le pseudo n'existe pas
        {
            this.connexion.ecrire(Connexion.FAUX);
            this.connexion();
        }
        return false;
    }

    @Override
    public final void run()
    {
        this.connexion.ecrire("Messagerie BRI - version " + VERSION);
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
                        if (selection == messages.size()) continue;
                        final Message message = messages.get(selection);
                        this.connexion.ecrire(message.afficher());
                        message.lire();
                        this.connexion.demander(Connexion.RETOUR);
                        this.connexion.ecrire(Connexion.VRAI);
                    }
                    else if (mode == 1)     // Ecriture
                    {
                        final ArrayList<IUtilisateur> utilisateurs = Utilisateurs.liste();
                        final int destinataire = this.connexion.demander_choix(utilisateurs.toArray(), "A qui voulez vous envoyer un message ?"
                        );
                        this.connexion.ecrire(Connexion.VRAI);
                        if (destinataire == utilisateurs.size()) continue;
                        final String contenu = this.connexion.demander("Ecrivez votre message : ");
                        this.connexion.ecrire(Connexion.VRAI);
                        try 
                        { 
                            synchronized (MESSAGES) 
                            { MESSAGES.add(MESSAGES_CONSTRUCTEUR.newInstance(this.utilisateur, utilisateurs.get(destinataire), contenu)); }
                            this.connexion.ecrire("Message envoyé !");
                        }
                        catch (Exception e) 
                        { 
                            this.connexion.ecrire("ERREUR : Impossible d'envoyer le message : " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
                this.connexion.ecrire(Connexion.VRAI);
                }
        }
        catch (IOException e) {}
    }
}
