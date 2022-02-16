package bri.serveur;

import bri.IBRI;

public interface IApp extends Runnable, IBRI
{
    public Thread thread();
    public void demarrer();
    public boolean accepter_client();
}
