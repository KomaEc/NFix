package com.gzoltar.shaded.javassist.compiler.ast;

import com.gzoltar.shaded.javassist.CtField;
import com.gzoltar.shaded.javassist.compiler.CompileError;

public class Member extends Symbol {
   private CtField field = null;

   public Member(String name) {
      super(name);
   }

   public void setField(CtField f) {
      this.field = f;
   }

   public CtField getField() {
      return this.field;
   }

   public void accept(Visitor v) throws CompileError {
      v.atMember(this);
   }
}
