package soot.javaToJimple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import polyglot.types.MethodInstance;
import soot.Body;
import soot.Local;
import soot.MethodSource;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;

public class PrivateMethodAccMethodSource implements MethodSource {
   private MethodInstance methodInst;

   public PrivateMethodAccMethodSource(MethodInstance methInst) {
      this.methodInst = methInst;
   }

   public void setMethodInst(MethodInstance mi) {
      this.methodInst = mi;
   }

   private boolean isCallParamType(Type sootType) {
      Iterator it = this.methodInst.formalTypes().iterator();

      Type compareType;
      do {
         if (!it.hasNext()) {
            return false;
         }

         compareType = Util.getSootType((polyglot.types.Type)it.next());
      } while(!compareType.equals(sootType));

      return true;
   }

   public Body getBody(SootMethod sootMethod, String phaseName) {
      Body body = Jimple.v().newBody(sootMethod);
      LocalGenerator lg = new LocalGenerator(body);
      Local base = null;
      ArrayList methParams = new ArrayList();
      ArrayList methParamsTypes = new ArrayList();
      Iterator paramIt = sootMethod.getParameterTypes().iterator();

      Type type;
      Local returnLocal;
      IdentityStmt invoke;
      for(int paramCounter = 0; paramIt.hasNext(); ++paramCounter) {
         type = (Type)paramIt.next();
         returnLocal = lg.generateLocal(type);
         ParameterRef paramRef = Jimple.v().newParameterRef(type, paramCounter);
         invoke = Jimple.v().newIdentityStmt(returnLocal, paramRef);
         body.getUnits().add((Unit)invoke);
         if (!this.isCallParamType(type)) {
            base = returnLocal;
         } else {
            methParams.add(returnLocal);
            methParamsTypes.add(returnLocal.getType());
         }
      }

      type = Util.getSootType(this.methodInst.returnType());
      returnLocal = null;
      if (!(type instanceof VoidType)) {
         returnLocal = lg.generateLocal(type);
      }

      SootMethodRef meth = Scene.v().makeMethodRef(((RefType)Util.getSootType(this.methodInst.container())).getSootClass(), this.methodInst.name(), methParamsTypes, Util.getSootType(this.methodInst.returnType()), this.methodInst.flags().isStatic());
      invoke = null;
      Object invoke;
      if (this.methodInst.flags().isStatic()) {
         invoke = Jimple.v().newStaticInvokeExpr(meth, (List)methParams);
      } else {
         invoke = Jimple.v().newSpecialInvokeExpr(base, meth, (List)methParams);
      }

      Stmt stmt = null;
      if (!(type instanceof VoidType)) {
         stmt = Jimple.v().newAssignStmt(returnLocal, (Value)invoke);
      } else {
         stmt = Jimple.v().newInvokeStmt((Value)invoke);
      }

      body.getUnits().add((Unit)stmt);
      Stmt retStmt = null;
      if (!(type instanceof VoidType)) {
         retStmt = Jimple.v().newReturnStmt(returnLocal);
      } else {
         retStmt = Jimple.v().newReturnVoidStmt();
      }

      body.getUnits().add((Unit)retStmt);
      return body;
   }
}
