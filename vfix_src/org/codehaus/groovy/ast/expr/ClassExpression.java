package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class ClassExpression extends Expression {
   public ClassExpression(ClassNode type) {
      super.setType(type);
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitClassExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      return this;
   }

   public String getText() {
      return this.getType().getName();
   }

   public String toString() {
      return super.toString() + "[type: " + this.getType().getName() + "]";
   }
}
