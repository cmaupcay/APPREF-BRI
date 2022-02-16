package bri.client.mode;

import bri.client.IMode;
import bri.serveur.apps.AppAmateur;

public class Amateur implements IMode
{
    @Override
    public final String nom() { return "Amateur"; }

    @Override
    public final int port() { return AppAmateur.PORT; }

    @Override
    public final void lancer()
    {
        
    }
}
