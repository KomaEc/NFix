package groovyjarjarasm.asm.tree;

import groovyjarjarasm.asm.MethodVisitor;
import java.util.Map;

public class MultiANewArrayInsnNode extends AbstractInsnNode {
   public String desc;
   public int dims;

   public MultiANewArrayInsnNode(String var1, int var2) {
      super(197);
      this.desc = var1;
      this.dims = var2;
   }

   public int getType() {
      return 12;
   }

   public void accept(MethodVisitor var1) {
      var1.visitMultiANewArrayInsn(this.desc, this.dims);
   }

   public AbstractInsnNode clone(Map var1) {
      return new MultiANewArrayInsnNode(this.desc, this.dims);
   }
}
