package groovyjarjarasm.asm.tree;

import groovyjarjarasm.asm.MethodVisitor;
import java.util.Map;

public class IincInsnNode extends AbstractInsnNode {
   public int var;
   public int incr;

   public IincInsnNode(int var1, int var2) {
      super(132);
      this.var = var1;
      this.incr = var2;
   }

   public int getType() {
      return 9;
   }

   public void accept(MethodVisitor var1) {
      var1.visitIincInsn(this.var, this.incr);
   }

   public AbstractInsnNode clone(Map var1) {
      return new IincInsnNode(this.var, this.incr);
   }
}
