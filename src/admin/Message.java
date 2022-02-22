package admin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bri.serveur.IUtilisateur;

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
    
    private final String affichage_creation() { return DateTimeFormatter.ofPattern("dd-MM-yyyy à HH:mm:ss").format(this.creation); }
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
        return "De : " + this.emissaire.pseudo() + "\nA : " + this.destinataire.pseudo() + "\nLe : " + 
        this.affichage_creation() + "\n\n" + this.contenu + "\n"; 
    }
}
