package org.codehaus.plexus.component.configurator;

import java.util.Map;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.MapOrientedComponent;
import org.codehaus.plexus.component.configurator.converters.composite.MapConverter;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public class MapOrientedComponentConfigurator extends AbstractComponentConfigurator {
   // $FF: synthetic field
   static Class class$org$codehaus$plexus$component$MapOrientedComponent;

   public void configureComponent(Object component, PlexusConfiguration configuration, ExpressionEvaluator expressionEvaluator, ClassRealm containerRealm, ConfigurationListener listener) throws ComponentConfigurationException {
      if (!(component instanceof MapOrientedComponent)) {
         throw new ComponentConfigurationException("This configurator can only process implementations of " + (class$org$codehaus$plexus$component$MapOrientedComponent == null ? (class$org$codehaus$plexus$component$MapOrientedComponent = class$("org.codehaus.plexus.component.MapOrientedComponent")) : class$org$codehaus$plexus$component$MapOrientedComponent).getName());
      } else {
         MapConverter converter = new MapConverter();
         Map context = (Map)converter.fromConfiguration(super.converterLookup, configuration, (Class)null, (Class)null, containerRealm.getClassLoader(), expressionEvaluator, listener);
         ((MapOrientedComponent)component).setComponentConfiguration(context);
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
