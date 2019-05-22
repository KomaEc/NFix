package org.codehaus.groovy.ast.stmt;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.VariableScope;
import org.codehaus.groovy.ast.expr.Expression;

public class ForStatement extends Statement {
   public static final Parameter FOR_LOOP_DUMMY;
   private Parameter variable;
   private Expression collectionExpression;
   private Statement loopBlock;
   private VariableScope scope;

   public ForStatement(Parameter variable, Expression collectionExpression, Statement loopBlock) {
      this.variable = variable;
      this.collectionExpression = collectionExpression;
      this.loopBlock = loopBlock;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitForLoop(this);
   }

   public Expression getCollectionExpression() {
      return this.collectionExpression;
   }

   public Statement getLoopBlock() {
      return this.loopBlock;
   }

   public Parameter getVariable() {
      return this.variable;
   }

   public ClassNode getVariableType() {
      return this.variable.getType();
   }

   public void setCollectionExpression(Expression collectionExpression) {
      this.collectionExpression = collectionExpression;
   }

   public void setVariableScope(VariableScope variableScope) {
      this.scope = variableScope;
   }

   public VariableScope getVariableScope() {
      return this.scope;
   }

   public void setLoopBlock(Statement loopBlock) {
      this.loopBlock = loopBlock;
   }

   static {
      FOR_LOOP_DUMMY = new Parameter(ClassHelper.OBJECT_TYPE, "forLoopDummyParameter");
   }
}
