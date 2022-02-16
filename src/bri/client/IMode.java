package bri.client;

import bri.IBRI;
import bri.client.mode.IAction;

public interface IMode extends IBRI
{
    public IAction choisir_action();
}
