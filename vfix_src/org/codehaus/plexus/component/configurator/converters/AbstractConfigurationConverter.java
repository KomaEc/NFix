package org.codehaus.plexus.component.configurator.converters;

import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.util.StringUtils;

public abstract class AbstractConfigurationConverter implements ConfigurationConverter {
   private static final String IMPLEMENTATION = "implementation";

   protected Class getClassForImplementationHint(Class type, PlexusConfiguration configuration, ClassLoader classLoader) throws ComponentConfigurationException {
      Class retValue = type;
      String implementation = configuration.getAttribute("implementation", (String)null);
      if (implementation != null) {
         try {
            retValue = classLoader.loadClass(implementation);
         } catch (ClassNotFoundException var8) {
            String msg = "Class name which was explicitly given in configuration using 'implementation' attribute: '" + implementation + "' cannot be loaded";
            throw new ComponentConfigurationException(msg, var8);
         }
      }

      return retValue;
   }

   protected Class loadClass(String classname, ClassLoader classLoader) throws ComponentConfigurationException {
      try {
         Class retValue = classLoader.loadClass(classname);
         return retValue;
      } catch (ClassNotFoundException var5) {
         throw new ComponentConfigurationException("Error loading class '" + classname + "'", var5);
      }
   }

   protected Object instantiateObject(String classname, ClassLoader classLoader) throws ComponentConfigurationException {
      Class clazz = this.loadClass(classname, classLoader);
      return this.instantiateObject(clazz);
   }

   protected Object instantiateObject(Class clazz) throws ComponentConfigurationException {
      try {
         Object retValue = clazz.newInstance();
         return retValue;
      } catch (IllegalAccessException var4) {
         throw new ComponentConfigurationException("Class '" + clazz.getName() + "' cannot be instantiated", var4);
      } catch (InstantiationException var5) {
         throw new ComponentConfigurationException("Class '" + clazz.getName() + "' cannot be instantiated", var5);
      }
   }

   protected String fromXML(String elementName) {
      return StringUtils.lowercaseFirstLetter(StringUtils.removeAndHump(elementName, "-"));
   }

   protected String toXML(String fieldName) {
      return StringUtils.addAndDeHump(fieldName);
   }

   protected Object fromExpression(PlexusConfiguration configuration, ExpressionEvaluator expressionEvaluator, Class type) throws ComponentConfigurationException {
      Object v = this.fromExpression(configuration, expressionEvaluator);
      if (v != null && !type.isAssignableFrom(v.getClass())) {
         String msg = "Cannot assign configuration entry '" + configuration.getName() + "' to '" + type + "' from '" + configuration.getValue((String)null) + "', which is of type " + v.getClass();
         throw new ComponentConfigurationException(configuration, msg);
      } else {
         return v;
      }
   }

   protected Object fromExpression(PlexusConfiguration configuration, ExpressionEvaluator expressionEvaluator) throws ComponentConfigurationException {
      Object v = null;
      String value = configuration.getValue((String)null);
      String msg;
      if (value != null && value.length() > 0) {
         try {
            v = expressionEvaluator.evaluate(value);
         } catch (ExpressionEvaluationException var8) {
            msg = "Error evaluating the expression '" + value + "' for configuration value '" + configuration.getName() + "'";
            throw new ComponentConfigurationException(configuration, msg, var8);
         }
      }

      if (v == null) {
         value = configuration.getAttribute("default-value", (String)null);
         if (value != null && value.length() > 0) {
            try {
               v = expressionEvaluator.evaluate(value);
            } catch (ExpressionEvaluationException var7) {
               msg = "Error evaluating the expression '" + value + "' for configuration value '" + configuration.getName() + "'";
               throw new ComponentConfigurationException(configuration, msg, var7);
            }
         }
      }

      return v;
   }

   public Object fromConfiguration(ConverterLookup converterLookup, PlexusConfiguration configuration, Class type, Class baseType, ClassLoader classLoader, ExpressionEvaluator expressionEvaluator) throws ComponentConfigurationException {
      return this.fromConfiguration(converterLookup, configuration, type, baseType, classLoader, expressionEvaluator, (ConfigurationListener)null);
   }

   // $FF: synthetic method
   public abstract Object fromConfiguration(ConverterLookup var1, PlexusConfiguration var2, Class var3, Class var4, ClassLoader var5, ExpressionEvaluator var6, ConfigurationListener var7) throws ComponentConfigurationException;

   // $FF: synthetic method
   public abstract boolean canConvert(Class var1);
}
