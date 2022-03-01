package bri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Sur-couche au Socket natif de Java.
 * Il implémente des fonctions utilitaires qui facilitent les échanges client-serveur.
 */
public class Connexion 
{
    /** Socket de connexion. */
    private Socket connexion;
    /** Flux d'entree du socket. */
    private BufferedReader entree;
    /** Flux de sortie du socket. */
    private PrintWriter sortie;

    /**
     * Retourne le socket de la connexion.
     * @return Socket associé à la connexion.
     */
    public final Socket socket() { return this.connexion; }

    /**
     * Construction d'une nouvelle connexion depuis un socket déjà conecté.
     * @param connexion Socket connecté.
     * @throws IOException Le socket n'est pas ouvert ou pas connecté.
     */
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

    /**
     * Indique si la connexion a été fermée.
     * @return Etat de fermeture du socket de connexion.
     */
    public final boolean ouverte() { return !this.connexion.isClosed(); }
    /**
     * Ferme la connexion.
     * @throws IOException Impossible de fermer la connexion, souvent car elle déjà fermée.
     */
    public final void fermer() throws IOException { this.connexion.close(); }

    /** Symbole standard d'une réponse acceptée. */
    public static final String VRAI = "1";
    /** Symbole standard d'une réponse refusée. */
    public static final String FAUX = "0";

    /**
     * Lecture de l'entrée et enregistrement dans le tampon.
     * @return Contenu du tampon.
     * @throws IOException Impossible de lire l'entrée.
     */
    public final String lire() throws IOException
    { return (this.tampon = this.entree.readLine()); }
    
    /**
     * Ecriture d'un message dans la sortie.
     * @param message Message à écrire.
     */
    public void ecrire(final String message)
    { 
        this.sortie.println(message);
        this.sortie.flush();
    }

    /** Tampon de lecture de l'entree. */
    private String tampon;
    /**
     * Retourne le contenu du tampon de lecture.
     * @return Tampon de lecture.
     */
    public final String tampon() { return this.tampon; }

    /** Symbole de la transmission d'un tableau. */
    public static final String TABLEAU = "@TAB";
    /**
     * Lecture séquentielle d'un tableau.
     * @return Tableau lu.
     * @throws IOException La lecture a échoué.
     */
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
    /**
     * Ecriture d'un tableau dans la sortie.
     * @param tableau Tableau à écrire.
     */    
    public void ecrire(final Object[] tableau)
    {
        this.sortie.println(TABLEAU);
        this.sortie.println(tableau.length);
        for (int o = 0; o < tableau.length; o++)
            this.sortie.println(tableau[o]);
        this.sortie.flush();
    }

    /** Symbole d'une demande d'information. */
    public static final String DEMANDE = "@DEM";
    /** Symbole de transmission d'un fichier. */
    public static final String FICHIER = "@FICHIER";
    /**
     * Ecriture des instructions de demande dans la sortie et lecture de la réponse.
     * @param message Message associé à la demande.
     * @return Réponse lue sur l'entrée.
     * @throws IOException Impossible de finaliser la demande.
     */
    public final String demander(final String message) throws IOException
    {
        this.sortie.println(DEMANDE);
        this.sortie.println(message);
        this.sortie.flush();
        return this.lire();
    }
    /**
     * Ecriture des instructions de demande d'un fichier dans la sortie et lecture de la réponse.
     * @param message Message associé à la demande.
     * @return Contenu lu sur l'entrée.
     * @throws IOException Impossible de finaliser la demande.
     */
    public final String demander_fichier(final String message) throws IOException
    { return demander(FICHIER + "\n" + message); }
    /**
     * Ecriture dans la sortie des instructions de selection dans un tableau et lecture de la réponse.
     * Le tableau de choix est étendu en menu (ajout d'un choix de retour en fin de tableau).
     * @param tableau Tableau dans lequel demander le choix.
     * @param message Message associé à la demande.
     * @return Indice dans le tableau du choix lu sur l'entrée.
     * @throws IOException Impossible de finaliser la demande.
     */
    public final int demander_choix(final Object[] tableau, final String message) throws IOException
    {
        this.sortie.println(DEMANDE);
        this.ecrire(menu(tableau));
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

    /** Message de retour ajouté aux menus générés. */
    public static final String RETOUR = "[/] Retour";
    /**
     * Génération d'un menu de choix depuis un tableau d'objet.
     * @param tableau Tableau de séléction.
     * @return Menu.
     */
    private static final Object[] menu(final Object[] tableau)
    {
        Object[] menu = new Object[tableau.length + 1];
        for (int i = 0; i < tableau.length; i++)
            menu[i] = tableau[i];
        menu[tableau.length] = RETOUR;
        return menu;   
    }
}
