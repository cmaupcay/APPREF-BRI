package bri.client;

import bri.client.mode.IAction;

public interface IMode
{
    public String nom();
    public int port();
    public IAction choisir_action();
}
