package org.codehaus.plexus.component.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.composition.CompositionException;
import org.codehaus.plexus.component.composition.CompositionResolver;
import org.codehaus.plexus.component.repository.exception.ComponentImplementationNotFoundException;
import org.codehaus.plexus.component.repository.exception.ComponentRepositoryException;
import org.codehaus.plexus.component.repository.io.PlexusTools;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.codehaus.plexus.logging.AbstractLogEnabled;

public class DefaultComponentRepository extends AbstractLogEnabled implements ComponentRepository {
   private static String COMPONENTS = "components";
   private static String COMPONENT = "component";
   private PlexusConfiguration configuration;
   private Map componentDescriptorMaps = new HashMap();
   private Map componentDescriptors = new HashMap();
   private CompositionResolver compositionResolver;
   private ClassRealm classRealm;

   protected PlexusConfiguration getConfiguration() {
      return this.configuration;
   }

   public boolean hasComponent(String role) {
      return this.componentDescriptors.containsKey(role);
   }

   public boolean hasComponent(String role, String roleHint) {
      return this.componentDescriptors.containsKey(role + roleHint);
   }

   public Map getComponentDescriptorMap(String role) {
      return (Map)this.componentDescriptorMaps.get(role);
   }

   public ComponentDescriptor getComponentDescriptor(String key) {
      return (ComponentDescriptor)this.componentDescriptors.get(key);
   }

   public void setClassRealm(ClassRealm classRealm) {
      this.classRealm = classRealm;
   }

   public void configure(PlexusConfiguration configuration) {
      this.configuration = configuration;
   }

   public void initialize() throws ComponentRepositoryException {
      this.initializeComponentDescriptors();
   }

   public void initializeComponentDescriptors() throws ComponentRepositoryException {
      this.initializeComponentDescriptorsFromUserConfiguration();
   }

   private void initializeComponentDescriptorsFromUserConfiguration() throws ComponentRepositoryException {
      PlexusConfiguration[] componentConfigurations = this.configuration.getChild(COMPONENTS).getChildren(COMPONENT);

      for(int i = 0; i < componentConfigurations.length; ++i) {
         this.addComponentDescriptor(componentConfigurations[i]);
      }

   }

   public void addComponentDescriptor(PlexusConfiguration configuration) throws ComponentRepositoryException {
      ComponentDescriptor componentDescriptor = null;

      try {
         componentDescriptor = PlexusTools.buildComponentDescriptor(configuration);
      } catch (PlexusConfigurationException var4) {
         throw new ComponentRepositoryException("Cannot unmarshall component descriptor:", var4);
      }

      this.addComponentDescriptor(componentDescriptor);
   }

   public void addComponentDescriptor(ComponentDescriptor componentDescriptor) throws ComponentRepositoryException {
      try {
         this.validateComponentDescriptor(componentDescriptor);
      } catch (ComponentImplementationNotFoundException var7) {
         throw new ComponentRepositoryException("Component descriptor validation failed: ", var7);
      }

      String role = componentDescriptor.getRole();
      String roleHint = componentDescriptor.getRoleHint();
      if (roleHint != null) {
         if (this.componentDescriptors.containsKey(role)) {
            ComponentDescriptor desc = (ComponentDescriptor)this.componentDescriptors.get(role);
            if (desc.getRoleHint() == null) {
               String message = "Component descriptor " + componentDescriptor.getHumanReadableKey() + " has a hint, but there are other implementations that don't";
               throw new ComponentRepositoryException(message);
            }
         }

         Map map = (Map)this.componentDescriptorMaps.get(role);
         if (map == null) {
            map = new HashMap();
            this.componentDescriptorMaps.put(role, map);
         }

         ((Map)map).put(roleHint, componentDescriptor);
      } else {
         String message;
         if (this.componentDescriptorMaps.containsKey(role)) {
            message = "Component descriptor " + componentDescriptor.getHumanReadableKey() + " has no hint, but there are other implementations that do";
            throw new ComponentRepositoryException(message);
         }

         if (this.componentDescriptors.containsKey(role) && !this.componentDescriptors.get(role).equals(componentDescriptor)) {
            message = "Component role " + role + " is already in the repository and different to attempted addition of " + componentDescriptor.getHumanReadableKey();
            throw new ComponentRepositoryException(message);
         }
      }

      try {
         this.compositionResolver.addComponentDescriptor(componentDescriptor);
      } catch (CompositionException var6) {
         throw new ComponentRepositoryException(var6.getMessage(), var6);
      }

      this.componentDescriptors.put(componentDescriptor.getComponentKey(), componentDescriptor);
      if (!this.componentDescriptors.containsKey(role)) {
         this.componentDescriptors.put(role, componentDescriptor);
      }

   }

   public void validateComponentDescriptor(ComponentDescriptor componentDescriptor) throws ComponentImplementationNotFoundException {
   }

   public List getComponentDependencies(ComponentDescriptor componentDescriptor) {
      return this.compositionResolver.getRequirements(componentDescriptor.getComponentKey());
   }
}
