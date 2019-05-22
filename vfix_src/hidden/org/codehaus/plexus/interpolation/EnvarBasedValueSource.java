package hidden.org.codehaus.plexus.interpolation;

import hidden.org.codehaus.plexus.interpolation.os.OperatingSystemUtils;
import java.io.IOException;
import java.util.Properties;

public class EnvarBasedValueSource implements ValueSource {
   private Properties envars;
   private final boolean caseSensitive;

   public EnvarBasedValueSource() throws IOException {
      this(true);
   }

   public EnvarBasedValueSource(boolean caseSensitive) throws IOException {
      this.caseSensitive = caseSensitive;
      this.envars = OperatingSystemUtils.getSystemEnvVars(caseSensitive);
   }

   public Object getValue(String expression) {
      String expr = expression;
      if (expression.startsWith("env.")) {
         expr = expression.substring("env.".length());
      }

      if (!this.caseSensitive) {
         expr = expr.toUpperCase();
      }

      return this.envars.getProperty(expr);
   }
}
