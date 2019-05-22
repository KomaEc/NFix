package org.apache.maven.usability.plugin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExpressionDocumentation implements Serializable {
   private List expressions;
   private Map expressionsBySyntax;
   private String modelEncoding = "UTF-8";

   public void addExpression(Expression expression) {
      this.getExpressions().add(expression);
   }

   public List getExpressions() {
      if (this.expressions == null) {
         this.expressions = new ArrayList();
      }

      return this.expressions;
   }

   public void removeExpression(Expression expression) {
      this.getExpressions().remove(expression);
   }

   public void setExpressions(List expressions) {
      this.expressions = expressions;
   }

   public Map getExpressionsBySyntax() {
      if (this.expressionsBySyntax == null) {
         this.expressionsBySyntax = new HashMap();
         List expressions = this.getExpressions();
         if (expressions != null && !expressions.isEmpty()) {
            Iterator it = expressions.iterator();

            while(it.hasNext()) {
               Expression expr = (Expression)it.next();
               this.expressionsBySyntax.put(expr.getSyntax(), expr);
            }
         }
      }

      return this.expressionsBySyntax;
   }

   public void flushExpressionsBySyntax() {
      this.expressionsBySyntax = null;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }
}
