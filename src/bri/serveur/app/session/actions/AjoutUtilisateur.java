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

public class AjoutUtilisateur extends Action
{
    @Override
    public final String nom() { return "Ajouter/Promouvoir un utilisateur"; }

    public AjoutUtilisateur(ISession parent) { super(parent); }

    private static final String TYPES_PACKAGE = "bri.serveur.utilisateurs";
    private static final String[] TYPES = {
        Utilisateur.class.getSimpleName(),
        Programmeur.class.getSimpleName()
    };

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    {
        try
        {
            final int type = connexion.demander_choix(TYPES, "Quel type d'utilisateur souhaitez-vous ajouter ?");
            Class<?> classe_utilisateur = null;
            try { classe_utilisateur = Class.forName(TYPES_PACKAGE + '.' + TYPES[type]); }
            catch (ClassNotFoundException e)
            {
                connexion.ecrire(Connexion.FAUX);
                Console.afficher(this.parent().parent(), "La classe d'utilisateur '" + TYPES[type] + "' n'existe pas dans le package '" + TYPES_PACKAGE + "'.");
                return true;
            }
            Constructor<?> creation = null;
            try { creation = classe_utilisateur.getDeclaredConstructor(Connexion.class); }
            catch (NoSuchMethodException e)
            {
                connexion.ecrire(Connexion.FAUX);
                Console.afficher(this.parent().parent(), "La classe d'utilisateur '" + TYPES[type] + "' n'implémente pas de constructeur depuis une connexion.");
                return true;
            }
            try 
            { 
                connexion.ecrire(Connexion.VRAI);
                IUtilisateur utilisateur = (IUtilisateur)creation.newInstance(connexion);
                if (Utilisateurs.ajouter(utilisateur)) connexion.ecrire("Utilisateur ajouté !");
                else connexion.ecrire("ERREUR : Impossible d'ajouter le nouvel utilisateur.");
            }
            catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e) 
            {
                connexion.ecrire(Connexion.FAUX);
                Console.afficher(this.parent().parent(), "Une erreur inattendue est survenue lors de la création d'un nouvel utilisateur : " + e.getMessage());
            }

        }
        catch (IOException e)
        { return false; }
        return true;
    }
}
