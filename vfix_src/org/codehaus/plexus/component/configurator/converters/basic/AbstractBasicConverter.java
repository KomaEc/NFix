package org.codehaus.plexus.component.configurator.converters.basic;

import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.AbstractConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public abstract class AbstractBasicConverter extends AbstractConfigurationConverter {
   protected abstract Object fromString(String var1) throws ComponentConfigurationException;

   public Object fromConfiguration(ConverterLookup converterLookup, PlexusConfiguration configuration, Class type, Class baseType, ClassLoader classLoader, ExpressionEvaluator expressionEvaluator, ConfigurationListener listener) throws ComponentConfigurationException {
      if (configuration.getChildCount() > 0) {
         throw new ComponentConfigurationException("When configuring a basic element the configuration cannot contain any child elements. Configuration element '" + configuration.getName() + "'.");
      } else {
         Object retValue = this.fromExpression(configuration, expressionEvaluator);
         if (retValue instanceof String) {
            retValue = this.fromString((String)retValue);
         }

         return retValue;
      }
   }
}
