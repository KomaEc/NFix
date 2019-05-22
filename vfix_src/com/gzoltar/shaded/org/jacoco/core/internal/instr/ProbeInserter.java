package com.gzoltar.shaded.org.jacoco.core.internal.instr;

import com.gzoltar.shaded.org.objectweb.asm.Label;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import com.gzoltar.shaded.org.objectweb.asm.Opcodes;
import com.gzoltar.shaded.org.objectweb.asm.Type;

class ProbeInserter extends MethodVisitor implements IProbeInserter {
   private final IProbeArrayStrategy arrayStrategy;
   private final boolean clinit;
   private final int variable;
   private int accessorStackSize;

   ProbeInserter(int access, String name, String desc, MethodVisitor mv, IProbeArrayStrategy arrayStrategy) {
      super(327680, mv);
      this.clinit = "<clinit>".equals(name);
      this.arrayStrategy = arrayStrategy;
      int pos = (8 & access) == 0 ? 1 : 0;
      Type[] arr$ = Type.getArgumentTypes(desc);
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Type t = arr$[i$];
         pos += t.getSize();
      }

      this.variable = pos;
   }

   public void insertProbe(int id) {
      this.mv.visitVarInsn(25, this.variable);
      InstrSupport.push(this.mv, id);
      this.mv.visitInsn(4);
      this.mv.visitInsn(84);
   }

   public void visitCode() {
      this.accessorStackSize = this.arrayStrategy.storeInstance(this.mv, this.clinit, this.variable);
      this.mv.visitCode();
   }

   public final void visitVarInsn(int opcode, int var) {
      this.mv.visitVarInsn(opcode, this.map(var));
   }

   public final void visitIincInsn(int var, int increment) {
      this.mv.visitIincInsn(this.map(var), increment);
   }

   public final void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
      this.mv.visitLocalVariable(name, desc, signature, start, end, this.map(index));
   }

   public void visitMaxs(int maxStack, int maxLocals) {
      int increasedStack = Math.max(maxStack + 3, this.accessorStackSize);
      this.mv.visitMaxs(increasedStack, maxLocals + 1);
   }

   private int map(int var) {
      return var < this.variable ? var : var + 1;
   }

   public final void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
      if (type != -1) {
         throw new IllegalArgumentException("ClassReader.accept() should be called with EXPAND_FRAMES flag");
      } else {
         Object[] newLocal = new Object[Math.max(nLocal, this.variable) + 1];
         int idx = 0;
         int newIdx = 0;
         int pos = 0;

         while(true) {
            while(idx < nLocal || pos <= this.variable) {
               if (pos == this.variable) {
                  newLocal[newIdx++] = "[Z";
                  ++pos;
               } else if (idx < nLocal) {
                  Object t = local[idx++];
                  newLocal[newIdx++] = t;
                  ++pos;
                  if (t == Opcodes.LONG || t == Opcodes.DOUBLE) {
                     ++pos;
                  }
               } else {
                  newLocal[newIdx++] = Opcodes.TOP;
                  ++pos;
               }
            }

            this.mv.visitFrame(type, newIdx, newLocal, nStack, stack);
            return;
         }
      }
   }
}
