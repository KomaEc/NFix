package com.gzoltar.shaded.javassist.expr;

import com.gzoltar.shaded.javassist.CtClass;
import com.gzoltar.shaded.javassist.CtConstructor;
import com.gzoltar.shaded.javassist.CtMethod;
import com.gzoltar.shaded.javassist.NotFoundException;
import com.gzoltar.shaded.javassist.bytecode.CodeIterator;
import com.gzoltar.shaded.javassist.bytecode.MethodInfo;

public class ConstructorCall extends MethodCall {
   protected ConstructorCall(int pos, CodeIterator i, CtClass decl, MethodInfo m) {
      super(pos, i, decl, m);
   }

   public String getMethodName() {
      return this.isSuper() ? "super" : "this";
   }

   public CtMethod getMethod() throws NotFoundException {
      throw new NotFoundException("this is a constructor call.  Call getConstructor().");
   }

   public CtConstructor getConstructor() throws NotFoundException {
      return this.getCtClass().getConstructor(this.getSignature());
   }

   public boolean isSuper() {
      return super.isSuper();
   }
}
