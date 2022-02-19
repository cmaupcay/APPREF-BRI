package bri.serveur.app.session.actions;

import bri.Connexion;
import bri.serveur.app.ISession;

public class AjoutService extends Action
{
    @Override
    public final String nom() { return "Ajouter un nouveau service"; }

    public AjoutService(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    { 
        // TODO Action d'ajout de service
        connexion.ecrire("ERREUR : Action non implémentée sur le serveur.");
        return true;
    }
}
