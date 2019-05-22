package com.gzoltar.shaded.org.objectweb.asm.tree;

import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import java.util.Map;

public class LdcInsnNode extends AbstractInsnNode {
   public Object cst;

   public LdcInsnNode(Object cst) {
      super(18);
      this.cst = cst;
   }

   public int getType() {
      return 9;
   }

   public void accept(MethodVisitor mv) {
      mv.visitLdcInsn(this.cst);
      this.acceptAnnotations(mv);
   }

   public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
      return (new LdcInsnNode(this.cst)).cloneAnnotations(this);
   }
}
