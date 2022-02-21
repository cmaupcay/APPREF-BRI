package bri.serveur.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

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
    public final String toString() 
    { return "[" + (this.actif ? '*' : ' ') + "] " + this.auteur.pseudo() + '/' + nom; }

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
    public final boolean activer() 
    { 
        if (!this.actif)
        {
            if (this.classe_service == null)
                if (!this.mettre_a_jour()) return false;
            this.actif = true;
            Console.afficher(this, "Service activé.");
            return true;
        }
        return false;
    }
    @Override
    public final boolean desactiver()
    {
        if (this.actif)
        {
            this.actif = false;
            Console.afficher(this, "Service désactivé.");
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
            Constructor<?> c = null;
            IServiceBRI service = null;
            try 
            { 
                c = this.classe_service.getDeclaredConstructor(Connexion.class); 
                service = (IServiceBRI)c.newInstance(connexion);
            }
            catch (NoSuchMethodException e)
            { 
                try { c = this.classe_service.getDeclaredConstructor(Socket.class); }
                catch (NoSuchMethodException e2) { throw new InstantiationException() ; }
                service = (IServiceBRI)c.newInstance(connexion.socket());
            }
            Console.afficher(this, "Nouvelle instance du service créée.");
            return service;
        }
        catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
        {
            Console.afficher(this, "ERREUR : Impossible d'instancier le service.");
            return null;
        }
    }
}
