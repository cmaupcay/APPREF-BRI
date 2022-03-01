package bri.serveur.app;

import bri.serveur.app.session.SessionAmateur;

/**
 * Application serveur destinée aux amateurs.
 */
public class AppAmateur extends App
{
    /** Nom de l'aplication. */
    public static final String NOM = "Amateur";
    /** Port associé à l'application. */
    public static final int PORT = 7001;
    @Override
    public final String nom() { return NOM; }
    @Override
    public final int port() { return PORT; }

    @Override
    public final ISession nouvelle_session() { return new SessionAmateur(); }
}
