package groovyjarjarasm.asm.commons;

import groovyjarjarasm.asm.ClassAdapter;
import groovyjarjarasm.asm.ClassVisitor;
import groovyjarjarasm.asm.MethodVisitor;

public class StaticInitMerger extends ClassAdapter {
   private String name;
   private MethodVisitor clinit;
   private final String prefix;
   private int counter;

   public StaticInitMerger(String var1, ClassVisitor var2) {
      super(var2);
      this.prefix = var1;
   }

   public void visit(int var1, int var2, String var3, String var4, String var5, String[] var6) {
      this.cv.visit(var1, var2, var3, var4, var5, var6);
      this.name = var3;
   }

   public MethodVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5) {
      MethodVisitor var8;
      if ("<clinit>".equals(var2)) {
         byte var6 = 10;
         String var7 = this.prefix + this.counter++;
         var8 = this.cv.visitMethod(var6, var7, var3, var4, var5);
         if (this.clinit == null) {
            this.clinit = this.cv.visitMethod(var6, var2, var3, (String)null, (String[])null);
         }

         this.clinit.visitMethodInsn(184, this.name, var7, var3);
      } else {
         var8 = this.cv.visitMethod(var1, var2, var3, var4, var5);
      }

      return var8;
   }

   public void visitEnd() {
      if (this.clinit != null) {
         this.clinit.visitInsn(177);
         this.clinit.visitMaxs(0, 0);
      }

      this.cv.visitEnd();
   }
}
