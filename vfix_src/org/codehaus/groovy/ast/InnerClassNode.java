package org.codehaus.groovy.ast;

import java.util.LinkedList;

public class InnerClassNode extends ClassNode {
   private ClassNode outerClass;
   private VariableScope scope;
   private boolean anonymous;

   public InnerClassNode(ClassNode outerClass, String name, int modifiers, ClassNode superClass) {
      this(outerClass, name, modifiers, superClass, ClassHelper.EMPTY_TYPE_ARRAY, MixinNode.EMPTY_ARRAY);
   }

   public InnerClassNode(ClassNode outerClass, String name, int modifiers, ClassNode superClass, ClassNode[] interfaces, MixinNode[] mixins) {
      super(name, modifiers, superClass, interfaces, mixins);
      this.outerClass = outerClass;
      if (outerClass.innerClasses == null) {
         outerClass.innerClasses = new LinkedList();
      }

      outerClass.innerClasses.add(this);
   }

   public ClassNode getOuterClass() {
      return this.outerClass;
   }

   public FieldNode getOuterField(String name) {
      return this.outerClass.getDeclaredField(name);
   }

   public VariableScope getVariableScope() {
      return this.scope;
   }

   public void setVariableScope(VariableScope scope) {
      this.scope = scope;
   }

   public boolean isAnonymous() {
      return this.anonymous;
   }

   public void setAnonymous(boolean anonymous) {
      this.anonymous = anonymous;
   }
}
