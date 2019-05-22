package com.mks.connect;

import com.mks.api.IntegrationPoint;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.Session;
import com.mks.api.VersionNumber;
import com.mks.api.response.APIException;
import com.mks.api.util.APIVersion;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class IntegrationPointImpl implements IntegrationPoint {
   private VersionNumber apiVersion;
   private String host;
   private int port;
   private boolean isClientIP;
   private Set sessions;
   private boolean secure;
   private boolean autoStartIC;
   private IntegrationPointFactory ipf;

   public IntegrationPointImpl(IntegrationPointFactory ipf, String hostname, int port, boolean secure, VersionNumber apiVersion) {
      this.ipf = ipf;
      this.host = hostname;
      this.port = port;
      this.secure = secure;
      this.sessions = new HashSet();
      this.isClientIP = port == 0;
      this.apiVersion = apiVersion;
   }

   public VersionNumber getAPIRequestVersion() {
      return this.apiVersion;
   }

   public String getHostname() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   void setPort(int port) {
      this.port = port;
   }

   public boolean isSecure() {
      return this.secure;
   }

   public Session createSession() throws APIException {
      return this.createSession((String)null, (String)null);
   }

   public Session createSession(String username, String password) throws APIException {
      return this.createSession(this.getAPIRequestVersion(), false, username, password);
   }

   public Session createSession(String username, String password, int apiMajorVersion, int apiMinorVersion) throws APIException {
      return this.createSession(new APIVersion(apiMajorVersion, apiMinorVersion), false, username, password);
   }

   private Session createSession(VersionNumber apiRequestVersion, boolean anonymous, String username, String password) throws APIException {
      Session uas = new UserApplicationSessionImpl(this, apiRequestVersion, username, password, anonymous);
      synchronized(this.sessions) {
         this.sessions.add(uas);
         return uas;
      }
   }

   public Session getCommonSession() throws APIException {
      return this.getCommonSession((String)null, (String)null);
   }

   public Session getCommonSession(String username, String password) throws APIException {
      return this.createSession(this.getAPIRequestVersion(), true, username, password);
   }

   public Iterator getSessions() {
      return Collections.unmodifiableSet(new HashSet(this.sessions)).iterator();
   }

   public boolean getAutoStartIntegrityClient() {
      return this.autoStartIC;
   }

   public void setAutoStartIntegrityClient(boolean autoStartIC) {
      this.autoStartIC = autoStartIC;
   }

   public void release() {
      synchronized(this.sessions) {
         Iterator it = this.sessions.iterator();

         while(true) {
            if (!it.hasNext()) {
               this.sessions.removeAll(this.sessions);
               break;
            }

            UserApplicationSessionImpl uasi = (UserApplicationSessionImpl)it.next();

            try {
               uasi.release(true, false);
            } catch (IOException var6) {
               IntegrationPointFactory.getLogger().exception((Object)this, "API", 0, var6);
            } catch (APIException var7) {
               IntegrationPointFactory.getLogger().exception((Object)this, "API", 0, var7);
            }
         }
      }

      this.ipf.removeIntegrationPoint(this);
   }

   protected void removeSession(Session s) {
      synchronized(this.sessions) {
         this.sessions.remove(s);
      }
   }

   public boolean isClientIntegrationPoint() {
      return this.isClientIP;
   }
}
