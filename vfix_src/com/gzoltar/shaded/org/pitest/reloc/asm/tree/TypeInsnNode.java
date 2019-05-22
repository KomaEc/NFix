package com.gzoltar.shaded.org.pitest.reloc.asm.tree;

import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import java.util.Map;

public class TypeInsnNode extends AbstractInsnNode {
   public String desc;

   public TypeInsnNode(int var1, String var2) {
      super(var1);
      this.desc = var2;
   }

   public void setOpcode(int var1) {
      this.opcode = var1;
   }

   public int getType() {
      return 3;
   }

   public void accept(MethodVisitor var1) {
      var1.visitTypeInsn(this.opcode, this.desc);
      this.acceptAnnotations(var1);
   }

   public AbstractInsnNode clone(Map var1) {
      return (new TypeInsnNode(this.opcode, this.desc)).cloneAnnotations(this);
   }
}
