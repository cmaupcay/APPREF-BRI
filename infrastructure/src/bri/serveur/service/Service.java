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
    private ServiceClassLoader loader;
    
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
        this.loader = null;
    }

    private boolean actif;
    @Override
    public final boolean actif() { return this.actif; }
    @Override
    public final boolean activer() 
    { 
        if (!this.actif)
        {
            if (this.loader == null)
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

    public final String classe() { return this.auteur.pseudo() + '.' + this.nom; }

    private final void charger_classe_distante() throws ClassNotFoundException, ClassFormatError
    {
        final String nom_classe = this.classe();
        ServiceClassLoader loader = null;
        Class<?> classe = null;
        loader = new ServiceClassLoader(this.auteur, this.nom);
        try { classe = loader.loadClass(nom_classe); }
        catch (ClassNotFoundException e) 
        {
            loader = new ServiceClassLoader(this.auteur);
            try { classe = loader.loadClass(nom_classe); }
            catch (ClassNotFoundException e2)
            { throw new ClassNotFoundException("la classe est introuvable sur le serveur.", e2); }
        }
        if (IServiceBRI.verifier_norme(classe))
        {
            try { classe.asSubclass(IServiceBRI.class); }
            catch (ClassCastException e) { throw new ClassFormatError("impossible d'importer la classe en tant que service BRI."); }
            this.loader = loader;
        }
        else throw new ClassFormatError("la classe ne respecte pas la norme BRI.");
    }

    @Override
    public final boolean mettre_a_jour()
    {
        Console.afficher(this, "Mise à jour depuis le serveur FTP...");
        try 
        { 
            this.charger_classe_distante(); 
            Console.afficher(this, "Classe du service mise à jour.");
            return true;
        }
        catch (ClassNotFoundException|ClassFormatError e)
        {
            Console.afficher(this, "ERREUR : Impossible de charger la classe distante : " + e.getMessage());
            return false;
        }
    }

    @Override
    public final IServiceBRI nouvelle_instance(Connexion connexion)
    {
        try 
        {
            Class<? extends IServiceBRI> classe = this.loader.loadClass(this.classe()).asSubclass(IServiceBRI.class);
            Constructor<?> c = null;
            IServiceBRI service = null;
            try 
            { 
                c = classe.getDeclaredConstructor(Connexion.class); 
                service = (IServiceBRI)c.newInstance(connexion);
            }
            catch (NoSuchMethodException e)
            { 
                try { c = classe.getDeclaredConstructor(Socket.class); }
                catch (NoSuchMethodException e2) { throw new InstantiationException() ; }
                service = (IServiceBRI)c.newInstance(connexion.socket());
            }
            Console.afficher(this, "Nouvelle instance du service créée.");
            return service;
        }
        catch (ClassNotFoundException|InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
        {
            Console.afficher(this, "ERREUR : Impossible d'instancier le service.");
            return null;
        }
    }
}
