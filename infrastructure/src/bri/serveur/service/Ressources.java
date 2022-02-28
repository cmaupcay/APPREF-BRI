package bri.serveur.service;

import java.util.HashMap;
import java.util.Map;

public abstract class Ressources 
{
    private static final Map<String, Object> RESSOURCES = new HashMap<>();

    public static final Object ressource(final String nom)
    { return RESSOURCES.get(nom); }

    public static final boolean ajouter(final String nom, final Object resource)
    {
        if (ressource(nom) != null) return false;
        RESSOURCES.put(nom, resource);
        return true;
    }
}
