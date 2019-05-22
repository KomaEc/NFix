package com.gzoltar.shaded.org.pitest.classinfo;

import com.gzoltar.shaded.org.pitest.functional.F5;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

public abstract class MethodFilteringAdapter extends ClassVisitor {
   private final F5<Integer, String, String, String, String[], Boolean> filter;

   public MethodFilteringAdapter(ClassVisitor writer, F5<Integer, String, String, String, String[], Boolean> filter) {
      super(327680, writer);
      this.filter = filter;
   }

   private boolean shouldInstrument(int access, String name, String desc, String signature, String[] exceptions) {
      return (Boolean)this.filter.apply(access, name, desc, signature, exceptions);
   }

   public final MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      MethodVisitor methodVisitor = this.cv.visitMethod(access, name, desc, signature, exceptions);
      return this.shouldInstrument(access, name, desc, signature, exceptions) ? this.visitMethodIfRequired(access, name, desc, signature, exceptions, methodVisitor) : methodVisitor;
   }

   public abstract MethodVisitor visitMethodIfRequired(int var1, String var2, String var3, String var4, String[] var5, MethodVisitor var6);
}
