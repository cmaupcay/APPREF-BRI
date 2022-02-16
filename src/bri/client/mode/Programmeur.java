package bri.client.mode;

import bri.serveur.apps.AppProgrammeur;

public class Programmeur extends Mode
{
    public Programmeur()
    {
        super("Programmeur", AppProgrammeur.PORT);
    }

    @Override
    protected final void charger_actions()
    {
        
    }
}
