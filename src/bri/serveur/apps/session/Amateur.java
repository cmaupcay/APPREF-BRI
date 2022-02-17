package bri.serveur.apps.session;

public class Amateur extends Session
{
    @Override
    public void run()
    {
        this.connexion().ecrire(new String[]{ "Exemple", "Oui" });
    }
}
