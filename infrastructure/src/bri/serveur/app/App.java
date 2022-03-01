package bri.serveur.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import bri.Connexion;
import bri.serveur.Console;
import bri.serveur.IApp;

/**
 * Classe de base des applications serveur.
 */
public abstract class App implements IApp
{
    /** Thread d'exécution de l'application. */
    protected Thread thread;
    @Override
    public Thread thread() { return this.thread; }

    @Override
    public void demarrer()
    {
        this.thread = new Thread(this);
        this.thread.start();
        Console.afficher(this, "Application démarrée.");
    }

    /** Liste des sessions démarrée par l'application. */
    protected List<ISession> sessions;
    /** Drapeau d'acceptation des nouvelles connexions. */
    public boolean accepter_nouvelle_connexion;
    @Override
    public boolean accepter_nouvelle_connexion() { return accepter_nouvelle_connexion; }

    /**
     * Création d'une nouvelle session.
     * @return Nouvelle session.
     */
    protected abstract ISession nouvelle_session();

    /** Création de l'application. */
    protected App()
    {
        this.thread = null;
        this.sessions = new ArrayList<>();
        this.accepter_nouvelle_connexion = false;
    }

    @Override
    public final void run()
    {
        ServerSocket connexion = null;
        try // Ouverture de l'application et de la connexion acceptante.
        { connexion = new ServerSocket(this.port()); }
        catch (IOException e)
        { Console.afficher(this, "| ERREUR | Impossible d'ouvrir la connexion sur le port " + this.port() + " : " + e.getMessage()); }
        this.accepter_nouvelle_connexion = true;
        Console.afficher(this, "Application ouverte sur le port " + this.port() + ".");
        
        // Boucle d'acceptation des nouvelles connexion.
        while (this.accepter_nouvelle_connexion)
        {
            try
            {
                Socket socket = connexion.accept();
                Connexion nouvelle_connexion = new Connexion(socket);
                Console.afficher(this, "Nouvelle connexion. Création de la session...");
                // Création d'une session pour la nouvelle connexion.
                ISession session = this.nouvelle_session();
                session.initialiser(this, nouvelle_connexion);
                this.sessions.add(session);
            }
            catch (IOException e)
            { Console.afficher(this, "| ERREUR | L'acceptation de la nouvelle connexion a échoué : " + e.getMessage()); }
        }

        try { connexion.close(); } // Fermeture de la connexion acceptante.
        catch (IOException e) { Console.afficher(this, "| ERREUR | Impossible de fermer la connexion : " + e.getMessage()); }

        // Fermeture des sessions
        for (int s = this.sessions.size() - 1; s >= 0; s--)
        {
            if (this.sessions.get(s).thread().isAlive())
            {
                try { this.sessions.get(s).thread().join(); }
                catch (InterruptedException e)
                { Console.afficher(this, "| ERREUR | Session interrompue : " + e.getMessage()); }
            }
            this.sessions.remove(s);
        }

        Console.afficher(this, "Application fermée.");
    }
}
