package bri.client;

import bri.Connexion;
import bri.IBRI;

public interface IMode extends IBRI
{
    public boolean accepter_connexion(Connexion connexion);
}
