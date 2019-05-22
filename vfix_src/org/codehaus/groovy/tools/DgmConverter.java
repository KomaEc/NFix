package org.codehaus.groovy.tools;

import groovyjarjarasm.asm.ClassWriter;
import groovyjarjarasm.asm.Label;
import groovyjarjarasm.asm.MethodVisitor;
import groovyjarjarasm.asm.Opcodes;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.codehaus.groovy.classgen.BytecodeHelper;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.CachedMethod;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.DateGroovyMethods;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.EncodingGroovyMethods;
import org.codehaus.groovy.runtime.ProcessGroovyMethods;
import org.codehaus.groovy.runtime.SqlGroovyMethods;
import org.codehaus.groovy.runtime.SwingGroovyMethods;
import org.codehaus.groovy.runtime.XmlGroovyMethods;

public class DgmConverter implements Opcodes {
   private static BytecodeHelper helper;

   public static void main(String[] args) throws IOException, ClassNotFoundException {
      Class[] classes = new Class[]{DefaultGroovyMethods.class, SwingGroovyMethods.class, SqlGroovyMethods.class, XmlGroovyMethods.class, EncodingGroovyMethods.class, DateGroovyMethods.class, ProcessGroovyMethods.class};
      List<CachedMethod> cachedMethodsList = new ArrayList();
      Class[] arr$ = classes;
      int len$ = classes.length;

      int i;
      for(i = 0; i < len$; ++i) {
         Class aClass = arr$[i];
         Collections.addAll(cachedMethodsList, ReflectionCache.getCachedClass(aClass).getMethods());
      }

      CachedMethod[] cachedMethods = (CachedMethod[])cachedMethodsList.toArray(new CachedMethod[cachedMethodsList.size()]);
      List<GeneratedMetaMethod.DgmMethodRecord> records = new ArrayList();
      i = 0;

      for(int cur = 0; i < cachedMethods.length; ++i) {
         CachedMethod method = cachedMethods[i];
         if (method.isStatic() && method.isPublic() && method.getCachedMethod().getAnnotation(Deprecated.class) == null && method.getParameterTypes().length != 0) {
            Class returnType = method.getReturnType();
            String className = "org/codehaus/groovy/runtime/dgm$" + cur;
            GeneratedMetaMethod.DgmMethodRecord record = new GeneratedMetaMethod.DgmMethodRecord();
            records.add(record);
            record.methodName = method.getName();
            record.returnType = method.getReturnType();
            record.parameters = method.getNativeParameterTypes();
            record.className = className;
            ClassWriter cw = new ClassWriter(1);
            cw.visit(47, 1, className, (String)null, "org/codehaus/groovy/reflection/GeneratedMetaMethod", (String[])null);
            createConstructor(cw);
            String methodDescriptor = BytecodeHelper.getMethodDescriptor(returnType, method.getNativeParameterTypes());
            createInvokeMethod(method, cw, returnType, methodDescriptor);
            createDoMethodInvokeMethod(method, cw, className, returnType, methodDescriptor);
            createIsValidMethodMethod(method, cw, className);
            cw.visitEnd();
            byte[] bytes = cw.toByteArray();
            FileOutputStream fileOutputStream = new FileOutputStream("target/classes/" + className + ".class");
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
            ++cur;
         }
      }

      GeneratedMetaMethod.DgmMethodRecord.saveDgmInfo(records, "target/classes/META-INF/dgminfo");
   }

   private static void createConstructor(ClassWriter cw) {
      MethodVisitor mv = cw.visitMethod(1, "<init>", "(Ljava/lang/String;Lorg/codehaus/groovy/reflection/CachedClass;Ljava/lang/Class;[Ljava/lang/Class;)V", (String)null, (String[])null);
      mv.visitCode();
      mv.visitVarInsn(25, 0);
      mv.visitVarInsn(25, 1);
      mv.visitVarInsn(25, 2);
      mv.visitVarInsn(25, 3);
      mv.visitVarInsn(25, 4);
      mv.visitMethodInsn(183, "org/codehaus/groovy/reflection/GeneratedMetaMethod", "<init>", "(Ljava/lang/String;Lorg/codehaus/groovy/reflection/CachedClass;Ljava/lang/Class;[Ljava/lang/Class;)V");
      mv.visitInsn(177);
      mv.visitMaxs(0, 0);
      mv.visitEnd();
   }

   private static void createIsValidMethodMethod(CachedMethod method, ClassWriter cw, String className) {
      if (method.getParamsCount() == 2 && method.getParameterTypes()[0].isNumber && method.getParameterTypes()[1].isNumber) {
         MethodVisitor mv = cw.visitMethod(1, "isValidMethod", "([Ljava/lang/Class;)Z", (String)null, (String[])null);
         mv.visitCode();
         mv.visitVarInsn(25, 1);
         Label l0 = new Label();
         mv.visitJumpInsn(198, l0);
         mv.visitVarInsn(25, 0);
         mv.visitMethodInsn(182, className, "getParameterTypes", "()[Lorg/codehaus/groovy/reflection/CachedClass;");
         mv.visitInsn(3);
         mv.visitInsn(50);
         mv.visitVarInsn(25, 1);
         mv.visitInsn(3);
         mv.visitInsn(50);
         mv.visitMethodInsn(182, "org/codehaus/groovy/reflection/CachedClass", "isAssignableFrom", "(Ljava/lang/Class;)Z");
         Label l1 = new Label();
         mv.visitJumpInsn(153, l1);
         mv.visitLabel(l0);
         mv.visitInsn(4);
         Label l2 = new Label();
         mv.visitJumpInsn(167, l2);
         mv.visitLabel(l1);
         mv.visitInsn(3);
         mv.visitLabel(l2);
         mv.visitInsn(172);
         mv.visitMaxs(0, 0);
         mv.visitEnd();
      }

   }

   private static void createDoMethodInvokeMethod(CachedMethod method, ClassWriter cw, String className, Class returnType, String methodDescriptor) {
      MethodVisitor mv = cw.visitMethod(17, "doMethodInvoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", (String)null, (String[])null);
      helper = new BytecodeHelper(mv);
      mv.visitCode();
      if (method.getParamsCount() == 2 && method.getParameterTypes()[0].isNumber && method.getParameterTypes()[1].isNumber) {
         mv.visitVarInsn(25, 1);
         helper.doCast(method.getParameterTypes()[0].getTheClass());
         mv.visitVarInsn(25, 0);
         mv.visitMethodInsn(182, className, "getParameterTypes", "()[Lorg/codehaus/groovy/reflection/CachedClass;");
         mv.visitInsn(3);
         mv.visitInsn(50);
         mv.visitVarInsn(25, 2);
         mv.visitInsn(3);
         mv.visitInsn(50);
         mv.visitMethodInsn(182, "org/codehaus/groovy/reflection/CachedClass", "coerceArgument", "(Ljava/lang/Object;)Ljava/lang/Object;");
         Class type = method.getParameterTypes()[1].getTheClass();
         if (type.isPrimitive()) {
            helper.unbox(type);
         } else {
            helper.doCast(type);
         }
      } else {
         mv.visitVarInsn(25, 0);
         mv.visitVarInsn(25, 2);
         mv.visitMethodInsn(182, className, "coerceArgumentsToClasses", "([Ljava/lang/Object;)[Ljava/lang/Object;");
         mv.visitVarInsn(58, 2);
         mv.visitVarInsn(25, 1);
         helper.doCast(method.getParameterTypes()[0].getTheClass());
         loadParameters(method, 2, mv);
      }

      mv.visitMethodInsn(184, BytecodeHelper.getClassInternalName(method.getDeclaringClass().getTheClass()), method.getName(), methodDescriptor);
      helper.box(returnType);
      if (method.getReturnType() == Void.TYPE) {
         mv.visitInsn(1);
      }

      mv.visitInsn(176);
      mv.visitMaxs(0, 0);
      mv.visitEnd();
   }

   private static void createInvokeMethod(CachedMethod method, ClassWriter cw, Class returnType, String methodDescriptor) {
      MethodVisitor mv = cw.visitMethod(1, "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", (String)null, (String[])null);
      helper = new BytecodeHelper(mv);
      mv.visitCode();
      mv.visitVarInsn(25, 1);
      helper.doCast(method.getParameterTypes()[0].getTheClass());
      loadParameters(method, 2, mv);
      mv.visitMethodInsn(184, BytecodeHelper.getClassInternalName(method.getDeclaringClass().getTheClass()), method.getName(), methodDescriptor);
      helper.box(returnType);
      if (method.getReturnType() == Void.TYPE) {
         mv.visitInsn(1);
      }

      mv.visitInsn(176);
      mv.visitMaxs(0, 0);
      mv.visitEnd();
   }

   protected static void loadParameters(CachedMethod method, int argumentIndex, MethodVisitor mv) {
      CachedClass[] parameters = method.getParameterTypes();
      int size = parameters.length - 1;

      for(int i = 0; i < size; ++i) {
         mv.visitVarInsn(25, argumentIndex);
         helper.pushConstant(i);
         mv.visitInsn(50);
         Class type = parameters[i + 1].getTheClass();
         if (type.isPrimitive()) {
            helper.unbox(type);
         } else {
            helper.doCast(type);
         }
      }

   }
}
