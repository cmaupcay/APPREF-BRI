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

    private URLClassLoader cl;
    @Override
    public final Class<?> charger_service_distant(final String nom) throws ClassNotFoundException
    {
        final String nom_classe = this.pseudo() + '.' + nom;
        if (cl != null)
        {
            final Class<?> classe = this.cl.loadClass(nom_classe);
            final Class<?>[] interfaces = classe.getInterfaces();
            for (Class<?> i : interfaces)
                if (i == IServiceBRI.class)
                {
                    try 
                    {
                        classe.getDeclaredConstructor(Connexion.class);
                        return classe;
                    }
                    catch (NoSuchMethodException e) {}
                }
        }
        throw new ClassNotFoundException(nom_classe);
    }

    private final URLClassLoader generer_cl()
    { 
        try { return new URLClassLoader(new URL[]{ new URL("ftp://" + this.ftp) }); }
        catch (MalformedURLException e) { return null; }
    }

    public Programmeur(final String pseudo, final String mdp, final String ftp)
    { 
        super(pseudo, mdp);
        this.ftp = ftp;
        this.cl = this.generer_cl();
    }

    public Programmeur(Connexion connexion) throws IOException
    {
        super(connexion);
        this.cl = null;
        while (this.cl == null)
        {
            this.ftp = connexion.demander("URL du serveur FTP : ");
            this.cl = this.generer_cl();
            if (this.cl == null) connexion.ecrire(Connexion.FAUX);
            else connexion.ecrire(Connexion.VRAI);
        }
    }
}
