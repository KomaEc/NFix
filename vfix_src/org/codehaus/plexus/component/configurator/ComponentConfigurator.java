package org.codehaus.plexus.component.configurator;

import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public interface ComponentConfigurator {
   String ROLE = (null.class$org$codehaus$plexus$component$configurator$ComponentConfigurator == null ? (null.class$org$codehaus$plexus$component$configurator$ComponentConfigurator = null.class$("org.codehaus.plexus.component.configurator.ComponentConfigurator")) : null.class$org$codehaus$plexus$component$configurator$ComponentConfigurator).getName();

   void configureComponent(Object var1, PlexusConfiguration var2, ClassRealm var3) throws ComponentConfigurationException;

   void configureComponent(Object var1, PlexusConfiguration var2, ExpressionEvaluator var3, ClassRealm var4) throws ComponentConfigurationException;

   void configureComponent(Object var1, PlexusConfiguration var2, ExpressionEvaluator var3, ClassRealm var4, ConfigurationListener var5) throws ComponentConfigurationException;
}
