package bri.serveur.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire des ressources partagées entre les services BRI.
 */
public abstract class Ressources 
{
    /** Liste des ressources partagées, avec association nom => valeur. */
    private static final Map<String, Object> RESSOURCES = new HashMap<>();

    /**
     * Récupération d'une ressource partagée depuis sa clé.
     * @param nom Nom de la ressource partagée.
     * @return Ressource partagée, null si introuvable.
     */
    public static final Object ressource(final String nom)
    { return RESSOURCES.get(nom); }

    /**
     * Ajout d'une nouvelle ressource partagée, si elle n'existe pas déjà.
     * @param nom Nom de la ressource partagée (clé).
     * @param resource Ressource partagée.
     * @return Indique si la ressource a bien été ajouté.
     */
    public static final boolean ajouter(final String nom, final Object resource)
    {
        if (ressource(nom) != null) return false;
        RESSOURCES.put(nom, resource);
        return true;
    }
}
