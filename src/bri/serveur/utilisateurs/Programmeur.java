package bri.serveur.utilisateurs;


public class Programmeur extends Utilisateur
{
    public static final String TYPE = "prog";
    @Override
    public final String type() { return TYPE; }

    public String ftp;
    @Override
    public final String ftp() { return this.ftp; }

    public Programmeur(final String pseudo, final String mdp, final String ftp)
    { 
        super(pseudo, mdp);
        this.ftp = ftp;
    }
}
