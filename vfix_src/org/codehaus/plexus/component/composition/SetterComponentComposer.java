package org.codehaus.plexus.component.composition;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.Statement;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.ComponentRequirement;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

public class SetterComponentComposer extends AbstractComponentComposer {
   // $FF: synthetic field
   static Class class$java$util$Map;
   // $FF: synthetic field
   static Class class$java$util$List;
   // $FF: synthetic field
   static Class class$java$util$Set;

   public List assembleComponent(Object component, ComponentDescriptor descriptor, PlexusContainer container) throws CompositionException, UndefinedComponentComposerException {
      List requirements = descriptor.getRequirements();
      BeanInfo beanInfo = null;

      try {
         beanInfo = Introspector.getBeanInfo(component.getClass());
      } catch (IntrospectionException var12) {
         this.reportErrorFailedToIntrospect(descriptor);
      }

      List retValue = new LinkedList();
      PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
      Iterator i = requirements.iterator();

      while(i.hasNext()) {
         ComponentRequirement requirement = (ComponentRequirement)i.next();
         PropertyDescriptor propertyDescriptor = this.findMatchingPropertyDescriptor(requirement, propertyDescriptors);
         if (propertyDescriptor != null) {
            List descriptors = this.setProperty(component, descriptor, requirement, propertyDescriptor, container);
            retValue.addAll(descriptors);
         } else {
            this.reportErrorNoSuchProperty(descriptor, requirement);
         }
      }

      return retValue;
   }

   private List setProperty(Object component, ComponentDescriptor descriptor, ComponentRequirement requirement, PropertyDescriptor propertyDescriptor, PlexusContainer container) throws CompositionException {
      List retValue = null;
      Method writeMethod = propertyDescriptor.getWriteMethod();
      String role = requirement.getRole();
      Object[] params = new Object[1];
      Class propertyType = propertyDescriptor.getPropertyType();

      try {
         Map dependencies;
         if (propertyType.isArray()) {
            dependencies = container.lookupMap(role);
            Object[] array = (Object[])Array.newInstance(propertyType, dependencies.size());
            retValue = container.getComponentDescriptorList(role);
            params[0] = dependencies.entrySet().toArray(array);
         } else if ((class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map).isAssignableFrom(propertyType)) {
            dependencies = container.lookupMap(role);
            retValue = container.getComponentDescriptorList(role);
            params[0] = dependencies;
         } else if ((class$java$util$List == null ? (class$java$util$List = class$("java.util.List")) : class$java$util$List).isAssignableFrom(propertyType)) {
            retValue = container.getComponentDescriptorList(role);
            params[0] = container.lookupList(role);
         } else if ((class$java$util$Set == null ? (class$java$util$Set = class$("java.util.Set")) : class$java$util$Set).isAssignableFrom(propertyType)) {
            dependencies = container.lookupMap(role);
            retValue = container.getComponentDescriptorList(role);
            params[0] = dependencies.entrySet();
         } else {
            String key = requirement.getRequirementKey();
            Object dependency = container.lookup(key);
            ComponentDescriptor componentDescriptor = container.getComponentDescriptor(key);
            retValue = new ArrayList(1);
            ((List)retValue).add(componentDescriptor);
            params[0] = dependency;
         }
      } catch (ComponentLookupException var15) {
         this.reportErrorCannotLookupRequiredComponent(descriptor, requirement, var15);
      }

      Statement statement = new Statement(component, writeMethod.getName(), params);

      try {
         statement.execute();
      } catch (Exception var14) {
         this.reportErrorCannotAssignRequiredComponent(descriptor, requirement, var14);
      }

      return (List)retValue;
   }

   protected PropertyDescriptor findMatchingPropertyDescriptor(ComponentRequirement requirement, PropertyDescriptor[] propertyDescriptors) {
      PropertyDescriptor retValue = null;
      String property = requirement.getFieldName();
      if (property != null) {
         retValue = this.getPropertyDescriptorByName(property, propertyDescriptors);
      } else {
         String role = requirement.getRole();
         retValue = this.getPropertyDescriptorByType(role, propertyDescriptors);
      }

      return retValue;
   }

   protected PropertyDescriptor getPropertyDescriptorByName(String name, PropertyDescriptor[] propertyDescriptors) {
      PropertyDescriptor retValue = null;

      for(int i = 0; i < propertyDescriptors.length; ++i) {
         PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
         if (name.equals(propertyDescriptor.getName())) {
            retValue = propertyDescriptor;
            break;
         }
      }

      return retValue;
   }

   protected PropertyDescriptor getPropertyDescriptorByType(String type, PropertyDescriptor[] propertyDescriptors) {
      PropertyDescriptor retValue = null;

      for(int i = 0; i < propertyDescriptors.length; ++i) {
         PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
         if (propertyDescriptor.getPropertyType().toString().indexOf(type) > 0) {
            retValue = propertyDescriptor;
            break;
         }
      }

      return retValue;
   }

   private void reportErrorNoSuchProperty(ComponentDescriptor descriptor, ComponentRequirement requirement) throws CompositionException {
      String causeDescriprion = "Failed to assign requirment using Java Bean introspection mechanism. No matching property was found in bean class";
      String msg = this.getErrorMessage(descriptor, requirement, "Failed to assign requirment using Java Bean introspection mechanism. No matching property was found in bean class");
      throw new CompositionException(msg);
   }

   private void reportErrorCannotAssignRequiredComponent(ComponentDescriptor descriptor, ComponentRequirement requirement, Exception e) throws CompositionException {
      String causeDescriprion = "Failed to assign requirment using Java Bean introspection mechanism. ";
      String msg = this.getErrorMessage(descriptor, requirement, "Failed to assign requirment using Java Bean introspection mechanism. ");
      throw new CompositionException(msg);
   }

   private void reportErrorCannotLookupRequiredComponent(ComponentDescriptor descriptor, ComponentRequirement requirement, Throwable cause) throws CompositionException {
      String causeDescriprion = "Failed to lookup required component.";
      String msg = this.getErrorMessage(descriptor, requirement, "Failed to lookup required component.");
      throw new CompositionException(msg, cause);
   }

   private void reportErrorFailedToIntrospect(ComponentDescriptor descriptor) throws CompositionException {
      String msg = this.getErrorMessage(descriptor, (ComponentRequirement)null, (String)null);
      throw new CompositionException(msg);
   }

   private String getErrorMessage(ComponentDescriptor descriptor, ComponentRequirement requirement, String causeDescription) {
      StringBuffer msg = new StringBuffer("Component composition failed.");
      msg.append("  Failed to resolve requirement for component of role: '");
      msg.append(descriptor.getRole());
      msg.append("'");
      if (descriptor.getRoleHint() != null) {
         msg.append(" and role-hint: '");
         msg.append(descriptor.getRoleHint());
         msg.append("'. ");
      }

      if (requirement != null) {
         msg.append("Failing requirement: " + requirement.getHumanReadableKey());
      }

      if (causeDescription != null) {
         msg.append(causeDescription);
      }

      return msg.toString();
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
