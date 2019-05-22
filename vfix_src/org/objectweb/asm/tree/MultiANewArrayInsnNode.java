package org.objectweb.asm.tree;

import java.util.Map;
import org.objectweb.asm.MethodVisitor;

public class MultiANewArrayInsnNode extends AbstractInsnNode {
   public String desc;
   public int dims;

   public MultiANewArrayInsnNode(String desc, int dims) {
      super(197);
      this.desc = desc;
      this.dims = dims;
   }

   public int getType() {
      return 13;
   }

   public void accept(MethodVisitor mv) {
      mv.visitMultiANewArrayInsn(this.desc, this.dims);
      this.acceptAnnotations(mv);
   }

   public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
      return (new MultiANewArrayInsnNode(this.desc, this.dims)).cloneAnnotations(this);
   }
}
