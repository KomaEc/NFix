package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.AbstractObject;
import soot.jimple.toolkits.pointer.representations.Environment;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;

public class JavaLangClassNative extends NativeMethodClass {
   public JavaLangClassNative(NativeHelper helper) {
      super(helper);
   }

   public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      String subSignature = method.getSubSignature();
      if (subSignature.equals("java.lang.Class forName0(java.lang.String,boolean,java.lang.ClassLoader)")) {
         this.java_lang_Class_forName0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.Object newInstance0()")) {
         this.java_lang_Class_newInstance0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.String getName()")) {
         this.java_lang_Class_getName(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.ClassLoader getClassLoader0()")) {
         this.java_lang_Class_getClassLoader0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.Class getSuperclass()")) {
         this.java_lang_Class_getSuperclass(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.Class[] getInterfaces()")) {
         this.java_lang_Class_getInterfaces(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.Class getComponentType()")) {
         this.java_lang_Class_getComponentType(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.Object[] getSigners()")) {
         this.java_lang_Class_getSigners(method, thisVar, returnVar, params);
      } else if (subSignature.equals("void setSigners(java.lang.Object[])")) {
         this.java_lang_Class_setSigners(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.Class getDeclaringClass()")) {
         this.java_lang_Class_getDeclaringClass(method, thisVar, returnVar, params);
      } else if (subSignature.equals("void setProtectionDomain0(java.security.ProtectionDomain)")) {
         this.java_lang_Class_setProtectionDomain0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.security.ProtectionDomain getProtectionDomain0()")) {
         this.java_lang_Class_getProtectionDomain0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.Class getPrimitiveClass(java.lang.String)")) {
         this.java_lang_Class_getPrimitiveClass(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.reflect.Field[] getFields0(int)")) {
         this.java_lang_Class_getFields0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.reflect.Method[] getMethods0(int)")) {
         this.java_lang_Class_getMethods0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.reflect.Constructor[] getConstructors0(int)")) {
         this.java_lang_Class_getConstructors0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.reflect.Field getField0(java.lang.String,int)")) {
         this.java_lang_Class_getField0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.reflect.Method getMethod0(java.lang.String,java.lang.Class[],int)")) {
         this.java_lang_Class_getMethod0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.reflect.Constructor getConstructor0(java.lang.Class[],int)")) {
         this.java_lang_Class_getConstructor0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.Class[] getDeclaredClasses0()")) {
         this.java_lang_Class_getDeclaredClasses0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.reflect.Constructor[] getDeclaredConstructors0(boolean)")) {
         this.java_lang_Class_getDeclaredConstructors0(method, thisVar, returnVar, params);
      } else {
         defaultMethod(method, thisVar, returnVar, params);
      }
   }

   public void java_lang_Class_forName0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getClassObject());
   }

   public void java_lang_Class_newInstance0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      ReferenceVariable instanceVar = this.helper.newInstanceOf(thisVar);
      this.helper.assign(returnVar, instanceVar);
   }

   public void java_lang_Class_getName(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getStringObject());
   }

   public void java_lang_Class_getClassLoader0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getClassLoaderObject());
   }

   public void java_lang_Class_getSuperclass(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getClassObject());
   }

   public void java_lang_Class_getInterfaces(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getClassObject());
   }

   public void java_lang_Class_getComponentType(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getClassObject());
   }

   public void java_lang_Class_setSigners(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      ReferenceVariable tempFld = this.helper.tempField("<java.lang.Class signers>");
      this.helper.assign(tempFld, params[0]);
   }

   public void java_lang_Class_getSigners(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      ReferenceVariable tempFld = this.helper.tempField("<java.lang.Class signers>");
      this.helper.assign(returnVar, tempFld);
   }

   public void java_lang_Class_getDeclaringClass(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getClassObject());
   }

   public void java_lang_Class_setProtectionDomain0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      ReferenceVariable protdmn = this.helper.tempField("<java.lang.Class ProtDmn>");
      this.helper.assign(protdmn, params[0]);
   }

   public void java_lang_Class_getProtectionDomain0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      ReferenceVariable protdmn = this.helper.tempField("<java.lang.Class ProtDmn>");
      this.helper.assign(returnVar, protdmn);
   }

   public void java_lang_Class_getPrimitiveClass(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getClassObject());
   }

   public void java_lang_Class_getFields0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getLeastArrayObject());
   }

   public void java_lang_Class_getMethods0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getLeastArrayObject());
   }

   public void java_lang_Class_getConstructors0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getLeastArrayObject());
   }

   public void java_lang_Class_getField0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getFieldObject());
   }

   public void java_lang_Class_getMethod0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getMethodObject());
   }

   public void java_lang_Class_getConstructor0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getConstructorObject());
   }

   public void java_lang_Class_getDeclaredClasses0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getLeastArrayObject());
   }

   public void java_lang_Class_getDeclaredConstructors0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      AbstractObject array = Environment.v().getLeastArrayObject();
      AbstractObject cons = Environment.v().getConstructorObject();
      this.helper.assignObjectTo(returnVar, array);
      this.helper.assignObjectTo(this.helper.arrayElementOf(returnVar), cons);
   }
}
