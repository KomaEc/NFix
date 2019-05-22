package org.codehaus.groovy.ast.expr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;

public abstract class Expression extends AnnotatedNode {
   private ClassNode type;

   public Expression() {
      this.type = ClassHelper.DYNAMIC_TYPE;
   }

   public abstract Expression transformExpression(ExpressionTransformer var1);

   protected List<Expression> transformExpressions(List<? extends Expression> expressions, ExpressionTransformer transformer) {
      List<Expression> list = new ArrayList(expressions.size());
      Iterator i$ = expressions.iterator();

      while(i$.hasNext()) {
         Expression expr = (Expression)i$.next();
         list.add(transformer.transform(expr));
      }

      return list;
   }

   protected <T extends Expression> List<T> transformExpressions(List<? extends Expression> expressions, ExpressionTransformer transformer, Class<T> transformedType) {
      List<T> list = new ArrayList(expressions.size());
      Iterator i$ = expressions.iterator();

      while(i$.hasNext()) {
         Expression expr = (Expression)i$.next();
         Expression transformed = transformer.transform(expr);
         if (!transformedType.isInstance(transformed)) {
            throw new GroovyBugError(String.format("Transformed expression should have type %s but has type %s", transformedType, transformed.getClass()));
         }

         list.add(transformedType.cast(transformed));
      }

      return list;
   }

   public ClassNode getType() {
      return this.type;
   }

   public void setType(ClassNode t) {
      this.type = t;
   }
}
