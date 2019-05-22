package org.apache.commons.httpclient.auth;

import java.util.Map;

public abstract class RFC2617Scheme extends AuthSchemeBase {
   private Map params = null;

   public RFC2617Scheme(String challenge) throws MalformedChallengeException {
      super(challenge);
      String s = AuthChallengeParser.extractScheme(challenge);
      if (!s.equalsIgnoreCase(this.getSchemeName())) {
         throw new MalformedChallengeException("Invalid " + this.getSchemeName() + " challenge: " + challenge);
      } else {
         this.params = AuthChallengeParser.extractParams(challenge);
      }
   }

   protected Map getParameters() {
      return this.params;
   }

   public String getParameter(String name) {
      if (name == null) {
         throw new IllegalArgumentException("Parameter name may not be null");
      } else {
         return (String)this.params.get(name.toLowerCase());
      }
   }

   public String getRealm() {
      return this.getParameter("realm");
   }

   public String getID() {
      return this.getRealm();
   }
}
