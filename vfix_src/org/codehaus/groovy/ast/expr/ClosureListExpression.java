package org.codehaus.groovy.ast.expr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.VariableScope;

public class ClosureListExpression extends ListExpression {
   private VariableScope scope;

   public ClosureListExpression(List<Expression> expressions) {
      super(expressions);
      this.scope = new VariableScope();
   }

   public ClosureListExpression() {
      this(new ArrayList(3));
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitClosureListExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new ClosureListExpression(this.transformExpressions(this.getExpressions(), transformer));
      ret.setSourcePosition(this);
      return ret;
   }

   public void setVariableScope(VariableScope scope) {
      this.scope = scope;
   }

   public VariableScope getVariableScope() {
      return this.scope;
   }

   public String getText() {
      StringBuffer buffer = new StringBuffer("(");
      boolean first = true;

      Expression expression;
      for(Iterator i$ = this.getExpressions().iterator(); i$.hasNext(); buffer.append(expression.getText())) {
         expression = (Expression)i$.next();
         if (first) {
            first = false;
         } else {
            buffer.append("; ");
         }
      }

      buffer.append(")");
      return buffer.toString();
   }
}
