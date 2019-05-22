package soot.javaToJimple;

import java.util.ArrayList;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.MethodSource;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Trap;
import soot.Unit;
import soot.VoidType;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.Expr;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.tagkit.ThrowCreatedByCompilerTag;

public class AssertClassMethodSource implements MethodSource {
   public Body getBody(SootMethod sootMethod, String phaseName) {
      Body classBody = Jimple.v().newBody(sootMethod);
      ParameterRef paramRef = Jimple.v().newParameterRef(RefType.v("java.lang.String"), 0);
      Local paramLocal = Jimple.v().newLocal("$r0", RefType.v("java.lang.String"));
      classBody.getLocals().add(paramLocal);
      Stmt stmt = Jimple.v().newIdentityStmt(paramLocal, paramRef);
      classBody.getUnits().add((Unit)stmt);
      ArrayList paramTypes = new ArrayList();
      paramTypes.add(RefType.v("java.lang.String"));
      SootMethodRef methodToInvoke = Scene.v().makeMethodRef(Scene.v().getSootClass("java.lang.Class"), "forName", paramTypes, RefType.v("java.lang.Class"), true);
      Local invokeLocal = Jimple.v().newLocal("$r1", RefType.v("java.lang.Class"));
      classBody.getLocals().add(invokeLocal);
      ArrayList params = new ArrayList();
      params.add(paramLocal);
      Expr invokeExpr = Jimple.v().newStaticInvokeExpr(methodToInvoke, (List)params);
      Stmt assign = Jimple.v().newAssignStmt(invokeLocal, invokeExpr);
      classBody.getUnits().add((Unit)assign);
      Stmt retStmt = Jimple.v().newReturnStmt(invokeLocal);
      classBody.getUnits().add((Unit)retStmt);
      Local catchRefLocal = Jimple.v().newLocal("$r2", RefType.v("java.lang.ClassNotFoundException"));
      classBody.getLocals().add(catchRefLocal);
      CaughtExceptionRef caughtRef = Jimple.v().newCaughtExceptionRef();
      Stmt caughtIdentity = Jimple.v().newIdentityStmt(catchRefLocal, caughtRef);
      classBody.getUnits().add((Unit)caughtIdentity);
      Local noClassDefLocal = Jimple.v().newLocal("$r3", RefType.v("java.lang.NoClassDefFoundError"));
      classBody.getLocals().add(noClassDefLocal);
      Expr newExpr = Jimple.v().newNewExpr(RefType.v("java.lang.NoClassDefFoundError"));
      Stmt noClassDefAssign = Jimple.v().newAssignStmt(noClassDefLocal, newExpr);
      classBody.getUnits().add((Unit)noClassDefAssign);
      paramTypes = new ArrayList();
      SootMethodRef initMethToInvoke = Scene.v().makeMethodRef(Scene.v().getSootClass("java.lang.NoClassDefFoundError"), "<init>", paramTypes, VoidType.v(), false);
      params = new ArrayList();
      Expr initInvoke = Jimple.v().newSpecialInvokeExpr(noClassDefLocal, initMethToInvoke, (List)params);
      Stmt initStmt = Jimple.v().newInvokeStmt(initInvoke);
      classBody.getUnits().add((Unit)initStmt);
      Local throwLocal = Jimple.v().newLocal("$r4", RefType.v("java.lang.Throwable"));
      classBody.getLocals().add(throwLocal);
      paramTypes = new ArrayList();
      paramTypes.add(RefType.v("java.lang.Throwable"));
      params = new ArrayList();
      params.add(catchRefLocal);
      SootMethodRef messageMethToInvoke = Scene.v().makeMethodRef(Scene.v().getSootClass("java.lang.Throwable"), "initCause", paramTypes, RefType.v("java.lang.Throwable"), false);
      Expr messageInvoke = Jimple.v().newVirtualInvokeExpr(noClassDefLocal, messageMethToInvoke, (List)params);
      Stmt messageAssign = Jimple.v().newAssignStmt(throwLocal, messageInvoke);
      classBody.getUnits().add((Unit)messageAssign);
      Stmt throwStmt = Jimple.v().newThrowStmt(throwLocal);
      throwStmt.addTag(new ThrowCreatedByCompilerTag());
      classBody.getUnits().add((Unit)throwStmt);
      Trap trap = Jimple.v().newTrap(Scene.v().getSootClass("java.lang.ClassNotFoundException"), (Unit)assign, (Unit)retStmt, (Unit)caughtIdentity);
      classBody.getTraps().add(trap);
      return classBody;
   }
}
