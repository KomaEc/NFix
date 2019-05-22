package org.apache.commons.httpclient.auth;

import java.security.MessageDigest;
import java.util.Map;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpConstants;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DigestScheme extends RFC2617Scheme {
   private static final Log LOG;
   private static final char[] HEXADECIMAL;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$auth$DigestScheme;

   public String getID() {
      String id = this.getRealm();
      String nonce = this.getParameter("nonce");
      if (nonce != null) {
         id = id + "-" + nonce;
      }

      return id;
   }

   public DigestScheme(String challenge) throws MalformedChallengeException {
      super(challenge);
      if (this.getParameter("realm") == null) {
         throw new MalformedChallengeException("realm missing");
      } else if (this.getParameter("nonce") == null) {
         throw new MalformedChallengeException("nonce missing");
      } else {
         this.getParameters().put("nc", "00000001");
      }
   }

   public String getSchemeName() {
      return "digest";
   }

   public String authenticate(Credentials credentials, String method, String uri) throws AuthenticationException {
      LOG.trace("enter DigestScheme.authenticate(Credentials, String, String)");
      UsernamePasswordCredentials usernamepassword = null;

      try {
         usernamepassword = (UsernamePasswordCredentials)credentials;
      } catch (ClassCastException var6) {
         throw new AuthenticationException("Credentials cannot be used for digest authentication: " + credentials.getClass().getName());
      }

      this.getParameters().put("cnonce", createCnonce());
      this.getParameters().put("methodname", method);
      this.getParameters().put("uri", uri);
      return authenticate(usernamepassword, this.getParameters());
   }

   public static String authenticate(UsernamePasswordCredentials credentials, Map params) throws AuthenticationException {
      LOG.trace("enter DigestScheme.authenticate(UsernamePasswordCredentials, Map)");
      String digest = createDigest(credentials.getUserName(), credentials.getPassword(), params);
      return "Digest " + createDigestHeader(credentials.getUserName(), params, digest);
   }

   public static String createDigest(String uname, String pwd, Map params) throws AuthenticationException {
      LOG.trace("enter DigestScheme.createDigest(String, String, Map)");
      String digAlg = "MD5";
      String uri = (String)params.get("uri");
      String realm = (String)params.get("realm");
      String nonce = (String)params.get("nonce");
      String nc = (String)params.get("nc");
      String cnonce = (String)params.get("cnonce");
      String qop = (String)params.get("qop");
      String method = (String)params.get("methodname");
      String algorithm = (String)params.get("algorithm");
      if (algorithm == null) {
         algorithm = "MD5";
      }

      if (qop != null) {
         qop = "auth";
      }

      MessageDigest md5Helper;
      try {
         md5Helper = MessageDigest.getInstance("MD5");
      } catch (Exception var19) {
         throw new AuthenticationException("Unsupported algorithm in HTTP Digest authentication: MD5");
      }

      String a1 = null;
      String md5a1;
      if (algorithm.equals("MD5")) {
         a1 = uname + ":" + realm + ":" + pwd;
      } else if (algorithm.equals("MD5-sess")) {
         md5a1 = encode(md5Helper.digest(HttpConstants.getContentBytes(uname + ":" + realm + ":" + pwd)));
         a1 = md5a1 + ":" + nonce + ":" + cnonce;
      } else {
         LOG.warn("Unhandled algorithm " + algorithm + " requested");
         a1 = uname + ":" + realm + ":" + pwd;
      }

      md5a1 = encode(md5Helper.digest(HttpConstants.getContentBytes(a1)));
      String a2 = method + ":" + uri;
      String md5a2 = encode(md5Helper.digest(HttpConstants.getBytes(a2)));
      String serverDigestValue;
      if (qop == null) {
         LOG.debug("Using null qop method");
         serverDigestValue = md5a1 + ":" + nonce + ":" + md5a2;
      } else {
         LOG.debug("Using qop method " + qop);
         serverDigestValue = md5a1 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + md5a2;
      }

      String serverDigest = encode(md5Helper.digest(HttpConstants.getBytes(serverDigestValue)));
      return serverDigest;
   }

   public static String createDigestHeader(String uname, Map params, String digest) {
      LOG.trace("enter DigestScheme.createDigestHeader(String, Map, String)");
      StringBuffer sb = new StringBuffer();
      String uri = (String)params.get("uri");
      String realm = (String)params.get("realm");
      String nonce = (String)params.get("nonce");
      String nc = (String)params.get("nc");
      String cnonce = (String)params.get("cnonce");
      String opaque = (String)params.get("opaque");
      String qop = (String)params.get("qop");
      String algorithm = (String)params.get("algorithm");
      if (qop != null) {
         qop = "auth";
      }

      sb.append("username=\"" + uname + "\"").append(", realm=\"" + realm + "\"").append(", nonce=\"" + nonce + "\"").append(", uri=\"" + uri + "\"").append(qop == null ? "" : ", qop=\"" + qop + "\"").append(algorithm == null ? "" : ", algorithm=\"" + algorithm + "\"").append(qop == null ? "" : ", nc=" + nc).append(qop == null ? "" : ", cnonce=\"" + cnonce + "\"").append(", response=\"" + digest + "\"").append(opaque == null ? "" : ", opaque=\"" + opaque + "\"");
      return sb.toString();
   }

   private static String encode(byte[] binaryData) {
      LOG.trace("enter DigestScheme.encode(byte[])");
      if (binaryData.length != 16) {
         return null;
      } else {
         char[] buffer = new char[32];

         for(int i = 0; i < 16; ++i) {
            int low = binaryData[i] & 15;
            int high = (binaryData[i] & 240) >> 4;
            buffer[i * 2] = HEXADECIMAL[high];
            buffer[i * 2 + 1] = HEXADECIMAL[low];
         }

         return new String(buffer);
      }
   }

   public static String createCnonce() throws AuthenticationException {
      LOG.trace("enter DigestScheme.createCnonce()");
      String var0 = "MD5";

      MessageDigest md5Helper;
      try {
         md5Helper = MessageDigest.getInstance("MD5");
      } catch (Exception var4) {
         throw new AuthenticationException("Unsupported algorithm in HTTP Digest authentication: MD5");
      }

      String cnonce = Long.toString(System.currentTimeMillis());
      cnonce = encode(md5Helper.digest(HttpConstants.getBytes(cnonce)));
      return cnonce;
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$auth$DigestScheme == null ? (class$org$apache$commons$httpclient$auth$DigestScheme = class$("org.apache.commons.httpclient.auth.DigestScheme")) : class$org$apache$commons$httpclient$auth$DigestScheme);
      HEXADECIMAL = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   }
}
