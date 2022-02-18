package bri.serveur.apps.session;

import java.io.IOException;
import java.util.ArrayList;

import bri.client.Connexion;
import bri.serveur.Console;
import bri.serveur.IApp;
import bri.serveur.apps.ISession;

public abstract class Session implements ISession
{
    private Thread thread;
    @Override
    public final Thread thread() { return this.thread; }

    private IApp parent;
    @Override
    public final IApp parent() { return this.parent; }

    private Connexion connexion;
    protected final Connexion connexion() { return this.connexion; }

    private ArrayList<IAction> actions;
    protected final ArrayList<IAction> actions() { return this.actions; };

    @Override
    public final void fermer() throws IOException { this.connexion.fermer(); }

    @Override
    public final void initialiser(IApp parent, Connexion connexion)
    {
        this.parent = parent;
        this.connexion = connexion;
        this.actions = new ArrayList<>();
        this.thread = new Thread(this);
        this.thread.start();
        Console.afficher(parent, "Session démarrée.");
    }
}
