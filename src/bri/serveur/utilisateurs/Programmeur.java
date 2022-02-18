package bri.serveur.utilisateurs;


public class Programmeur extends Utilisateur
{
    public static final String TYPE = "prog";
    @Override
    public final String type() { return TYPE; }

    public Programmeur(final String pseudo, final String mdp)
    { super(pseudo, mdp); }
}
