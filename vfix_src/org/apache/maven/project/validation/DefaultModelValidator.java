package org.apache.maven.project.validation;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.apache.maven.model.Resource;
import org.codehaus.plexus.util.StringUtils;

public class DefaultModelValidator implements ModelValidator {
   private static final String ID_REGEX = "[A-Za-z0-9_\\-.]+";

   public ModelValidationResult validate(Model model) {
      ModelValidationResult result = new ModelValidationResult();
      this.validateStringNotEmpty("modelVersion", result, model.getModelVersion());
      this.validateId("groupId", result, model.getGroupId());
      this.validateId("artifactId", result, model.getArtifactId());
      this.validateStringNotEmpty("packaging", result, model.getPackaging());
      if (!model.getModules().isEmpty() && !"pom".equals(model.getPackaging())) {
         result.addMessage("Packaging '" + model.getPackaging() + "' is invalid. Aggregator projects " + "require 'pom' as packaging.");
      }

      Parent parent = model.getParent();
      if (parent != null && parent.getGroupId().equals(model.getGroupId()) && parent.getArtifactId().equals(model.getArtifactId())) {
         result.addMessage("The parent element cannot have the same ID as the project.");
      }

      this.validateStringNotEmpty("version", result, model.getVersion());
      Iterator it = model.getDependencies().iterator();

      while(it.hasNext()) {
         Dependency d = (Dependency)it.next();
         this.validateId("dependencies.dependency.artifactId", result, d.getArtifactId());
         this.validateId("dependencies.dependency.groupId", result, d.getGroupId());
         this.validateStringNotEmpty("dependencies.dependency.type", result, d.getType(), d.getManagementKey());
         this.validateStringNotEmpty("dependencies.dependency.version", result, d.getVersion(), d.getManagementKey());
         if ("system".equals(d.getScope())) {
            String systemPath = d.getSystemPath();
            if (StringUtils.isEmpty(systemPath)) {
               result.addMessage("For dependency " + d + ": system-scoped dependency must specify systemPath.");
            } else if (!(new File(systemPath)).isAbsolute()) {
               result.addMessage("For dependency " + d + ": system-scoped dependency must " + "specify an absolute path systemPath.");
            }
         } else if (StringUtils.isNotEmpty(d.getSystemPath())) {
            result.addMessage("For dependency " + d + ": only dependency with system scope can specify systemPath.");
         }
      }

      DependencyManagement mgmt = model.getDependencyManagement();
      if (mgmt != null) {
         Iterator it = mgmt.getDependencies().iterator();

         while(it.hasNext()) {
            Dependency d = (Dependency)it.next();
            this.validateSubElementStringNotEmpty(d, "dependencyManagement.dependencies.dependency.artifactId", result, d.getArtifactId());
            this.validateSubElementStringNotEmpty(d, "dependencyManagement.dependencies.dependency.groupId", result, d.getGroupId());
            if ("system".equals(d.getScope())) {
               String systemPath = d.getSystemPath();
               if (StringUtils.isEmpty(systemPath)) {
                  result.addMessage("For managed dependency " + d + ": system-scoped dependency must specify systemPath.");
               } else if (!(new File(systemPath)).isAbsolute()) {
                  result.addMessage("For managed dependency " + d + ": system-scoped dependency must " + "specify an absolute path systemPath.");
               }
            } else if (StringUtils.isNotEmpty(d.getSystemPath())) {
               result.addMessage("For managed dependency " + d + ": only dependency with system scope can specify systemPath.");
            } else if ("import".equals(d.getScope())) {
               if (!"pom".equals(d.getType())) {
                  result.addMessage("For managed dependency " + d + ": dependencies with import scope must have type 'pom'.");
               } else if (d.getClassifier() != null) {
                  result.addMessage("For managed dependency " + d + ": dependencies with import scope must NOT have a classifier.");
               }
            }
         }
      }

      Build build = model.getBuild();
      if (build != null) {
         Iterator it = build.getPlugins().iterator();

         while(it.hasNext()) {
            Plugin p = (Plugin)it.next();
            this.validateStringNotEmpty("build.plugins.plugin.artifactId", result, p.getArtifactId());
            this.validateStringNotEmpty("build.plugins.plugin.groupId", result, p.getGroupId());
         }

         it = build.getResources().iterator();

         Resource r;
         while(it.hasNext()) {
            r = (Resource)it.next();
            this.validateStringNotEmpty("build.resources.resource.directory", result, r.getDirectory());
         }

         it = build.getTestResources().iterator();

         while(it.hasNext()) {
            r = (Resource)it.next();
            this.validateStringNotEmpty("build.testResources.testResource.directory", result, r.getDirectory());
         }
      }

      Reporting reporting = model.getReporting();
      if (reporting != null) {
         Iterator it = reporting.getPlugins().iterator();

         while(it.hasNext()) {
            ReportPlugin p = (ReportPlugin)it.next();
            this.validateStringNotEmpty("reporting.plugins.plugin.artifactId", result, p.getArtifactId());
            this.validateStringNotEmpty("reporting.plugins.plugin.groupId", result, p.getGroupId());
         }
      }

      this.validateRepositories(result, model.getRepositories(), "repositories.repository");
      this.validateRepositories(result, model.getPluginRepositories(), "pluginRepositories.pluginRepository");
      this.forcePluginExecutionIdCollision(model, result);
      return result;
   }

   private boolean validateId(String fieldName, ModelValidationResult result, String id) {
      if (!this.validateStringNotEmpty(fieldName, result, id)) {
         return false;
      } else {
         boolean match = id.matches("[A-Za-z0-9_\\-.]+");
         if (!match) {
            result.addMessage("'" + fieldName + "' with value '" + id + "' does not match a valid id pattern.");
         }

         return match;
      }
   }

   private void validateRepositories(ModelValidationResult result, List repositories, String prefix) {
      Iterator it = repositories.iterator();

      while(it.hasNext()) {
         Repository repository = (Repository)it.next();
         this.validateStringNotEmpty(prefix + ".id", result, repository.getId());
         this.validateStringNotEmpty(prefix + ".url", result, repository.getUrl());
      }

   }

   private void forcePluginExecutionIdCollision(Model model, ModelValidationResult result) {
      Build build = model.getBuild();
      if (build != null) {
         List plugins = build.getPlugins();
         if (plugins != null) {
            Iterator it = plugins.iterator();

            while(it.hasNext()) {
               Plugin plugin = (Plugin)it.next();

               try {
                  plugin.getExecutionsAsMap();
               } catch (IllegalStateException var8) {
                  result.addMessage(var8.getMessage());
               }
            }
         }
      }

   }

   private boolean validateStringNotEmpty(String fieldName, ModelValidationResult result, String string) {
      return this.validateStringNotEmpty(fieldName, result, string, (String)null);
   }

   private boolean validateStringNotEmpty(String fieldName, ModelValidationResult result, String string, String sourceHint) {
      if (!this.validateNotNull(fieldName, result, string, sourceHint)) {
         return false;
      } else if (string.length() > 0) {
         return true;
      } else {
         if (sourceHint != null) {
            result.addMessage("'" + fieldName + "' is missing for " + sourceHint);
         } else {
            result.addMessage("'" + fieldName + "' is missing.");
         }

         return false;
      }
   }

   private boolean validateSubElementStringNotEmpty(Object subElementInstance, String fieldName, ModelValidationResult result, String string) {
      if (!this.validateSubElementNotNull(subElementInstance, fieldName, result, string)) {
         return false;
      } else if (string.length() > 0) {
         return true;
      } else {
         result.addMessage("In " + subElementInstance + ":\n\n       -> '" + fieldName + "' is missing.");
         return false;
      }
   }

   private boolean validateNotNull(String fieldName, ModelValidationResult result, Object object, String sourceHint) {
      if (object != null) {
         return true;
      } else {
         if (sourceHint != null) {
            result.addMessage("'" + fieldName + "' is missing for " + sourceHint);
         } else {
            result.addMessage("'" + fieldName + "' is missing.");
         }

         return false;
      }
   }

   private boolean validateSubElementNotNull(Object subElementInstance, String fieldName, ModelValidationResult result, Object object) {
      if (object != null) {
         return true;
      } else {
         result.addMessage("In " + subElementInstance + ":\n\n       -> '" + fieldName + "' is missing.");
         return false;
      }
   }
}
