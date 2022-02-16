package bri.client.mode;

import bri.client.IMode;
import bri.serveur.apps.AppProgrammeur;

public class Programmeur implements IMode
{
    @Override
    public final String nom() { return "Programmeur"; }

    @Override
    public final int port() { return AppProgrammeur.PORT; }

    @Override
    public final void lancer()
    {
        
    }
}
