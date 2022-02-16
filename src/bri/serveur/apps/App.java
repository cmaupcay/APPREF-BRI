package bri.serveur.apps;

import java.io.IOException;
import java.net.ServerSocket;

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


    @Override
    public final void run()
    {
        try
        {
            ServerSocket connexion = new ServerSocket(this.port());
            Console.afficher(this, "Connexion ouverte sur le port " + this.port() + ".");
            
            connexion.close();
            Console.afficher(this, "Connexion fermée.");
        }
        catch (IOException e)
        {
            Console.afficher(this, "Impossible d'ouvrir la connexion sur le port " + this.port() + ".");
        }
    }
}
