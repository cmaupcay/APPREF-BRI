package bri.serveur.app.session.actions;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import bri.Connexion;
import bri.serveur.Console;
import bri.serveur.IUtilisateur;
import bri.serveur.Utilisateurs;
import bri.serveur.app.ISession;
import bri.serveur.utilisateur.Programmeur;
import bri.serveur.utilisateur.Utilisateur;

/**
 * Action d'ajout d'un nouvel utilisateur.
 */
public class AjoutUtilisateur extends Action
{
    @Override
    public final String nom() { return "Ajouter/Promouvoir un utilisateur"; }

    /**
     * Construction de l'action.
     * @param parent Session parente.
     */
    public AjoutUtilisateur(ISession parent) { super(parent); }

    /** Nom du package Java contenant les types d'utilisateur. */
    private static final String TYPES_PACKAGE = "bri.serveur.utilisateur";
    /** Tableau des types d'utilisateurs. */
    private static final String[] TYPES = {
        Utilisateur.class.getSimpleName(),  // Utilisateur standard.
        Programmeur.class.getSimpleName()   // Utilisateur programmeur / administrateur.
    };

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    {
        try
        {
            // Requiert le pseudo de l'utilisateur authentifié en premier argument.
            if (arguments.length < 1) return false;
            IUtilisateur auteur = Utilisateurs.utilisateur(arguments[0]);
            if (auteur == null) return false;

            // Choix du type d'utilisateur à ajouter.
            final int type = connexion.demander_choix(TYPES, "Quel type d'utilisateur souhaitez-vous ajouter ?");
            if (type == TYPES.length)
            {
                connexion.ecrire(Connexion.VRAI);
                return true;
            }
            // Récupération de la classe Java associée.
            Class<?> classe_utilisateur = null;
            try { classe_utilisateur = Class.forName(TYPES_PACKAGE + '.' + TYPES[type]); }
            catch (ClassNotFoundException e)
            {
                connexion.ecrire(Connexion.FAUX);
                Console.afficher(this.parent().parent(), "La classe d'utilisateur '" + TYPES[type] + "' n'existe pas dans le package '" + TYPES_PACKAGE + "'.");
                return true;
            }
            // Récupération du constructeur interactif.
            Constructor<?> creation = null;
            try { creation = classe_utilisateur.getDeclaredConstructor(Connexion.class); }
            catch (NoSuchMethodException e)
            {
                connexion.ecrire(Connexion.FAUX);
                Console.afficher(this.parent().parent(), "La classe d'utilisateur '" + TYPES[type] + "' n'implémente pas de constructeur depuis une connexion.");
                return true;
            }
            // Création interactive de l'utilisateur.
            try 
            { 
                connexion.ecrire(Connexion.VRAI);
                IUtilisateur utilisateur = (IUtilisateur)creation.newInstance(connexion);
                if (Utilisateurs.ajouter(utilisateur)) connexion.ecrire("Utilisateur ajouté !");
                else connexion.ecrire("| ERREUR | Impossible d'ajouter le nouvel utilisateur.");
            }
            catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e) 
            {
                connexion.ecrire(Connexion.FAUX);
                Console.afficher(this.parent().parent(), "Une erreur inattendue est survenue lors de la création d'un nouvel utilisateur : " + e.getMessage());
            }

        }
        catch (IOException e) { return false; }
        return true;
    }
}
