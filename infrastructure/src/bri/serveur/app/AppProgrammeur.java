package bri.serveur.app;

import bri.serveur.app.session.SessionProgrammeur;

/**
 * Application serveur destinée aux programmeurs.
 */
public class AppProgrammeur extends App
{
    /** Nom de l'aplication. */
    public static final String NOM = "Programmeur";
    /** Port associé à l'application. */
    public static final int PORT = 7000;
    @Override
    public final String nom() { return NOM; }
    @Override
    public final int port() { return PORT; }

    @Override
    public final ISession nouvelle_session() { return new SessionProgrammeur(); }
}
