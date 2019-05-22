package groovyjarjarasm.asm.tree;

import groovyjarjarasm.asm.MethodVisitor;
import java.util.Map;

public class IntInsnNode extends AbstractInsnNode {
   public int operand;

   public IntInsnNode(int var1, int var2) {
      super(var1);
      this.operand = var2;
   }

   public void setOpcode(int var1) {
      this.opcode = var1;
   }

   public int getType() {
      return 1;
   }

   public void accept(MethodVisitor var1) {
      var1.visitIntInsn(this.opcode, this.operand);
   }

   public AbstractInsnNode clone(Map var1) {
      return new IntInsnNode(this.opcode, this.operand);
   }
}
