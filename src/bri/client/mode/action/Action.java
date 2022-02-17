package bri.client.mode.action;

import bri.client.mode.IAction;

public abstract class Action implements IAction
{
    @Override
    public final String toString() { return this.nom(); }
}
