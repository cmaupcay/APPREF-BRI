package bri.serveur;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire des utilisateurs du serveur.
 */
public abstract class Utilisateurs 
{
    /** Liste des utilisateurs BRI. */
    private static final List<IUtilisateur> UTILISATEURS = new ArrayList<>();
    /**
     * Retourne la liste des utilisateurs du serveur.
     * @return Liste des utilisateurs BRI.
     */
    public static final  List<IUtilisateur> liste() { return UTILISATEURS; }
    
    /** Nom et mot de passe de l'utilisateur par défaut. */
    public static final String DEFAUT = "admin";
    /** Adresse FTP de l'utilisateur par défaut. */
    public static final String DEFAUT_FTP = "localhost:2121";

    /**
     * Retourne l'utilisateur possédant le pseudo donné.
     * @param pseudo Pseudo de l'utilisateur cible.
     * @return Utilisateur possédant le pseudo recherché, null si ce pseudo est introuvable.
     */
    public static IUtilisateur utilisateur(final String pseudo)
    {
        synchronized (UTILISATEURS)
        {
            for (IUtilisateur u : UTILISATEURS)
            if (u.pseudo().equals(pseudo)) return u;
        }
        return null;
    }

    /**
     * Vérification de l'existence d'un utilisateur grâce à son pseudo.
     * @param pseudo Pseudo de l'utilisateur cible.
     * @return Existence du pseudo dans la liste.
     */
    public static boolean existe(final String pseudo) { return utilisateur(pseudo) != null; }

    /**
     * Ajout d'un nouvel utilisateur à la liste.
     * @param utilisateur Utilisateur à ajouter.
     * @return Indique si l'utilisateur a été ajouté.
     */
    public static boolean ajouter(IUtilisateur utilisateur)
    {
        if (utilisateur.pseudo() == null || utilisateur.mdp() == null) return false;
        if (existe(utilisateur.pseudo())) return false;
        synchronized (UTILISATEURS) { UTILISATEURS.add(utilisateur); }
        Console.afficher("Nouvel utilisateur : " + utilisateur + '.');
        return true;
    }
    
    /**
     * Suppression d'un utilisateur de la liste.
     * @param pseudo Pseudo de l'utilisateur cible.
     * @return Indique si la suppression à réussi.
     */
    public static boolean supprimer(final String pseudo)
    {
        synchronized (UTILISATEURS)
        {
            final int n = UTILISATEURS.size();
            for (int u = 0; u < n; u++)
                if (UTILISATEURS.get(u).pseudo().equals(pseudo))
                {
                    UTILISATEURS.remove(u);
                    Console.afficher("Utilisateur supprimé : " + pseudo + ".");
                    return true;
                }
        }
        return false;
    }
}
