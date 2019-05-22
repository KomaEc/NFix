package org.codehaus.plexus.component.composition;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.ComponentRequirement;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.util.ReflectionUtils;

public class FieldComponentComposer extends AbstractComponentComposer {
   // $FF: synthetic field
   static Class class$java$util$Map;
   // $FF: synthetic field
   static Class class$java$util$List;
   // $FF: synthetic field
   static Class class$java$util$Set;
   // $FF: synthetic field
   static Class class$java$lang$Object;

   public List assembleComponent(Object component, ComponentDescriptor componentDescriptor, PlexusContainer container) throws CompositionException {
      List retValue = new LinkedList();
      List requirements = componentDescriptor.getRequirements();
      Iterator i = requirements.iterator();

      while(i.hasNext()) {
         ComponentRequirement requirement = (ComponentRequirement)i.next();
         Field field = this.findMatchingField(component, componentDescriptor, requirement, container);
         if (!field.isAccessible()) {
            field.setAccessible(true);
         }

         List descriptors = this.assignRequirementToField(component, field, container, requirement);
         retValue.addAll(descriptors);
      }

      return retValue;
   }

   private List assignRequirementToField(Object component, Field field, PlexusContainer container, ComponentRequirement requirement) throws CompositionException {
      try {
         String role = requirement.getRole();
         List dependencies;
         Object retValue;
         if (field.getType().isArray()) {
            dependencies = container.lookupList(role);
            Object[] array = (Object[])Array.newInstance(field.getType(), dependencies.size());
            retValue = container.getComponentDescriptorList(role);
            field.set(component, dependencies.toArray(array));
         } else {
            Map dependencies;
            if ((class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map).isAssignableFrom(field.getType())) {
               dependencies = container.lookupMap(role);
               retValue = container.getComponentDescriptorList(role);
               field.set(component, dependencies);
            } else if ((class$java$util$List == null ? (class$java$util$List = class$("java.util.List")) : class$java$util$List).isAssignableFrom(field.getType())) {
               dependencies = container.lookupList(role);
               retValue = container.getComponentDescriptorList(role);
               field.set(component, dependencies);
            } else if ((class$java$util$Set == null ? (class$java$util$Set = class$("java.util.Set")) : class$java$util$Set).isAssignableFrom(field.getType())) {
               dependencies = container.lookupMap(role);
               retValue = container.getComponentDescriptorList(role);
               field.set(component, dependencies.entrySet());
            } else {
               String key = requirement.getRequirementKey();
               Object dependency = container.lookup(key);
               ComponentDescriptor componentDescriptor = container.getComponentDescriptor(key);
               retValue = new ArrayList(1);
               ((List)retValue).add(componentDescriptor);
               field.set(component, dependency);
            }
         }

         return (List)retValue;
      } catch (IllegalArgumentException var10) {
         throw new CompositionException("Composition failed for the field " + field.getName() + " " + "in object of type " + component.getClass().getName(), var10);
      } catch (IllegalAccessException var11) {
         throw new CompositionException("Composition failed for the field " + field.getName() + " " + "in object of type " + component.getClass().getName(), var11);
      } catch (ComponentLookupException var12) {
         throw new CompositionException("Composition failed of field " + field.getName() + " " + "in object of type " + component.getClass().getName() + " because the requirement " + requirement + " was missing", var12);
      }
   }

   protected Field findMatchingField(Object component, ComponentDescriptor componentDescriptor, ComponentRequirement requirement, PlexusContainer container) throws CompositionException {
      String fieldName = requirement.getFieldName();
      Field field = null;
      if (fieldName != null) {
         field = this.getFieldByName(component, fieldName, componentDescriptor);
      } else {
         Class fieldClass = null;

         try {
            if (container != null) {
               fieldClass = container.getContainerRealm().loadClass(requirement.getRole());
            } else {
               fieldClass = Thread.currentThread().getContextClassLoader().loadClass(requirement.getRole());
            }
         } catch (ClassNotFoundException var10) {
            StringBuffer msg = new StringBuffer("Component Composition failed for component: ");
            msg.append(componentDescriptor.getHumanReadableKey());
            msg.append(": Requirement class: '");
            msg.append(requirement.getRole());
            msg.append("' not found.");
            throw new CompositionException(msg.toString(), var10);
         }

         field = this.getFieldByType(component, fieldClass, componentDescriptor);
      }

      return field;
   }

   protected Field getFieldByName(Object component, String fieldName, ComponentDescriptor componentDescriptor) throws CompositionException {
      Field field = ReflectionUtils.getFieldByNameIncludingSuperclasses(fieldName, component.getClass());
      if (field == null) {
         StringBuffer msg = new StringBuffer("Component Composition failed. No field of name: '");
         msg.append(fieldName);
         msg.append("' exists in component: ");
         msg.append(componentDescriptor.getHumanReadableKey());
         throw new CompositionException(msg.toString());
      } else {
         return field;
      }
   }

   protected Field getFieldByTypeIncludingSuperclasses(Class componentClass, Class type, ComponentDescriptor componentDescriptor) throws CompositionException {
      List fields = this.getFieldsByTypeIncludingSuperclasses(componentClass, type, componentDescriptor);
      if (fields.size() == 0) {
         return null;
      } else if (fields.size() == 1) {
         return (Field)fields.get(0);
      } else {
         throw new CompositionException("There are several fields of type '" + type + "', " + "use 'field-name' to select the correct field.");
      }
   }

   protected List getFieldsByTypeIncludingSuperclasses(Class componentClass, Class type, ComponentDescriptor componentDescriptor) throws CompositionException {
      Class arrayType = Array.newInstance(type, 0).getClass();
      Field[] fields = componentClass.getDeclaredFields();
      List foundFields = new ArrayList();

      for(int i = 0; i < fields.length; ++i) {
         Class fieldType = fields[i].getType();
         if (fieldType.isAssignableFrom(type) || fieldType.isAssignableFrom(arrayType)) {
            foundFields.add(fields[i]);
         }
      }

      if (componentClass.getSuperclass() != (class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object)) {
         List superFields = this.getFieldsByTypeIncludingSuperclasses(componentClass.getSuperclass(), type, componentDescriptor);
         foundFields.addAll(superFields);
      }

      return foundFields;
   }

   protected Field getFieldByType(Object component, Class type, ComponentDescriptor componentDescriptor) throws CompositionException {
      Field field = this.getFieldByTypeIncludingSuperclasses(component.getClass(), type, componentDescriptor);
      if (field == null) {
         StringBuffer msg = new StringBuffer("Component composition failed. No field of type: '");
         msg.append(type);
         msg.append("' exists in class '");
         msg.append(component.getClass().getName());
         msg.append("'.");
         if (componentDescriptor != null) {
            msg.append(" Component: ");
            msg.append(componentDescriptor.getHumanReadableKey());
         }

         throw new CompositionException(msg.toString());
      } else {
         return field;
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
