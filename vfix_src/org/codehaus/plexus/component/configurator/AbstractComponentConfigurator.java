package org.codehaus.plexus.component.configurator;

import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.converters.lookup.DefaultConverterLookup;
import org.codehaus.plexus.component.configurator.expression.DefaultExpressionEvaluator;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public abstract class AbstractComponentConfigurator implements ComponentConfigurator {
   protected ConverterLookup converterLookup = new DefaultConverterLookup();

   public void configureComponent(Object component, PlexusConfiguration configuration, ClassRealm containerRealm) throws ComponentConfigurationException {
      this.configureComponent(component, configuration, new DefaultExpressionEvaluator(), containerRealm);
   }

   public void configureComponent(Object component, PlexusConfiguration configuration, ExpressionEvaluator expressionEvaluator, ClassRealm containerRealm) throws ComponentConfigurationException {
      this.configureComponent(component, configuration, expressionEvaluator, containerRealm, (ConfigurationListener)null);
   }

   public void configureComponent(Object component, PlexusConfiguration configuration, ExpressionEvaluator expressionEvaluator, ClassRealm containerRealm, ConfigurationListener listener) throws ComponentConfigurationException {
      this.configureComponent(component, configuration, expressionEvaluator, containerRealm);
   }
}
