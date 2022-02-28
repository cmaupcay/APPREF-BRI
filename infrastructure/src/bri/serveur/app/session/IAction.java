package bri.serveur.app.session;

import bri.Connexion;

// TOCOMMENT IAction et actions
public interface IAction 
{
    public String nom();
    public boolean executer(Connexion connexion, String[] arguments);
}
