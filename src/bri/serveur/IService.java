package bri.serveur;

import bri.Connexion;
import bri.serveur.service.IServiceBRI;

public interface IService
{
    public String nom();
    public IUtilisateur auteur();

    public boolean actif();
    public boolean activer();
    public boolean desactiver();

    public boolean mettre_a_jour();

    public IServiceBRI nouvelle_instance(Connexion connexion);
}
