package admin.messagerie;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bri.serveur.IUtilisateur;
import bri.serveur.Utilisateurs;

/**
 * Message manipulé par la messagerie.
 * Les messages doivent être enregistrés sous forme de String, via la fonction serialiser().
 */
public class Message
{
    /** Emissaire du message. */
    private IUtilisateur emissaire;
    /** Destinataire du message. */
    private IUtilisateur destinataire;
    /** Contenu textuel du message. */
    private String contenu;
    /** Date et heure de création du message. */
    private LocalDateTime creation;
    /** Drapeau indiquant si le message a été lu par son destinataire. */
    private boolean lu;

    /**
     * Retourne l'émissaire du message.
     * @return Utilisateur ayant émis le message.
     */
    public final IUtilisateur emissaire() { return this.emissaire; }
    /**
     * Retourne le destinataire du message.
     * @return Utilisateur auquel le message est destiné.
     */
    public final IUtilisateur destinataire() { return this.destinataire; }
    /**
     * Retourne le contenu textuel du message.
     * @return Contenu du message.
     */
    public final String contenu() { return this.contenu; }
    /**
     * Retourne la date et l'heure de création du message sous forme d'objet.
     * @return Date et heure et création du message.
     */
    public final LocalDateTime creation() { return this.creation; }
    /**
     * Retourne la valeur du drapeau de lecture du message.
     * @return Drapeau indiquant si le message a été lu par le destinataire du message.
     */
    public final boolean lu() { return this.lu; }
    
    /** Pattern d'affichage et de sérialisation de la date et l'heure de création du message. */
    private static final String CREATION_PATTERN = "dd-MM-yyyy à HH:mm:ss";
    /**
     * Retourne la date et l'heure de création du message sous forme de texte.
     * @return Date et heure de création du message.
     */
    private final String affichage_creation() { return DateTimeFormatter.ofPattern(CREATION_PATTERN).format(this.creation); }
    /**
     * Modifie la valeur du drapeau de lecture du message à vrai.
     */
    public final void lire() { this.lu = true; }

    /**
     * Construction d'un nouveau message.
     * @param emissaire
     * @param destinataire
     * @param contenu
     */
    public Message(final IUtilisateur emissaire, final IUtilisateur destinataire, final String contenu)
    {
        this.emissaire = emissaire;
        this.destinataire = destinataire;
        this.contenu = contenu;
        this.lu = false;
        this.creation = LocalDateTime.now();
    }

    @Override
    public final String toString()
    { return "[" + (this.lu ? ' ' : '*') + "] De " + this.emissaire.pseudo() + " le " + this.affichage_creation(); }

    public final String afficher()
    { 
        return "De : " + this.emissaire.pseudo() + "\nA  : " + this.destinataire.pseudo() + "\nLe : " + 
        this.affichage_creation() + "\n\n" + this.contenu + "\n"; 
    }

    private static final String SEP = "@@@@@@@"; // Choisir un séparateur qui ne sera normalement pas présent dans le contenu du message.
    public final String exporter()
    { return this.emissaire.pseudo() + SEP + this.destinataire.pseudo() + SEP + this.contenu + SEP + (this.lu ? "1" : "0") + SEP + this.affichage_creation(); }

    public Message(final String message_exporte) throws Exception
    {
        final String[] message = message_exporte.split(SEP);
        if (message.length != 5) throw new Exception("nombre d'arguments incohérents dans le message sérialisé.");
        this.emissaire = Utilisateurs.utilisateur(message[0]);
        this.destinataire = Utilisateurs.utilisateur(message[1]);
        this.contenu = message[2];
        if (message[3].equals("1")) this.lu = true; else this.lu = false;
        this.creation = LocalDateTime.parse(message[4], DateTimeFormatter.ofPattern(CREATION_PATTERN));
    }
}
