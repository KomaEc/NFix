package groovyjarjarasm.asm.tree;

import groovyjarjarasm.asm.MethodVisitor;
import java.util.Map;

public class LdcInsnNode extends AbstractInsnNode {
   public Object cst;

   public LdcInsnNode(Object var1) {
      super(18);
      this.cst = var1;
   }

   public int getType() {
      return 8;
   }

   public void accept(MethodVisitor var1) {
      var1.visitLdcInsn(this.cst);
   }

   public AbstractInsnNode clone(Map var1) {
      return new LdcInsnNode(this.cst);
   }
}
