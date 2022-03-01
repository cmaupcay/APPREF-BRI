package admin;

import java.io.IOException;
import java.net.Socket;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import admin.messagerie.IMenu;
import bri.Connexion;
import bri.serveur.IUtilisateur;
import bri.serveur.Utilisateurs;
import bri.serveur.service.IServiceBRI;
import bri.serveur.service.Ressources;

/**
 * Service BRI de messagerie.
 * Le service est à exporter en archive JAR vers le serveur FTP de l'auteur.
 */
public class Messagerie implements IServiceBRI
{
    /** Version du service BRI. */
    public static final String VERSION = "1.0.0";

    /** Référence à la liste des messages (ressource partagée). */
    private static List<String> MESSAGES;
    /** Nom de la ressource partagée associée à la liste des messages. */
    private static final String MESSAGES_NOM_RESSOURCE = "msg";

    /** Menu de la messagerie. */
    private static List<IMenu> MENU;

    static
    {
        try
        {
            // Chargement des dépendances depuis l'archive JAR.
            Messagerie.class.getClassLoader().loadClass("admin.messagerie.Message");
            /// Chargement des classes du menu
            Messagerie.class.getClassLoader().loadClass("admin.messagerie.IMenu");
            final Class<? extends IMenu> lecture = Messagerie.class.getClassLoader().loadClass("admin.messagerie.menus.Lecture").asSubclass(IMenu.class);
            final Class<? extends IMenu> ecriture = Messagerie.class.getClassLoader().loadClass("admin.messagerie.menus.Ecriture").asSubclass(IMenu.class);
            /// Création du menu
            MENU = new ArrayList<>();
            MENU.add(lecture.getConstructor().newInstance());
            MENU.add(ecriture.getConstructor().newInstance());

            // Fermeture du chargement depuis l'URL, indispensable pour un rechargement du service.
            ((URLClassLoader)Messagerie.class.getClassLoader()).close();

            // Récupération de la ressource partagée associée à la liste des messages.
            try { MESSAGES = (List<String>)Ressources.ressource(MESSAGES_NOM_RESSOURCE); }
            catch (ClassCastException e) { e.printStackTrace(); }
            // Si la ressource n'existe pas, on la créer et on récupère la référence.
            if (MESSAGES == null && Ressources.ajouter(MESSAGES_NOM_RESSOURCE, new ArrayList<>()))
                MESSAGES = (List<String>)Ressources.ressource(MESSAGES_NOM_RESSOURCE);
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    /** Connexion cliente. */
    private Connexion connexion;

    /**
     * Construction d'une instance du service BRI.
     * @param connexion Socket client ouvert et connecté.
     * @throws IOException Impossible d'ouvrir la connexion depuis le socket client.
     */
    public Messagerie(Socket connexion) throws IOException
    {
        this.connexion = new Connexion(connexion);
    }
    
    /** Nombre de tentatives de connexion maximales par connexion. */
    private static final int TENTATIVES_MAX = 3;
    /** Utilisateur authentifié. */
    private IUtilisateur utilisateur;

    /**
     * Vérification du mot de passe de l'utilisateur authentifié.
     * @param mdp Mot de passe à vérifier.
     * @return Indique si les mots de passe sont similaires.
     */
    private final boolean verifier_mdp(final String mdp)
    { return this.utilisateur.mdp().equals(mdp); }

    /**
     * Procédure de connexion et d'authentification de l'utilisateur.
     * @return Indique si l'utilisateur a pu être authentifié.
     * @throws IOException Impossible de lire les informations de connexion.
     */
    private final boolean connexion() throws IOException
    {
        final String pseudo = this.connexion.demander("Utilisateur : ");
        if (pseudo == null) return false;
        this.utilisateur = Utilisateurs.utilisateur(pseudo);
        if (this.utilisateur == null) // Le pseudo n'existe pas
        {
            this.connexion.ecrire(Connexion.FAUX);
            return this.connexion(); // On relance une procédure de connexion.
        }
        else
        {
            this.connexion.ecrire(Connexion.VRAI);
            int tentatives = 0;
            // Boucle de tentative de connexion.
            while (tentatives++ < TENTATIVES_MAX)
            {
                if (this.verifier_mdp(this.connexion.demander("Mot de passe : "))) // Vérification du mot de passe.
                {
                    // Authentification réussi.
                    this.connexion.ecrire(Connexion.VRAI);
                    return true;
                }
                else this.connexion.ecrire(Connexion.FAUX);
            }
        }
        return false;
    }

    @Override
    public final void run()
    {
        this.connexion.ecrire("Messagerie BRI - version " + VERSION);
        try
        {
            if (this.connexion()) // Authentification de l'utilisateur.
            {
                this.connexion.ecrire("Bienvenue sur votre espace messagerie !");
                final int n = MENU.size();
                int sous_menu = -1;
                // Boucle de choix du sous menu.
                while ((sous_menu = this.connexion.demander_choix(MENU.toArray(), "Que voulez-vous faire ?")) < n)
                {
                    this.connexion.ecrire(Connexion.VRAI);
                    MENU.get(sous_menu).executer(MESSAGES, this.connexion, this.utilisateur);
                }
                this.connexion.ecrire(Connexion.VRAI);
            }
        }
        catch (IOException e) {}
    }
}
