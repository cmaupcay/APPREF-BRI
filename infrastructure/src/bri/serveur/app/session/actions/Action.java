package bri.serveur.app.session.actions;

import bri.Connexion;
import bri.serveur.app.ISession;
import bri.serveur.app.session.IAction;

/**
 * Classe de base des actions de session.
 */
public abstract class Action implements IAction
{
    /** Session parente ayant lancé l'action. */
    private ISession parent;
    /**
     * Retourne la session parente de l'action.
     * @return Session parente ayant lancé l'action.
     */
    protected final ISession parent() { return this.parent; }

    /**
     * Construction de l'action de session.
     * @param parent Session parente.
     */
    protected Action(ISession parent)
    {
        this.parent = parent;
    }

    @Override
    public final String toString() { return this.nom(); }

    /**
     * Lance l'action de contrôle de l'activité d'un service dans la session parente.
     * @param connexion Connexion cliente ouverte et connectée.
     * @param pseudo Pseudo de l'utilisateur authentifié.
     * @param id_service Index du service cible dans la liste des services publiés par l'utilisateur.
     * @return Indique si la session doit continuer ou non.
     */
    protected final boolean controle_activite_service(Connexion connexion, final String pseudo, final int id_service)
    { return (new ControleService(this.parent)).executer(connexion, new String[]{ pseudo, Integer.toString(id_service) }); }

    /**
     * Lance une action pour quitter la session parente.
     * @param connexion Connexion cliente ouverte et connectée.
     * @return Indique si la session doit continuer ou non.
     */
    protected final boolean quitter_session(Connexion connexion)
    { return (new Quitter(this.parent)).executer(connexion, null); }
}
