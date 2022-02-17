package bri.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connexion 
{
    private Socket connexion;
    private BufferedReader entree;
    private PrintWriter sortie;

    public Connexion(Socket connexion) throws IOException
    {
        if (connexion != null && connexion.isConnected())
        {
            this.connexion = connexion;
            this.entree = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            this.sortie = new PrintWriter(connexion.getOutputStream());
        }
        else throw new IOException("La connexion n'est pas ouverte.");
    }

    public final boolean ouverte() { return this.connexion.isConnected(); }
    public final void fermer() throws IOException { this.connexion.close(); }

    public static final String VRAI = "1";
    public static final String FAUX = "0";

    public final String lire() throws IOException
    { return this.entree.readLine(); }
    public void ecrire(final String message)
    { 
        this.sortie.println(message);
        this.sortie.flush();
    }
}
