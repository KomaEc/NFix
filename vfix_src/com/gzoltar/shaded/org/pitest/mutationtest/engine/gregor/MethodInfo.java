package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.reloc.asm.Type;

public class MethodInfo {
   private final ClassInfo owningClass;
   private final int access;
   private final String methodName;
   private final String methodDescriptor;

   public MethodInfo() {
      this(new ClassInfo(0, 0, "", "", "", new String[0]), 0, "", "()V");
   }

   private MethodInfo(ClassInfo owningClass, int access, String name, String methodDescriptor) {
      this.owningClass = owningClass;
      this.access = access;
      this.methodName = name;
      this.methodDescriptor = methodDescriptor;
   }

   public String getDescription() {
      return this.owningClass.getName() + "::" + this.getName();
   }

   public String getName() {
      return this.methodName;
   }

   public String getMethodDescriptor() {
      return this.methodDescriptor;
   }

   public String toString() {
      return "MethodInfo [access=" + this.access + ", desc=" + this.methodDescriptor + ",  name=" + this.methodName + "]";
   }

   public boolean isStatic() {
      return (this.access & 8) != 0;
   }

   public boolean isSynthetic() {
      return (this.access & 4096) != 0;
   }

   public boolean isConstructor() {
      return isConstructor(this.methodName);
   }

   public static boolean isConstructor(String methodName) {
      return "<init>".equals(methodName);
   }

   public Type getReturnType() {
      return Type.getReturnType(this.methodDescriptor);
   }

   public static boolean isVoid(String desc) {
      return Type.getReturnType(desc).equals(Type.VOID_TYPE);
   }

   public boolean isStaticInitializer() {
      return "<clinit>".equals(this.methodName);
   }

   public boolean isVoid() {
      return isVoid(this.methodDescriptor);
   }

   public boolean takesNoParameters() {
      return this.methodDescriptor.startsWith("()");
   }

   public boolean isInGroovyClass() {
      return this.owningClass.isGroovyClass();
   }

   public boolean isGeneratedEnumMethod() {
      return this.owningClass.isEnum() && (this.isValuesMethod() || this.isValueOfMethod() || this.isStaticInitializer());
   }

   private boolean isValuesMethod() {
      return this.getName().equals("values") && this.takesNoParameters() && this.isStatic();
   }

   private boolean isValueOfMethod() {
      return this.getName().equals("valueOf") && this.methodDescriptor.startsWith("(Ljava/lang/String;)") && this.isStatic();
   }

   public MethodInfo withMethodDescriptor(String newDescriptor) {
      return new MethodInfo(this.owningClass, this.access, this.methodName, newDescriptor);
   }

   public MethodInfo withAccess(int accessModifier) {
      return new MethodInfo(this.owningClass, accessModifier, this.methodName, this.methodDescriptor);
   }

   public MethodInfo withMethodName(String newMethodName) {
      return new MethodInfo(this.owningClass, this.access, newMethodName, this.methodDescriptor);
   }

   public MethodInfo withOwner(ClassInfo newOwnerClass) {
      return new MethodInfo(newOwnerClass, this.access, this.methodName, this.methodDescriptor);
   }
}
