package org.apache.commons.httpclient.auth;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpConstants;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.util.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BasicScheme extends RFC2617Scheme {
   private static final Log LOG;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$auth$BasicScheme;

   public BasicScheme(String challenge) throws MalformedChallengeException {
      super(challenge);
   }

   public String getSchemeName() {
      return "basic";
   }

   public String authenticate(Credentials credentials, String method, String uri) throws AuthenticationException {
      LOG.trace("enter BasicScheme.authenticate(Credentials, String, String)");
      UsernamePasswordCredentials usernamepassword = null;

      try {
         usernamepassword = (UsernamePasswordCredentials)credentials;
      } catch (ClassCastException var6) {
         throw new AuthenticationException("Credentials cannot be used for basic authentication: " + credentials.getClass().getName());
      }

      return authenticate(usernamepassword);
   }

   public static String authenticate(UsernamePasswordCredentials credentials) {
      LOG.trace("enter BasicScheme.authenticate(UsernamePasswordCredentials)");
      if (credentials == null) {
         throw new IllegalArgumentException("Credentials may not be null");
      } else {
         StringBuffer buffer = new StringBuffer();
         buffer.append(credentials.getUserName());
         buffer.append(":");
         buffer.append(credentials.getPassword());
         return "Basic " + HttpConstants.getAsciiString(Base64.encode(HttpConstants.getContentBytes(buffer.toString())));
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$auth$BasicScheme == null ? (class$org$apache$commons$httpclient$auth$BasicScheme = class$("org.apache.commons.httpclient.auth.BasicScheme")) : class$org$apache$commons$httpclient$auth$BasicScheme);
   }
}
