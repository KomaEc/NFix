package groovyjarjarasm.asm.tree;

import groovyjarjarasm.asm.MethodVisitor;
import java.util.Map;

public class JumpInsnNode extends AbstractInsnNode {
   public LabelNode label;

   public JumpInsnNode(int var1, LabelNode var2) {
      super(var1);
      this.label = var2;
   }

   public void setOpcode(int var1) {
      this.opcode = var1;
   }

   public int getType() {
      return 6;
   }

   public void accept(MethodVisitor var1) {
      var1.visitJumpInsn(this.opcode, this.label.getLabel());
   }

   public AbstractInsnNode clone(Map var1) {
      return new JumpInsnNode(this.opcode, clone(this.label, var1));
   }
}
