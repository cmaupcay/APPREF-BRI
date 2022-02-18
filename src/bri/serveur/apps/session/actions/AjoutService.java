package bri.serveur.apps.session.actions;

import bri.client.Connexion;
import bri.serveur.apps.ISession;

public class AjoutService extends Action
{
    @Override
    public final String nom() { return "Ajouter un nouveau service"; }

    public AjoutService(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    { 
        // TODO Action d'ajout de service
        return false;
    }
}
