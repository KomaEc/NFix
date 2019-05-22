package com.gzoltar.shaded.org.objectweb.asm.tree.analysis;

import com.gzoltar.shaded.org.objectweb.asm.Type;
import com.gzoltar.shaded.org.objectweb.asm.tree.AbstractInsnNode;
import java.util.List;

public abstract class Interpreter<V extends Value> {
   protected final int api;

   protected Interpreter(int api) {
      this.api = api;
   }

   public abstract V newValue(Type var1);

   public abstract V newOperation(AbstractInsnNode var1) throws AnalyzerException;

   public abstract V copyOperation(AbstractInsnNode var1, V var2) throws AnalyzerException;

   public abstract V unaryOperation(AbstractInsnNode var1, V var2) throws AnalyzerException;

   public abstract V binaryOperation(AbstractInsnNode var1, V var2, V var3) throws AnalyzerException;

   public abstract V ternaryOperation(AbstractInsnNode var1, V var2, V var3, V var4) throws AnalyzerException;

   public abstract V naryOperation(AbstractInsnNode var1, List<? extends V> var2) throws AnalyzerException;

   public abstract void returnOperation(AbstractInsnNode var1, V var2, V var3) throws AnalyzerException;

   public abstract V merge(V var1, V var2);
}
