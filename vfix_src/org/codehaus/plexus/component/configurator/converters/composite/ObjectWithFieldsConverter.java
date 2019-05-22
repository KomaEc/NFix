package org.codehaus.plexus.component.configurator.converters.composite;

import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.AbstractConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.ComponentValueSetter;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public class ObjectWithFieldsConverter extends AbstractConfigurationConverter {
   // $FF: synthetic field
   static Class class$java$util$Dictionary;
   // $FF: synthetic field
   static Class class$java$util$Map;
   // $FF: synthetic field
   static Class class$java$util$Collection;

   public boolean canConvert(Class type) {
      boolean retValue = true;
      if ((class$java$util$Dictionary == null ? (class$java$util$Dictionary = class$("java.util.Dictionary")) : class$java$util$Dictionary).isAssignableFrom(type)) {
         retValue = false;
      } else if ((class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map).isAssignableFrom(type)) {
         retValue = false;
      } else if ((class$java$util$Collection == null ? (class$java$util$Collection = class$("java.util.Collection")) : class$java$util$Collection).isAssignableFrom(type)) {
         retValue = false;
      }

      return retValue;
   }

   public Object fromConfiguration(ConverterLookup converterLookup, PlexusConfiguration configuration, Class type, Class baseType, ClassLoader classLoader, ExpressionEvaluator expressionEvaluator, ConfigurationListener listener) throws ComponentConfigurationException {
      Object retValue = this.fromExpression(configuration, expressionEvaluator, type);
      if (retValue == null) {
         try {
            Class implementation = this.getClassForImplementationHint(type, configuration, classLoader);
            retValue = this.instantiateObject(implementation);
            this.processConfiguration(converterLookup, retValue, classLoader, configuration, expressionEvaluator, listener);
         } catch (ComponentConfigurationException var10) {
            if (var10.getFailedConfiguration() == null) {
               var10.setFailedConfiguration(configuration);
            }

            throw var10;
         }
      }

      return retValue;
   }

   public void processConfiguration(ConverterLookup converterLookup, Object object, ClassLoader classLoader, PlexusConfiguration configuration) throws ComponentConfigurationException {
      this.processConfiguration(converterLookup, object, classLoader, configuration, (ExpressionEvaluator)null);
   }

   public void processConfiguration(ConverterLookup converterLookup, Object object, ClassLoader classLoader, PlexusConfiguration configuration, ExpressionEvaluator expressionEvaluator) throws ComponentConfigurationException {
      this.processConfiguration(converterLookup, object, classLoader, configuration, expressionEvaluator, (ConfigurationListener)null);
   }

   public void processConfiguration(ConverterLookup converterLookup, Object object, ClassLoader classLoader, PlexusConfiguration configuration, ExpressionEvaluator expressionEvaluator, ConfigurationListener listener) throws ComponentConfigurationException {
      int items = configuration.getChildCount();

      for(int i = 0; i < items; ++i) {
         PlexusConfiguration childConfiguration = configuration.getChild(i);
         String elementName = childConfiguration.getName();
         ComponentValueSetter valueSetter = new ComponentValueSetter(this.fromXML(elementName), object, converterLookup, listener);
         valueSetter.configure(childConfiguration, classLoader, expressionEvaluator);
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
