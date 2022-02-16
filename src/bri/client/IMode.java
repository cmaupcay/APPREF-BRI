package bri.client;

import java.net.Socket;

public interface IMode 
{
    public String nom();
    public int port();
    public int choisir_action();
    public void executer_action(final int id, Socket connexion);
}
