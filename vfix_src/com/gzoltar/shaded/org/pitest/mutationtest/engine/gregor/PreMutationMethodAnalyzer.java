package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import java.util.Set;

public class PreMutationMethodAnalyzer extends MethodVisitor {
   private final Set<String> loggingClasses;
   private int currentLineNumber;
   private final PremutationClassInfo classInfo;

   public PreMutationMethodAnalyzer(Set<String> loggingClasses, PremutationClassInfo classInfo) {
      super(327680, new TryWithResourcesMethodVisitor(classInfo));
      this.classInfo = classInfo;
      this.loggingClasses = loggingClasses;
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      if (FCollection.contains(this.loggingClasses, matches(owner))) {
         this.classInfo.registerLineToAvoid(this.currentLineNumber);
      }

      super.visitMethodInsn(opcode, owner, name, desc, itf);
   }

   private static F<String, Boolean> matches(final String owner) {
      return new F<String, Boolean>() {
         public Boolean apply(String a) {
            return owner.startsWith(a);
         }
      };
   }

   public void visitLineNumber(int line, Label start) {
      this.currentLineNumber = line;
      super.visitLineNumber(line, start);
   }
}
