package groovyjarjarasm.asm.tree;

import groovyjarjarasm.asm.MethodVisitor;
import java.util.Map;

public class MethodInsnNode extends AbstractInsnNode {
   public String owner;
   public String name;
   public String desc;

   public MethodInsnNode(int var1, String var2, String var3, String var4) {
      super(var1);
      this.owner = var2;
      this.name = var3;
      this.desc = var4;
   }

   public void setOpcode(int var1) {
      this.opcode = var1;
   }

   public int getType() {
      return 5;
   }

   public void accept(MethodVisitor var1) {
      var1.visitMethodInsn(this.opcode, this.owner, this.name, this.desc);
   }

   public AbstractInsnNode clone(Map var1) {
      return new MethodInsnNode(this.opcode, this.owner, this.name, this.desc);
   }
}
