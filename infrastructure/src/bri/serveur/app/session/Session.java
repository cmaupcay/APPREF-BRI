package bri.serveur.app.session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bri.Connexion;
import bri.serveur.BRILaunch;
import bri.serveur.Console;
import bri.serveur.IApp;
import bri.serveur.app.ISession;

/**
 * Classe de base des sessions d'application.
 */
public abstract class Session implements ISession
{
    /** Thread d'exécution de la session. */
    private Thread thread;
    @Override
    public final Thread thread() { return this.thread; }

    /** Application parente ayant démarré la session. */
    private IApp parent;
    @Override
    public final IApp parent() { return this.parent; }

    /** Connexion cliente. */
    private Connexion connexion;
    protected final Connexion connexion() { return this.connexion; }

    /** Liste des actions de session disponibles. */
    private List<IAction> actions;
    /**
     * Retourne la liste des actions de session disponibles.
     * @return Liste des actions disponibles.
     */
    protected final List<IAction> actions() { return this.actions; };

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
