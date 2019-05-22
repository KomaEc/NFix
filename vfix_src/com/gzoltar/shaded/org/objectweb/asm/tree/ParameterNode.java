package com.gzoltar.shaded.org.objectweb.asm.tree;

import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;

public class ParameterNode {
   public String name;
   public int access;

   public ParameterNode(String name, int access) {
      this.name = name;
      this.access = access;
   }

   public void accept(MethodVisitor mv) {
      mv.visitParameter(this.name, this.access);
   }
}
