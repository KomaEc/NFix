package hidden.org.codehaus.plexus.interpolation;

import java.util.List;

public class PrefixedObjectValueSource implements FeedbackEnabledValueSource, QueryEnabledValueSource {
   private final PrefixedValueSourceWrapper delegate;

   public PrefixedObjectValueSource(String prefix, Object root) {
      this.delegate = new PrefixedValueSourceWrapper(new ObjectBasedValueSource(root), prefix);
   }

   public PrefixedObjectValueSource(List possiblePrefixes, Object root, boolean allowUnprefixedExpressions) {
      this.delegate = new PrefixedValueSourceWrapper(new ObjectBasedValueSource(root), possiblePrefixes, allowUnprefixedExpressions);
   }

   public Object getValue(String expression) {
      return this.delegate.getValue(expression);
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
}
