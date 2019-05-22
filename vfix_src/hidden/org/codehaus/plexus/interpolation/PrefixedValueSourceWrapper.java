package hidden.org.codehaus.plexus.interpolation;

import hidden.org.codehaus.plexus.interpolation.util.ValueSourceUtils;
import java.util.Collections;
import java.util.List;

public class PrefixedValueSourceWrapper implements FeedbackEnabledValueSource, QueryEnabledValueSource {
   private final ValueSource valueSource;
   private final List possiblePrefixes;
   private boolean allowUnprefixedExpressions;
   private String lastExpression;

   public PrefixedValueSourceWrapper(ValueSource valueSource, String prefix) {
      this.valueSource = valueSource;
      this.possiblePrefixes = Collections.singletonList(prefix);
   }

   public PrefixedValueSourceWrapper(ValueSource valueSource, String prefix, boolean allowUnprefixedExpressions) {
      this.valueSource = valueSource;
      this.possiblePrefixes = Collections.singletonList(prefix);
      this.allowUnprefixedExpressions = allowUnprefixedExpressions;
   }

   public PrefixedValueSourceWrapper(ValueSource valueSource, List possiblePrefixes) {
      this.valueSource = valueSource;
      this.possiblePrefixes = possiblePrefixes;
   }

   public PrefixedValueSourceWrapper(ValueSource valueSource, List possiblePrefixes, boolean allowUnprefixedExpressions) {
      this.valueSource = valueSource;
      this.possiblePrefixes = possiblePrefixes;
      this.allowUnprefixedExpressions = allowUnprefixedExpressions;
   }

   public Object getValue(String expression) {
      this.lastExpression = ValueSourceUtils.trimPrefix(expression, this.possiblePrefixes, this.allowUnprefixedExpressions);
      return this.lastExpression == null ? null : this.valueSource.getValue(this.lastExpression);
   }

   public List getFeedback() {
      return this.valueSource instanceof FeedbackEnabledValueSource ? ((FeedbackEnabledValueSource)this.valueSource).getFeedback() : Collections.EMPTY_LIST;
   }

   public String getLastExpression() {
      return this.valueSource instanceof QueryEnabledValueSource ? ((QueryEnabledValueSource)this.valueSource).getLastExpression() : this.lastExpression;
   }

   public void clearFeedback() {
      if (this.valueSource instanceof FeedbackEnabledValueSource) {
         ((FeedbackEnabledValueSource)this.valueSource).clearFeedback();
      }

   }
}
