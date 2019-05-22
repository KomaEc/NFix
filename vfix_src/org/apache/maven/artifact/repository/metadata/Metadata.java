package org.apache.maven.artifact.repository.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Metadata implements Serializable {
   private String groupId;
   private String artifactId;
   private String version;
   private Versioning versioning;
   private List<Plugin> plugins;
   private String modelEncoding = "UTF-8";

   public void addPlugin(Plugin plugin) {
      if (!(plugin instanceof Plugin)) {
         throw new ClassCastException("Metadata.addPlugins(plugin) parameter must be instanceof " + Plugin.class.getName());
      } else {
         this.getPlugins().add(plugin);
      }
   }

   public String getArtifactId() {
      return this.artifactId;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }

   public List<Plugin> getPlugins() {
      if (this.plugins == null) {
         this.plugins = new ArrayList();
      }

      return this.plugins;
   }

   public String getVersion() {
      return this.version;
   }

   public Versioning getVersioning() {
      return this.versioning;
   }

   public void removePlugin(Plugin plugin) {
      if (!(plugin instanceof Plugin)) {
         throw new ClassCastException("Metadata.removePlugins(plugin) parameter must be instanceof " + Plugin.class.getName());
      } else {
         this.getPlugins().remove(plugin);
      }
   }

   public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public void setPlugins(List<Plugin> plugins) {
      this.plugins = plugins;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public void setVersioning(Versioning versioning) {
      this.versioning = versioning;
   }

   public boolean merge(Metadata sourceMetadata) {
      boolean changed = false;
      Iterator i = sourceMetadata.getPlugins().iterator();

      while(i.hasNext()) {
         Plugin plugin = (Plugin)i.next();
         boolean found = false;
         Iterator it = this.getPlugins().iterator();

         while(it.hasNext() && !found) {
            Plugin preExisting = (Plugin)it.next();
            if (preExisting.getPrefix().equals(plugin.getPrefix())) {
               found = true;
            }
         }

         if (!found) {
            Plugin mappedPlugin = new Plugin();
            mappedPlugin.setArtifactId(plugin.getArtifactId());
            mappedPlugin.setPrefix(plugin.getPrefix());
            mappedPlugin.setName(plugin.getName());
            this.addPlugin(mappedPlugin);
            changed = true;
         }
      }

      Versioning versioning = sourceMetadata.getVersioning();
      if (versioning != null) {
         Versioning v = this.getVersioning();
         if (v == null) {
            v = new Versioning();
            this.setVersioning(v);
            changed = true;
         }

         Iterator i = versioning.getVersions().iterator();

         while(i.hasNext()) {
            String version = (String)i.next();
            if (!v.getVersions().contains(version)) {
               changed = true;
               v.getVersions().add(version);
            }
         }

         if ("null".equals(versioning.getLastUpdated())) {
            versioning.setLastUpdated((String)null);
         }

         if ("null".equals(v.getLastUpdated())) {
            v.setLastUpdated((String)null);
         }

         if (versioning.getLastUpdated() == null || versioning.getLastUpdated().length() == 0) {
            versioning.setLastUpdated(v.getLastUpdated());
         }

         if (v.getLastUpdated() == null || v.getLastUpdated().length() == 0 || versioning.getLastUpdated().compareTo(v.getLastUpdated()) >= 0) {
            changed = true;
            v.setLastUpdated(versioning.getLastUpdated());
            if (versioning.getRelease() != null) {
               changed = true;
               v.setRelease(versioning.getRelease());
            }

            if (versioning.getLatest() != null) {
               changed = true;
               v.setLatest(versioning.getLatest());
            }

            Snapshot s = v.getSnapshot();
            Snapshot snapshot = versioning.getSnapshot();
            if (snapshot != null) {
               if (s == null) {
                  s = new Snapshot();
                  v.setSnapshot(s);
                  changed = true;
               }

               label65: {
                  if (s.getTimestamp() == null) {
                     if (snapshot.getTimestamp() == null) {
                        break label65;
                     }
                  } else if (s.getTimestamp().equals(snapshot.getTimestamp())) {
                     break label65;
                  }

                  s.setTimestamp(snapshot.getTimestamp());
                  changed = true;
               }

               if (s.getBuildNumber() != snapshot.getBuildNumber()) {
                  s.setBuildNumber(snapshot.getBuildNumber());
                  changed = true;
               }

               if (s.isLocalCopy() != snapshot.isLocalCopy()) {
                  s.setLocalCopy(snapshot.isLocalCopy());
                  changed = true;
               }
            }
         }
      }

      return changed;
   }
}
