package groovyjarjarasm.asm.tree;

import groovyjarjarasm.asm.MethodVisitor;
import java.util.Map;

public class VarInsnNode extends AbstractInsnNode {
   public int var;

   public VarInsnNode(int var1, int var2) {
      super(var1);
      this.var = var2;
   }

   public void setOpcode(int var1) {
      this.opcode = var1;
   }

   public int getType() {
      return 2;
   }

   public void accept(MethodVisitor var1) {
      var1.visitVarInsn(this.opcode, this.var);
   }

   public AbstractInsnNode clone(Map var1) {
      return new VarInsnNode(this.opcode, this.var);
   }
}
