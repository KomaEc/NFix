package org.apache.maven.profiles.io.xpp3;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import org.apache.maven.profiles.Activation;
import org.apache.maven.profiles.ActivationFile;
import org.apache.maven.profiles.ActivationOS;
import org.apache.maven.profiles.ActivationProperty;
import org.apache.maven.profiles.Profile;
import org.apache.maven.profiles.ProfilesRoot;
import org.apache.maven.profiles.Repository;
import org.apache.maven.profiles.RepositoryBase;
import org.apache.maven.profiles.RepositoryPolicy;
import org.codehaus.plexus.util.xml.pull.MXSerializer;
import org.codehaus.plexus.util.xml.pull.XmlSerializer;

public class ProfilesXpp3Writer {
   private static final String NAMESPACE = null;

   public void write(Writer writer, ProfilesRoot profilesRoot) throws IOException {
      XmlSerializer serializer = new MXSerializer();
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
      serializer.setOutput(writer);
      serializer.startDocument(profilesRoot.getModelEncoding(), (Boolean)null);
      this.writeProfilesRoot(profilesRoot, "profilesXml", serializer);
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

   private void writeProfile(Profile profile, String tagName, XmlSerializer serializer) throws IOException {
      if (profile != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (profile.getId() != null) {
            serializer.startTag(NAMESPACE, "id").text(profile.getId()).endTag(NAMESPACE, "id");
         }

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

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeProfilesRoot(ProfilesRoot profilesRoot, String tagName, XmlSerializer serializer) throws IOException {
      if (profilesRoot != null) {
         serializer.setPrefix("", "http://maven.apache.org/PROFILES/1.0.0");
         serializer.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance");
         serializer.startTag(NAMESPACE, tagName);
         serializer.attribute("", "xsi:schemaLocation", "http://maven.apache.org/PROFILES/1.0.0 http://maven.apache.org/xsd/profiles-1.0.0.xsd");
         Iterator iter;
         if (profilesRoot.getProfiles() != null && profilesRoot.getProfiles().size() > 0) {
            serializer.startTag(NAMESPACE, "profiles");
            iter = profilesRoot.getProfiles().iterator();

            while(iter.hasNext()) {
               Profile o = (Profile)iter.next();
               this.writeProfile(o, "profile", serializer);
            }

            serializer.endTag(NAMESPACE, "profiles");
         }

         if (profilesRoot.getActiveProfiles() != null && profilesRoot.getActiveProfiles().size() > 0) {
            serializer.startTag(NAMESPACE, "activeProfiles");
            iter = profilesRoot.getActiveProfiles().iterator();

            while(iter.hasNext()) {
               String activeProfile = (String)iter.next();
               serializer.startTag(NAMESPACE, "activeProfile").text(activeProfile).endTag(NAMESPACE, "activeProfile");
            }

            serializer.endTag(NAMESPACE, "activeProfiles");
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
}
