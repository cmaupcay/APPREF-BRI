package bri.serveur.app.session.actions;

import bri.Connexion;
import bri.serveur.app.ISession;
import bri.serveur.app.session.IAction;

public abstract class Action implements IAction
{
    private ISession parent;
    protected final ISession parent() { return this.parent; }

    public Action(ISession parent)
    {
        this.parent = parent;
    }

    @Override
    public final String toString() { return this.nom(); }

    protected final boolean controle_activite_service(Connexion connexion, final String pseudo, final int id_service)
    { return (new ControleService(this.parent())).executer(connexion, new String[]{ pseudo, Integer.toString(id_service) }); }
}
