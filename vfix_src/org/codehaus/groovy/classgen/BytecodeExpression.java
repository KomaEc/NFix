package org.codehaus.groovy.classgen;

import groovyjarjarasm.asm.MethodVisitor;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ExpressionTransformer;

public abstract class BytecodeExpression extends Expression {
   public static final BytecodeExpression NOP = new BytecodeExpression() {
      public void visit(MethodVisitor visitor) {
      }
   };

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitBytecodeExpression(this);
   }

   public abstract void visit(MethodVisitor var1);

   public Expression transformExpression(ExpressionTransformer transformer) {
      return this;
   }
}
