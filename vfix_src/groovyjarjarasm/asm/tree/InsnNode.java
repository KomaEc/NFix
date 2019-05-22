package groovyjarjarasm.asm.tree;

import groovyjarjarasm.asm.MethodVisitor;
import java.util.Map;

public class InsnNode extends AbstractInsnNode {
   public InsnNode(int var1) {
      super(var1);
   }

   public int getType() {
      return 0;
   }

   public void accept(MethodVisitor var1) {
      var1.visitInsn(this.opcode);
   }

   public AbstractInsnNode clone(Map var1) {
      return new InsnNode(this.opcode);
   }
}
