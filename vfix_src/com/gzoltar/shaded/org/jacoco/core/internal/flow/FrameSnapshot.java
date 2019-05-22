package com.gzoltar.shaded.org.jacoco.core.internal.flow;

import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import com.gzoltar.shaded.org.objectweb.asm.Opcodes;
import com.gzoltar.shaded.org.objectweb.asm.commons.AnalyzerAdapter;
import java.util.ArrayList;
import java.util.List;

class FrameSnapshot implements IFrame {
   private static final FrameSnapshot NOP = new FrameSnapshot((Object[])null, (Object[])null);
   private final Object[] locals;
   private final Object[] stack;

   private FrameSnapshot(Object[] locals, Object[] stack) {
      this.locals = locals;
      this.stack = stack;
   }

   static IFrame create(AnalyzerAdapter analyzer, int popCount) {
      if (analyzer != null && analyzer.locals != null) {
         Object[] locals = reduce(analyzer.locals, 0);
         Object[] stack = reduce(analyzer.stack, popCount);
         return new FrameSnapshot(locals, stack);
      } else {
         return NOP;
      }
   }

   private static Object[] reduce(List<Object> source, int popCount) {
      List<Object> copy = new ArrayList(source);
      int size = source.size() - popCount;
      copy.subList(size, source.size()).clear();
      int i = size;

      while(true) {
         Object type;
         do {
            --i;
            if (i < 0) {
               return copy.toArray();
            }

            type = source.get(i);
         } while(type != Opcodes.LONG && type != Opcodes.DOUBLE);

         copy.remove(i + 1);
      }
   }

   public void accept(MethodVisitor mv) {
      if (this.locals != null) {
         mv.visitFrame(-1, this.locals.length, this.locals, this.stack.length, this.stack);
      }

   }
}
