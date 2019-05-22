package org.codehaus.plexus.component.configurator;

import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.configurator.converters.composite.ObjectWithFieldsConverter;
import org.codehaus.plexus.component.configurator.converters.special.ClassRealmConverter;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public class BasicComponentConfigurator extends AbstractComponentConfigurator {
   public void configureComponent(Object component, PlexusConfiguration configuration, ExpressionEvaluator expressionEvaluator, ClassRealm containerRealm, ConfigurationListener listener) throws ComponentConfigurationException {
      super.converterLookup.registerConverter(new ClassRealmConverter(containerRealm));
      ObjectWithFieldsConverter converter = new ObjectWithFieldsConverter();
      converter.processConfiguration(super.converterLookup, component, containerRealm.getClassLoader(), configuration, expressionEvaluator, listener);
   }
}
