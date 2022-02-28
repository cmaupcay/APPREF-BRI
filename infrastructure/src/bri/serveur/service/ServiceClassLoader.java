package bri.serveur.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import bri.serveur.IUtilisateur;

/**
 * Chargeur de classe distante et autorisant la redéfinition de classe (mise à jour).
 */
public class ServiceClassLoader extends ClassLoader
{
    /** URL cible du chargeur de classe. */
    private URL url;

    /**
     * Construction d'un nouveau chargeur de classe ciblant un serveur FTP.
     * @param auteur Auteur des services à charger.
     */
    public ServiceClassLoader(final IUtilisateur auteur)
    {
        super(Thread.currentThread().getContextClassLoader());
        try { this.url = new URL("ftp://" + auteur.ftp() + '/'); }
        catch (MalformedURLException e) { e.printStackTrace(); }
    }
    /**
     * Construction d'un nouveau chargeur de classe ciblant une archive JAR sur un serveur FTP.
     * @param auteur Auteur de l'archive JAR à charger.
     * @param bibliotheque_jar Nom de l'archive JAR.
     */
    public ServiceClassLoader(final IUtilisateur auteur, final String bibliotheque_jar)
    {
        super(Thread.currentThread().getContextClassLoader());
        try { this.url = new URL("ftp://" + auteur.ftp() + '/' + auteur.pseudo() + '/' + bibliotheque_jar + ".jar"); }
        catch (MalformedURLException e) { e.printStackTrace(); }
    }

    @Override
    protected synchronized Class<?> loadClass(String nom, boolean resoudre) throws ClassNotFoundException
    { 
        try 
        { 
            // L'URLClassLoader doit-être fermé par le service pour pouvoir être mis à jour.
            URLClassLoader ftp = new URLClassLoader(new URL[]{ this.url }, this.getParent());
            return ftp.loadClass(nom);
        }
        catch (ClassNotFoundException e)
        { return super.loadClass(nom, resoudre); }
    }
}
