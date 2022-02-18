package bri.serveur.apps.session.actions;

import bri.client.Connexion;
import bri.serveur.apps.ISession;

public class ControleService extends Action
{
    @Override
    public final String nom() { return "Démarrer/Arrêter un service"; }

    public ControleService(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    {
        // TODO Action de controle des services 
        return false;
    }
}
