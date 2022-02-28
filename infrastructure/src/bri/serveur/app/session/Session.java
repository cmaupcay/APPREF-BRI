package bri.serveur.app.session;

import java.io.IOException;
import java.util.ArrayList;

import bri.Connexion;
import bri.serveur.BRILaunch;
import bri.serveur.Console;
import bri.serveur.IApp;
import bri.serveur.app.ISession;

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
        connexion.ecrire("Plateforme BRI - version " + BRILaunch.VERSION);
        this.thread.start();
        Console.afficher(parent, "Session démarrée.");
    }
}
