package admin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URLClassLoader;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import bri.Connexion;
import bri.serveur.service.IServiceBRI;

/**
 * Service BRI d'analyse de fichier XML.
 */
public class AnalyseXML implements IServiceBRI
{
    static
    {
        // Fermeture du chargement depuis l'URL, indispensable pour un rechargement du service.
        try { ((URLClassLoader)AnalyseXML.class.getClassLoader()).close(); }
        catch (IOException e) {}
    }

    /** Connexion cliente. */
    private Connexion connexion;

    /**
     * Construction d'une instance du service BRI.
     * @param connexion Socket client ouvert et connecté.
     * @throws IOException Impossible d'ouvrir la connexion depuis le socket client.
     */
    public AnalyseXML(Socket connexion) throws IOException
    {
        this.connexion = new Connexion(connexion);
    }

    @Override
    public final void run()
    {
        try
        {
            // Initialisation du lecteur XSD.
            SchemaFactory xsd_factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // Lecture du fichier XSD distant.
            final String xsd_contenu = String.join("\n", this.connexion.demander_fichier("Fichier XSD de référence : "));
            final InputStream xsd_stream = new ByteArrayInputStream(xsd_contenu.getBytes("UTF-8"));
            Validator xsd = null;
            try { xsd = xsd_factory.newSchema(new StreamSource(xsd_stream)).newValidator(); }
            catch (SAXException e)
            {
                this.connexion.ecrire(Connexion.FAUX);
                this.connexion.ecrire("| ERREUR | Le schéma XSD contient des erreurs de syntaxe : " + e.getMessage());
                return;
            }
            this.connexion.ecrire(Connexion.VRAI);

            // Lecture du fichier XML distant.
            final String xml_contenu = String.join("\n", this.connexion.demander_fichier("Fichier XML à analyser : "));
            final InputStream xml = new ByteArrayInputStream(xml_contenu.getBytes("UTF-8"));
            this.connexion.ecrire(Connexion.VRAI);

            // Vérification du fichier XML selon le schéma XSD.
            try
            {
                xsd.validate(new StreamSource(xml));
                this.connexion.ecrire("Le fichier XML respecte bien le schéma XSD !");
            }
            catch (SAXException e)
            { this.connexion.ecrire("Le fichier XML ne respecte pas le schéma XSD : " + e.getMessage()); }
        }
        catch (IOException|NullPointerException e) { }
    }
}
