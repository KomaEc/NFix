package org.codehaus.groovy.ast;

public class MixinNode extends ClassNode {
   public static final MixinNode[] EMPTY_ARRAY = new MixinNode[0];

   public MixinNode(String name, int modifiers, ClassNode superType) {
      this(name, modifiers, superType, ClassHelper.EMPTY_TYPE_ARRAY);
   }

   public MixinNode(String name, int modifiers, ClassNode superType, ClassNode[] interfaces) {
      super(name, modifiers, superType, interfaces, EMPTY_ARRAY);
   }
}
