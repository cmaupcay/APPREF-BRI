package bri.serveur.app.session.actions;

import bri.Connexion;
import bri.serveur.app.ISession;

public class MiseAJourService extends Action
{
    @Override
    public final String nom() { return "Mettre à jour un service"; }

    public MiseAJourService(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    {
        // TODO Action de mise a jour d'un service 
        connexion.ecrire("ERREUR : Action non implémentée sur le serveur.");
        return true;
    }
}
