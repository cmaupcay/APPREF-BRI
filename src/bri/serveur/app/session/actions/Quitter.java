package bri.serveur.app.session.actions;

import bri.Connexion;
import bri.serveur.Console;
import bri.serveur.app.ISession;

public class Quitter extends Action
{
    @Override
    public final String nom() { return "Quitter"; }

    public Quitter(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    { 
        Console.afficher(this.parent().parent(), "Session quittée par le client.");
        return false;
    }
}
