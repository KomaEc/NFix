package org.codehaus.groovy.ast;

import groovyjarjarasm.asm.Opcodes;
import java.lang.reflect.Field;
import org.codehaus.groovy.ast.expr.Expression;

public class FieldNode extends AnnotatedNode implements Opcodes, Variable {
   private String name;
   private int modifiers;
   private ClassNode type;
   private ClassNode owner;
   private Expression initialValueExpression;
   private boolean dynamicTyped;
   private boolean holder;
   private boolean closureShare = false;

   public static FieldNode newStatic(Class theClass, String name) throws SecurityException, NoSuchFieldException {
      Field field = theClass.getField(name);
      ClassNode fldType = ClassHelper.make(field.getType());
      return new FieldNode(name, 9, fldType, ClassHelper.make(theClass), (Expression)null);
   }

   public FieldNode(String name, int modifiers, ClassNode type, ClassNode owner, Expression initialValueExpression) {
      this.name = name;
      this.modifiers = modifiers;
      this.type = type;
      if (this.type == ClassHelper.DYNAMIC_TYPE && initialValueExpression != null) {
         this.setType(initialValueExpression.getType());
      }

      this.setType(type);
      this.owner = owner;
      this.initialValueExpression = initialValueExpression;
   }

   public Expression getInitialExpression() {
      return this.initialValueExpression;
   }

   public int getModifiers() {
      return this.modifiers;
   }

   public String getName() {
      return this.name;
   }

   public ClassNode getType() {
      return this.type;
   }

   public void setType(ClassNode type) {
      this.type = type;
      this.dynamicTyped |= type == ClassHelper.DYNAMIC_TYPE;
   }

   public ClassNode getOwner() {
      return this.owner;
   }

   public boolean isHolder() {
      return this.holder;
   }

   public void setHolder(boolean holder) {
      this.holder = holder;
   }

   public boolean isDynamicTyped() {
      return this.dynamicTyped;
   }

   public void setModifiers(int modifiers) {
      this.modifiers = modifiers;
   }

   public boolean isStatic() {
      return (this.modifiers & 8) != 0;
   }

   public boolean isEnum() {
      return (this.modifiers & 16384) != 0;
   }

   public boolean isFinal() {
      return (this.modifiers & 16) != 0;
   }

   public boolean isPublic() {
      return (this.modifiers & 1) != 0;
   }

   public void setOwner(ClassNode owner) {
      this.owner = owner;
   }

   public boolean hasInitialExpression() {
      return this.initialValueExpression != null;
   }

   public boolean isInStaticContext() {
      return this.isStatic();
   }

   public Expression getInitialValueExpression() {
      return this.initialValueExpression;
   }

   public void setInitialValueExpression(Expression initialValueExpression) {
      this.initialValueExpression = initialValueExpression;
   }

   public boolean isClosureSharedVariable() {
      return false;
   }

   public void setClosureSharedVariable(boolean inClosure) {
      this.closureShare = inClosure;
   }

   public ClassNode getOriginType() {
      return this.getType();
   }

   public void rename(String name) {
      this.declaringClass.renameField(this.name, name);
      this.name = name;
   }
}
