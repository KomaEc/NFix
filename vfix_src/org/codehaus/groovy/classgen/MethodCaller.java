package org.codehaus.groovy.classgen;

import groovyjarjarasm.asm.MethodVisitor;
import groovyjarjarasm.asm.Opcodes;
import groovyjarjarasm.asm.Type;
import java.lang.reflect.Method;

public class MethodCaller implements Opcodes {
   private int opcode;
   private String internalName;
   private String name;
   private Class theClass;
   private String methodDescriptor;

   public static MethodCaller newStatic(Class theClass, String name) {
      return new MethodCaller(184, theClass, name);
   }

   public static MethodCaller newInterface(Class theClass, String name) {
      return new MethodCaller(185, theClass, name);
   }

   public static MethodCaller newVirtual(Class theClass, String name) {
      return new MethodCaller(182, theClass, name);
   }

   public MethodCaller(int opcode, Class theClass, String name) {
      this.opcode = opcode;
      this.internalName = Type.getInternalName(theClass);
      this.theClass = theClass;
      this.name = name;
   }

   public void call(MethodVisitor methodVisitor) {
      methodVisitor.visitMethodInsn(this.opcode, this.internalName, this.name, this.getMethodDescriptor());
   }

   public String getMethodDescriptor() {
      if (this.methodDescriptor == null) {
         Method method = this.getMethod();
         this.methodDescriptor = Type.getMethodDescriptor(method);
      }

      return this.methodDescriptor;
   }

   protected Method getMethod() {
      Method[] methods = this.theClass.getMethods();

      for(int i = 0; i < methods.length; ++i) {
         Method method = methods[i];
         if (method.getName().equals(this.name)) {
            return method;
         }
      }

      throw new ClassGeneratorException("Could not find method: " + this.name + " on class: " + this.theClass);
   }
}
