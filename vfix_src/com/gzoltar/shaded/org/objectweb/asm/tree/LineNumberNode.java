package com.gzoltar.shaded.org.objectweb.asm.tree;

import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import java.util.Map;

public class LineNumberNode extends AbstractInsnNode {
   public int line;
   public LabelNode start;

   public LineNumberNode(int line, LabelNode start) {
      super(-1);
      this.line = line;
      this.start = start;
   }

   public int getType() {
      return 15;
   }

   public void accept(MethodVisitor mv) {
      mv.visitLineNumber(this.line, this.start.getLabel());
   }

   public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
      return new LineNumberNode(this.line, clone(this.start, labels));
   }
}
