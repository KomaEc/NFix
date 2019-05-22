package org.codehaus.plexus.component.configurator.converters.composite;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.AbstractConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.ConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.util.StringUtils;

public class ArrayConverter extends AbstractConfigurationConverter {
   // $FF: synthetic field
   static Class class$java$util$List;
   // $FF: synthetic field
   static Class class$java$util$Set;

   public boolean canConvert(Class type) {
      return type.isArray();
   }

   public Object fromConfiguration(ConverterLookup converterLookup, PlexusConfiguration configuration, Class type, Class baseType, ClassLoader classLoader, ExpressionEvaluator expressionEvaluator, ConfigurationListener listener) throws ComponentConfigurationException {
      Object retValue = this.fromExpression(configuration, expressionEvaluator, type);
      if (retValue != null) {
         return retValue;
      } else {
         List values = new ArrayList();

         for(int i = 0; i < configuration.getChildCount(); ++i) {
            PlexusConfiguration c = configuration.getChild(i);
            String configEntry = c.getName();
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
               } catch (ClassNotFoundException var20) {
               }
            }

            if (childType == null) {
               childType = type.getComponentType();
            }

            ConfigurationConverter converter = converterLookup.lookupConverterForType(childType);
            Object object = converter.fromConfiguration(converterLookup, c, childType, baseType, classLoader, expressionEvaluator, listener);
            values.add(object);
         }

         return values.toArray((Object[])Array.newInstance(type.getComponentType(), 0));
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
