package bri.serveur.app;

import java.io.IOException;
import bri.Connexion;
import bri.serveur.IApp;

public interface ISession extends Runnable
{
    public void initialiser(IApp parent, Connexion connexion);
    public void fermer() throws IOException;
    public Thread thread();
    public IApp parent();
}
