package org.codehaus.plexus.component.composition;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.MapOrientedComponent;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.ComponentRequirement;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.util.StringUtils;

public class MapOrientedComponentComposer extends AbstractComponentComposer {
   private static final String SINGLE_MAPPING_TYPE = "single";
   private static final String MAP_MAPPING_TYPE = "map";
   private static final String SET_MAPPING_TYPE = "set";
   private static final String DEFAULT_MAPPING_TYPE = "single";
   // $FF: synthetic field
   static Class class$org$codehaus$plexus$component$MapOrientedComponent;

   public List assembleComponent(Object component, ComponentDescriptor componentDescriptor, PlexusContainer container) throws CompositionException {
      if (!(component instanceof MapOrientedComponent)) {
         throw new CompositionException("Cannot compose component: " + component.getClass().getName() + "; it does not implement " + (class$org$codehaus$plexus$component$MapOrientedComponent == null ? (class$org$codehaus$plexus$component$MapOrientedComponent = class$("org.codehaus.plexus.component.MapOrientedComponent")) : class$org$codehaus$plexus$component$MapOrientedComponent).getName());
      } else {
         List retValue = new LinkedList();
         List requirements = componentDescriptor.getRequirements();
         Iterator i = requirements.iterator();

         while(i.hasNext()) {
            ComponentRequirement requirement = (ComponentRequirement)i.next();
            List descriptors = this.addRequirement((MapOrientedComponent)component, container, requirement);
            retValue.addAll(descriptors);
         }

         return retValue;
      }
   }

   private List addRequirement(MapOrientedComponent component, PlexusContainer container, ComponentRequirement requirement) throws CompositionException {
      try {
         String role = requirement.getRole();
         String hint = requirement.getRoleHint();
         String mappingType = requirement.getFieldMappingType();
         Object value = null;
         String key;
         ComponentDescriptor componentDescriptor;
         List retValue;
         if (StringUtils.isNotEmpty(hint)) {
            key = requirement.getRequirementKey();
            value = container.lookup(key);
            componentDescriptor = container.getComponentDescriptor(key);
            retValue = Collections.singletonList(componentDescriptor);
         } else if ("single".equals(mappingType)) {
            key = requirement.getRequirementKey();
            value = container.lookup(key);
            componentDescriptor = container.getComponentDescriptor(key);
            retValue = Collections.singletonList(componentDescriptor);
         } else if ("map".equals(mappingType)) {
            value = container.lookupMap(role);
            retValue = container.getComponentDescriptorList(role);
         } else if ("set".equals(mappingType)) {
            value = new HashSet(container.lookupList(role));
            retValue = container.getComponentDescriptorList(role);
         } else {
            key = requirement.getRequirementKey();
            value = container.lookup(key);
            componentDescriptor = container.getComponentDescriptor(key);
            retValue = Collections.singletonList(componentDescriptor);
         }

         component.addComponentRequirement(requirement, value);
         return retValue;
      } catch (ComponentLookupException var11) {
         throw new CompositionException("Composition failed in object of type " + component.getClass().getName() + " because the requirement " + requirement + " was missing", var11);
      } catch (ComponentConfigurationException var12) {
         throw new CompositionException("Composition failed in object of type " + component.getClass().getName() + " because the requirement " + requirement + " cannot be set on the component.", var12);
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
