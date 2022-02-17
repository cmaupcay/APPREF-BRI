package bri.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import bri.client.mode.Amateur;
import bri.client.mode.IAction;
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

    public static final void main(String[] args)
    {
        Console.afficher("Bienvenue dans le client BRI !");
        charger_modes();
        final IMode mode = modes.get(Console.choisir(modes.toArray(), "Choisissez le mode à utiliser : "));

        Connexion connexion = null;
        try 
        {
            connexion = new Connexion(new Socket(SERVEUR, mode.port()));
            if (mode.accepter_connexion(connexion))
            {
                final IAction action = mode.choisir_action();
                action.executer(connexion);
            }
            connexion.fermer();
        }
        catch (UnknownHostException e)
        { 
            Console.afficher("ERREUR : Impossible de résoudre le  nom d'hôte du serveur BRI (" + SERVEUR + ':' + mode.port() + ").");
            return;
        }
        catch (IOException e)
        {
            Console.afficher("ERREUR :Impossible de se connecter au serveur BRI (" + SERVEUR + ':' + mode.port() + ")."); 
            return;
        }
    }   
}