package org.codehaus.groovy.ast;

import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;

public class ConstructorNode extends MethodNode {
   public ConstructorNode(int modifiers, Statement code) {
      this(modifiers, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, code);
   }

   public ConstructorNode(int modifiers, Parameter[] parameters, ClassNode[] exceptions, Statement code) {
      super("<init>", modifiers, ClassHelper.VOID_TYPE, parameters, exceptions, code);
      VariableScope scope = new VariableScope();

      for(int i = 0; i < parameters.length; ++i) {
         scope.putDeclaredVariable(parameters[i]);
      }

      this.setVariableScope(scope);
   }

   public boolean firstStatementIsSpecialConstructorCall() {
      Statement code = this.getFirstStatement();
      if (code != null && code instanceof ExpressionStatement) {
         Expression expression = ((ExpressionStatement)code).getExpression();
         if (!(expression instanceof ConstructorCallExpression)) {
            return false;
         } else {
            ConstructorCallExpression cce = (ConstructorCallExpression)expression;
            return cce.isSpecialCall();
         }
      } else {
         return false;
      }
   }
}
