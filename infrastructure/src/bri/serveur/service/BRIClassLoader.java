package bri.serveur.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import bri.serveur.IUtilisateur;

public class BRIClassLoader extends ClassLoader
{
    private class ProxyClassLoader extends ClassLoader
    {
        public ProxyClassLoader(ClassLoader parent)
        { super(parent); }

        @Override
        public Class<?> findClass(String nom) throws ClassNotFoundException
        { return super.findClass(nom); }
    }

    private class FTPClassLoader extends URLClassLoader
    {
        private ProxyClassLoader proxy;

        public FTPClassLoader(final IUtilisateur auteur, final String cible, ProxyClassLoader proxy) throws MalformedURLException
        { 
            super(new URL[]{ new URL("ftp://" + auteur.ftp() + '/' + cible) }, null);
            this.proxy = proxy;
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException
        {
            try { return super.findClass(name); }
            catch (ClassNotFoundException e)
            { return this.proxy.loadClass(name); }
        }
    }

    private FTPClassLoader ftp;

    private final void charger_dependances() throws ClassNotFoundException
    {
        this.loadClass(IServiceBRI.class.getName());
    }

    public BRIClassLoader(final IUtilisateur auteur, final String cible)
    {
        super(Thread.currentThread().getContextClassLoader());
        
        try { this.ftp = new FTPClassLoader(auteur, cible, new ProxyClassLoader(this.getParent())); }
        catch (MalformedURLException e) { e.printStackTrace(); }

        try { this.charger_dependances(); } 
        catch (ClassNotFoundException e) { e.printStackTrace(); }
    }

    @Override
    protected synchronized Class<?> loadClass(String nom, boolean resoudre) throws ClassNotFoundException
    { return this.ftp.findClass(nom); }
}
