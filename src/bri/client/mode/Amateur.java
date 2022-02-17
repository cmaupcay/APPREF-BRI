package bri.client.mode;

import bri.client.Connexion;
import bri.serveur.apps.AppAmateur;

public class Amateur extends Mode
{
    public Amateur()
    {
        super(AppAmateur.NOM, AppAmateur.PORT);
    }

    @Override
    public final boolean accepter_connexion(Connexion connexion) { return true; }
}
