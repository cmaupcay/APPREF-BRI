package bri.serveur.app.session.actions;

import bri.Connexion;
import bri.serveur.app.ISession;

public class ControleService extends Action
{
    @Override
    public final String nom() { return "Démarrer/Arrêter un service"; }

    public ControleService(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    {
        // TODO Action de controle des services 
        connexion.ecrire("ERREUR : Action non implémentée sur le serveur.");
        return true;
    }
}