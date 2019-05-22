package bsh;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ClassGeneratorImpl extends ClassGenerator {
   public Class generateClass(String var1, Modifiers var2, Class[] var3, Class var4, BSHBlock var5, boolean var6, CallStack var7, Interpreter var8) throws EvalError {
      return generateClassImpl(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public Object invokeSuperclassMethod(BshClassManager var1, Object var2, String var3, Object[] var4) throws UtilEvalError, ReflectError, InvocationTargetException {
      return invokeSuperclassMethodImpl(var1, var2, var3, var4);
   }

   public void setInstanceNameSpaceParent(Object var1, String var2, NameSpace var3) {
      This var4 = ClassGeneratorUtil.getClassInstanceThis(var1, var2);
      var4.getNameSpace().setParent(var3);
   }

   public static Class generateClassImpl(String var0, Modifiers var1, Class[] var2, Class var3, BSHBlock var4, boolean var5, CallStack var6, Interpreter var7) throws EvalError {
      try {
         Capabilities.setAccessibility(true);
      } catch (Capabilities.Unavailable var25) {
         throw new EvalError("Defining classes currently requires reflective Accessibility.", var4, var6);
      }

      NameSpace var8 = var6.top();
      String var9 = var8.getPackage();
      String var10 = var8.isClass ? var8.getName() + "$" + var0 : var0;
      String var11 = var9 == null ? var10 : var9 + "." + var10;
      BshClassManager var12 = var7.getClassManager();
      var12.definingClass(var11);
      NameSpace var13 = new NameSpace(var8, var10);
      var13.isClass = true;
      var6.push(var13);
      var4.evalBlock(var6, var7, true, ClassGeneratorImpl.ClassNodeFilter.CLASSCLASSES);
      Variable[] var14 = getDeclaredVariables(var4, var6, var7, var9);
      DelayedEvalBshMethod[] var15 = getDeclaredMethods(var4, var6, var7, var9);
      ClassGeneratorUtil var16 = new ClassGeneratorUtil(var1, var10, var9, var3, var2, var14, var15, var13, var5);
      byte[] var17 = var16.generateClass();
      String var18 = System.getProperty("debugClasses");
      if (var18 != null) {
         try {
            FileOutputStream var19 = new FileOutputStream(var18 + "/" + var10 + ".class");
            var19.write(var17);
            var19.close();
         } catch (IOException var24) {
         }
      }

      Class var26 = var12.defineClass(var11, var17);
      var8.importClass(var11.replace('$', '.'));

      try {
         var13.setLocalVariable("_bshInstanceInitializer", var4, false);
      } catch (UtilEvalError var23) {
         throw new InterpreterError("unable to init static: " + var23);
      }

      var13.setClassStatic(var26);
      var4.evalBlock(var6, var7, true, ClassGeneratorImpl.ClassNodeFilter.CLASSSTATIC);
      var6.pop();
      if (!var26.isInterface()) {
         String var20 = "_bshStatic" + var10;

         try {
            LHS var21 = Reflect.getLHSStaticField(var26, var20);
            var21.assign(var13.getThis(var7), false);
         } catch (Exception var22) {
            throw new InterpreterError("Error in class gen setup: " + var22);
         }
      }

      var12.doneDefiningClass(var11);
      return var26;
   }

   static Variable[] getDeclaredVariables(BSHBlock var0, CallStack var1, Interpreter var2, String var3) {
      ArrayList var4 = new ArrayList();

      for(int var5 = 0; var5 < var0.jjtGetNumChildren(); ++var5) {
         SimpleNode var6 = (SimpleNode)var0.jjtGetChild(var5);
         if (var6 instanceof BSHTypedVariableDeclaration) {
            BSHTypedVariableDeclaration var7 = (BSHTypedVariableDeclaration)var6;
            Modifiers var8 = var7.modifiers;
            String var9 = var7.getTypeDescriptor(var1, var2, var3);
            BSHVariableDeclarator[] var10 = var7.getDeclarators();

            for(int var11 = 0; var11 < var10.length; ++var11) {
               String var12 = var10[var11].name;

               try {
                  Variable var13 = new Variable(var12, var9, (Object)null, var8);
                  var4.add(var13);
               } catch (UtilEvalError var14) {
               }
            }
         }
      }

      return (Variable[])var4.toArray(new Variable[0]);
   }

   static DelayedEvalBshMethod[] getDeclaredMethods(BSHBlock var0, CallStack var1, Interpreter var2, String var3) throws EvalError {
      ArrayList var4 = new ArrayList();

      for(int var5 = 0; var5 < var0.jjtGetNumChildren(); ++var5) {
         SimpleNode var6 = (SimpleNode)var0.jjtGetChild(var5);
         if (var6 instanceof BSHMethodDeclaration) {
            BSHMethodDeclaration var7 = (BSHMethodDeclaration)var6;
            var7.insureNodesParsed();
            Modifiers var8 = var7.modifiers;
            String var9 = var7.name;
            String var10 = var7.getReturnTypeDescriptor(var1, var2, var3);
            BSHReturnType var11 = var7.getReturnTypeNode();
            BSHFormalParameters var12 = var7.paramsNode;
            String[] var13 = var12.getTypeDescriptors(var1, var2, var3);
            DelayedEvalBshMethod var14 = new DelayedEvalBshMethod(var9, var10, var11, var7.paramsNode.getParamNames(), var13, var12, var7.blockNode, (NameSpace)null, var8, var1, var2);
            var4.add(var14);
         }
      }

      return (DelayedEvalBshMethod[])var4.toArray(new DelayedEvalBshMethod[0]);
   }

   public static Object invokeSuperclassMethodImpl(BshClassManager var0, Object var1, String var2, Object[] var3) throws UtilEvalError, ReflectError, InvocationTargetException {
      String var4 = "_bshSuper" + var2;
      Class var5 = var1.getClass();
      Method var6 = Reflect.resolveJavaMethod(var0, var5, var4, Types.getTypes(var3), false);
      if (var6 != null) {
         return Reflect.invokeMethod(var6, var1, var3);
      } else {
         Class var7 = var5.getSuperclass();
         var6 = Reflect.resolveExpectedJavaMethod(var0, var7, var1, var2, var3, false);
         return Reflect.invokeMethod(var6, var1, var3);
      }
   }

   static class ClassNodeFilter implements BSHBlock.NodeFilter {
      public static final int STATIC = 0;
      public static final int INSTANCE = 1;
      public static final int CLASSES = 2;
      public static ClassGeneratorImpl.ClassNodeFilter CLASSSTATIC = new ClassGeneratorImpl.ClassNodeFilter(0);
      public static ClassGeneratorImpl.ClassNodeFilter CLASSINSTANCE = new ClassGeneratorImpl.ClassNodeFilter(1);
      public static ClassGeneratorImpl.ClassNodeFilter CLASSCLASSES = new ClassGeneratorImpl.ClassNodeFilter(2);
      int context;

      private ClassNodeFilter(int var1) {
         this.context = var1;
      }

      public boolean isVisible(SimpleNode var1) {
         if (this.context == 2) {
            return var1 instanceof BSHClassDeclaration;
         } else if (var1 instanceof BSHClassDeclaration) {
            return false;
         } else if (this.context == 0) {
            return this.isStatic(var1);
         } else if (this.context == 1) {
            return !this.isStatic(var1);
         } else {
            return true;
         }
      }

      boolean isStatic(SimpleNode var1) {
         if (var1 instanceof BSHTypedVariableDeclaration) {
            return ((BSHTypedVariableDeclaration)var1).modifiers != null && ((BSHTypedVariableDeclaration)var1).modifiers.hasModifier("static");
         } else if (!(var1 instanceof BSHMethodDeclaration)) {
            return var1 instanceof BSHBlock ? false : false;
         } else {
            return ((BSHMethodDeclaration)var1).modifiers != null && ((BSHMethodDeclaration)var1).modifiers.hasModifier("static");
         }
      }
   }
}
