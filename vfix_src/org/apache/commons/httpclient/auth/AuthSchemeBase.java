package org.apache.commons.httpclient.auth;

import org.apache.commons.httpclient.Credentials;

public abstract class AuthSchemeBase implements AuthScheme {
   private String challenge = null;

   public AuthSchemeBase(String challenge) throws MalformedChallengeException {
      if (challenge == null) {
         throw new IllegalArgumentException("Challenge may not be null");
      } else {
         this.challenge = challenge;
      }
   }

   public boolean equals(Object obj) {
      return obj instanceof AuthSchemeBase ? this.challenge.equals(((AuthSchemeBase)obj).challenge) : super.equals(obj);
   }

   public int hashCode() {
      return this.challenge.hashCode();
   }

   public String toString() {
      return this.challenge;
   }

   public abstract String authenticate(Credentials var1, String var2, String var3) throws AuthenticationException;

   public abstract String getID();

   public abstract String getRealm();

   public abstract String getParameter(String var1);

   public abstract String getSchemeName();
}
