package bri.serveur;

import java.util.ArrayList;

import bri.serveur.service.Service;

public abstract class Services 
{
    private static final ArrayList<IService> services = new ArrayList<>();
    
    public static final boolean ajouter(final IUtilisateur auteur, final String nom)
    {
        for (IService s : services)
            if (s.nom().equals(nom)) return false;
        services.add(new Service(auteur, nom));
        return true;
    }
    
    public static final boolean supprimer(final IUtilisateur auteur, final String nom)
    {
        for (IService s : services)
            if (s.nom().equals(nom))
            {
                if (!s.auteur().equals(auteur)) return false;
                services.remove(s);
                return true;
            }
        return false;
    }

    public static final ArrayList<IService> services_actifs(final ArrayList<IService> services)
    {
        ArrayList<IService> services_actifs = new ArrayList<>();
        for (IService s : services) if (s.actif()) services_actifs.add(s);
        return services_actifs;
    }
    public static final ArrayList<IService> services_actifs() { return services_actifs(services); }

    public static final ArrayList<IService> services_publies(final ArrayList<IService> services, final IUtilisateur auteur)
    {
        ArrayList<IService> services_p = new ArrayList<>();
        for (IService s : services) if (s.auteur().equals(auteur)) services_p.add(s);
        return services_p;
    }
    public static final ArrayList<IService> services_publies(final IUtilisateur auteur) { return services_publies(services, auteur); }
}
