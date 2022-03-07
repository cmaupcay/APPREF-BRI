package bri.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import bri.Connexion;
import bri.client.mode.Amateur;
import bri.client.mode.Programmeur;

/**
 * Classe principale du client BRI.
 */
public class Client 
{
    /** Adresse du serveur BRI. */
    private static String SERVEUR = "localhost";
    /** Modes de connexion disponibles. */
    private static ArrayList<IMode> modes = new ArrayList<>();

    /**
     * Ajout des modes de connexion.
     */
    private static final void charger_modes()
    {
        modes.add(new Amateur());
        modes.add(new Programmeur());
    }

    /**
     * Séléction du mode de connexion au serveur BRI.
     * @return Mode de connexion séléctionné.
     */
    private static final IMode choisir_mode()
    { return modes.get(Console.choisir(modes.toArray(), "Choisissez le mode à utiliser : ")); }

    /**
     * Procédure de réponse aux demandes du serveur.
     * @param connexion Connexion au serveur BRI.
     * @return Indique si le client doit continuer à répondre aux demandes.
     */
    private static final boolean repondre_aux_demandes(Connexion connexion)
    {
        String[] elements = null;
        String message = null;
        try { elements = connexion.lire_tableau(); }
        catch (IOException e)
        {
            Console.afficher("| ERREUR | Impossible de charger les éléments distants : " + e.getMessage());
            return false;
        }
        if (elements == null)
        {
            message = connexion.tampon();
            if (message.equals(Connexion.FICHIER)) // Demande d'un fichier
            {
                String chemin = null;
                try { chemin = Console.demander(connexion.lire(), false); }
                catch (IOException e)
                {
                    Console.afficher("| ERREUR | Impossible de lire le message associé à la demande : " + e.getMessage());
                    return false;
                }
                final File fichier = new File(chemin);
                if (fichier.exists())
                {
                    try
                    {
                        // Lecture du contenu du fichier.
                        final BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
                        String ligne;
                        List<String> contenu = new ArrayList<>();
                        while ((ligne = lecteur.readLine()) != null) contenu.add(ligne);
                        lecteur.close();
                        // Envoi du contenu sous forme de tableau.
                        connexion.ecrire(contenu.toArray());
                        Console.afficher("Fichier transmis.");
                    }
                    catch (IOException e)
                    { 
                        Console.afficher("| ERREUR | Impossible de lire le fichier '" + chemin + "' : " + e.getMessage());
                        return false;
                    }
                }
                else
                {
                    Console.afficher("| ERREUR | Le fichier '" + chemin + "' est introuvable.");
                    return false;
                }
            }
            else connexion.ecrire(Console.demander(message, true)); // Demande d'une ligne
        }
        else                    // Demande d'un index dans le tableau reçu
        {
            try { message = connexion.lire(); }
            catch (IOException e)
            {
                Console.afficher("| ERREUR | Impossible de lire le message associé à la demande : " + e.getMessage());
                return false;
            }
            int index = Console.choisir(elements, message);
            connexion.ecrire(String.valueOf(index));
        }
        try { message = connexion.lire(); }
        catch (IOException e)
        {
            Console.afficher("| ERREUR | Impossible de lire la réponse du serveur : " + e.getMessage());
            return false;
        }
        switch (message)
        {
        case Connexion.FAUX: // L'erreur n'est pas fatale
            Console.afficher("| ERREUR | Votre réponse a été refusée par le serveur.");
        case Connexion.VRAI:
            return true;
        default:
            Console.afficher("| ERREUR | La réponse du serveur est incohérente : " + message);
            return false;
        }
    }

    public static final void main(String[] args)
    {
        Console.afficher("Bienvenue dans le client BRI !");
        charger_modes();
        final IMode mode = choisir_mode();

        Connexion connexion = null;
        try 
        {
            connexion = new Connexion(new Socket(SERVEUR, mode.port()));
            if (mode.accepter_connexion(connexion))
            {
                String tmp = "";
                while ((tmp = connexion.lire()) != null)
                {
                    try
                    {
                        if (tmp.equals(Connexion.DEMANDE))
                        { if (!repondre_aux_demandes(connexion)) break; }
                        else Console.afficher(tmp);
                    }
                    catch (NullPointerException e)
                    { Console.afficher("| ERREUR | La connexion a été fermée par le serveur."); }
                }
            }
            else Console.afficher("| ERREUR | Connexion refusée par le mode.");
            connexion.fermer();
        }
        catch (UnknownHostException e)
        { Console.afficher("| ERREUR | Impossible de résoudre le nom d'hôte du serveur BRI (" + SERVEUR + ':' + mode.port() + ")."); }
        catch (IOException e)
        { Console.afficher("| ERREUR | Impossible de se connecter au serveur BRI (" + SERVEUR + ':' + mode.port() + ")."); }
    }   
}