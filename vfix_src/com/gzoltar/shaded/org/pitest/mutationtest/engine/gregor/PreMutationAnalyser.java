package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.reloc.asm.AnnotationVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.Attribute;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.FieldVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import java.util.Set;

public class PreMutationAnalyser extends ClassVisitor {
   private final PremutationClassInfo classInfo = new PremutationClassInfo();
   private final Set<String> loggingClasses;

   public PreMutationAnalyser(Set<String> loggingClasses) {
      super(327680);
      this.loggingClasses = loggingClasses;
   }

   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
   }

   public void visitSource(String source, String debug) {
   }

   public void visitOuterClass(String owner, String name, String desc) {
   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
      return null;
   }

   public void visitAttribute(Attribute attr) {
   }

   public void visitInnerClass(String name, String outerName, String innerName, int access) {
   }

   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
      return null;
   }

   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      return new PreMutationMethodAnalyzer(this.loggingClasses, this.classInfo);
   }

   public void visitEnd() {
   }

   public PremutationClassInfo getClassInfo() {
      return this.classInfo;
   }
}
