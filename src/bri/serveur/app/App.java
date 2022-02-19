package bri.serveur.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import bri.Connexion;
import bri.serveur.Console;
import bri.serveur.IApp;

public abstract class App implements IApp
{
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

    protected ArrayList<ISession> sessions;
    public boolean accepter_nouvelle_connexion;
    @Override
    public boolean accepter_nouvelle_connexion() { return accepter_nouvelle_connexion; }

    protected abstract ISession nouvelle_session(Connexion connexion) throws IOException;

    protected App()
    {
        this.thread = null;
        this.sessions = new ArrayList<>();
        this.accepter_nouvelle_connexion = false;
    }

    @Override
    public final void run()
    {
        try
        {
            ServerSocket connexion = new ServerSocket(this.port());
            this.accepter_nouvelle_connexion = true;
            Console.afficher(this, "Application ouverte sur le port " + this.port() + ".");
            
            Socket socket = null;
            Connexion nouvelle_connexion = null;
            ISession session = null;
            while (this.accepter_nouvelle_connexion)
            {
                socket = connexion.accept();
                nouvelle_connexion = new Connexion(socket);
                Console.afficher(this, "Nouvelle connexion. Création de la session...");
                try 
                { 
                    session = this.nouvelle_session(nouvelle_connexion);
                    session.initialiser(this, nouvelle_connexion);
                    this.sessions.add(session);
                }
                catch (IOException e)
                { 
                    Console.afficher(this, "ERREUR : Impossible de créer la session.");
                    socket.close();
                    socket = null;
                    nouvelle_connexion = null;
                }
            }

            // Fermeture des sessions
            for (ISession s : this.sessions)
            {
                try { s.thread().join();; }
                catch (InterruptedException e)
                { Console.afficher(this, "ERREUR : Session interrompue."); }
            }

            connexion.close();
            Console.afficher(this, "Application fermée.");
        }
        catch (IOException e)
        {
            Console.afficher(this, "ERREUR : Impossible d'ouvrir la connexion sur le port " + this.port() + ".");
        }
    }
}
