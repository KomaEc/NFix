package org.apache.maven.project.injection;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.project.ModelUtils;

public class DefaultModelDefaultsInjector implements ModelDefaultsInjector {
   public void injectDefaults(Model model) {
      this.injectDependencyDefaults(model.getDependencies(), model.getDependencyManagement());
      if (model.getBuild() != null) {
         this.injectPluginDefaults(model.getBuild(), model.getBuild().getPluginManagement());
      }

   }

   private void injectPluginDefaults(Build build, PluginManagement pluginManagement) {
      if (pluginManagement != null) {
         List buildPlugins = build.getPlugins();
         if (buildPlugins != null && !buildPlugins.isEmpty()) {
            Map pmPlugins = pluginManagement.getPluginsAsMap();
            if (pmPlugins != null && !pmPlugins.isEmpty()) {
               Iterator it = buildPlugins.iterator();

               while(it.hasNext()) {
                  Plugin buildPlugin = (Plugin)it.next();
                  Plugin pmPlugin = (Plugin)pmPlugins.get(buildPlugin.getKey());
                  if (pmPlugin != null) {
                     this.mergePluginWithDefaults(buildPlugin, pmPlugin);
                  }
               }
            }
         }

      }
   }

   public void mergePluginWithDefaults(Plugin plugin, Plugin def) {
      ModelUtils.mergePluginDefinitions(plugin, def, false);
   }

   private void injectDependencyDefaults(List dependencies, DependencyManagement dependencyManagement) {
      if (dependencyManagement != null) {
         Map depsMap = new TreeMap();
         Iterator it = dependencies.iterator();

         while(it.hasNext()) {
            Dependency dep = (Dependency)it.next();
            depsMap.put(dep.getManagementKey(), dep);
         }

         List managedDependencies = dependencyManagement.getDependencies();
         Iterator it = managedDependencies.iterator();

         while(it.hasNext()) {
            Dependency def = (Dependency)it.next();
            String key = def.getManagementKey();
            Dependency dep = (Dependency)depsMap.get(key);
            if (dep != null) {
               this.mergeDependencyWithDefaults(dep, def);
            }
         }
      }

   }

   private void mergeDependencyWithDefaults(Dependency dep, Dependency def) {
      if (dep.getScope() == null && def.getScope() != null) {
         dep.setScope(def.getScope());
         dep.setSystemPath(def.getSystemPath());
      }

      if (dep.getVersion() == null && def.getVersion() != null) {
         dep.setVersion(def.getVersion());
      }

      if (dep.getClassifier() == null && def.getClassifier() != null) {
         dep.setClassifier(def.getClassifier());
      }

      if (dep.getType() == null && def.getType() != null) {
         dep.setType(def.getType());
      }

      List exclusions = dep.getExclusions();
      if (exclusions == null || exclusions.isEmpty()) {
         dep.setExclusions(def.getExclusions());
      }

   }
}
