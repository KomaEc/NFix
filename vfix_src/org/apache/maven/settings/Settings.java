package org.apache.maven.settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Settings extends TrackableBase implements Serializable {
   private String localRepository;
   private boolean interactiveMode = true;
   private boolean usePluginRegistry = false;
   private boolean offline = false;
   private List<Proxy> proxies;
   private List<Server> servers;
   private List<Mirror> mirrors;
   private List<Profile> profiles;
   private List<String> activeProfiles;
   private List<String> pluginGroups;
   private String modelEncoding = "UTF-8";
   private Proxy activeProxy;
   private Map profileMap;
   private RuntimeInfo runtimeInfo;

   public void addActiveProfile(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("Settings.addActiveProfiles(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getActiveProfiles().add(string);
      }
   }

   public void addMirror(Mirror mirror) {
      if (!(mirror instanceof Mirror)) {
         throw new ClassCastException("Settings.addMirrors(mirror) parameter must be instanceof " + Mirror.class.getName());
      } else {
         this.getMirrors().add(mirror);
      }
   }

   public void addPluginGroup(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("Settings.addPluginGroups(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getPluginGroups().add(string);
      }
   }

   public void addProfile(Profile profile) {
      if (!(profile instanceof Profile)) {
         throw new ClassCastException("Settings.addProfiles(profile) parameter must be instanceof " + Profile.class.getName());
      } else {
         this.getProfiles().add(profile);
      }
   }

   public void addProxy(Proxy proxy) {
      if (!(proxy instanceof Proxy)) {
         throw new ClassCastException("Settings.addProxies(proxy) parameter must be instanceof " + Proxy.class.getName());
      } else {
         this.getProxies().add(proxy);
      }
   }

   public void addServer(Server server) {
      if (!(server instanceof Server)) {
         throw new ClassCastException("Settings.addServers(server) parameter must be instanceof " + Server.class.getName());
      } else {
         this.getServers().add(server);
      }
   }

   public List<String> getActiveProfiles() {
      if (this.activeProfiles == null) {
         this.activeProfiles = new ArrayList();
      }

      return this.activeProfiles;
   }

   public String getLocalRepository() {
      return this.localRepository;
   }

   public List<Mirror> getMirrors() {
      if (this.mirrors == null) {
         this.mirrors = new ArrayList();
      }

      return this.mirrors;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }

   public List<String> getPluginGroups() {
      if (this.pluginGroups == null) {
         this.pluginGroups = new ArrayList();
      }

      return this.pluginGroups;
   }

   public List<Profile> getProfiles() {
      if (this.profiles == null) {
         this.profiles = new ArrayList();
      }

      return this.profiles;
   }

   public List<Proxy> getProxies() {
      if (this.proxies == null) {
         this.proxies = new ArrayList();
      }

      return this.proxies;
   }

   public List<Server> getServers() {
      if (this.servers == null) {
         this.servers = new ArrayList();
      }

      return this.servers;
   }

   public boolean isInteractiveMode() {
      return this.interactiveMode;
   }

   public boolean isOffline() {
      return this.offline;
   }

   public boolean isUsePluginRegistry() {
      return this.usePluginRegistry;
   }

   public void removeActiveProfile(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("Settings.removeActiveProfiles(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getActiveProfiles().remove(string);
      }
   }

   public void removeMirror(Mirror mirror) {
      if (!(mirror instanceof Mirror)) {
         throw new ClassCastException("Settings.removeMirrors(mirror) parameter must be instanceof " + Mirror.class.getName());
      } else {
         this.getMirrors().remove(mirror);
      }
   }

   public void removePluginGroup(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("Settings.removePluginGroups(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getPluginGroups().remove(string);
      }
   }

   public void removeProfile(Profile profile) {
      if (!(profile instanceof Profile)) {
         throw new ClassCastException("Settings.removeProfiles(profile) parameter must be instanceof " + Profile.class.getName());
      } else {
         this.getProfiles().remove(profile);
      }
   }

   public void removeProxy(Proxy proxy) {
      if (!(proxy instanceof Proxy)) {
         throw new ClassCastException("Settings.removeProxies(proxy) parameter must be instanceof " + Proxy.class.getName());
      } else {
         this.getProxies().remove(proxy);
      }
   }

   public void removeServer(Server server) {
      if (!(server instanceof Server)) {
         throw new ClassCastException("Settings.removeServers(server) parameter must be instanceof " + Server.class.getName());
      } else {
         this.getServers().remove(server);
      }
   }

   public void setActiveProfiles(List<String> activeProfiles) {
      this.activeProfiles = activeProfiles;
   }

   public void setInteractiveMode(boolean interactiveMode) {
      this.interactiveMode = interactiveMode;
   }

   public void setLocalRepository(String localRepository) {
      this.localRepository = localRepository;
   }

   public void setMirrors(List<Mirror> mirrors) {
      this.mirrors = mirrors;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public void setOffline(boolean offline) {
      this.offline = offline;
   }

   public void setPluginGroups(List<String> pluginGroups) {
      this.pluginGroups = pluginGroups;
   }

   public void setProfiles(List<Profile> profiles) {
      this.profiles = profiles;
   }

   public void setProxies(List<Proxy> proxies) {
      this.proxies = proxies;
   }

   public void setServers(List<Server> servers) {
      this.servers = servers;
   }

   public void setUsePluginRegistry(boolean usePluginRegistry) {
      this.usePluginRegistry = usePluginRegistry;
   }

   public Boolean getInteractiveMode() {
      return this.isInteractiveMode();
   }

   public void flushActiveProxy() {
      this.activeProxy = null;
   }

   public synchronized Proxy getActiveProxy() {
      if (this.activeProxy == null) {
         List proxies = this.getProxies();
         if (proxies != null && !proxies.isEmpty()) {
            Iterator it = proxies.iterator();

            while(it.hasNext()) {
               Proxy proxy = (Proxy)it.next();
               if (proxy.isActive()) {
                  this.activeProxy = proxy;
                  break;
               }
            }
         }
      }

      return this.activeProxy;
   }

   public Server getServer(String serverId) {
      Server match = null;
      List servers = this.getServers();
      if (servers != null && serverId != null) {
         Iterator it = servers.iterator();

         while(it.hasNext()) {
            Server server = (Server)it.next();
            if (serverId.equals(server.getId())) {
               match = server;
               break;
            }
         }
      }

      return match;
   }

   /** @deprecated */
   @Deprecated
   public Mirror getMirrorOf(String repositoryId) {
      Mirror match = null;
      List mirrors = this.getMirrors();
      if (mirrors != null && repositoryId != null) {
         Iterator it = mirrors.iterator();

         while(it.hasNext()) {
            Mirror mirror = (Mirror)it.next();
            if (repositoryId.equals(mirror.getMirrorOf())) {
               match = mirror;
               break;
            }
         }
      }

      return match;
   }

   public void flushProfileMap() {
      this.profileMap = null;
   }

   public Map getProfilesAsMap() {
      if (this.profileMap == null) {
         this.profileMap = new LinkedHashMap();
         if (this.getProfiles() != null) {
            Iterator it = this.getProfiles().iterator();

            while(it.hasNext()) {
               Profile profile = (Profile)it.next();
               this.profileMap.put(profile.getId(), profile);
            }
         }
      }

      return this.profileMap;
   }

   public void setRuntimeInfo(RuntimeInfo runtimeInfo) {
      this.runtimeInfo = runtimeInfo;
   }

   public RuntimeInfo getRuntimeInfo() {
      return this.runtimeInfo;
   }
}
