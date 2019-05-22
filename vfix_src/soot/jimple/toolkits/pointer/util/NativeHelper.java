package soot.jimple.toolkits.pointer.util;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.AbstractObject;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;

public abstract class NativeHelper {
   public void assign(ReferenceVariable lhs, ReferenceVariable rhs) {
      this.assignImpl(lhs, rhs);
   }

   public void assignObjectTo(ReferenceVariable lhs, AbstractObject obj) {
      this.assignObjectToImpl(lhs, obj);
   }

   public void throwException(AbstractObject obj) {
      this.throwExceptionImpl(obj);
   }

   public ReferenceVariable arrayElementOf(ReferenceVariable base) {
      return this.arrayElementOfImpl(base);
   }

   public ReferenceVariable cloneObject(ReferenceVariable source) {
      return this.cloneObjectImpl(source);
   }

   public ReferenceVariable newInstanceOf(ReferenceVariable cls) {
      return this.newInstanceOfImpl(cls);
   }

   public ReferenceVariable staticField(String className, String fieldName) {
      return this.staticFieldImpl(className, fieldName);
   }

   public ReferenceVariable tempField(String fieldsig) {
      return this.tempFieldImpl(fieldsig);
   }

   public ReferenceVariable tempVariable() {
      return this.tempVariableImpl();
   }

   public ReferenceVariable tempLocalVariable(SootMethod method) {
      return this.tempLocalVariableImpl(method);
   }

   protected abstract void assignImpl(ReferenceVariable var1, ReferenceVariable var2);

   protected abstract void assignObjectToImpl(ReferenceVariable var1, AbstractObject var2);

   protected abstract void throwExceptionImpl(AbstractObject var1);

   protected abstract ReferenceVariable arrayElementOfImpl(ReferenceVariable var1);

   protected abstract ReferenceVariable cloneObjectImpl(ReferenceVariable var1);

   protected abstract ReferenceVariable newInstanceOfImpl(ReferenceVariable var1);

   protected abstract ReferenceVariable staticFieldImpl(String var1, String var2);

   protected abstract ReferenceVariable tempFieldImpl(String var1);

   protected abstract ReferenceVariable tempVariableImpl();

   protected abstract ReferenceVariable tempLocalVariableImpl(SootMethod var1);
}
