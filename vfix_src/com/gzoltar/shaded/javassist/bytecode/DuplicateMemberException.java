package com.gzoltar.shaded.javassist.bytecode;

import com.gzoltar.shaded.javassist.CannotCompileException;

public class DuplicateMemberException extends CannotCompileException {
   public DuplicateMemberException(String msg) {
      super(msg);
   }
}
