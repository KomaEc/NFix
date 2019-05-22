package com.gzoltar.shaded.org.objectweb.asm.tree;

import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import java.util.Map;

public class JumpInsnNode extends AbstractInsnNode {
   public LabelNode label;

   public JumpInsnNode(int opcode, LabelNode label) {
      super(opcode);
      this.label = label;
   }

   public void setOpcode(int opcode) {
      this.opcode = opcode;
   }

   public int getType() {
      return 7;
   }

   public void accept(MethodVisitor mv) {
      mv.visitJumpInsn(this.opcode, this.label.getLabel());
      this.acceptAnnotations(mv);
   }

   public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
      return (new JumpInsnNode(this.opcode, clone(this.label, labels))).cloneAnnotations(this);
   }
}
