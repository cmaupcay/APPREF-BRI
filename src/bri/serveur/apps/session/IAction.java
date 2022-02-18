package bri.serveur.apps.session;

import bri.Connexion;

public interface IAction 
{
    public String nom();
    public boolean executer(Connexion connexion, String[] arguments);
}
