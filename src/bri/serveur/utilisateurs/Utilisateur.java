package bri.serveur.utilisateurs;

import bri.serveur.IUtilisateur;

public abstract class Utilisateur implements IUtilisateur
{
    private String pseudo;
    @Override
    public final String pseudo() { return this.pseudo; }

    private String mdp;
    @Override
    public final String mdp() { return this.mdp; }

    public Utilisateur(final String pseudo, final String mdp)
    {
        this.pseudo = pseudo;
        this.mdp = mdp;
    }

}
