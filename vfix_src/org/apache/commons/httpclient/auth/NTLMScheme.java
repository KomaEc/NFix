package org.apache.commons.httpclient.auth;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.NTLM;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NTLMScheme extends AuthSchemeBase {
   private static final Log LOG;
   private String ntlmchallenge = null;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$auth$NTLMScheme;

   public NTLMScheme(String challenge) throws MalformedChallengeException {
      super(challenge);
      String s = AuthChallengeParser.extractScheme(challenge);
      if (!s.equalsIgnoreCase(this.getSchemeName())) {
         throw new MalformedChallengeException("Invalid NTLM challenge: " + challenge);
      } else {
         int i = challenge.indexOf(32);
         if (i != -1) {
            s = challenge.substring(i, challenge.length());
            this.ntlmchallenge = s.trim();
         } else {
            this.ntlmchallenge = "";
         }

      }
   }

   public String getSchemeName() {
      return "ntlm";
   }

   public String getRealm() {
      return null;
   }

   public String getID() {
      return this.ntlmchallenge;
   }

   public String getParameter(String name) {
      if (name == null) {
         throw new IllegalArgumentException("Parameter name may not be null");
      } else {
         return null;
      }
   }

   public static String authenticate(NTCredentials credentials, String challenge) throws AuthenticationException {
      LOG.trace("enter NTLMScheme.authenticate(NTCredentials, String)");
      if (credentials == null) {
         throw new IllegalArgumentException("Credentials may not be null");
      } else {
         NTLM ntlm = new NTLM();
         String s = null;

         try {
            s = ntlm.getResponseFor(challenge, credentials.getUserName(), credentials.getPassword(), credentials.getHost(), credentials.getDomain());
         } catch (HttpException var5) {
            throw new AuthenticationException(var5.getMessage());
         }

         return "NTLM " + s;
      }
   }

   public String authenticate(Credentials credentials, String method, String uri) throws AuthenticationException {
      LOG.trace("enter NTLMScheme.authenticate(Credentials, String, String)");
      NTCredentials ntcredentials = null;

      try {
         ntcredentials = (NTCredentials)credentials;
      } catch (ClassCastException var6) {
         throw new AuthenticationException("Credentials cannot be used for NTLM authentication: " + credentials.getClass().getName());
      }

      return authenticate(ntcredentials, this.ntlmchallenge);
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$auth$NTLMScheme == null ? (class$org$apache$commons$httpclient$auth$NTLMScheme = class$("org.apache.commons.httpclient.auth.NTLMScheme")) : class$org$apache$commons$httpclient$auth$NTLMScheme);
   }
}
