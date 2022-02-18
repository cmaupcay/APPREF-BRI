package bri;

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
            this.tampon = "";
        }
        else throw new IOException("La connexion n'est pas ouverte.");
    }

    public final boolean ouverte() { return !this.connexion.isClosed(); }
    public final void fermer() throws IOException { this.connexion.close(); }

    public static final String VRAI = "1";
    public static final String FAUX = "0";

    public final String lire() throws IOException
    { return (this.tampon = this.entree.readLine()); }
    public void ecrire(final String message)
    { 
        this.sortie.println(message);
        this.sortie.flush();
    }

    private String tampon;
    public final String tampon() { return this.tampon; }

    // TABLEAU
    public static final String TABLEAU = "@TAB";
    public final String[] lire_tableau() throws IOException
    {
        if (this.lire().equals(TABLEAU))
        {
            try
            {
                final int n = Integer.parseInt(this.lire());
                String[] tableau = new String[n];
                for (int o = 0; o < n; o++)
                    tableau[o] = this.lire();
                return tableau;
            }
            catch (NumberFormatException e) {}
        }
        return null;
    }
    public void ecrire(final Object[] tableau)
    {
        this.sortie.println(TABLEAU);
        this.sortie.println(tableau.length);
        for (int o = 0; o < tableau.length; o++)
            this.sortie.println(tableau[o]);
        this.sortie.flush();
    }

    // DEMANDE
    public static final String DEMANDE = "@DEM";
    public final String demander(final String message) throws IOException
    {
        this.sortie.println(DEMANDE);
        this.sortie.println(message);
        this.sortie.flush();
        return this.lire();
    }
    public final int demander_choix(final Object[] tableau, final String message) throws IOException
    {
        this.sortie.println(DEMANDE);
        this.ecrire(tableau);
        this.sortie.println(message);
        this.sortie.flush();
        int i = 0;
        boolean valide = false;
        while (!valide)
        {
            try 
            {
                i = Integer.parseInt(this.lire());
                valide = true;
            }
            catch (NumberFormatException e) {}
        }
        return i;
    }
}
