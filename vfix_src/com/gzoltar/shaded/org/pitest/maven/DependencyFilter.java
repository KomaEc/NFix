package com.gzoltar.shaded.org.pitest.maven;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.mutationtest.config.PluginServices;
import com.gzoltar.shaded.org.pitest.plugin.ClientClasspathPlugin;
import com.gzoltar.shaded.org.pitest.util.PitError;
import java.util.HashSet;
import java.util.Set;
import org.apache.maven.artifact.Artifact;

public class DependencyFilter implements Predicate<Artifact> {
   private final Set<DependencyFilter.GroupIdPair> groups = new HashSet();

   public DependencyFilter(PluginServices plugins) {
      Iterable<? extends ClientClasspathPlugin> runtimePlugins = plugins.findClientClasspathPlugins();
      FCollection.mapTo(runtimePlugins, artifactToPair(), this.groups);
   }

   private static F<ClientClasspathPlugin, DependencyFilter.GroupIdPair> artifactToPair() {
      return new F<ClientClasspathPlugin, DependencyFilter.GroupIdPair>() {
         public DependencyFilter.GroupIdPair apply(ClientClasspathPlugin a) {
            Package p = a.getClass().getPackage();
            DependencyFilter.GroupIdPair g = new DependencyFilter.GroupIdPair(p.getImplementationVendor(), p.getImplementationTitle());
            if (g.id == null) {
               this.reportBadPlugin("title", a);
            }

            if (g.group == null) {
               this.reportBadPlugin("vendor", a);
            }

            return g;
         }

         private void reportBadPlugin(String missingProperty, ClientClasspathPlugin a) {
            Class<?> clss = a.getClass();
            throw new PitError("No implementation " + missingProperty + " in manifest of plugin jar for " + clss + " in " + clss.getProtectionDomain().getCodeSource().getLocation());
         }
      };
   }

   public Boolean apply(Artifact a) {
      DependencyFilter.GroupIdPair p = new DependencyFilter.GroupIdPair(a.getGroupId(), a.getArtifactId());
      return this.groups.contains(p);
   }

   private static class GroupIdPair {
      private final String group;
      private final String id;

      GroupIdPair(String group, String id) {
         this.group = group;
         this.id = id;
      }

      public int hashCode() {
         int prime = true;
         int result = 1;
         int result = 31 * result + (this.group == null ? 0 : this.group.hashCode());
         result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
         return result;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (obj == null) {
            return false;
         } else if (this.getClass() != obj.getClass()) {
            return false;
         } else {
            DependencyFilter.GroupIdPair other = (DependencyFilter.GroupIdPair)obj;
            if (this.group == null) {
               if (other.group != null) {
                  return false;
               }
            } else if (!this.group.equals(other.group)) {
               return false;
            }

            if (this.id == null) {
               if (other.id != null) {
                  return false;
               }
            } else if (!this.id.equals(other.id)) {
               return false;
            }

            return true;
         }
      }
   }
}
