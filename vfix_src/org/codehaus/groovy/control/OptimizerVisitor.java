package org.codehaus.groovy.control;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;

public class OptimizerVisitor extends ClassCodeExpressionTransformer {
   private ClassNode currentClass;
   private SourceUnit source;
   private Map const2Var = new HashMap();
   private List<FieldNode> missingFields = new LinkedList();

   public OptimizerVisitor(CompilationUnit cu) {
   }

   public void visitClass(ClassNode node, SourceUnit source) {
      this.currentClass = node;
      this.source = source;
      this.const2Var.clear();
      this.missingFields.clear();
      super.visitClass(node);
      this.addMissingFields();
   }

   private void addMissingFields() {
      Iterator i$ = this.missingFields.iterator();

      while(i$.hasNext()) {
         Object missingField = i$.next();
         FieldNode f = (FieldNode)missingField;
         this.currentClass.addField(f);
      }

   }

   private void setConstField(ConstantExpression constantExpression) {
      Object n = constantExpression.getValue();
      if (n instanceof Number || n instanceof Character) {
         FieldNode field = (FieldNode)this.const2Var.get(n);
         if (field != null) {
            constantExpression.setConstantName(field.getName());
         } else {
            String name = "$const$" + this.const2Var.size();
            field = this.currentClass.getDeclaredField(name);
            if (field == null) {
               field = new FieldNode(name, 4122, constantExpression.getType(), this.currentClass, constantExpression);
               field.setSynthetic(true);
               this.missingFields.add(field);
            }

            constantExpression.setConstantName(field.getName());
            this.const2Var.put(n, field);
         }
      }
   }

   public Expression transform(Expression exp) {
      if (exp == null) {
         return null;
      } else {
         if (!this.currentClass.isInterface() && exp.getClass() == ConstantExpression.class) {
            this.setConstField((ConstantExpression)exp);
         }

         return exp.transformExpression(this);
      }
   }

   protected SourceUnit getSourceUnit() {
      return this.source;
   }

   public void visitClosureExpression(ClosureExpression expression) {
   }
}
