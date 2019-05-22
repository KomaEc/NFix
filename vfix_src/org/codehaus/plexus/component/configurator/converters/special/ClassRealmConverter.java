package org.codehaus.plexus.component.configurator.converters.special;

import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.AbstractConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public class ClassRealmConverter extends AbstractConfigurationConverter {
   public static final String ROLE;
   private ClassRealm classRealm;
   // $FF: synthetic field
   static Class class$org$codehaus$plexus$component$configurator$converters$ConfigurationConverter;
   // $FF: synthetic field
   static Class class$org$codehaus$classworlds$ClassRealm;

   public ClassRealmConverter(ClassRealm classRealm) {
      this.setClassRealm(classRealm);
   }

   public void setClassRealm(ClassRealm classRealm) {
      this.classRealm = classRealm;
   }

   public boolean canConvert(Class type) {
      return (class$org$codehaus$classworlds$ClassRealm == null ? (class$org$codehaus$classworlds$ClassRealm = class$("org.codehaus.classworlds.ClassRealm")) : class$org$codehaus$classworlds$ClassRealm).isAssignableFrom(type);
   }

   public Object fromConfiguration(ConverterLookup converterLookup, PlexusConfiguration configuration, Class type, Class baseType, ClassLoader classLoader, ExpressionEvaluator expressionEvaluator, ConfigurationListener listener) throws ComponentConfigurationException {
      Object retValue = this.fromExpression(configuration, expressionEvaluator, type);
      return retValue != null ? retValue : this.classRealm;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      ROLE = (class$org$codehaus$plexus$component$configurator$converters$ConfigurationConverter == null ? (class$org$codehaus$plexus$component$configurator$converters$ConfigurationConverter = class$("org.codehaus.plexus.component.configurator.converters.ConfigurationConverter")) : class$org$codehaus$plexus$component$configurator$converters$ConfigurationConverter).getName();
   }
}
