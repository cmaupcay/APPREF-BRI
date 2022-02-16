package bri;

/**
 * Interface commune aux applications serveurs et aux modes du client.
 * Permet le partage du nom et du port de l'application.
 */
public interface IBRI 
{
    public String nom();
    public int port();
}
