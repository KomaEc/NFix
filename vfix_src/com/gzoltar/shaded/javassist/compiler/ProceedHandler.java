package com.gzoltar.shaded.javassist.compiler;

import com.gzoltar.shaded.javassist.bytecode.Bytecode;
import com.gzoltar.shaded.javassist.compiler.ast.ASTList;

public interface ProceedHandler {
   void doit(JvstCodeGen gen, Bytecode b, ASTList args) throws CompileError;

   void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError;
}
