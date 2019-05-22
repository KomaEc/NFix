package org.codehaus.groovy.ast.stmt;

import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.expr.BooleanExpression;

public class WhileStatement extends Statement {
   private BooleanExpression booleanExpression;
   private Statement loopBlock;

   public WhileStatement(BooleanExpression booleanExpression, Statement loopBlock) {
      this.booleanExpression = booleanExpression;
      this.loopBlock = loopBlock;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitWhileLoop(this);
   }

   public BooleanExpression getBooleanExpression() {
      return this.booleanExpression;
   }

   public Statement getLoopBlock() {
      return this.loopBlock;
   }

   public void setBooleanExpression(BooleanExpression booleanExpression) {
      this.booleanExpression = booleanExpression;
   }

   public void setLoopBlock(Statement loopBlock) {
      this.loopBlock = loopBlock;
   }
}
