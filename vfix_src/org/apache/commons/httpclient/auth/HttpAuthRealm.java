package org.apache.commons.httpclient.auth;

public class HttpAuthRealm {
   private String realm = null;
   private String domain = null;

   public HttpAuthRealm(String domain, String realm) {
      this.domain = domain;
      this.realm = realm;
   }

   private static boolean domainAttribMatch(String d1, String d2) {
      return d1 == null || d2 == null || d1.equalsIgnoreCase(d2);
   }

   private static boolean realmAttribMatch(String r1, String r2) {
      return r1 == null || r2 == null || r1.equals(r2);
   }

   public boolean equals(Object o) {
      if (o == null) {
         return false;
      } else if (o == this) {
         return true;
      } else if (!(o instanceof HttpAuthRealm)) {
         return super.equals(o);
      } else {
         HttpAuthRealm that = (HttpAuthRealm)o;
         return domainAttribMatch(this.domain, that.domain) && realmAttribMatch(this.realm, that.realm);
      }
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("Authentication domain: '");
      buffer.append(this.domain);
      buffer.append("', authentication realm: '");
      buffer.append(this.realm);
      buffer.append("'");
      return buffer.toString();
   }

   public int hashCode() {
      StringBuffer buffer = new StringBuffer();
      buffer.append(this.domain);
      buffer.append(this.realm);
      return buffer.toString().hashCode();
   }
}
