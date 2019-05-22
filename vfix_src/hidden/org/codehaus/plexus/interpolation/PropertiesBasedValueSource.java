package hidden.org.codehaus.plexus.interpolation;

import java.util.Properties;

public class PropertiesBasedValueSource implements ValueSource {
   private final Properties properties;

   public PropertiesBasedValueSource(Properties properties) {
      this.properties = properties;
   }

   public Object getValue(String expression) {
      return this.properties == null ? null : this.properties.getProperty(expression);
   }
}
