package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

/** @deprecated */
@Deprecated
public class RegexExpression extends Expression {
   private final Expression string;

   public RegexExpression(Expression string) {
      this.string = string;
      super.setType(ClassHelper.PATTERN_TYPE);
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitRegexExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new RegexExpression(transformer.transform(this.string));
      ret.setSourcePosition(this);
      return ret;
   }

   public Expression getRegex() {
      return this.string;
   }
}
