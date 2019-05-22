package com.gzoltar.shaded.org.objectweb.asm.tree;

import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import java.util.Map;

public class InsnNode extends AbstractInsnNode {
   public InsnNode(int opcode) {
      super(opcode);
   }

   public int getType() {
      return 0;
   }

   public void accept(MethodVisitor mv) {
      mv.visitInsn(this.opcode);
      this.acceptAnnotations(mv);
   }

   public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
      return (new InsnNode(this.opcode)).cloneAnnotations(this);
   }
}
