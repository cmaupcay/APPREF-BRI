package bri.client;

import java.net.Socket;
import java.util.ArrayList;

import bri.client.mode.Amateur;
import bri.client.mode.Programmeur;

public class Client 
{
    private static ArrayList<IMode> modes = new ArrayList<>();

    private static final void charger_modes()
    {
        modes.add(new Amateur());
        modes.add(new Programmeur());
    }

    private static final IMode choisir_mode()
    {
        Console.afficher("Choisissez le mode à utiliser : ");
        final int n_modes = modes.size();
        for (int m = 0; m < n_modes; m++) 
            Console.sortie.println("\t# " + m + " - " + modes.get(m).nom());
        Console.sortie.println("");
        Console.afficher_message_demande("");
        final int i_mode = Console.entree.nextInt();
        if (i_mode >= n_modes) return choisir_mode();
        return modes.get(i_mode);
    }

    private static Socket initialiser_connexion(final int port)
    {
        // TODO Initialisation connexion
    }

    public static final void main(String[] args)
    {
        Console.afficher("Bienvenue dans le client BRI !");
        charger_modes();
        final IMode mode = choisir_mode();
        
        Socket connexion = initialiser_connexion(mode.port());

        final int action = mode.choisir_action();
        mode.executer_action(action, connexion);

        Console.afficher("Au revoir !");
    }   
}