package bri.serveur.apps.session.actions;

import bri.Connexion;
import bri.serveur.apps.ISession;

public class SuppressionService extends Action
{
    @Override
    public final String nom() { return "Supprimer un service "; }

    public SuppressionService(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    {
        // TODO Action de suppression d'un service
        connexion.ecrire("ERREUR : Action non implémentée sur le serveur.");
        return true;
    }
}
