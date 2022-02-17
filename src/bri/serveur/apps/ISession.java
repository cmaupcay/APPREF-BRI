package bri.serveur.apps;

import java.io.IOException;
import bri.client.Connexion;

public interface ISession extends Runnable
{
    public void initialiser(Connexion connexion);
    public void fermer() throws IOException;
    public Thread thread();
}
