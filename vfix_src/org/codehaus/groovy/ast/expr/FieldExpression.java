package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class FieldExpression extends Expression {
   private final FieldNode field;
   private boolean useRef;

   public FieldExpression(FieldNode field) {
      this.field = field;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitFieldExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      return this;
   }

   public String getFieldName() {
      return this.field.getName();
   }

   public FieldNode getField() {
      return this.field;
   }

   public String getText() {
      return "this." + this.field.getName();
   }

   public boolean isDynamicTyped() {
      return this.field.isDynamicTyped();
   }

   public void setType(ClassNode type) {
      super.setType(type);
      this.field.setType(type);
   }

   public ClassNode getType() {
      return this.field.getType();
   }

   public void setUseReferenceDirectly(boolean useRef) {
      this.useRef = useRef;
   }

   public boolean isUseReferenceDirectly() {
      return this.useRef;
   }
}
