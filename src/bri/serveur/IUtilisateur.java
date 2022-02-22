package bri.serveur;

public interface IUtilisateur 
{
    public String pseudo();
    public String mdp();

    public String ftp();
    public void modifier_ftp(final String ftp);

    public Class<?> charger_service_distant(final String nom) throws ClassNotFoundException;
}
