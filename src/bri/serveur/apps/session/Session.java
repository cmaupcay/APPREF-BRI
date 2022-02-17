package bri.serveur.apps.session;

import java.io.IOException;

import bri.client.Connexion;
import bri.serveur.apps.ISession;

public abstract class Session implements ISession
{
    private Thread thread;
    @Override
    public Thread thread() { return this.thread; }
    private Connexion connexion;
    protected Connexion connexion() { return this.connexion; }

    @Override
    public void fermer() throws IOException { this.connexion.fermer(); }

    @Override
    public void initialiser(Connexion connexion)
    {
        this.connexion = connexion;
        this.thread = new Thread(this);
        this.thread.start();
    }
}
