package bri.client.mode;

import java.util.ArrayList;

import bri.client.Console;
import bri.client.IMode;

public abstract class Mode implements IMode
{
    protected final String nom;
    protected final int port;
    protected ArrayList<IAction> actions;

    protected abstract void charger_actions();

    protected Mode(final String nom, final int port)
    {
        this.nom = nom;
        this.port = port;
        this.actions = new ArrayList<>();
        this.charger_actions();
    }

    @Override
    public final String nom() { return this.nom; }
    @Override
    public final String toString() { return this.nom; }

    @Override
    public final int port() { return this.port; }

    @Override
    public final IAction choisir_action()
    { return this.actions.get(Console.choisir(this.actions.toArray(), "Que voulez-vous faire ?")); }

}
