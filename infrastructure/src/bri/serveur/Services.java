package bri.serveur;

import java.util.ArrayList;
import java.util.List;

import bri.serveur.service.Service;

/**
 * Gestionnaire des services BRI.
 */
public abstract class Services 
{
    /** Liste des services BRI du serveur. */
    private static final List<IService> SERVICES = new ArrayList<>();
    /**
     * Retourne la liste des services sur le serveur.
     * @return Liste des services BRI.
     */
    public static final List<IService> services() { return SERVICES; }

    /**
     * Ajout d'un nouveau service.
     * @param auteur Auteur du service BRI.
     * @param nom Nom du service BRI.
     * @return Indique si l'ajout du service a réussi.
     */
    public static final boolean ajouter(final IUtilisateur auteur, final String nom)
    {
        if (auteur.pseudo() == null || auteur.mdp() == null || nom == null) return false;
        synchronized (SERVICES)
        {
            for (IService s : SERVICES)
            if (s.nom().equals(nom)) return false;
            SERVICES.add(new Service(auteur, nom));
        }
        Console.afficher("Nouveau service : " + auteur.pseudo() + '/' + nom);
        return true;
    }
    
    /**
     * Suppression d'un service BRI?
     * @param auteur Auteur du service cible.
     * @param nom Nom du service cible.
     * @return Indique si la suppression du service a réussi.
     */
    public static final boolean supprimer(final IUtilisateur auteur, final String nom)
    {
        synchronized (SERVICES)
        {
            for (IService s : SERVICES)
            if (s.nom().equals(nom))
            {
                if (!s.auteur().equals(auteur)) return false;
                if (s.actif()) return false;
                SERVICES.remove(s);
                Console.afficher("Service supprimé : " + auteur.pseudo() + '/' + nom);
                return true;
            }
        }
        return false;
    }

    /**
     * Filtre une liste de service selon si les services sont actifs.
     * @param services Liste des services à filtrer.
     * @return Liste filtrée.
     */
    public static final List<IService> services_actifs(final List<IService> services)
    {
        List<IService> services_actifs = new ArrayList<>();
        for (IService s : services) if (s.actif()) services_actifs.add(s);
        return services_actifs;
    }
    /**
     * Retourne la liste des services actifs du serveur.
     * @return Liste des services actifs.
     */
    public static final List<IService> services_actifs() 
    { synchronized(SERVICES) { return services_actifs(SERVICES); } }

    /**
     * Filtre une liste de service selon si les services sont inactifs.
     * @param services Liste des services à filtrer.
     * @return Liste filtrée.
     */
    public static final List<IService> services_inactifs(final List<IService> services)
    {
        List<IService> services_inactifs = new ArrayList<>();
        for (IService s : services) if (!s.actif()) services_inactifs.add(s);
        return services_inactifs;
    }
    /**
     * Retourne la liste des services inactifs du serveur.
     * @return Liste des services inactifs.
     */
    public static final List<IService> services_inactifs() 
    { synchronized(SERVICES) { return services_inactifs(SERVICES); } }

    /**
     * Filtre une liste de service selon l'auteur des services.
     * @param services Liste des services à filtrer.
     * @param auteur Auteur cible.
     * @return Liste filtrée.
     */
    public static final List<IService> services_publies(final List<IService> services, final IUtilisateur auteur)
    {
        List<IService> services_p = new ArrayList<>();
        for (IService s : services) if (s.auteur().equals(auteur)) services_p.add(s);
        return services_p;
    }
    /**
     * Retourne la liste des services publiés par un auteur.
     * @param auteur Auteur cible.
     * @return Liste des services publiés par l'auteur.
     */
    public static final List<IService> services_publies(final IUtilisateur auteur)
    { synchronized (SERVICES) { return services_publies(SERVICES, auteur); } }
}
