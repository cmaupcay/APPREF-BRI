package bri.client;

import bri.IBRI;
import bri.client.mode.IAction;

public interface IMode extends IBRI
{
    public boolean accepter_connexion(Connexion connexion);
    public IAction choisir_action();
}
