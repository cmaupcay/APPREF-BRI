package bri.serveur.app.session.actions;

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
}
