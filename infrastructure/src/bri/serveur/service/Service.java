package bri.serveur.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import bri.Connexion;
import bri.serveur.Console;
import bri.serveur.IService;
import bri.serveur.IUtilisateur;

public class Service implements IService
{
    private Class<? extends IServiceBRI> classe_service;
    
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

    private final void charger_classe_distante() throws ClassNotFoundException, ClassFormatError
    {
        final String nom_classe = this.auteur.pseudo() + '.' + this.nom;
        ClassLoader cl = null;
        Class<?> classe = null;
        try {
            cl = URLClassLoader.newInstance(new URL[]{ new URL("ftp://" + this.auteur.ftp() + "/" + this.auteur.pseudo() + "/" + this.nom + ".jar") });
        } catch (MalformedURLException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        try { classe = cl.loadClass(nom_classe); }
        catch (ClassNotFoundException e) 
        {
            try {
                cl = URLClassLoader.newInstance(new URL[]{ new URL("ftp://" + this.auteur.ftp() + "/") });
            } catch (MalformedURLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try { classe = cl.loadClass(nom_classe); }
            catch (ClassNotFoundException e2)
            { throw new ClassNotFoundException("la classe est introuvable sur le serveur.", e2); }
        }
        if (IServiceBRI.verifier_norme(classe))
        {
            try { this.classe_service = classe.asSubclass(IServiceBRI.class); }
            catch (ClassCastException e) { throw new ClassFormatError("impossible d'importer la classe en tant que service BRI."); }
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
