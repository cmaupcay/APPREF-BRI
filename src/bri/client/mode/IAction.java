package bri.client.mode;

import java.net.Socket;


public interface IAction
{
    public String nom();
    public void executer(Socket connexion);
}
