package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyRuntimeException;
import groovyjarjarasm.asm.ClassWriter;
import groovyjarjarasm.asm.Label;
import groovyjarjarasm.asm.MethodVisitor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.classgen.BytecodeHelper;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.CachedMethod;

public class CallSiteGenerator {
   private static final String GRE = BytecodeHelper.getClassInternalName(ClassHelper.make(GroovyRuntimeException.class));

   private CallSiteGenerator() {
   }

   private static MethodVisitor writeMethod(ClassWriter cw, String name, int argumentCount, String superClass, CachedMethod cachedMethod, String receiverType, String parameterDescription, boolean useArray) {
      MethodVisitor mv = cw.visitMethod(1, "call" + name, "(L" + receiverType + ";" + parameterDescription + ")Ljava/lang/Object;", (String)null, (String[])null);
      mv.visitCode();
      Label tryStart = new Label();
      mv.visitLabel(tryStart);

      for(int i = 0; i < argumentCount; ++i) {
         mv.visitVarInsn(25, i);
      }

      mv.visitMethodInsn(182, superClass, "checkCall", "(Ljava/lang/Object;" + parameterDescription + ")Z");
      Label l0 = new Label();
      mv.visitJumpInsn(153, l0);
      BytecodeHelper helper = new BytecodeHelper(mv);
      Class callClass = cachedMethod.getDeclaringClass().getTheClass();
      boolean useInterface = callClass.isInterface();
      String type = BytecodeHelper.getClassInternalName(callClass.getName());
      String descriptor = BytecodeHelper.getMethodDescriptor(cachedMethod.getReturnType(), cachedMethod.getNativeParameterTypes());
      int invokeMethodCode = 182;
      if (cachedMethod.isStatic()) {
         invokeMethodCode = 184;
      } else {
         mv.visitVarInsn(25, 1);
         helper.doCast(callClass);
         if (useInterface) {
            invokeMethodCode = 185;
         }
      }

      Method method = cachedMethod.setAccessible();
      Class<?>[] parameters = method.getParameterTypes();
      int size = parameters.length;

      int i;
      for(i = 0; i < size; ++i) {
         if (useArray) {
            mv.visitVarInsn(25, 2);
            helper.pushConstant(i);
            mv.visitInsn(50);
         } else {
            mv.visitVarInsn(25, i + 2);
         }

         Class parameterType = parameters[i];
         if (parameterType.isPrimitive()) {
            helper.unbox(parameterType);
         } else {
            helper.doCast(parameterType);
         }
      }

      mv.visitMethodInsn(invokeMethodCode, type, cachedMethod.getName(), descriptor);
      helper.box(cachedMethod.getReturnType());
      if (cachedMethod.getReturnType() == Void.TYPE) {
         mv.visitInsn(1);
      }

      mv.visitInsn(176);
      mv.visitLabel(l0);

      for(i = 0; i < argumentCount; ++i) {
         mv.visitVarInsn(25, i);
      }

      if (!useArray) {
         mv.visitMethodInsn(184, "org/codehaus/groovy/runtime/ArrayUtil", "createArray", "(" + parameterDescription + ")[Ljava/lang/Object;");
      }

      mv.visitMethodInsn(184, "org/codehaus/groovy/runtime/callsite/CallSiteArray", "defaultCall" + name, "(Lorg/codehaus/groovy/runtime/callsite/CallSite;L" + receiverType + ";[Ljava/lang/Object;)Ljava/lang/Object;");
      mv.visitInsn(176);
      Label tryEnd = new Label();
      mv.visitLabel(tryEnd);
      Label catchStart = new Label();
      mv.visitLabel(catchStart);
      mv.visitMethodInsn(184, "org/codehaus/groovy/runtime/ScriptBytecodeAdapter", "unwrap", "(Lgroovy/lang/GroovyRuntimeException;)Ljava/lang/Throwable;");
      mv.visitInsn(191);
      mv.visitTryCatchBlock(tryStart, tryEnd, catchStart, GRE);
      mv.visitMaxs(0, 0);
      mv.visitEnd();
      return mv;
   }

   public static void genCallWithFixedParams(ClassWriter cw, String name, String superClass, CachedMethod cachedMethod, String receiverType) {
      if (cachedMethod.getParamsCount() <= 4) {
         StringBuilder pdescb = new StringBuilder();
         int pc = cachedMethod.getParamsCount();

         for(int i = 0; i != pc; ++i) {
            pdescb.append("Ljava/lang/Object;");
         }

         writeMethod(cw, name, pc + 2, superClass, cachedMethod, receiverType, pdescb.toString(), false);
      }
   }

   public static void genCallXxxWithArray(ClassWriter cw, String name, String superClass, CachedMethod cachedMethod, String receiverType) {
      writeMethod(cw, name, 3, superClass, cachedMethod, receiverType, "[Ljava/lang/Object;", true);
   }

   private static void genConstructor(ClassWriter cw, String superClass) {
      MethodVisitor mv = cw.visitMethod(1, "<init>", "(Lorg/codehaus/groovy/runtime/callsite/CallSite;Lgroovy/lang/MetaClassImpl;Lgroovy/lang/MetaMethod;[Ljava/lang/Class;)V", (String)null, (String[])null);
      mv.visitCode();
      mv.visitVarInsn(25, 0);
      mv.visitVarInsn(25, 1);
      mv.visitVarInsn(25, 2);
      mv.visitVarInsn(25, 3);
      mv.visitVarInsn(25, 4);
      mv.visitMethodInsn(183, superClass, "<init>", "(Lorg/codehaus/groovy/runtime/callsite/CallSite;Lgroovy/lang/MetaClassImpl;Lgroovy/lang/MetaMethod;[Ljava/lang/Class;)V");
      mv.visitInsn(177);
      mv.visitMaxs(0, 0);
      mv.visitEnd();
   }

   public static byte[] genPogoMetaMethodSite(CachedMethod cachedMethod, ClassWriter cw, String name) {
      cw.visit(48, 4097, name.replace('.', '/'), (String)null, "org/codehaus/groovy/runtime/callsite/PogoMetaMethodSite", (String[])null);
      genConstructor(cw, "org/codehaus/groovy/runtime/callsite/PogoMetaMethodSite");
      genCallXxxWithArray(cw, "Current", "org/codehaus/groovy/runtime/callsite/PogoMetaMethodSite", cachedMethod, "groovy/lang/GroovyObject");
      genCallXxxWithArray(cw, "", "org/codehaus/groovy/runtime/callsite/PogoMetaMethodSite", cachedMethod, "java/lang/Object");
      genCallWithFixedParams(cw, "Current", "org/codehaus/groovy/runtime/callsite/PogoMetaMethodSite", cachedMethod, "groovy/lang/GroovyObject");
      genCallWithFixedParams(cw, "", "org/codehaus/groovy/runtime/callsite/PogoMetaMethodSite", cachedMethod, "java/lang/Object");
      cw.visitEnd();
      return cw.toByteArray();
   }

   public static byte[] genPojoMetaMethodSite(CachedMethod cachedMethod, ClassWriter cw, String name) {
      cw.visit(48, 4097, name.replace('.', '/'), (String)null, "org/codehaus/groovy/runtime/callsite/PojoMetaMethodSite", (String[])null);
      genConstructor(cw, "org/codehaus/groovy/runtime/callsite/PojoMetaMethodSite");
      genCallXxxWithArray(cw, "", "org/codehaus/groovy/runtime/callsite/PojoMetaMethodSite", cachedMethod, "java/lang/Object");
      genCallWithFixedParams(cw, "", "org/codehaus/groovy/runtime/callsite/PojoMetaMethodSite", cachedMethod, "java/lang/Object");
      cw.visitEnd();
      return cw.toByteArray();
   }

   public static byte[] genStaticMetaMethodSite(CachedMethod cachedMethod, ClassWriter cw, String name) {
      cw.visit(48, 4097, name.replace('.', '/'), (String)null, "org/codehaus/groovy/runtime/callsite/StaticMetaMethodSite", (String[])null);
      genConstructor(cw, "org/codehaus/groovy/runtime/callsite/StaticMetaMethodSite");
      genCallXxxWithArray(cw, "", "org/codehaus/groovy/runtime/callsite/StaticMetaMethodSite", cachedMethod, "java/lang/Object");
      genCallXxxWithArray(cw, "Static", "org/codehaus/groovy/runtime/callsite/StaticMetaMethodSite", cachedMethod, "java/lang/Class");
      genCallWithFixedParams(cw, "", "org/codehaus/groovy/runtime/callsite/StaticMetaMethodSite", cachedMethod, "java/lang/Object");
      genCallWithFixedParams(cw, "Static", "org/codehaus/groovy/runtime/callsite/StaticMetaMethodSite", cachedMethod, "java/lang/Class");
      cw.visitEnd();
      return cw.toByteArray();
   }

   public static Constructor compilePogoMethod(CachedMethod cachedMethod) {
      ClassWriter cw = new ClassWriter(1);
      CachedClass declClass = cachedMethod.getDeclaringClass();
      CallSiteClassLoader callSiteLoader = declClass.getCallSiteLoader();
      String name = callSiteLoader.createClassName(cachedMethod.setAccessible());
      byte[] bytes = genPogoMetaMethodSite(cachedMethod, cw, name);
      return callSiteLoader.defineClassAndGetConstructor(name, bytes);
   }

   public static Constructor compilePojoMethod(CachedMethod cachedMethod) {
      ClassWriter cw = new ClassWriter(1);
      CachedClass declClass = cachedMethod.getDeclaringClass();
      CallSiteClassLoader callSiteLoader = declClass.getCallSiteLoader();
      String name = callSiteLoader.createClassName(cachedMethod.setAccessible());
      byte[] bytes = genPojoMetaMethodSite(cachedMethod, cw, name);
      return callSiteLoader.defineClassAndGetConstructor(name, bytes);
   }

   public static Constructor compileStaticMethod(CachedMethod cachedMethod) {
      ClassWriter cw = new ClassWriter(1);
      CachedClass declClass = cachedMethod.getDeclaringClass();
      CallSiteClassLoader callSiteLoader = declClass.getCallSiteLoader();
      String name = callSiteLoader.createClassName(cachedMethod.setAccessible());
      byte[] bytes = genStaticMetaMethodSite(cachedMethod, cw, name);
      return callSiteLoader.defineClassAndGetConstructor(name, bytes);
   }

   public static boolean isCompilable(CachedMethod method) {
      return GroovySunClassLoader.sunVM != null || Modifier.isPublic(method.cachedClass.getModifiers()) && method.isPublic() && publicParams(method);
   }

   private static boolean publicParams(CachedMethod method) {
      Class[] arr$ = method.getNativeParameterTypes();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class nativeParamType = arr$[i$];
         if (!Modifier.isPublic(nativeParamType.getModifiers())) {
            return false;
         }
      }

      return true;
   }
}
