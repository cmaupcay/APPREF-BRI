package bri.serveur.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import bri.serveur.IUtilisateur;

public class ServiceClassLoader extends ClassLoader
{
    private URLClassLoader loader;

    public ServiceClassLoader(final IUtilisateur auteur)
    {
        super(Thread.currentThread().getContextClassLoader());
        try 
        { 
            this.loader = new URLClassLoader(
                new URL[]{new URL("ftp://" + auteur.ftp() + '/')},
                this.getParent()
            );
        }
        catch (MalformedURLException e) { e.printStackTrace(); }
    }
    public ServiceClassLoader(final IUtilisateur auteur, final String bibliotheque_jar)
    {
        super(Thread.currentThread().getContextClassLoader());
        try 
        {
            this.loader = new URLClassLoader(
                new URL[]{new URL("ftp://" + auteur.ftp() + '/' + auteur.pseudo() + '/' + bibliotheque_jar + ".jar")}, 
                this.getParent()
            );
        }
        catch (MalformedURLException e) { e.printStackTrace(); }
    }

    @Override
    protected synchronized Class<?> loadClass(String nom, boolean resoudre) throws ClassNotFoundException
    { 
        try 
        { 
            Class<?> classe = this.loader.loadClass(nom);
            return classe;
        }
        catch (ClassNotFoundException e)
        { return super.loadClass(nom, resoudre); }
    }
}
