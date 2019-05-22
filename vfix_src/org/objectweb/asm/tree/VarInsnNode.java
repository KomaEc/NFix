package org.objectweb.asm.tree;

import java.util.Map;
import org.objectweb.asm.MethodVisitor;

public class VarInsnNode extends AbstractInsnNode {
   public int var;

   public VarInsnNode(int opcode, int var) {
      super(opcode);
      this.var = var;
   }

   public void setOpcode(int opcode) {
      this.opcode = opcode;
   }

   public int getType() {
      return 2;
   }

   public void accept(MethodVisitor mv) {
      mv.visitVarInsn(this.opcode, this.var);
      this.acceptAnnotations(mv);
   }

   public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
      return (new VarInsnNode(this.opcode, this.var)).cloneAnnotations(this);
   }
}
