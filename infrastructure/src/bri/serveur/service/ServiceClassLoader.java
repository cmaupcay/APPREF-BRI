package bri.serveur.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import bri.serveur.IUtilisateur;

public class ServiceClassLoader extends ClassLoader
{
    private URL url;

    public ServiceClassLoader(final IUtilisateur auteur)
    {
        super(Thread.currentThread().getContextClassLoader());
        
        try { this.url = new URL("ftp://" + auteur.ftp() + '/'); }
        catch (MalformedURLException e) { e.printStackTrace(); }
    }
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
            URLClassLoader ftp = new URLClassLoader(new URL[]{ this.url }, this.getParent());
            Class<?> classe = ftp.loadClass(nom);
            ftp.close();
            return classe;
        }
        catch (ClassNotFoundException|IOException e)
        { return super.loadClass(nom, resoudre); }
    }
}
