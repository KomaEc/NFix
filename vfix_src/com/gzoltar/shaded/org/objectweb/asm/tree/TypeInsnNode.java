package com.gzoltar.shaded.org.objectweb.asm.tree;

import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import java.util.Map;

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
