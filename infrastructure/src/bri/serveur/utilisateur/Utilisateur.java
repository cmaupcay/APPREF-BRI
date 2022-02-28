package bri.serveur.utilisateur;

import java.io.IOException;

import bri.Connexion;
import bri.serveur.IUtilisateur;
import bri.serveur.Utilisateurs;

/**
 * Classe des utilisateurs BRI de base.
 */
public class Utilisateur implements IUtilisateur
{
    /** Pseudo de l'utilisateur. */
    private String pseudo;
    @Override
    public final String pseudo() { return this.pseudo; }

    /** Mot de passe de l'utilisateur. */
    private String mdp;
    @Override
    public final String mdp() { return this.mdp; }

    @Override
    public String ftp() { return null; }
    @Override
    public void modifier_ftp(final String ftp) {}

    /**
     * Création d'un nouvel utilisateur.
     * @param pseudo Pseudo de l'utilisateur.
     * @param mdp Mot de passe de l'utilisateur.
     */
    public Utilisateur(final String pseudo, final String mdp)
    {
        this.pseudo = pseudo;
        this.mdp = mdp;
    }

    @Override
    public final String toString() { return this.pseudo() + " (" + this.getClass().getSimpleName() + ')'; }

    /**
     * Vérifie que, si un utilisateur ayant le même pseudo est enregistré, il est un rang différent.
     * @return Résultat de la vérification.
     */
    private final boolean rang_different()
    {
        for (IUtilisateur u : Utilisateurs.liste())
            if (u.pseudo().equals(this.pseudo))
                return !u.getClass().equals(this.getClass());
        return true;
    }
    /**
     * Proposition de promotion du rang de l'utilisateur déjà enregistré.
     * @param connexion Connexion cliente.
     * @return Indique si la promotion à eu lieu.
     * @throws IOException Impossible de lire les informations de connexion.
     */
    private final boolean proposer_promotion(Connexion connexion) throws IOException
    {
        if (this.rang_different())
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
        else return false;
    }

    /**
     * Création d'un nouvel utilisateur de manière interactive depuis une connexion.
     * @param connexion Connexion cliente.
     * @throws IOException Impossible de lire les informations de connexion.
     */
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
