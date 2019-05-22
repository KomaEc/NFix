package org.apache.commons.httpclient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.auth.HttpAuthRealm;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpState {
   private boolean preemptive;
   /** @deprecated */
   public static final String PREEMPTIVE_PROPERTY = "httpclient.authentication.preemptive";
   /** @deprecated */
   public static final String PREEMPTIVE_DEFAULT = "false";
   private HashMap credMap = new HashMap();
   private HashMap proxyCred = new HashMap();
   public static final HttpAuthRealm DEFAULT_AUTH_REALM = new HttpAuthRealm((String)null, (String)null);
   private ArrayList cookies = new ArrayList();
   private int cookiePolicy = 2;
   private HttpConnectionManager httpConnectionManager;
   private static final Log LOG;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$HttpState;

   public HttpState() {
      this.cookiePolicy = CookiePolicy.getDefaultPolicy();
      String preemptiveDefault = null;

      try {
         preemptiveDefault = System.getProperty("httpclient.authentication.preemptive");
      } catch (SecurityException var3) {
      }

      if (preemptiveDefault == null) {
         preemptiveDefault = "false";
      }

      preemptiveDefault = preemptiveDefault.trim().toLowerCase();
      if (!preemptiveDefault.equals("true") && !preemptiveDefault.equals("false")) {
         LOG.warn("Configuration property httpclient.authentication.preemptive must be either true or false.  Using default: false");
         preemptiveDefault = "false";
      }

      this.preemptive = "true".equals(preemptiveDefault);
   }

   public synchronized void addCookie(Cookie cookie) {
      LOG.trace("enter HttpState.addCookie(Cookie)");
      if (cookie != null) {
         Iterator it = this.cookies.iterator();

         while(it.hasNext()) {
            Cookie tmp = (Cookie)it.next();
            if (cookie.equals(tmp)) {
               it.remove();
               break;
            }
         }

         if (!cookie.isExpired()) {
            this.cookies.add(cookie);
         }
      }

   }

   public synchronized void addCookies(Cookie[] cookies) {
      LOG.trace("enter HttpState.addCookies(Cookie[])");
      if (cookies != null) {
         for(int i = 0; i < cookies.length; ++i) {
            this.addCookie(cookies[i]);
         }
      }

   }

   public synchronized Cookie[] getCookies() {
      LOG.trace("enter HttpState.getCookies()");
      return (Cookie[])this.cookies.toArray(new Cookie[this.cookies.size()]);
   }

   /** @deprecated */
   public synchronized Cookie[] getCookies(String domain, int port, String path, boolean secure, Date now) {
      return this.getCookies(domain, port, path, secure);
   }

   /** @deprecated */
   public synchronized Cookie[] getCookies(String domain, int port, String path, boolean secure) {
      LOG.trace("enter HttpState.getCookies(String, int, String, boolean)");
      CookieSpec matcher = CookiePolicy.getDefaultSpec();
      ArrayList list = new ArrayList(this.cookies.size());
      int i = 0;

      for(int m = this.cookies.size(); i < m; ++i) {
         Cookie cookie = (Cookie)this.cookies.get(i);
         if (matcher.match(domain, port, path, secure, cookie)) {
            list.add(cookie);
         }
      }

      return (Cookie[])list.toArray(new Cookie[list.size()]);
   }

   public synchronized boolean purgeExpiredCookies() {
      LOG.trace("enter HttpState.purgeExpiredCookies()");
      return this.purgeExpiredCookies(new Date());
   }

   public synchronized boolean purgeExpiredCookies(Date date) {
      LOG.trace("enter HttpState.purgeExpiredCookies(Date)");
      boolean removed = false;
      Iterator it = this.cookies.iterator();

      while(it.hasNext()) {
         if (((Cookie)it.next()).isExpired(date)) {
            it.remove();
            removed = true;
         }
      }

      return removed;
   }

   public int getCookiePolicy() {
      return this.cookiePolicy;
   }

   public void setAuthenticationPreemptive(boolean value) {
      this.preemptive = value;
   }

   public boolean isAuthenticationPreemptive() {
      return this.preemptive;
   }

   public void setCookiePolicy(int policy) {
      this.cookiePolicy = policy;
   }

   /** @deprecated */
   public synchronized void setCredentials(String realm, Credentials credentials) {
      LOG.trace("enter HttpState.setCredentials(String, Credentials)");
      this.setCredentials(realm, (String)null, credentials);
   }

   public synchronized void setCredentials(String realm, String host, Credentials credentials) {
      LOG.trace("enter HttpState.setCredentials(String realm, String host, Credentials credentials)");
      this.credMap.put(new HttpAuthRealm(host, realm), credentials);
   }

   private static Credentials matchCredentials(HashMap map, String realm, String host) {
      HttpAuthRealm entry = new HttpAuthRealm(host, realm);
      Credentials creds = (Credentials)map.get(entry);
      if (creds == null && host != null && realm != null) {
         entry = new HttpAuthRealm(host, (String)null);
         creds = (Credentials)map.get(entry);
         if (creds == null) {
            entry = new HttpAuthRealm((String)null, realm);
            creds = (Credentials)map.get(entry);
         }
      }

      if (creds == null) {
         creds = (Credentials)map.get(DEFAULT_AUTH_REALM);
      }

      return creds;
   }

   public synchronized Credentials getCredentials(String realm, String host) {
      LOG.trace("enter HttpState.getCredentials(String, String");
      return matchCredentials(this.credMap, realm, host);
   }

   /** @deprecated */
   public synchronized Credentials getCredentials(String realm) {
      LOG.trace("enter HttpState.getCredentials(String)");
      return this.getCredentials(realm, (String)null);
   }

   /** @deprecated */
   public synchronized void setProxyCredentials(String realm, Credentials credentials) {
      LOG.trace("enter HttpState.setProxyCredentials(String, credentials)");
      this.setProxyCredentials(realm, (String)null, credentials);
   }

   public synchronized void setProxyCredentials(String realm, String proxyHost, Credentials credentials) {
      LOG.trace("enter HttpState.setProxyCredentials(String, String, Credentials");
      this.proxyCred.put(new HttpAuthRealm(proxyHost, realm), credentials);
   }

   /** @deprecated */
   public synchronized Credentials getProxyCredentials(String realm) {
      LOG.trace("enter HttpState.getProxyCredentials(String)");
      return this.getProxyCredentials(realm, (String)null);
   }

   public synchronized Credentials getProxyCredentials(String realm, String proxyHost) {
      LOG.trace("enter HttpState.getCredentials(String, String");
      return matchCredentials(this.proxyCred, realm, proxyHost);
   }

   public synchronized String toString() {
      StringBuffer sbResult = new StringBuffer();
      sbResult.append("[");
      sbResult.append(getProxyCredentialsStringRepresentation(this.proxyCred));
      sbResult.append(" | ");
      sbResult.append(getCredentialsStringRepresentation(this.proxyCred));
      sbResult.append(" | ");
      sbResult.append(getCookiesStringRepresentation(this.cookies));
      sbResult.append("]");
      String strResult = sbResult.toString();
      return strResult;
   }

   private static String getProxyCredentialsStringRepresentation(Map proxyCredMap) {
      StringBuffer sbResult = new StringBuffer();
      Iterator iter = proxyCredMap.keySet().iterator();

      while(iter.hasNext()) {
         Object key = iter.next();
         Credentials cred = (Credentials)proxyCredMap.get(key);
         if (sbResult.length() > 0) {
            sbResult.append(", ");
         }

         sbResult.append(key);
         sbResult.append("#");
         sbResult.append(cred.toString());
      }

      return sbResult.toString();
   }

   private static String getCredentialsStringRepresentation(Map credMap) {
      StringBuffer sbResult = new StringBuffer();
      Iterator iter = credMap.keySet().iterator();

      while(iter.hasNext()) {
         Object key = iter.next();
         Credentials cred = (Credentials)credMap.get(key);
         if (sbResult.length() > 0) {
            sbResult.append(", ");
         }

         sbResult.append(key);
         sbResult.append("#");
         sbResult.append(cred.toString());
      }

      return sbResult.toString();
   }

   private static String getCookiesStringRepresentation(List cookies) {
      StringBuffer sbResult = new StringBuffer();

      Cookie ck;
      for(Iterator iter = cookies.iterator(); iter.hasNext(); sbResult.append(ck.toExternalForm())) {
         ck = (Cookie)iter.next();
         if (sbResult.length() > 0) {
            sbResult.append("#");
         }
      }

      return sbResult.toString();
   }

   /** @deprecated */
   public synchronized HttpConnectionManager getHttpConnectionManager() {
      return this.httpConnectionManager;
   }

   /** @deprecated */
   public synchronized void setHttpConnectionManager(HttpConnectionManager httpConnectionManager) {
      this.httpConnectionManager = httpConnectionManager;
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$HttpState == null ? (class$org$apache$commons$httpclient$HttpState = class$("org.apache.commons.httpclient.HttpState")) : class$org$apache$commons$httpclient$HttpState);
   }
}
