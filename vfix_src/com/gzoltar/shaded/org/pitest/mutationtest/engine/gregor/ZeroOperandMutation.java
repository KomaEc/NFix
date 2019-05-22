package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

public interface ZeroOperandMutation {
   void apply(int var1, MethodVisitor var2);

   String decribe(int var1, MethodInfo var2);
}
