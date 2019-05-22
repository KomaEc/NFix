package groovyjarjarasm.asm.tree;

import groovyjarjarasm.asm.MethodVisitor;

public class TryCatchBlockNode {
   public LabelNode start;
   public LabelNode end;
   public LabelNode handler;
   public String type;

   public TryCatchBlockNode(LabelNode var1, LabelNode var2, LabelNode var3, String var4) {
      this.start = var1;
      this.end = var2;
      this.handler = var3;
      this.type = var4;
   }

   public void accept(MethodVisitor var1) {
      var1.visitTryCatchBlock(this.start.getLabel(), this.end.getLabel(), this.handler == null ? null : this.handler.getLabel(), this.type);
   }
}
