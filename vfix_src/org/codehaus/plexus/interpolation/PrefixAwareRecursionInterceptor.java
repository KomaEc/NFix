package org.codehaus.plexus.interpolation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import org.codehaus.plexus.interpolation.util.ValueSourceUtils;

public class PrefixAwareRecursionInterceptor implements RecursionInterceptor {
   public static final String DEFAULT_START_TOKEN = "\\$\\{";
   public static final String DEFAULT_END_TOKEN = "\\}";
   private Stack nakedExpressions = new Stack();
   private final Collection possiblePrefixes;
   private String endToken = "\\}";
   private String startToken = "\\$\\{";
   private boolean watchUnprefixedExpressions = true;

   public PrefixAwareRecursionInterceptor(Collection possiblePrefixes, boolean watchUnprefixedExpressions) {
      this.possiblePrefixes = possiblePrefixes;
      this.watchUnprefixedExpressions = watchUnprefixedExpressions;
   }

   public PrefixAwareRecursionInterceptor(Collection possiblePrefixes) {
      this.possiblePrefixes = possiblePrefixes;
   }

   public boolean hasRecursiveExpression(String expression) {
      String realExpr = ValueSourceUtils.trimPrefix(expression, this.possiblePrefixes, this.watchUnprefixedExpressions);
      return realExpr != null ? this.nakedExpressions.contains(realExpr) : false;
   }

   public void expressionResolutionFinished(String expression) {
      this.nakedExpressions.pop();
   }

   public void expressionResolutionStarted(String expression) {
      String realExpr = ValueSourceUtils.trimPrefix(expression, this.possiblePrefixes, this.watchUnprefixedExpressions);
      this.nakedExpressions.push(realExpr);
   }

   public List getExpressionCycle(String expression) {
      String expr = ValueSourceUtils.trimPrefix(expression, this.possiblePrefixes, this.watchUnprefixedExpressions);
      if (expr == null) {
         return Collections.EMPTY_LIST;
      } else {
         int idx = this.nakedExpressions.indexOf(expression);
         return idx < 0 ? Collections.EMPTY_LIST : this.nakedExpressions.subList(idx, this.nakedExpressions.size());
      }
   }
}
