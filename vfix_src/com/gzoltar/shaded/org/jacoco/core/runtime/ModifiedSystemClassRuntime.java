package com.gzoltar.shaded.org.jacoco.core.runtime;

import com.gzoltar.shaded.org.jacoco.core.internal.Java9Support;
import com.gzoltar.shaded.org.objectweb.asm.ClassReader;
import com.gzoltar.shaded.org.objectweb.asm.ClassVisitor;
import com.gzoltar.shaded.org.objectweb.asm.ClassWriter;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.security.ProtectionDomain;

public class ModifiedSystemClassRuntime extends AbstractRuntime {
   private static final String ACCESS_FIELD_TYPE = "Ljava/lang/Object;";
   private final Class<?> systemClass;
   private final String systemClassName;
   private final String accessFieldName;

   public ModifiedSystemClassRuntime(Class<?> systemClass, String accessFieldName) {
      this.systemClass = systemClass;
      this.systemClassName = systemClass.getName().replace('.', '/');
      this.accessFieldName = accessFieldName;
   }

   public void startup(RuntimeData data) throws Exception {
      super.startup(data);
      Field field = this.systemClass.getField(this.accessFieldName);
      field.set((Object)null, data);
   }

   public void shutdown() {
   }

   public int generateDataAccessor(long classid, String classname, int probecount, MethodVisitor mv) {
      mv.visitFieldInsn(178, this.systemClassName, this.accessFieldName, "Ljava/lang/Object;");
      RuntimeData.generateAccessCall(classid, classname, probecount, mv);
      return 6;
   }

   public static IRuntime createFor(Instrumentation inst, String className) throws ClassNotFoundException {
      return createFor(inst, className, "$jacocoAccess");
   }

   public static IRuntime createFor(Instrumentation inst, final String className, final String accessFieldName) throws ClassNotFoundException {
      ClassFileTransformer transformer = new ClassFileTransformer() {
         public byte[] transform(ClassLoader loader, String name, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] source) throws IllegalClassFormatException {
            return name.equals(className) ? ModifiedSystemClassRuntime.instrument(source, accessFieldName) : null;
         }
      };
      inst.addTransformer(transformer);
      Class<?> clazz = Class.forName(className.replace('/', '.'));
      inst.removeTransformer(transformer);

      try {
         clazz.getField(accessFieldName);
      } catch (NoSuchFieldException var6) {
         throw new RuntimeException(String.format("Class %s could not be instrumented.", className), var6);
      }

      return new ModifiedSystemClassRuntime(clazz, accessFieldName);
   }

   public static byte[] instrument(byte[] source, final String accessFieldName) {
      ClassReader reader = new ClassReader(Java9Support.downgradeIfRequired(source));
      ClassWriter writer = new ClassWriter(reader, 0);
      reader.accept(new ClassVisitor(327680, writer) {
         public void visitEnd() {
            ModifiedSystemClassRuntime.createDataField(this.cv, accessFieldName);
            super.visitEnd();
         }
      }, 8);
      return writer.toByteArray();
   }

   private static void createDataField(ClassVisitor visitor, String dataField) {
      visitor.visitField(4233, dataField, "Ljava/lang/Object;", (String)null, (Object)null);
   }
}
