package org.codehaus.groovy.ast.expr;

import java.util.Iterator;
import java.util.Map;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class AnnotationConstantExpression extends ConstantExpression {
   public AnnotationConstantExpression(AnnotationNode node) {
      super(node);
      this.setType(node.getClassNode());
   }

   public void visit(GroovyCodeVisitor visitor) {
      AnnotationNode node = (AnnotationNode)this.getValue();
      Map<String, Expression> attrs = node.getMembers();
      Iterator i$ = attrs.values().iterator();

      while(i$.hasNext()) {
         Expression expr = (Expression)i$.next();
         expr.visit(visitor);
      }

   }
}
