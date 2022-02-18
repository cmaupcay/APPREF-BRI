package bri.serveur.apps.session;

import bri.client.Connexion;

public interface IAction 
{
    public String nom();
    public boolean executer(Connexion connexion, String[] arguments);
}
