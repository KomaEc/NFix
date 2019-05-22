package org.codehaus.groovy.reflection;

import groovyjarjarasm.asm.ClassWriter;
import groovyjarjarasm.asm.MethodVisitor;
import groovyjarjarasm.asm.Opcodes;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.codehaus.groovy.classgen.BytecodeHelper;

public class MethodHandleFactory implements Opcodes {
   private static final String[] EXCEPTIONS = new String[]{"java/lang/Throwable"};

   public static MethodHandle unreflect(Method method) {
      return (MethodHandle)(SunClassLoader.sunVM == null && !checkAccessable(method) ? new MethodHandleFactory.ReflectiveMethodHandle(method) : createCompiledMethodHandle(method, ClassInfo.getClassInfo(method.getDeclaringClass()).getArtifactClassLoader()));
   }

   private static MethodHandle unreflect(Method method, ClassLoaderForClassArtifacts loader) {
      return (MethodHandle)(SunClassLoader.sunVM == null && !checkAccessable(method) ? new MethodHandleFactory.ReflectiveMethodHandle(method) : createCompiledMethodHandle(method, loader));
   }

   private static boolean checkAccessable(Method method) {
      if (!Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
         return false;
      } else if (!Modifier.isPublic(method.getModifiers())) {
         return false;
      } else {
         Class[] arr$ = method.getParameterTypes();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class paramType = arr$[i$];
            if (!Modifier.isPublic(paramType.getModifiers())) {
               return false;
            }
         }

         return true;
      }
   }

   public static void genLoadParameters(int argumentIndex, MethodVisitor mv, BytecodeHelper helper, Method method) {
      Class<?>[] parameters = method.getParameterTypes();
      int size = parameters.length;

      for(int i = 0; i < size; ++i) {
         mv.visitVarInsn(25, argumentIndex);
         helper.pushConstant(i);
         mv.visitInsn(50);
         Class type = parameters[i];
         if (type.isPrimitive()) {
            helper.unbox(type);
         } else {
            helper.doCast(type);
         }
      }

   }

   public static void genLoadParametersDirect(int argumentIndex, MethodVisitor mv, BytecodeHelper helper, Method method) {
      Class<?>[] parameters = method.getParameterTypes();
      int size = parameters.length;

      for(int i = 0; i < size; ++i) {
         mv.visitVarInsn(25, argumentIndex + i);
         Class type = parameters[i];
         if (type.isPrimitive()) {
            helper.unbox(type);
         } else {
            helper.doCast(type);
         }
      }

   }

   public static void genLoadParametersPrimitiveDirect(int argumentIndex, MethodVisitor mv, BytecodeHelper helper, Method method) {
      Class<?>[] parameters = method.getParameterTypes();
      int size = parameters.length;
      int idx = 0;

      for(int i = 0; i < size; ++idx) {
         Class type = parameters[i];
         if (type == Double.TYPE) {
            mv.visitVarInsn(24, idx++);
         } else if (type == Float.TYPE) {
            mv.visitVarInsn(23, idx);
         } else if (type == Long.TYPE) {
            mv.visitVarInsn(22, idx++);
         } else if (type != Boolean.TYPE && type != Character.TYPE && type != Byte.TYPE && type != Integer.TYPE && type != Short.TYPE) {
            mv.visitVarInsn(25, idx);
            helper.doCast(type);
         } else {
            mv.visitVarInsn(21, idx);
         }

         ++i;
      }

   }

   private static MethodHandle createCompiledMethodHandle(Method method, ClassLoaderForClassArtifacts loader) {
      try {
         Constructor c = compileMethodHandle(method, loader);
         if (c != null) {
            return (MethodHandle)c.newInstance();
         }
      } catch (Throwable var3) {
      }

      return new MethodHandleFactory.ReflectiveMethodHandle(method);
   }

   private static Constructor compileMethodHandle(Method cachedMethod, ClassLoaderForClassArtifacts loader) {
      ClassWriter cw = new ClassWriter(1);
      String name = loader.createClassName(cachedMethod);
      byte[] bytes = genMethodHandle(cachedMethod, cw, name);
      return loader.defineClassAndGetConstructor(name, bytes);
   }

   private static byte[] genMethodHandle(Method method, ClassWriter cw, String name) {
      cw.visit(48, 1, name.replace('.', '/'), (String)null, "org/codehaus/groovy/reflection/MethodHandle", (String[])null);
      genConstructor(cw, "org/codehaus/groovy/reflection/MethodHandle");
      genInvokeXxxWithArray(cw, method);
      genInvokeWithFixedParams(cw, method);
      genInvokeWithFixedPrimitiveParams(cw, method);
      cw.visitEnd();
      return cw.toByteArray();
   }

   private static void genConstructor(ClassWriter cw, String superClass) {
      MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", (String)null, (String[])null);
      mv.visitCode();
      mv.visitVarInsn(25, 0);
      mv.visitMethodInsn(183, superClass, "<init>", "()V");
      mv.visitInsn(177);
      mv.visitMaxs(0, 0);
      mv.visitEnd();
   }

   public static void genInvokeXxxWithArray(ClassWriter cw, Method method) {
      MethodVisitor mv = cw.visitMethod(1, "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", (String)null, EXCEPTIONS);
      mv.visitCode();
      BytecodeHelper helper = new BytecodeHelper(mv);
      Class callClass = method.getDeclaringClass();
      boolean useInterface = callClass.isInterface();
      String type = BytecodeHelper.getClassInternalName(callClass.getName());
      String descriptor = BytecodeHelper.getMethodDescriptor(method.getReturnType(), method.getParameterTypes());
      if (Modifier.isStatic(method.getModifiers())) {
         genLoadParameters(2, mv, helper, method);
         mv.visitMethodInsn(184, type, method.getName(), descriptor);
      } else {
         mv.visitVarInsn(25, 1);
         helper.doCast(callClass);
         genLoadParameters(2, mv, helper, method);
         mv.visitMethodInsn(useInterface ? 185 : 182, type, method.getName(), descriptor);
      }

      helper.box(method.getReturnType());
      if (method.getReturnType() == Void.TYPE) {
         mv.visitInsn(1);
      }

      mv.visitInsn(176);
      mv.visitMaxs(0, 0);
      mv.visitEnd();
   }

   private static void genInvokeWithFixedParams(ClassWriter cw, Method method) {
      int pc = method.getParameterTypes().length;
      if (pc <= 4) {
         StringBuilder pdescb = new StringBuilder();

         for(int i = 0; i != pc; ++i) {
            pdescb.append("Ljava/lang/Object;");
         }

         String pdesc = pdescb.toString();
         MethodVisitor mv = cw.visitMethod(1, "invoke", "(Ljava/lang/Object;" + pdesc + ")Ljava/lang/Object;", (String)null, EXCEPTIONS);
         mv.visitCode();
         BytecodeHelper helper = new BytecodeHelper(mv);
         Class callClass = method.getDeclaringClass();
         boolean useInterface = callClass.isInterface();
         String type = BytecodeHelper.getClassInternalName(callClass.getName());
         String descriptor = BytecodeHelper.getMethodDescriptor(method.getReturnType(), method.getParameterTypes());
         if (Modifier.isStatic(method.getModifiers())) {
            genLoadParametersDirect(2, mv, helper, method);
            mv.visitMethodInsn(184, type, method.getName(), descriptor);
         } else {
            mv.visitVarInsn(25, 1);
            helper.doCast(callClass);
            genLoadParametersDirect(2, mv, helper, method);
            mv.visitMethodInsn(useInterface ? 185 : 182, type, method.getName(), descriptor);
         }

         helper.box(method.getReturnType());
         if (method.getReturnType() == Void.TYPE) {
            mv.visitInsn(1);
         }

         mv.visitInsn(176);
         mv.visitMaxs(0, 0);
         mv.visitEnd();
      }

   }

   private static void genInvokeWithFixedPrimitiveParams(ClassWriter cw, Method method) {
      Class<?>[] pt = method.getParameterTypes();
      int pc = pt.length;
      if (pc > 0 && pc <= 3) {
         StringBuilder pdescb = new StringBuilder();
         boolean hasPrimitive = false;

         for(int i = 0; i != pc; ++i) {
            if (pt[i].isPrimitive()) {
               hasPrimitive = true;
               pdescb.append(BytecodeHelper.getTypeDescription(pt[i]));
            } else {
               pdescb.append("Ljava/lang/Object;");
            }
         }

         if (!hasPrimitive) {
            return;
         }

         String pdesc = pdescb.toString();
         MethodVisitor mv = cw.visitMethod(1, "invoke", "(Ljava/lang/Object;" + pdesc + ")Ljava/lang/Object;", (String)null, EXCEPTIONS);
         mv.visitCode();
         BytecodeHelper helper = new BytecodeHelper(mv);
         Class callClass = method.getDeclaringClass();
         boolean useInterface = callClass.isInterface();
         String type = BytecodeHelper.getClassInternalName(callClass.getName());
         String descriptor = BytecodeHelper.getMethodDescriptor(method.getReturnType(), method.getParameterTypes());
         if (Modifier.isStatic(method.getModifiers())) {
            genLoadParametersPrimitiveDirect(2, mv, helper, method);
            mv.visitMethodInsn(184, type, method.getName(), descriptor);
         } else {
            mv.visitVarInsn(25, 1);
            helper.doCast(callClass);
            genLoadParametersPrimitiveDirect(2, mv, helper, method);
            mv.visitMethodInsn(useInterface ? 185 : 182, type, method.getName(), descriptor);
         }

         helper.box(method.getReturnType());
         if (method.getReturnType() == Void.TYPE) {
            mv.visitInsn(1);
         }

         mv.visitInsn(176);
         mv.visitMaxs(0, 0);
         mv.visitEnd();
      }

   }

   private static class ReflectiveMethodHandle extends MethodHandle {
      private final Method method;

      public ReflectiveMethodHandle(Method method) {
         this.method = method;
         method.setAccessible(true);
      }

      public Object invoke(Object receiver, Object[] args) throws Throwable {
         return this.method.invoke(receiver, args);
      }
   }
}
