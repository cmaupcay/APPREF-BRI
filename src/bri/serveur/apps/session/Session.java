package bri.serveur.apps.session;

import java.io.IOException;

import bri.client.Connexion;
import bri.serveur.Console;
import bri.serveur.IApp;
import bri.serveur.apps.ISession;

public abstract class Session implements ISession
{
    private Thread thread;
    @Override
    public Thread thread() { return this.thread; }

    private IApp parent;
    public IApp parent() { return this.parent; }

    private Connexion connexion;
    protected Connexion connexion() { return this.connexion; }

    @Override
    public void fermer() throws IOException { this.connexion.fermer(); }

    @Override
    public void initialiser(IApp parent, Connexion connexion)
    {
        this.parent = parent;
        this.connexion = connexion;
        this.thread = new Thread(this);
        this.thread.start();
        Console.afficher(parent, "Session démarrée.");
    }
}
