package com.gzoltar.shaded.javassist.convert;

import com.gzoltar.shaded.javassist.CannotCompileException;
import com.gzoltar.shaded.javassist.CtClass;
import com.gzoltar.shaded.javassist.bytecode.BadBytecode;
import com.gzoltar.shaded.javassist.bytecode.CodeAttribute;
import com.gzoltar.shaded.javassist.bytecode.CodeIterator;
import com.gzoltar.shaded.javassist.bytecode.ConstPool;
import com.gzoltar.shaded.javassist.bytecode.MethodInfo;
import com.gzoltar.shaded.javassist.bytecode.Opcode;

public abstract class Transformer implements Opcode {
   private Transformer next;

   public Transformer(Transformer t) {
      this.next = t;
   }

   public Transformer getNext() {
      return this.next;
   }

   public void initialize(ConstPool cp, CodeAttribute attr) {
   }

   public void initialize(ConstPool cp, CtClass clazz, MethodInfo minfo) throws CannotCompileException {
      this.initialize(cp, minfo.getCodeAttribute());
   }

   public void clean() {
   }

   public abstract int transform(CtClass clazz, int pos, CodeIterator it, ConstPool cp) throws CannotCompileException, BadBytecode;

   public int extraLocals() {
      return 0;
   }

   public int extraStack() {
      return 0;
   }
}
