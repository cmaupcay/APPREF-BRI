package bri.serveur;

import bri.IBRI;

/**
 * Sur-couche d'affichage console pour le serveur BRI.
 */
public abstract class Console
{
    /** Préfixe d'affichage des messages. */
    private static final String PREFIX = " [BRI] ";

    /**
     * Affichage d'un message sur la console du client.
     * @param message Message à afficher.
     * @param ligne Drapeau de saut de ligne.
     */
    public static void afficher(final String message, final boolean ligne)
    {
        if (ligne) System.out.println(PREFIX + message);
        else System.out.print(PREFIX + message);
    }
    /**
     * Affichage d'un message sur la console du client avec un saut de ligne.
     * @param message Message à afficher.
     */
    public static void afficher(final String message) { afficher(message, true); }
    /**
     * Affichage d'un message associé à un objet BRI (ex: une application serveur).
     * @param objet Objet BRI à l'origine du message.
     * @param message Message à afficher.
     */
    public static void afficher(final IBRI objet, final String message)
    { afficher("<" + objet.nom() + ">\t" + message); }
    /**
     * Affichage d'un message associé à un service BRI.
     * @param service Service à l'origine du message.
     * @param message Message à afficher.
     */
    public static void afficher(final IService service, final String message)
    { afficher("<Service/" + service.nom() + ">\t" + message); }
}
