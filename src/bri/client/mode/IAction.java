package bri.client.mode;

import bri.client.Connexion;

public interface IAction
{
    public String nom();
    public void executer(Connexion connexion);
}
