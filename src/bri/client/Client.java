package bri.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import bri.Connexion;
import bri.client.mode.Amateur;
import bri.client.mode.Programmeur;

public class Client 
{
    private static String SERVEUR = "localhost";
    private static ArrayList<IMode> modes = new ArrayList<>();

    private static final void charger_modes()
    {
        modes.add(new Amateur());
        modes.add(new Programmeur());
    }

    private static final IMode choisir_mode()
    { return modes.get(Console.choisir(modes.toArray(), "Choisissez le mode à utiliser : ")); }

    private static final boolean repondre_aux_demandes(Connexion connexion)
    {
        String[] elements = null;
        try { elements = connexion.lire_tableau(); }
        catch (IOException e)
        {
            Console.afficher("ERREUR : Impossible de charger les éléments distants.");
            return false;
        }
        String message = null;
        try { message = connexion.lire(); }
        catch (IOException e)
        {
            Console.afficher("ERREUR : Impossible de lire le message associé à la demande.");
            return false;
        }
        int index = Console.choisir(elements, message);
        connexion.ecrire(String.valueOf(index));
        try { message = connexion.lire(); }
        catch (IOException e)
        {
            Console.afficher("ERREUR : Impossible de lire la réponse du serveur.");
            return false;
        }
        switch (message)
        {
        case Connexion.FAUX: // L'erreur n'est pas fatale
            Console.afficher("ERREUR : Votre réponse a été refusée par le serveur.");
        case Connexion.VRAI:
            return true;
        default:
            Console.afficher("ERREUR : La réponse du serveur est incohérente : " + message);
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
                    { Console.afficher("ERREUR : La connexion a été fermée par le serveur."); }
                    
                }
            }
            else Console.afficher("ERREUR : Connexion refusée par le mode.");
            connexion.fermer();
        }
        catch (UnknownHostException e)
        { Console.afficher("ERREUR : Impossible de résoudre le nom d'hôte du serveur BRI (" + SERVEUR + ':' + mode.port() + ")."); }
        catch (IOException e)
        { Console.afficher("ERREUR :Impossible de se connecter au serveur BRI (" + SERVEUR + ':' + mode.port() + ")."); }
    }   
}