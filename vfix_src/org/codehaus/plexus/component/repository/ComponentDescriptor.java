package org.codehaus.plexus.component.repository;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public class ComponentDescriptor {
   private String alias = null;
   private String role = null;
   private String roleHint = null;
   private String implementation = null;
   private String version = null;
   private String componentType = null;
   private PlexusConfiguration configuration = null;
   private String instantiationStrategy = null;
   private String lifecycleHandler = null;
   private String componentProfile = null;
   private List requirements;
   private String componentFactory;
   private String componentComposer;
   private String componentConfigurator;
   private String description;
   private boolean isolatedRealm;
   private List dependencies;
   private ComponentSetDescriptor componentSetDescriptor;

   public String getComponentKey() {
      return this.getRoleHint() != null ? this.getRole() + this.getRoleHint() : this.getRole();
   }

   public String getHumanReadableKey() {
      StringBuffer key = new StringBuffer();
      key.append("role: '" + this.role + "'");
      key.append(", implementation: '" + this.implementation + "'");
      if (this.roleHint != null) {
         key.append(", role hint: '" + this.roleHint + "'");
      }

      if (this.alias != null) {
         key.append(", alias: '" + this.alias + "'");
      }

      return key.toString();
   }

   public String getAlias() {
      return this.alias;
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }

   public String getRole() {
      return this.role;
   }

   public void setRole(String role) {
      this.role = role;
   }

   public String getRoleHint() {
      return this.roleHint;
   }

   public void setRoleHint(String roleHint) {
      this.roleHint = roleHint;
   }

   public String getImplementation() {
      return this.implementation;
   }

   public void setImplementation(String implementation) {
      this.implementation = implementation;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getComponentType() {
      return this.componentType;
   }

   public void setComponentType(String componentType) {
      this.componentType = componentType;
   }

   public String getInstantiationStrategy() {
      return this.instantiationStrategy;
   }

   public PlexusConfiguration getConfiguration() {
      return this.configuration;
   }

   public void setConfiguration(PlexusConfiguration configuration) {
      this.configuration = configuration;
   }

   public boolean hasConfiguration() {
      return this.configuration != null;
   }

   public String getLifecycleHandler() {
      return this.lifecycleHandler;
   }

   public void setLifecycleHandler(String lifecycleHandler) {
      this.lifecycleHandler = lifecycleHandler;
   }

   public String getComponentProfile() {
      return this.componentProfile;
   }

   public void setComponentProfile(String componentProfile) {
      this.componentProfile = componentProfile;
   }

   public void addRequirement(ComponentRequirement requirement) {
      this.getRequirements().add(requirement);
   }

   public List getRequirements() {
      if (this.requirements == null) {
         this.requirements = new ArrayList();
      }

      return this.requirements;
   }

   public String getComponentFactory() {
      return this.componentFactory;
   }

   public void setComponentFactory(String componentFactory) {
      this.componentFactory = componentFactory;
   }

   public String getComponentComposer() {
      return this.componentComposer;
   }

   public void setComponentComposer(String componentComposer) {
      this.componentComposer = componentComposer;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setInstantiationStrategy(String instantiationStrategy) {
      this.instantiationStrategy = instantiationStrategy;
   }

   public boolean isIsolatedRealm() {
      return this.isolatedRealm;
   }

   public void setComponentSetDescriptor(ComponentSetDescriptor componentSetDescriptor) {
      this.componentSetDescriptor = componentSetDescriptor;
   }

   public ComponentSetDescriptor getComponentSetDescriptor() {
      return this.componentSetDescriptor;
   }

   public void setIsolatedRealm(boolean isolatedRealm) {
      this.isolatedRealm = isolatedRealm;
   }

   public List getDependencies() {
      return this.dependencies;
   }

   public String getComponentConfigurator() {
      return this.componentConfigurator;
   }

   public void setComponentConfigurator(String componentConfigurator) {
      this.componentConfigurator = componentConfigurator;
   }

   public boolean equals(Object other) {
      if (!(other instanceof ComponentDescriptor)) {
         return false;
      } else {
         ComponentDescriptor otherDescriptor = (ComponentDescriptor)other;
         boolean isEqual = true;
         String role = this.getRole();
         String otherRole = otherDescriptor.getRole();
         isEqual = isEqual && (role == otherRole || role.equals(otherRole));
         String roleHint = this.getRoleHint();
         String otherRoleHint = otherDescriptor.getRoleHint();
         isEqual = isEqual && (roleHint == otherRoleHint || roleHint.equals(otherRoleHint));
         return isEqual;
      }
   }

   public String toString() {
      return this.getClass().getName() + " [role: '" + this.getRole() + "', hint: '" + this.getRoleHint() + "']";
   }

   public int hashCode() {
      int result = this.getRole().hashCode() + 1;
      String hint = this.getRoleHint();
      if (hint != null) {
         result += hint.hashCode();
      }

      return result;
   }
}
