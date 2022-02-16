package bri.serveur.apps;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

    public boolean accepter_client;
    @Override
    public boolean accepter_client() { return accepter_client; }

    protected abstract void nouvelle_connexion(Socket connexion);

    protected App()
    {
        this.thread = null;
        this.accepter_client = false;
    }

    @Override
    public final void run()
    {
        try
        {
            ServerSocket connexion = new ServerSocket(this.port());
            this.accepter_client = true;
            Console.afficher(this, "Application ouverte sur le port " + this.port() + ".");
            
            while (this.accepter_client)
            {
                this.nouvelle_connexion(connexion.accept());
                Console.afficher(this, "Nouvelle connexion.");
            }

            connexion.close();
            Console.afficher(this, "Application fermée.");
        }
        catch (IOException e)
        {
            Console.afficher(this, "Impossible d'ouvrir la connexion sur le port " + this.port() + ".");
        }
    }
}
