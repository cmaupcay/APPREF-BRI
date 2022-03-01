package admin.messagerie;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import bri.serveur.IUtilisateur;
import bri.serveur.Utilisateurs;
import bri.serveur.utilisateur.Utilisateur;

// TOCOMMENT
public class Message
{
    private IUtilisateur emissaire;
    private IUtilisateur destinataire;
    private String contenu;
    private LocalDateTime creation;
    private boolean lu;

    public final IUtilisateur emissaire() { return this.emissaire; }
    public final IUtilisateur destinataire() { return this.destinataire; }
    public final String contenu() { return this.contenu; }
    public final LocalDateTime creation() { return this.creation; }
    public final boolean lu() { return this.lu; }
    
    private static final String CREATION_PATTERN = "dd-MM-yyyy à HH:mm:ss";
    private final String affichage_creation() { return DateTimeFormatter.ofPattern(CREATION_PATTERN).format(this.creation); }
    public final void lire() { this.lu = true; }

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
