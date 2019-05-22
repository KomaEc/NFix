package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.blocks;

import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import java.util.HashSet;
import java.util.Set;

public class BlockTrackingMethodDecorator extends MethodVisitor {
   private final BlockCounter blockCounter;
   private final Set<Label> handlers = new HashSet();

   public BlockTrackingMethodDecorator(BlockCounter blockCounter, MethodVisitor mv) {
      super(327680, mv);
      this.blockCounter = blockCounter;
   }

   public void visitInsn(int opcode) {
      this.mv.visitInsn(opcode);
      if (this.endsBlock(opcode)) {
         this.blockCounter.registerFinallyBlockEnd();
         this.blockCounter.registerNewBlock();
      }

   }

   public void visitJumpInsn(int arg0, Label arg1) {
      this.mv.visitJumpInsn(arg0, arg1);
      this.blockCounter.registerNewBlock();
   }

   public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
      super.visitTryCatchBlock(start, end, handler, type);
      if (type == null) {
         this.handlers.add(handler);
      }

   }

   public void visitLabel(Label label) {
      super.visitLabel(label);
      if (this.handlers.contains(label)) {
         this.blockCounter.registerFinallyBlockStart();
      }

   }

   private boolean endsBlock(int opcode) {
      switch(opcode) {
      case 172:
      case 173:
      case 174:
      case 175:
      case 176:
      case 177:
      case 191:
         return true;
      case 178:
      case 179:
      case 180:
      case 181:
      case 182:
      case 183:
      case 184:
      case 185:
      case 186:
      case 187:
      case 188:
      case 189:
      case 190:
      default:
         return false;
      }
   }
}
