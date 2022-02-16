package bri.client.mode;

import bri.serveur.apps.AppProgrammeur;

public class Programmeur extends Mode
{
    public Programmeur()
    {
        super(AppProgrammeur.NOM, AppProgrammeur.PORT);
    }

    @Override
    protected final void charger_actions()
    {
        
    }
}
