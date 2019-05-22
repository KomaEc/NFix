package com.gzoltar.shaded.org.pitest.classinfo;

import com.gzoltar.shaded.org.pitest.bytecode.NullVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.AnnotationVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassReader;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.Type;
import java.util.ArrayList;
import java.util.List;

public final class ClassInfoVisitor extends MethodFilteringAdapter {
   private final ClassInfoBuilder classInfo;

   private ClassInfoVisitor(ClassInfoBuilder classInfo, ClassVisitor writer) {
      super(writer, BridgeMethodFilter.INSTANCE);
      this.classInfo = classInfo;
   }

   public static ClassInfoBuilder getClassInfo(ClassName name, byte[] bytes, long hash) {
      ClassReader reader = new ClassReader(bytes);
      ClassVisitor writer = new NullVisitor();
      ClassInfoBuilder info = new ClassInfoBuilder();
      info.id = new ClassIdentifier(hash, name);
      reader.accept(new ClassInfoVisitor(info, writer), 0);
      return info;
   }

   public void visitSource(String source, String debug) {
      super.visitSource(source, debug);
      this.classInfo.sourceFile = source;
   }

   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
      super.visit(version, access, name, signature, superName, interfaces);
      this.classInfo.access = access;
      this.classInfo.superClass = superName;
   }

   public void visitOuterClass(String owner, String name, String desc) {
      super.visitOuterClass(owner, name, desc);
      this.classInfo.outerClass = owner;
   }

   public void visitInnerClass(String name, String outerName, String innerName, int access) {
      super.visitInnerClass(name, outerName, innerName, access);
      if (outerName != null && this.classInfo.id.getName().equals(new ClassName(name))) {
         this.classInfo.outerClass = outerName;
      }

   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
      String type = desc.substring(1, desc.length() - 1);
      this.classInfo.registerAnnotation(type);
      return new ClassInfoVisitor.ClassAnnotationValueVisitor(this.classInfo, new ClassName(type));
   }

   public MethodVisitor visitMethodIfRequired(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor methodVisitor) {
      return new InfoMethodVisitor(this.classInfo, methodVisitor);
   }

   private static class ClassAnnotationValueVisitor extends AnnotationVisitor {
      private final ClassInfoBuilder classInfo;
      private final ClassName annotation;

      public ClassAnnotationValueVisitor(ClassInfoBuilder classInfo, ClassName annotation) {
         super(327680, (AnnotationVisitor)null);
         this.classInfo = classInfo;
         this.annotation = annotation;
      }

      public void visit(String name, Object value) {
         if (name.equals("value")) {
            this.classInfo.registerClassAnnotationValue(this.annotation, this.simplify(value));
         }

         super.visit(name, value);
      }

      public AnnotationVisitor visitArray(String name) {
         if (name.equals("value")) {
            final List<Object> arrayValue = new ArrayList();
            return new AnnotationVisitor(327680, (AnnotationVisitor)null) {
               public void visit(String name, Object value) {
                  arrayValue.add(ClassAnnotationValueVisitor.this.simplify(value));
                  super.visit(name, value);
               }

               public void visitEnd() {
                  ClassAnnotationValueVisitor.this.classInfo.registerClassAnnotationValue(ClassAnnotationValueVisitor.this.annotation, arrayValue.toArray());
               }
            };
         } else {
            return super.visitArray(name);
         }
      }

      private Object simplify(Object value) {
         Object newValue = value;
         if (value instanceof Type) {
            newValue = ((Type)value).getClassName();
         }

         return newValue;
      }
   }
}
