package bsh;

import bsh.org.objectweb.asm.ClassWriter;
import bsh.org.objectweb.asm.CodeVisitor;
import bsh.org.objectweb.asm.Constants;
import bsh.org.objectweb.asm.Label;
import bsh.org.objectweb.asm.Type;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ClassGeneratorUtil implements Constants {
   static final String BSHSTATIC = "_bshStatic";
   static final String BSHTHIS = "_bshThis";
   static final String BSHSUPER = "_bshSuper";
   static final String BSHINIT = "_bshInstanceInitializer";
   static final String BSHCONSTRUCTORS = "_bshConstructors";
   static final int DEFAULTCONSTRUCTOR = -1;
   static final String OBJECT = "Ljava/lang/Object;";
   String className;
   String fqClassName;
   Class superClass;
   String superClassName;
   Class[] interfaces;
   Variable[] vars;
   Constructor[] superConstructors;
   DelayedEvalBshMethod[] constructors;
   DelayedEvalBshMethod[] methods;
   NameSpace classStaticNameSpace;
   Modifiers classModifiers;
   boolean isInterface;
   // $FF: synthetic field
   static Class class$java$lang$Object;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class array$Ljava$lang$Object;
   // $FF: synthetic field
   static Class class$bsh$Interpreter;
   // $FF: synthetic field
   static Class class$bsh$CallStack;
   // $FF: synthetic field
   static Class class$bsh$SimpleNode;

   public ClassGeneratorUtil(Modifiers var1, String var2, String var3, Class var4, Class[] var5, Variable[] var6, DelayedEvalBshMethod[] var7, NameSpace var8, boolean var9) {
      this.classModifiers = var1;
      this.className = var2;
      if (var3 != null) {
         this.fqClassName = var3.replace('.', '/') + "/" + var2;
      } else {
         this.fqClassName = var2;
      }

      if (var4 == null) {
         var4 = class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object;
      }

      this.superClass = var4;
      this.superClassName = Type.getInternalName(var4);
      if (var5 == null) {
         var5 = new Class[0];
      }

      this.interfaces = var5;
      this.vars = var6;
      this.classStaticNameSpace = var8;
      this.superConstructors = var4.getDeclaredConstructors();
      ArrayList var10 = new ArrayList();
      ArrayList var11 = new ArrayList();
      String var12 = getBaseName(var2);

      for(int var13 = 0; var13 < var7.length; ++var13) {
         if (var7[var13].getName().equals(var12)) {
            var10.add(var7[var13]);
         } else {
            var11.add(var7[var13]);
         }
      }

      this.constructors = (DelayedEvalBshMethod[])var10.toArray(new DelayedEvalBshMethod[0]);
      this.methods = (DelayedEvalBshMethod[])var11.toArray(new DelayedEvalBshMethod[0]);

      try {
         var8.setLocalVariable("_bshConstructors", this.constructors, false);
      } catch (UtilEvalError var15) {
         throw new InterpreterError("can't set cons var");
      }

      this.isInterface = var9;
   }

   public byte[] generateClass() {
      int var1 = getASMModifiers(this.classModifiers) | 1;
      if (this.isInterface) {
         var1 |= 512;
      }

      String[] var2 = new String[this.interfaces.length];

      for(int var3 = 0; var3 < this.interfaces.length; ++var3) {
         var2[var3] = Type.getInternalName(this.interfaces[var3]);
      }

      String var4 = "BeanShell Generated via ASM (www.objectweb.org)";
      ClassWriter var5 = new ClassWriter(false);
      var5.visit(var1, this.fqClassName, this.superClassName, var2, var4);
      if (!this.isInterface) {
         generateField("_bshThis" + this.className, "Lbsh/This;", 1, var5);
         generateField("_bshStatic" + this.className, "Lbsh/This;", 9, var5);
      }

      int var8;
      for(int var6 = 0; var6 < this.vars.length; ++var6) {
         String var7 = this.vars[var6].getTypeDescriptor();
         if (!this.vars[var6].hasModifier("private") && var7 != null) {
            if (this.isInterface) {
               var8 = 25;
            } else {
               var8 = getASMModifiers(this.vars[var6].getModifiers());
            }

            generateField(this.vars[var6].getName(), var7, var8, var5);
         }
      }

      boolean var14 = false;

      int var9;
      for(var8 = 0; var8 < this.constructors.length; ++var8) {
         if (!this.constructors[var8].hasModifier("private")) {
            var9 = getASMModifiers(this.constructors[var8].getModifiers());
            this.generateConstructor(var8, this.constructors[var8].getParamTypeDescriptors(), var9, var5);
            var14 = true;
         }
      }

      if (!this.isInterface && !var14) {
         this.generateConstructor(-1, new String[0], 1, var5);
      }

      for(var9 = 0; var9 < this.methods.length; ++var9) {
         String var10 = this.methods[var9].getReturnTypeDescriptor();
         if (!this.methods[var9].hasModifier("private")) {
            int var11 = getASMModifiers(this.methods[var9].getModifiers());
            if (this.isInterface) {
               var11 |= 1025;
            }

            generateMethod(this.className, this.fqClassName, this.methods[var9].getName(), var10, this.methods[var9].getParamTypeDescriptors(), var11, var5);
            boolean var12 = (var11 & 8) > 0;
            boolean var13 = this.classContainsMethod(this.superClass, this.methods[var9].getName(), this.methods[var9].getParamTypeDescriptors());
            if (!var12 && var13) {
               generateSuperDelegateMethod(this.superClassName, this.methods[var9].getName(), var10, this.methods[var9].getParamTypeDescriptors(), var11, var5);
            }
         }
      }

      return var5.toByteArray();
   }

   static int getASMModifiers(Modifiers var0) {
      int var1 = 0;
      if (var0 == null) {
         return var1;
      } else {
         if (var0.hasModifier("public")) {
            ++var1;
         }

         if (var0.hasModifier("protected")) {
            var1 += 4;
         }

         if (var0.hasModifier("static")) {
            var1 += 8;
         }

         if (var0.hasModifier("synchronized")) {
            var1 += 32;
         }

         if (var0.hasModifier("abstract")) {
            var1 += 1024;
         }

         return var1;
      }
   }

   static void generateField(String var0, String var1, int var2, ClassWriter var3) {
      var3.visitField(var2, var0, var1, (Object)null);
   }

   static void generateMethod(String var0, String var1, String var2, String var3, String[] var4, int var5, ClassWriter var6) {
      Object var7 = null;
      boolean var8 = (var5 & 8) != 0;
      if (var3 == null) {
         var3 = "Ljava/lang/Object;";
      }

      String var9 = getMethodDescriptor(var3, var4);
      CodeVisitor var10 = var6.visitMethod(var5, var2, var9, (String[])var7);
      if ((var5 & 1024) == 0) {
         if (var8) {
            var10.visitFieldInsn(178, var1, "_bshStatic" + var0, "Lbsh/This;");
         } else {
            var10.visitVarInsn(25, 0);
            var10.visitFieldInsn(180, var1, "_bshThis" + var0, "Lbsh/This;");
         }

         var10.visitLdcInsn(var2);
         generateParameterReifierCode(var4, var8, var10);
         var10.visitInsn(1);
         var10.visitInsn(1);
         var10.visitInsn(1);
         var10.visitInsn(4);
         var10.visitMethodInsn(182, "bsh/This", "invokeMethod", Type.getMethodDescriptor(Type.getType(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object), new Type[]{Type.getType(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String), Type.getType(array$Ljava$lang$Object == null ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object), Type.getType(class$bsh$Interpreter == null ? (class$bsh$Interpreter = class$("bsh.Interpreter")) : class$bsh$Interpreter), Type.getType(class$bsh$CallStack == null ? (class$bsh$CallStack = class$("bsh.CallStack")) : class$bsh$CallStack), Type.getType(class$bsh$SimpleNode == null ? (class$bsh$SimpleNode = class$("bsh.SimpleNode")) : class$bsh$SimpleNode), Type.getType(Boolean.TYPE)}));
         var10.visitMethodInsn(184, "bsh/Primitive", "unwrap", "(Ljava/lang/Object;)Ljava/lang/Object;");
         generateReturnCode(var3, var10);
         var10.visitMaxs(20, 20);
      }
   }

   void generateConstructor(int var1, String[] var2, int var3, ClassWriter var4) {
      int var5 = var2.length + 1;
      int var6 = var2.length + 2;
      Object var7 = null;
      String var8 = getMethodDescriptor("V", var2);
      CodeVisitor var9 = var4.visitMethod(var3, "<init>", var8, (String[])var7);
      generateParameterReifierCode(var2, false, var9);
      var9.visitVarInsn(58, var5);
      this.generateConstructorSwitch(var1, var5, var6, var9);
      var9.visitVarInsn(25, 0);
      var9.visitLdcInsn(this.className);
      var9.visitVarInsn(25, var5);
      var9.visitMethodInsn(184, "bsh/ClassGeneratorUtil", "initInstance", "(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)V");
      var9.visitInsn(177);
      var9.visitMaxs(20, 20);
   }

   void generateConstructorSwitch(int var1, int var2, int var3, CodeVisitor var4) {
      Label var5 = new Label();
      Label var6 = new Label();
      int var7 = this.superConstructors.length + this.constructors.length;
      Label[] var8 = new Label[var7];

      for(int var9 = 0; var9 < var7; ++var9) {
         var8[var9] = new Label();
      }

      var4.visitLdcInsn(this.superClass.getName());
      var4.visitFieldInsn(178, this.fqClassName, "_bshStatic" + this.className, "Lbsh/This;");
      var4.visitVarInsn(25, var2);
      var4.visitIntInsn(16, var1);
      var4.visitMethodInsn(184, "bsh/ClassGeneratorUtil", "getConstructorArgs", "(Ljava/lang/String;Lbsh/This;[Ljava/lang/Object;I)Lbsh/ClassGeneratorUtil$ConstructorArgs;");
      var4.visitVarInsn(58, var3);
      var4.visitVarInsn(25, var3);
      var4.visitFieldInsn(180, "bsh/ClassGeneratorUtil$ConstructorArgs", "selector", "I");
      var4.visitTableSwitchInsn(0, var7 - 1, var5, var8);
      int var10 = 0;

      for(int var11 = 0; var11 < this.superConstructors.length; ++var10) {
         doSwitchBranch(var10, this.superClassName, getTypeDescriptors(this.superConstructors[var11].getParameterTypes()), var6, var8, var3, var4);
         ++var11;
      }

      for(int var12 = 0; var12 < this.constructors.length; ++var10) {
         doSwitchBranch(var10, this.fqClassName, this.constructors[var12].getParamTypeDescriptors(), var6, var8, var3, var4);
         ++var12;
      }

      var4.visitLabel(var5);
      var4.visitVarInsn(25, 0);
      var4.visitMethodInsn(183, this.superClassName, "<init>", "()V");
      var4.visitLabel(var6);
   }

   static void doSwitchBranch(int var0, String var1, String[] var2, Label var3, Label[] var4, int var5, CodeVisitor var6) {
      var6.visitLabel(var4[var0]);
      var6.visitVarInsn(25, 0);

      String var8;
      for(int var7 = 0; var7 < var2.length; ++var7) {
         var8 = var2[var7];
         String var9 = null;
         if (var8.equals("Z")) {
            var9 = "getBoolean";
         } else if (var8.equals("B")) {
            var9 = "getByte";
         } else if (var8.equals("C")) {
            var9 = "getChar";
         } else if (var8.equals("S")) {
            var9 = "getShort";
         } else if (var8.equals("I")) {
            var9 = "getInt";
         } else if (var8.equals("J")) {
            var9 = "getLong";
         } else if (var8.equals("D")) {
            var9 = "getDouble";
         } else if (var8.equals("F")) {
            var9 = "getFloat";
         } else {
            var9 = "getObject";
         }

         var6.visitVarInsn(25, var5);
         String var10 = "bsh/ClassGeneratorUtil$ConstructorArgs";
         String var11;
         if (var9.equals("getObject")) {
            var11 = "Ljava/lang/Object;";
         } else {
            var11 = var8;
         }

         var6.visitMethodInsn(182, var10, var9, "()" + var11);
         if (var9.equals("getObject")) {
            var6.visitTypeInsn(192, descriptorToClassName(var8));
         }
      }

      var8 = getMethodDescriptor("V", var2);
      var6.visitMethodInsn(183, var1, "<init>", var8);
      var6.visitJumpInsn(167, var3);
   }

   static String getMethodDescriptor(String var0, String[] var1) {
      StringBuffer var2 = new StringBuffer("(");

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.append(var1[var3]);
      }

      var2.append(")" + var0);
      return var2.toString();
   }

   static void generateSuperDelegateMethod(String var0, String var1, String var2, String[] var3, int var4, ClassWriter var5) {
      Object var6 = null;
      if (var2 == null) {
         var2 = "Ljava/lang/Object;";
      }

      String var7 = getMethodDescriptor(var2, var3);
      CodeVisitor var8 = var5.visitMethod(var4, "_bshSuper" + var1, var7, (String[])var6);
      var8.visitVarInsn(25, 0);
      int var9 = 1;

      for(int var10 = 0; var10 < var3.length; ++var10) {
         if (isPrimitive(var3[var10])) {
            var8.visitVarInsn(21, var9);
         } else {
            var8.visitVarInsn(25, var9);
         }

         var9 += !var3[var10].equals("D") && !var3[var10].equals("J") ? 1 : 2;
      }

      var8.visitMethodInsn(183, var0, var1, var7);
      generatePlainReturnCode(var2, var8);
      var8.visitMaxs(20, 20);
   }

   boolean classContainsMethod(Class var1, String var2, String[] var3) {
      while(var1 != null) {
         Method[] var4 = var1.getDeclaredMethods();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getName().equals(var2)) {
               String[] var6 = getTypeDescriptors(var4[var5].getParameterTypes());
               boolean var7 = true;

               for(int var8 = 0; var8 < var6.length; ++var8) {
                  if (!var3[var8].equals(var6[var8])) {
                     var7 = false;
                     break;
                  }
               }

               if (var7) {
                  return true;
               }
            }
         }

         var1 = var1.getSuperclass();
      }

      return false;
   }

   static void generatePlainReturnCode(String var0, CodeVisitor var1) {
      if (var0.equals("V")) {
         var1.visitInsn(177);
      } else if (isPrimitive(var0)) {
         short var2 = 172;
         if (var0.equals("D")) {
            var2 = 175;
         } else if (var0.equals("F")) {
            var2 = 174;
         } else if (var0.equals("J")) {
            var2 = 173;
         }

         var1.visitInsn(var2);
      } else {
         var1.visitTypeInsn(192, descriptorToClassName(var0));
         var1.visitInsn(176);
      }

   }

   public static void generateParameterReifierCode(String[] var0, boolean var1, CodeVisitor var2) {
      var2.visitIntInsn(17, var0.length);
      var2.visitTypeInsn(189, "java/lang/Object");
      int var3 = var1 ? 0 : 1;

      for(int var4 = 0; var4 < var0.length; ++var4) {
         String var5 = var0[var4];
         var2.visitInsn(89);
         var2.visitIntInsn(17, var4);
         if (isPrimitive(var5)) {
            byte var6;
            if (var5.equals("F")) {
               var6 = 23;
            } else if (var5.equals("D")) {
               var6 = 24;
            } else if (var5.equals("J")) {
               var6 = 22;
            } else {
               var6 = 21;
            }

            String var7 = "bsh/Primitive";
            var2.visitTypeInsn(187, var7);
            var2.visitInsn(89);
            var2.visitVarInsn(var6, var3);
            var2.visitMethodInsn(183, var7, "<init>", "(" + var5 + ")V");
         } else {
            var2.visitVarInsn(25, var3);
         }

         var2.visitInsn(83);
         var3 += !var5.equals("D") && !var5.equals("J") ? 1 : 2;
      }

   }

   public static void generateReturnCode(String var0, CodeVisitor var1) {
      if (var0.equals("V")) {
         var1.visitInsn(87);
         var1.visitInsn(177);
      } else if (isPrimitive(var0)) {
         short var2 = 172;
         String var3;
         String var4;
         if (var0.equals("B")) {
            var3 = "java/lang/Byte";
            var4 = "byteValue";
         } else if (var0.equals("I")) {
            var3 = "java/lang/Integer";
            var4 = "intValue";
         } else if (var0.equals("Z")) {
            var3 = "java/lang/Boolean";
            var4 = "booleanValue";
         } else if (var0.equals("D")) {
            var2 = 175;
            var3 = "java/lang/Double";
            var4 = "doubleValue";
         } else if (var0.equals("F")) {
            var2 = 174;
            var3 = "java/lang/Float";
            var4 = "floatValue";
         } else if (var0.equals("J")) {
            var2 = 173;
            var3 = "java/lang/Long";
            var4 = "longValue";
         } else if (var0.equals("C")) {
            var3 = "java/lang/Character";
            var4 = "charValue";
         } else {
            var3 = "java/lang/Short";
            var4 = "shortValue";
         }

         var1.visitTypeInsn(192, var3);
         var1.visitMethodInsn(182, var3, var4, "()" + var0);
         var1.visitInsn(var2);
      } else {
         var1.visitTypeInsn(192, descriptorToClassName(var0));
         var1.visitInsn(176);
      }

   }

   public static ClassGeneratorUtil.ConstructorArgs getConstructorArgs(String var0, This var1, Object[] var2, int var3) {
      DelayedEvalBshMethod[] var4;
      try {
         var4 = (DelayedEvalBshMethod[])var1.getNameSpace().getVariable("_bshConstructors");
      } catch (Exception var26) {
         throw new InterpreterError("unable to get instance initializer: " + var26);
      }

      if (var3 == -1) {
         return ClassGeneratorUtil.ConstructorArgs.DEFAULT;
      } else {
         DelayedEvalBshMethod var5 = var4[var3];
         if (var5.methodBody.jjtGetNumChildren() == 0) {
            return ClassGeneratorUtil.ConstructorArgs.DEFAULT;
         } else {
            String var6 = null;
            BSHArguments var7 = null;
            SimpleNode var8 = (SimpleNode)var5.methodBody.jjtGetChild(0);
            if (var8 instanceof BSHPrimaryExpression) {
               var8 = (SimpleNode)var8.jjtGetChild(0);
            }

            if (var8 instanceof BSHMethodInvocation) {
               BSHMethodInvocation var9 = (BSHMethodInvocation)var8;
               BSHAmbiguousName var10 = var9.getNameNode();
               if (var10.text.equals("super") || var10.text.equals("this")) {
                  var6 = var10.text;
                  var7 = var9.getArgsNode();
               }
            }

            if (var6 == null) {
               return ClassGeneratorUtil.ConstructorArgs.DEFAULT;
            } else {
               NameSpace var27 = new NameSpace(var1.getNameSpace(), "consArgs");
               String[] var28 = var5.getParameterNames();
               Class[] var11 = var5.getParameterTypes();

               for(int var12 = 0; var12 < var2.length; ++var12) {
                  try {
                     var27.setTypedVariable(var28[var12], var11[var12], var2[var12], (Modifiers)null);
                  } catch (UtilEvalError var25) {
                     throw new InterpreterError("err setting local cons arg:" + var25);
                  }
               }

               CallStack var13 = new CallStack();
               var13.push(var27);
               Object[] var14 = null;
               Interpreter var15 = var1.declaringInterpreter;

               try {
                  var14 = var7.getArguments(var13, var15);
               } catch (EvalError var24) {
                  throw new InterpreterError("Error evaluating constructor args: " + var24);
               }

               Class[] var16 = Types.getTypes(var14);
               var14 = Primitive.unwrap(var14);
               Class var17 = var15.getClassManager().classForName(var0);
               if (var17 == null) {
                  throw new InterpreterError("can't find superclass: " + var0);
               } else {
                  Constructor[] var18 = var17.getDeclaredConstructors();
                  if (var6.equals("super")) {
                     int var29 = Reflect.findMostSpecificConstructorIndex(var16, var18);
                     if (var29 == -1) {
                        throw new InterpreterError("can't find constructor for args!");
                     } else {
                        return new ClassGeneratorUtil.ConstructorArgs(var29, var14);
                     }
                  } else {
                     Class[][] var19 = new Class[var4.length][];

                     for(int var20 = 0; var20 < var19.length; ++var20) {
                        var19[var20] = var4[var20].getParameterTypes();
                     }

                     int var21 = Reflect.findMostSpecificSignature(var16, var19);
                     if (var21 == -1) {
                        throw new InterpreterError("can't find constructor for args 2!");
                     } else {
                        int var22 = var21 + var18.length;
                        int var23 = var3 + var18.length;
                        if (var22 == var23) {
                           throw new InterpreterError("Recusive constructor call.");
                        } else {
                           return new ClassGeneratorUtil.ConstructorArgs(var22, var14);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static void initInstance(Object var0, String var1, Object[] var2) {
      Class[] var3 = Types.getTypes(var2);
      CallStack var4 = new CallStack();
      This var5 = getClassInstanceThis(var0, var1);
      Interpreter var7;
      NameSpace var10;
      if (var5 == null) {
         This var6 = getClassStaticThis(var0.getClass(), var1);
         var7 = var6.declaringInterpreter;

         BSHBlock var8;
         try {
            var8 = (BSHBlock)var6.getNameSpace().getVariable("_bshInstanceInitializer");
         } catch (Exception var13) {
            throw new InterpreterError("unable to get instance initializer: " + var13);
         }

         var10 = new NameSpace(var6.getNameSpace(), var1);
         var10.isClass = true;
         var5 = var10.getThis(var7);

         try {
            LHS var9 = Reflect.getLHSObjectField(var0, "_bshThis" + var1);
            var9.assign(var5, false);
         } catch (Exception var12) {
            throw new InterpreterError("Error in class gen setup: " + var12);
         }

         var10.setClassInstance(var0);
         var4.push(var10);

         try {
            var8.evalBlock(var4, var7, true, ClassGeneratorImpl.ClassNodeFilter.CLASSINSTANCE);
         } catch (Exception var11) {
            throw new InterpreterError("Error in class initialization: " + var11);
         }

         var4.pop();
      } else {
         var7 = var5.declaringInterpreter;
         var10 = var5.getNameSpace();
      }

      String var15 = getBaseName(var1);

      try {
         BshMethod var17 = var10.getMethod(var15, var3, true);
         if (var2.length > 0 && var17 == null) {
            throw new InterpreterError("Can't find constructor: " + var1);
         } else {
            if (var17 != null) {
               var17.invoke(var2, var7, var4, (SimpleNode)null, false);
            }

         }
      } catch (Exception var14) {
         Exception var16 = var14;
         if (var14 instanceof TargetError) {
            var16 = (Exception)((TargetError)var14).getTarget();
         }

         if (var16 instanceof InvocationTargetException) {
            var16 = (Exception)((InvocationTargetException)var16).getTargetException();
         }

         var16.printStackTrace(System.err);
         throw new InterpreterError("Error in class initialization: " + var16);
      }
   }

   static This getClassStaticThis(Class var0, String var1) {
      try {
         return (This)Reflect.getStaticFieldValue(var0, "_bshStatic" + var1);
      } catch (Exception var3) {
         throw new InterpreterError("Unable to get class static space: " + var3);
      }
   }

   static This getClassInstanceThis(Object var0, String var1) {
      try {
         Object var2 = Reflect.getObjectFieldValue(var0, "_bshThis" + var1);
         return (This)Primitive.unwrap(var2);
      } catch (Exception var3) {
         throw new InterpreterError("Generated class: Error getting This" + var3);
      }
   }

   private static boolean isPrimitive(String var0) {
      return var0.length() == 1;
   }

   static String[] getTypeDescriptors(Class[] var0) {
      String[] var1 = new String[var0.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = BSHType.getTypeDescriptor(var0[var2]);
      }

      return var1;
   }

   private static String descriptorToClassName(String var0) {
      return !var0.startsWith("[") && var0.startsWith("L") ? var0.substring(1, var0.length() - 1) : var0;
   }

   private static String getBaseName(String var0) {
      int var1 = var0.indexOf("$");
      return var1 == -1 ? var0 : var0.substring(var1 + 1);
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static class ConstructorArgs {
      public static ClassGeneratorUtil.ConstructorArgs DEFAULT = new ClassGeneratorUtil.ConstructorArgs();
      public int selector = -1;
      Object[] args;
      int arg = 0;

      ConstructorArgs() {
      }

      ConstructorArgs(int var1, Object[] var2) {
         this.selector = var1;
         this.args = var2;
      }

      Object next() {
         return this.args[this.arg++];
      }

      public boolean getBoolean() {
         return (Boolean)this.next();
      }

      public byte getByte() {
         return (Byte)this.next();
      }

      public char getChar() {
         return (Character)this.next();
      }

      public short getShort() {
         return (Short)this.next();
      }

      public int getInt() {
         return (Integer)this.next();
      }

      public long getLong() {
         return (Long)this.next();
      }

      public double getDouble() {
         return (Double)this.next();
      }

      public float getFloat() {
         return (Float)this.next();
      }

      public Object getObject() {
         return this.next();
      }
   }
}
