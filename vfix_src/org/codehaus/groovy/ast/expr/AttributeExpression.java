package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class AttributeExpression extends PropertyExpression {
   public AttributeExpression(Expression objectExpression, Expression property) {
      super(objectExpression, property, false);
   }

   public AttributeExpression(Expression objectExpression, Expression property, boolean safe) {
      super(objectExpression, property, safe);
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitAttributeExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      AttributeExpression ret = new AttributeExpression(transformer.transform(this.getObjectExpression()), transformer.transform(this.getProperty()), this.isSafe());
      ret.setSourcePosition(this);
      ret.setSpreadSafe(this.isSpreadSafe());
      return ret;
   }
}
