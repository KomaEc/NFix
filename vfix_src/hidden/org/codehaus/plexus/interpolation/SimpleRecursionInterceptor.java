package hidden.org.codehaus.plexus.interpolation;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class SimpleRecursionInterceptor implements RecursionInterceptor {
   private Stack expressions = new Stack();

   public void expressionResolutionFinished(String expression) {
      this.expressions.pop();
   }

   public void expressionResolutionStarted(String expression) {
      this.expressions.push(expression);
   }

   public boolean hasRecursiveExpression(String expression) {
      return this.expressions.contains(expression);
   }

   public List getExpressionCycle(String expression) {
      int idx = this.expressions.indexOf(expression);
      return idx < 0 ? Collections.EMPTY_LIST : this.expressions.subList(idx, this.expressions.size());
   }
}
