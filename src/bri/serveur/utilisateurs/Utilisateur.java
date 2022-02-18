package bri.serveur.utilisateurs;

import java.io.IOException;

import bri.Connexion;
import bri.serveur.IUtilisateur;
import bri.serveur.Utilisateurs;

public class Utilisateur implements IUtilisateur
{
    private String pseudo;
    @Override
    public final String pseudo() { return this.pseudo; }

    private String mdp;
    @Override
    public final String mdp() { return this.mdp; }

    @Override
    public String ftp() { return null; }

    public Utilisateur(final String pseudo, final String mdp)
    {
        this.pseudo = pseudo;
        this.mdp = mdp;
    }

    @Override
    public final String toString() { return this.pseudo() + " (" + this.getClass().getSimpleName() + ')'; }

    private boolean proposer_promotion(Connexion connexion) throws IOException
    {
        connexion.ecrire("ATTENTION : Une promotion supprime le compte précédent.");
        final String choix = connexion.demander("Promouvoir " + this.pseudo + " au rang " + this.getClass().getSimpleName() + " (o/n) ? ");
        connexion.ecrire(Connexion.VRAI);
        if (choix.toLowerCase().equals("o"))
        {
            Utilisateurs.supprimer(this.pseudo());
            return true;
        }
        connexion.ecrire("Désolé, vous allez devoir choisir un autre pseudo. :(");
        return false;
    }

    public Utilisateur(Connexion connexion) throws IOException
    {
        this.pseudo = connexion.demander("Pseudo : ");
        if (Utilisateurs.existe(this.pseudo))
        {
            connexion.ecrire(Connexion.FAUX);
            if (!proposer_promotion(connexion))
            { // L'utilisateur a refusé la promotion
                boolean valide = false;
                while (!valide)
                {
                    this.pseudo = connexion.demander("Pseudo : ");
                    if (!(valide = !Utilisateurs.existe(this.pseudo)))
                    {
                        connexion.ecrire(Connexion.FAUX);
                        connexion.ecrire("Ce pseudo est déjà utilisé.");
                    }
                }
                connexion.ecrire(Connexion.VRAI);
            }
        }
        else connexion.ecrire(Connexion.VRAI);
        this.mdp = connexion.demander("Mot de passe : ");
        connexion.ecrire(Connexion.VRAI);
    }
}
