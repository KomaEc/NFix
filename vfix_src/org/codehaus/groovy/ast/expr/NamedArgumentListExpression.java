package org.codehaus.groovy.ast.expr;

import java.util.List;

public class NamedArgumentListExpression extends MapExpression {
   public NamedArgumentListExpression() {
   }

   public NamedArgumentListExpression(List<MapEntryExpression> mapEntryExpressions) {
      super(mapEntryExpressions);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new NamedArgumentListExpression(this.transformExpressions(this.getMapEntryExpressions(), transformer, MapEntryExpression.class));
      ret.setSourcePosition(this);
      return ret;
   }
}
