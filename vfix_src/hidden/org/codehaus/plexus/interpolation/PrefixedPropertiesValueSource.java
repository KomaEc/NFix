package hidden.org.codehaus.plexus.interpolation;

import java.util.List;
import java.util.Properties;

public class PrefixedPropertiesValueSource implements QueryEnabledValueSource {
   private final PrefixedValueSourceWrapper delegate;

   public PrefixedPropertiesValueSource(String prefix, Properties properties) {
      this.delegate = new PrefixedValueSourceWrapper(new PropertiesBasedValueSource(properties), prefix);
   }

   public PrefixedPropertiesValueSource(List possiblePrefixes, Properties properties, boolean allowUnprefixedExpressions) {
      this.delegate = new PrefixedValueSourceWrapper(new PropertiesBasedValueSource(properties), possiblePrefixes, allowUnprefixedExpressions);
   }

   public void clearFeedback() {
      this.delegate.clearFeedback();
   }

   public List getFeedback() {
      return this.delegate.getFeedback();
   }

   public String getLastExpression() {
      return this.delegate.getLastExpression();
   }

   public Object getValue(String expression) {
      return this.delegate.getValue(expression);
   }
}
