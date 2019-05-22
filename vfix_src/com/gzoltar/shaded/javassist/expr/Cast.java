package com.gzoltar.shaded.javassist.expr;

import com.gzoltar.shaded.javassist.CannotCompileException;
import com.gzoltar.shaded.javassist.ClassPool;
import com.gzoltar.shaded.javassist.CtBehavior;
import com.gzoltar.shaded.javassist.CtClass;
import com.gzoltar.shaded.javassist.NotFoundException;
import com.gzoltar.shaded.javassist.bytecode.BadBytecode;
import com.gzoltar.shaded.javassist.bytecode.Bytecode;
import com.gzoltar.shaded.javassist.bytecode.CodeAttribute;
import com.gzoltar.shaded.javassist.bytecode.CodeIterator;
import com.gzoltar.shaded.javassist.bytecode.ConstPool;
import com.gzoltar.shaded.javassist.bytecode.MethodInfo;
import com.gzoltar.shaded.javassist.compiler.CompileError;
import com.gzoltar.shaded.javassist.compiler.Javac;
import com.gzoltar.shaded.javassist.compiler.JvstCodeGen;
import com.gzoltar.shaded.javassist.compiler.JvstTypeChecker;
import com.gzoltar.shaded.javassist.compiler.ProceedHandler;
import com.gzoltar.shaded.javassist.compiler.ast.ASTList;

public class Cast extends Expr {
   protected Cast(int pos, CodeIterator i, CtClass declaring, MethodInfo m) {
      super(pos, i, declaring, m);
   }

   public CtBehavior where() {
      return super.where();
   }

   public int getLineNumber() {
      return super.getLineNumber();
   }

   public String getFileName() {
      return super.getFileName();
   }

   public CtClass getType() throws NotFoundException {
      ConstPool cp = this.getConstPool();
      int pos = this.currentPos;
      int index = this.iterator.u16bitAt(pos + 1);
      String name = cp.getClassInfo(index);
      return this.thisClass.getClassPool().getCtClass(name);
   }

   public CtClass[] mayThrow() {
      return super.mayThrow();
   }

   public void replace(String statement) throws CannotCompileException {
      this.thisClass.getClassFile();
      ConstPool constPool = this.getConstPool();
      int pos = this.currentPos;
      int index = this.iterator.u16bitAt(pos + 1);
      Javac jc = new Javac(this.thisClass);
      ClassPool cp = this.thisClass.getClassPool();
      CodeAttribute ca = this.iterator.get();

      try {
         CtClass[] params = new CtClass[]{cp.get("java.lang.Object")};
         CtClass retType = this.getType();
         int paramVar = ca.getMaxLocals();
         jc.recordParams("java.lang.Object", params, true, paramVar, this.withinStatic());
         int retVar = jc.recordReturnType(retType, true);
         jc.recordProceed(new Cast.ProceedForCast(index, retType));
         checkResultValue(retType, statement);
         Bytecode bytecode = jc.getBytecode();
         storeStack(params, true, paramVar, bytecode);
         jc.recordLocalVariables(ca, pos);
         bytecode.addConstZero(retType);
         bytecode.addStore(retVar, retType);
         jc.compileStmnt(statement);
         bytecode.addLoad(retVar, retType);
         this.replace0(pos, bytecode, 3);
      } catch (CompileError var13) {
         throw new CannotCompileException(var13);
      } catch (NotFoundException var14) {
         throw new CannotCompileException(var14);
      } catch (BadBytecode var15) {
         throw new CannotCompileException("broken method");
      }
   }

   static class ProceedForCast implements ProceedHandler {
      int index;
      CtClass retType;

      ProceedForCast(int i, CtClass t) {
         this.index = i;
         this.retType = t;
      }

      public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
         if (gen.getMethodArgsLength(args) != 1) {
            throw new CompileError("$proceed() cannot take more than one parameter for cast");
         } else {
            gen.atMethodArgs(args, new int[1], new int[1], new String[1]);
            bytecode.addOpcode(192);
            bytecode.addIndex(this.index);
            gen.setType(this.retType);
         }
      }

      public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
         c.atMethodArgs(args, new int[1], new int[1], new String[1]);
         c.setType(this.retType);
      }
   }
}
