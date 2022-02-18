package bri.serveur.apps.session.actions;

import bri.serveur.apps.ISession;
import bri.serveur.apps.session.IAction;

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
}
