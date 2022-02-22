package bri.serveur.utilisateur;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import bri.Connexion;
import bri.serveur.service.IServiceBRI;

public class Programmeur extends Utilisateur
{
    private String ftp;
    @Override
    public final String ftp() { return this.ftp; }
    @Override
    public void modifier_ftp(final String ftp) { this.ftp = ftp; }

    @Override
    public final Class<?> charger_service_distant(final String nom) throws ClassNotFoundException
    {
        final String nom_classe = this.pseudo() + '.' + nom;
        URLClassLoader cl = null;
        Class<?> classe = null;
        cl = this.generer_cl(nom + ".jar");
        if (cl != null)
        {
            try { classe = cl.loadClass(nom_classe); }
            catch (ClassNotFoundException e)
            {
                cl = this.generer_cl("");
                classe = cl.loadClass(nom_classe);
            }
            if (IServiceBRI.verifier_norme(classe)) return classe;
        }
        throw new ClassNotFoundException(nom_classe);
    }

    private final URLClassLoader generer_cl(final String cible)
    { 
        try { return new URLClassLoader(new URL[]{ new URL("ftp://" + this.ftp + '/' + cible) }); }
        catch (MalformedURLException e) { return null; }
    }

    public Programmeur(final String pseudo, final String mdp, final String ftp)
    { 
        super(pseudo, mdp);
        this.ftp = ftp;
    }

    public Programmeur(Connexion connexion) throws IOException
    {
        super(connexion);
        // Définition de l'URL du serveur FTP
        // NOTE : Le ClassLoader n'est pas membre de Programmeur afin de pouvoir décharger les classes non utilisées.
        // En effet, pour décharger une classe, la classe et son ClassLoader doivent être collectés par le GC.
        URLClassLoader cl = null;
        while (cl == null)
        {
            this.ftp = connexion.demander("URL du serveur FTP : ");
            cl = this.generer_cl("");
            if (cl == null) connexion.ecrire(Connexion.FAUX);
            else connexion.ecrire(Connexion.VRAI);
        }
    }
}
