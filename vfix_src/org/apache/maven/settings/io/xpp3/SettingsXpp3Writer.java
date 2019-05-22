package org.apache.maven.settings.io.xpp3;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import org.apache.maven.settings.Activation;
import org.apache.maven.settings.ActivationFile;
import org.apache.maven.settings.ActivationOS;
import org.apache.maven.settings.ActivationProperty;
import org.apache.maven.settings.IdentifiableBase;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Profile;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Repository;
import org.apache.maven.settings.RepositoryBase;
import org.apache.maven.settings.RepositoryPolicy;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.TrackableBase;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.pull.MXSerializer;
import org.codehaus.plexus.util.xml.pull.XmlSerializer;

public class SettingsXpp3Writer {
   private static final String NAMESPACE = null;

   public void write(Writer writer, Settings settings) throws IOException {
      XmlSerializer serializer = new MXSerializer();
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
      serializer.setOutput(writer);
      serializer.startDocument(settings.getModelEncoding(), (Boolean)null);
      this.writeSettings(settings, "settings", serializer);
      serializer.endDocument();
   }

   private void writeActivation(Activation activation, String tagName, XmlSerializer serializer) throws IOException {
      if (activation != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (activation.isActiveByDefault()) {
            serializer.startTag(NAMESPACE, "activeByDefault").text(String.valueOf(activation.isActiveByDefault())).endTag(NAMESPACE, "activeByDefault");
         }

         if (activation.getJdk() != null) {
            serializer.startTag(NAMESPACE, "jdk").text(activation.getJdk()).endTag(NAMESPACE, "jdk");
         }

         if (activation.getOs() != null) {
            this.writeActivationOS(activation.getOs(), "os", serializer);
         }

         if (activation.getProperty() != null) {
            this.writeActivationProperty(activation.getProperty(), "property", serializer);
         }

         if (activation.getFile() != null) {
            this.writeActivationFile(activation.getFile(), "file", serializer);
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeActivationFile(ActivationFile activationFile, String tagName, XmlSerializer serializer) throws IOException {
      if (activationFile != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (activationFile.getMissing() != null) {
            serializer.startTag(NAMESPACE, "missing").text(activationFile.getMissing()).endTag(NAMESPACE, "missing");
         }

         if (activationFile.getExists() != null) {
            serializer.startTag(NAMESPACE, "exists").text(activationFile.getExists()).endTag(NAMESPACE, "exists");
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeActivationOS(ActivationOS activationOS, String tagName, XmlSerializer serializer) throws IOException {
      if (activationOS != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (activationOS.getName() != null) {
            serializer.startTag(NAMESPACE, "name").text(activationOS.getName()).endTag(NAMESPACE, "name");
         }

         if (activationOS.getFamily() != null) {
            serializer.startTag(NAMESPACE, "family").text(activationOS.getFamily()).endTag(NAMESPACE, "family");
         }

         if (activationOS.getArch() != null) {
            serializer.startTag(NAMESPACE, "arch").text(activationOS.getArch()).endTag(NAMESPACE, "arch");
         }

         if (activationOS.getVersion() != null) {
            serializer.startTag(NAMESPACE, "version").text(activationOS.getVersion()).endTag(NAMESPACE, "version");
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeActivationProperty(ActivationProperty activationProperty, String tagName, XmlSerializer serializer) throws IOException {
      if (activationProperty != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (activationProperty.getName() != null) {
            serializer.startTag(NAMESPACE, "name").text(activationProperty.getName()).endTag(NAMESPACE, "name");
         }

         if (activationProperty.getValue() != null) {
            serializer.startTag(NAMESPACE, "value").text(activationProperty.getValue()).endTag(NAMESPACE, "value");
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeIdentifiableBase(IdentifiableBase identifiableBase, String tagName, XmlSerializer serializer) throws IOException {
      if (identifiableBase != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (identifiableBase.getId() != null && !identifiableBase.getId().equals("default")) {
            serializer.startTag(NAMESPACE, "id").text(identifiableBase.getId()).endTag(NAMESPACE, "id");
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeMirror(Mirror mirror, String tagName, XmlSerializer serializer) throws IOException {
      if (mirror != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (mirror.getMirrorOf() != null) {
            serializer.startTag(NAMESPACE, "mirrorOf").text(mirror.getMirrorOf()).endTag(NAMESPACE, "mirrorOf");
         }

         if (mirror.getName() != null) {
            serializer.startTag(NAMESPACE, "name").text(mirror.getName()).endTag(NAMESPACE, "name");
         }

         if (mirror.getUrl() != null) {
            serializer.startTag(NAMESPACE, "url").text(mirror.getUrl()).endTag(NAMESPACE, "url");
         }

         if (mirror.getId() != null && !mirror.getId().equals("default")) {
            serializer.startTag(NAMESPACE, "id").text(mirror.getId()).endTag(NAMESPACE, "id");
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeProfile(Profile profile, String tagName, XmlSerializer serializer) throws IOException {
      if (profile != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (profile.getActivation() != null) {
            this.writeActivation(profile.getActivation(), "activation", serializer);
         }

         Iterator iter;
         if (profile.getProperties() != null && profile.getProperties().size() > 0) {
            serializer.startTag(NAMESPACE, "properties");
            iter = profile.getProperties().keySet().iterator();

            while(iter.hasNext()) {
               String key = (String)iter.next();
               String value = (String)profile.getProperties().get(key);
               serializer.startTag(NAMESPACE, "" + key + "").text(value).endTag(NAMESPACE, "" + key + "");
            }

            serializer.endTag(NAMESPACE, "properties");
         }

         Repository o;
         if (profile.getRepositories() != null && profile.getRepositories().size() > 0) {
            serializer.startTag(NAMESPACE, "repositories");
            iter = profile.getRepositories().iterator();

            while(iter.hasNext()) {
               o = (Repository)iter.next();
               this.writeRepository(o, "repository", serializer);
            }

            serializer.endTag(NAMESPACE, "repositories");
         }

         if (profile.getPluginRepositories() != null && profile.getPluginRepositories().size() > 0) {
            serializer.startTag(NAMESPACE, "pluginRepositories");
            iter = profile.getPluginRepositories().iterator();

            while(iter.hasNext()) {
               o = (Repository)iter.next();
               this.writeRepository(o, "pluginRepository", serializer);
            }

            serializer.endTag(NAMESPACE, "pluginRepositories");
         }

         if (profile.getId() != null && !profile.getId().equals("default")) {
            serializer.startTag(NAMESPACE, "id").text(profile.getId()).endTag(NAMESPACE, "id");
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeProxy(Proxy proxy, String tagName, XmlSerializer serializer) throws IOException {
      if (proxy != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (!proxy.isActive()) {
            serializer.startTag(NAMESPACE, "active").text(String.valueOf(proxy.isActive())).endTag(NAMESPACE, "active");
         }

         if (proxy.getProtocol() != null && !proxy.getProtocol().equals("http")) {
            serializer.startTag(NAMESPACE, "protocol").text(proxy.getProtocol()).endTag(NAMESPACE, "protocol");
         }

         if (proxy.getUsername() != null) {
            serializer.startTag(NAMESPACE, "username").text(proxy.getUsername()).endTag(NAMESPACE, "username");
         }

         if (proxy.getPassword() != null) {
            serializer.startTag(NAMESPACE, "password").text(proxy.getPassword()).endTag(NAMESPACE, "password");
         }

         if (proxy.getPort() != 8080) {
            serializer.startTag(NAMESPACE, "port").text(String.valueOf(proxy.getPort())).endTag(NAMESPACE, "port");
         }

         if (proxy.getHost() != null) {
            serializer.startTag(NAMESPACE, "host").text(proxy.getHost()).endTag(NAMESPACE, "host");
         }

         if (proxy.getNonProxyHosts() != null) {
            serializer.startTag(NAMESPACE, "nonProxyHosts").text(proxy.getNonProxyHosts()).endTag(NAMESPACE, "nonProxyHosts");
         }

         if (proxy.getId() != null && !proxy.getId().equals("default")) {
            serializer.startTag(NAMESPACE, "id").text(proxy.getId()).endTag(NAMESPACE, "id");
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeRepository(Repository repository, String tagName, XmlSerializer serializer) throws IOException {
      if (repository != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (repository.getReleases() != null) {
            this.writeRepositoryPolicy(repository.getReleases(), "releases", serializer);
         }

         if (repository.getSnapshots() != null) {
            this.writeRepositoryPolicy(repository.getSnapshots(), "snapshots", serializer);
         }

         if (repository.getId() != null) {
            serializer.startTag(NAMESPACE, "id").text(repository.getId()).endTag(NAMESPACE, "id");
         }

         if (repository.getName() != null) {
            serializer.startTag(NAMESPACE, "name").text(repository.getName()).endTag(NAMESPACE, "name");
         }

         if (repository.getUrl() != null) {
            serializer.startTag(NAMESPACE, "url").text(repository.getUrl()).endTag(NAMESPACE, "url");
         }

         if (repository.getLayout() != null && !repository.getLayout().equals("default")) {
            serializer.startTag(NAMESPACE, "layout").text(repository.getLayout()).endTag(NAMESPACE, "layout");
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeRepositoryBase(RepositoryBase repositoryBase, String tagName, XmlSerializer serializer) throws IOException {
      if (repositoryBase != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (repositoryBase.getId() != null) {
            serializer.startTag(NAMESPACE, "id").text(repositoryBase.getId()).endTag(NAMESPACE, "id");
         }

         if (repositoryBase.getName() != null) {
            serializer.startTag(NAMESPACE, "name").text(repositoryBase.getName()).endTag(NAMESPACE, "name");
         }

         if (repositoryBase.getUrl() != null) {
            serializer.startTag(NAMESPACE, "url").text(repositoryBase.getUrl()).endTag(NAMESPACE, "url");
         }

         if (repositoryBase.getLayout() != null && !repositoryBase.getLayout().equals("default")) {
            serializer.startTag(NAMESPACE, "layout").text(repositoryBase.getLayout()).endTag(NAMESPACE, "layout");
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeRepositoryPolicy(RepositoryPolicy repositoryPolicy, String tagName, XmlSerializer serializer) throws IOException {
      if (repositoryPolicy != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (!repositoryPolicy.isEnabled()) {
            serializer.startTag(NAMESPACE, "enabled").text(String.valueOf(repositoryPolicy.isEnabled())).endTag(NAMESPACE, "enabled");
         }

         if (repositoryPolicy.getUpdatePolicy() != null) {
            serializer.startTag(NAMESPACE, "updatePolicy").text(repositoryPolicy.getUpdatePolicy()).endTag(NAMESPACE, "updatePolicy");
         }

         if (repositoryPolicy.getChecksumPolicy() != null) {
            serializer.startTag(NAMESPACE, "checksumPolicy").text(repositoryPolicy.getChecksumPolicy()).endTag(NAMESPACE, "checksumPolicy");
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeServer(Server server, String tagName, XmlSerializer serializer) throws IOException {
      if (server != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (server.getUsername() != null) {
            serializer.startTag(NAMESPACE, "username").text(server.getUsername()).endTag(NAMESPACE, "username");
         }

         if (server.getPassword() != null) {
            serializer.startTag(NAMESPACE, "password").text(server.getPassword()).endTag(NAMESPACE, "password");
         }

         if (server.getPrivateKey() != null) {
            serializer.startTag(NAMESPACE, "privateKey").text(server.getPrivateKey()).endTag(NAMESPACE, "privateKey");
         }

         if (server.getPassphrase() != null) {
            serializer.startTag(NAMESPACE, "passphrase").text(server.getPassphrase()).endTag(NAMESPACE, "passphrase");
         }

         if (server.getFilePermissions() != null) {
            serializer.startTag(NAMESPACE, "filePermissions").text(server.getFilePermissions()).endTag(NAMESPACE, "filePermissions");
         }

         if (server.getDirectoryPermissions() != null) {
            serializer.startTag(NAMESPACE, "directoryPermissions").text(server.getDirectoryPermissions()).endTag(NAMESPACE, "directoryPermissions");
         }

         if (server.getConfiguration() != null) {
            ((Xpp3Dom)server.getConfiguration()).writeToSerializer(NAMESPACE, serializer);
         }

         if (server.getId() != null && !server.getId().equals("default")) {
            serializer.startTag(NAMESPACE, "id").text(server.getId()).endTag(NAMESPACE, "id");
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeSettings(Settings settings, String tagName, XmlSerializer serializer) throws IOException {
      if (settings != null) {
         serializer.setPrefix("", "http://maven.apache.org/SETTINGS/1.0.0");
         serializer.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance");
         serializer.startTag(NAMESPACE, tagName);
         serializer.attribute("", "xsi:schemaLocation", "http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd");
         if (settings.getLocalRepository() != null) {
            serializer.startTag(NAMESPACE, "localRepository").text(settings.getLocalRepository()).endTag(NAMESPACE, "localRepository");
         }

         if (!settings.isInteractiveMode()) {
            serializer.startTag(NAMESPACE, "interactiveMode").text(String.valueOf(settings.isInteractiveMode())).endTag(NAMESPACE, "interactiveMode");
         }

         if (settings.isUsePluginRegistry()) {
            serializer.startTag(NAMESPACE, "usePluginRegistry").text(String.valueOf(settings.isUsePluginRegistry())).endTag(NAMESPACE, "usePluginRegistry");
         }

         if (settings.isOffline()) {
            serializer.startTag(NAMESPACE, "offline").text(String.valueOf(settings.isOffline())).endTag(NAMESPACE, "offline");
         }

         Iterator iter;
         if (settings.getProxies() != null && settings.getProxies().size() > 0) {
            serializer.startTag(NAMESPACE, "proxies");
            iter = settings.getProxies().iterator();

            while(iter.hasNext()) {
               Proxy o = (Proxy)iter.next();
               this.writeProxy(o, "proxy", serializer);
            }

            serializer.endTag(NAMESPACE, "proxies");
         }

         if (settings.getServers() != null && settings.getServers().size() > 0) {
            serializer.startTag(NAMESPACE, "servers");
            iter = settings.getServers().iterator();

            while(iter.hasNext()) {
               Server o = (Server)iter.next();
               this.writeServer(o, "server", serializer);
            }

            serializer.endTag(NAMESPACE, "servers");
         }

         if (settings.getMirrors() != null && settings.getMirrors().size() > 0) {
            serializer.startTag(NAMESPACE, "mirrors");
            iter = settings.getMirrors().iterator();

            while(iter.hasNext()) {
               Mirror o = (Mirror)iter.next();
               this.writeMirror(o, "mirror", serializer);
            }

            serializer.endTag(NAMESPACE, "mirrors");
         }

         if (settings.getProfiles() != null && settings.getProfiles().size() > 0) {
            serializer.startTag(NAMESPACE, "profiles");
            iter = settings.getProfiles().iterator();

            while(iter.hasNext()) {
               Profile o = (Profile)iter.next();
               this.writeProfile(o, "profile", serializer);
            }

            serializer.endTag(NAMESPACE, "profiles");
         }

         String pluginGroup;
         if (settings.getActiveProfiles() != null && settings.getActiveProfiles().size() > 0) {
            serializer.startTag(NAMESPACE, "activeProfiles");
            iter = settings.getActiveProfiles().iterator();

            while(iter.hasNext()) {
               pluginGroup = (String)iter.next();
               serializer.startTag(NAMESPACE, "activeProfile").text(pluginGroup).endTag(NAMESPACE, "activeProfile");
            }

            serializer.endTag(NAMESPACE, "activeProfiles");
         }

         if (settings.getPluginGroups() != null && settings.getPluginGroups().size() > 0) {
            serializer.startTag(NAMESPACE, "pluginGroups");
            iter = settings.getPluginGroups().iterator();

            while(iter.hasNext()) {
               pluginGroup = (String)iter.next();
               serializer.startTag(NAMESPACE, "pluginGroup").text(pluginGroup).endTag(NAMESPACE, "pluginGroup");
            }

            serializer.endTag(NAMESPACE, "pluginGroups");
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeTrackableBase(TrackableBase trackableBase, String tagName, XmlSerializer serializer) throws IOException {
      if (trackableBase != null) {
         serializer.startTag(NAMESPACE, tagName);
         serializer.endTag(NAMESPACE, tagName);
      }

   }
}
