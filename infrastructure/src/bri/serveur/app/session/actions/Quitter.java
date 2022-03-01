package bri.serveur.app.session.actions;

import bri.Connexion;
import bri.serveur.Console;
import bri.serveur.app.ISession;

/**
 * Action permettant de quitter une session.
 */
public class Quitter extends Action
{
    @Override
    public final String nom() { return "Quitter"; }

    /**
     * Construction de l'action.
     * @param parent Session parente.
     */
    public Quitter(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    { 
        connexion.ecrire("Au revoir !");
        Console.afficher(this.parent().parent(), "Session quittée par le client.");
        return false; // On demande à la session parente de s'arrêter.
    }
}
