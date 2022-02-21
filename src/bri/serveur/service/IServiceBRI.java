package bri.serveur.service;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;

public interface IServiceBRI extends Runnable
{
    // public String toStringue();

    /**
     * NORME BRI
     * Le développeur de services BRi doit respecter la norme qui lui permettra de soumettre sa classe Java à notre
     * plateforme. Une classe de service BRi doit :                             Vérification
     *  * implémenter l'interface BRi.Service                                   OK
     *  * ne pas être abstract                                                  OK
     *  * être publique                                                         OK
     *  * avoir un constructeur public (Socket) sans exception                  OK
     *  * avoir un attribut Socket private final                                NO  // TODO Vérification de l'attribut Socket
     *  * avoir une méthode public static String toStringue() sans exception    //
     */
    public static boolean verifier_norme(Class<?> classe)
    {
        final Class<?>[] interfaces = classe.getInterfaces();
        for (Class<?> i : interfaces)
            if (i == IServiceBRI.class)
            {
                final int mod = classe.getModifiers();
                if (!Modifier.isPublic(mod)) return false;
                if (Modifier.isAbstract(mod)) return false;
                try 
                { 
                    classe.getDeclaredConstructor(Socket.class);
                    // final Method toStringue = classe.getMethod("toStringue");
                    // if (toStringue.getExceptionTypes().length > 0) return false;
                }
                catch (NoSuchMethodException e) { return false; }
            }
        return false;
    }
}
