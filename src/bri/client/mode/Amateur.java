package bri.client.mode;

import bri.serveur.apps.AppAmateur;

public class Amateur extends Mode
{
    public Amateur()
    {
        super("Amateur", AppAmateur.PORT);
    }

    @Override
    protected final void charger_actions()
    {
        
    }
}
