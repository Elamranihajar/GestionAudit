package gestionAudits.controller.authentification;

import gestionAudits.models.User;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;

public class AuthentificationControllerWithActiveDirectory {

    public static User authenticate(String username, String password) {
        String ldapUrl = "ldap://your-ad-server:389"; // URL du serveur Active Directory
        String domain = "example.com"; // Nom de domaine Active Directory
        String baseDn = "DC=authentificationGestionAudits,DC=com"; // Base DN du domaine Active Directory

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, username + "@" + domain);
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            // Authentification via Active Directory
            DirContext ctx = new InitialDirContext(env);

            // Requête pour récupérer les informations utilisateur
            String searchFilter = "(sAMAccountName=" + username + ")";
            String[] attributesToFetch = {"email", "name", "role"}; // Attributs à récupérer

            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            searchControls.setReturningAttributes(attributesToFetch);

            NamingEnumeration<SearchResult> results = ctx.search(baseDn, searchFilter, searchControls);

            if (results.hasMore()) {
                SearchResult result = results.next();
                Attributes attrs = result.getAttributes();

                // Construire l'objet User
                String name = attrs.get("name") != null ? attrs.get("name").get().toString() : null;
                String email = attrs.get("email") != null ? attrs.get("email").get().toString() : null;
                String role = attrs.get("role") != null ? attrs.get("role").get().toString() : null;
                String photo = attrs.get("photo") != null ? attrs.get("photo").get().toString() : null;
                assert role != null;
                User user = new User(username, email, name, Integer.parseInt(role),photo);
                ctx.close();
                return user;
            } else {
                // Aucun utilisateur trouvé
                ctx.close();
                return null;
            }
        } catch (NamingException e) {
            // Authentification échouée ou autre erreur
            System.err.println("Erreur d'authentification ou de recherche : " + e.getMessage());
            return null;
        }
    }
}
