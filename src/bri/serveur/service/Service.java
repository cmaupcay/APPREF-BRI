package bri.serveur.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;

import bri.Connexion;
import bri.serveur.Console;
import bri.serveur.IService;
import bri.serveur.IUtilisateur;

public class Service implements IService
{
    private Class<?> classe_service;
    private String nom;
    @Override
    public final String nom() { return this.nom; }
    @Override
    public final String toString() { return this.auteur.pseudo() + '/' + nom; }

    private IUtilisateur auteur;
    @Override
    public final IUtilisateur auteur() { return this.auteur; }

    public Service(final IUtilisateur auteur, final String nom)
    {
        this.nom = nom;
        this.auteur = auteur;
        this.actif = false;
        this.classe_service = null;
    }

    private boolean actif;
    @Override
    public final boolean actif() { return this.actif; }
    @Override
    public final boolean demarrer() 
    { 
        if (!this.actif)
        {
            if (this.classe_service == null)
                this.mettre_a_jour();
            this.actif = true;
            Console.afficher(this, "Service démarré.");
            return true;
        }
        return false;
    }
    @Override
    public final boolean arreter()
    {
        if (this.actif)
        {
            this.actif = false;
            Console.afficher(this, "Service arrêté.");
            return true;
        }
        return false;
    }

    @Override
    public final boolean mettre_a_jour()
    {
        Console.afficher(this, "Mise à jour depuis le serveur FTP...");
        try { this.classe_service = this.auteur.charger_service_distant(this.nom()); }
        catch (ClassNotFoundException e)
        {
            Console.afficher(this, "ERREUR : Impossible de charger la classe distante.");
            return false;
        }
        Console.afficher(this, "Classe du service mise à jour.");
        return true;
    }

    @Override
    public final IServiceBRI nouvelle_instance(Connexion connexion)
    {
        try 
        {
            Constructor<?> c = this.classe_service.getDeclaredConstructor(Connexion.class);
            IServiceBRI service = (IServiceBRI)c.newInstance(connexion);
            Console.afficher(this, "Nouvelle instance du service créée.");
            return service;
        }
        catch (NoSuchMethodException|InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
        {
            connexion.ecrire(Connexion.FAUX);
            Console.afficher(this, "ERREUR : Impossible d'instancier le service.");
            return null;
        }
    }
}
