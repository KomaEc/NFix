package org.codehaus.plexus.component.configurator.converters.composite;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.AbstractConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.ConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.util.StringUtils;

public class CollectionConverter extends AbstractConfigurationConverter {
   // $FF: synthetic field
   static Class class$java$util$Collection;
   // $FF: synthetic field
   static Class class$java$util$Map;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$util$List;
   // $FF: synthetic field
   static Class class$java$util$Set;

   public boolean canConvert(Class type) {
      return (class$java$util$Collection == null ? (class$java$util$Collection = class$("java.util.Collection")) : class$java$util$Collection).isAssignableFrom(type) && !(class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map).isAssignableFrom(type);
   }

   public Object fromConfiguration(ConverterLookup converterLookup, PlexusConfiguration configuration, Class type, Class baseType, ClassLoader classLoader, ExpressionEvaluator expressionEvaluator, ConfigurationListener listener) throws ComponentConfigurationException {
      Object retValue = this.fromExpression(configuration, expressionEvaluator, type);
      if (retValue != null) {
         return retValue;
      } else {
         Class implementation = this.getClassForImplementationHint((Class)null, configuration, classLoader);
         int i;
         String configEntry;
         if (implementation != null) {
            retValue = this.instantiateObject(implementation);
         } else {
            i = type.getModifiers();
            if (Modifier.isAbstract(i)) {
               retValue = this.getDefaultCollection(type);
            } else {
               try {
                  retValue = type.newInstance();
               } catch (IllegalAccessException var20) {
                  configEntry = "An attempt to convert configuration entry " + configuration.getName() + "' into " + type + " object failed: " + var20.getMessage();
                  throw new ComponentConfigurationException(configEntry, var20);
               } catch (InstantiationException var21) {
                  configEntry = "An attempt to convert configuration entry " + configuration.getName() + "' into " + type + " object failed: " + var21.getMessage();
                  throw new ComponentConfigurationException(configEntry, var21);
               }
            }
         }

         for(i = 0; i < configuration.getChildCount(); ++i) {
            PlexusConfiguration c = configuration.getChild(i);
            configEntry = c.getName();
            String name = this.fromXML(configEntry);
            Class childType = this.getClassForImplementationHint((Class)null, c, classLoader);
            if (childType == null && name.indexOf(46) > 0) {
               try {
                  childType = classLoader.loadClass(name);
               } catch (ClassNotFoundException var19) {
               }
            }

            if (childType == null) {
               String baseTypeName = baseType.getName();
               int lastDot = baseTypeName.lastIndexOf(46);
               String className;
               if (lastDot == -1) {
                  className = name;
               } else {
                  String basePackage = baseTypeName.substring(0, lastDot);
                  className = basePackage + "." + StringUtils.capitalizeFirstLetter(name);
               }

               try {
                  childType = classLoader.loadClass(className);
               } catch (ClassNotFoundException var22) {
                  if (c.getChildCount() != 0) {
                     throw new ComponentConfigurationException("Error loading class '" + className + "'", var22);
                  }

                  childType = class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String;
               }
            }

            ConfigurationConverter converter = converterLookup.lookupConverterForType(childType);
            Object object = converter.fromConfiguration(converterLookup, c, childType, baseType, classLoader, expressionEvaluator, listener);
            Collection collection = (Collection)retValue;
            collection.add(object);
         }

         return retValue;
      }
   }

   protected Collection getDefaultCollection(Class collectionType) {
      Collection retValue = null;
      if ((class$java$util$List == null ? (class$java$util$List = class$("java.util.List")) : class$java$util$List).isAssignableFrom(collectionType)) {
         retValue = new ArrayList();
      } else if ((class$java$util$Set == null ? (class$java$util$Set = class$("java.util.Set")) : class$java$util$Set).isAssignableFrom(collectionType)) {
         retValue = new HashSet();
      }

      return (Collection)retValue;
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
