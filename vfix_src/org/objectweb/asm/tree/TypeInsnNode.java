package org.objectweb.asm.tree;

import java.util.Map;
import org.objectweb.asm.MethodVisitor;

public class TypeInsnNode extends AbstractInsnNode {
   public String desc;

   public TypeInsnNode(int opcode, String desc) {
      super(opcode);
      this.desc = desc;
   }

   public void setOpcode(int opcode) {
      this.opcode = opcode;
   }

   public int getType() {
      return 3;
   }

   public void accept(MethodVisitor mv) {
      mv.visitTypeInsn(this.opcode, this.desc);
      this.acceptAnnotations(mv);
   }

   public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
      return (new TypeInsnNode(this.opcode, this.desc)).cloneAnnotations(this);
   }
}
