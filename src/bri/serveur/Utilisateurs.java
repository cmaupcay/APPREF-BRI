package bri.serveur;

import java.util.ArrayList;

public abstract class Utilisateurs 
{
    private static final ArrayList<IUtilisateur> utilisateurs = new ArrayList<>();
    public static final  ArrayList<IUtilisateur> liste() { return utilisateurs; }
    
    public static final String DEFAUT = "admin";

    public static IUtilisateur utilisateur(final String pseudo)
    {
        for (IUtilisateur u : utilisateurs)
            if (u.pseudo().equals(pseudo)) return u;
        return null;
    }
    public static boolean existe(final String pseudo) { return utilisateur(pseudo) != null; }

    public static boolean ajouter(IUtilisateur utilisateur)
    {
        if (existe(utilisateur.pseudo())) return false;
        utilisateurs.add(utilisateur);
        Console.afficher("Nouvel utilisateur : " + utilisateur + '.');
        return true;
    }
    
    public static boolean supprimer(final String pseudo)
    {
        final int n = utilisateurs.size();
        for (int u = 0; u < n; u++)
            if (utilisateurs.get(u).pseudo().equals(pseudo))
            {
                utilisateurs.remove(u);
                Console.afficher("Utilisateur supprimé : " + pseudo + ".");
                return true;
            }
        return false;
    }
}
