package org.objectweb.asm.tree;

import java.util.Map;
import org.objectweb.asm.MethodVisitor;

public class IincInsnNode extends AbstractInsnNode {
   public int var;
   public int incr;

   public IincInsnNode(int var, int incr) {
      super(132);
      this.var = var;
      this.incr = incr;
   }

   public int getType() {
      return 10;
   }

   public void accept(MethodVisitor mv) {
      mv.visitIincInsn(this.var, this.incr);
      this.acceptAnnotations(mv);
   }

   public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
      return (new IincInsnNode(this.var, this.incr)).cloneAnnotations(this);
   }
}
