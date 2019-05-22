package soot.jimple.toolkits.invoke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.Body;
import soot.G;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.util.Chain;

public class SynchronizerManager {
   public HashMap<SootClass, SootField> classToClassField = new HashMap();

   public SynchronizerManager(Singletons.Global g) {
   }

   public static SynchronizerManager v() {
      return G.v().soot_jimple_toolkits_invoke_SynchronizerManager();
   }

   public Local addStmtsToFetchClassBefore(JimpleBody jb, Stmt target) {
      SootClass sc = jb.getMethod().getDeclaringClass();
      SootField classCacher = (SootField)this.classToClassField.get(sc);
      String lName;
      if (classCacher == null) {
         for(lName = "class$" + sc.getName().replace('.', '$'); sc.declaresFieldByName(lName); lName = "_" + lName) {
         }

         classCacher = Scene.v().makeSootField(lName, RefType.v("java.lang.Class"), 8);
         sc.addField(classCacher);
         this.classToClassField.put(sc, classCacher);
      }

      lName = "$uniqueClass";

      while(true) {
         Iterator it = jb.getLocals().iterator();
         boolean oops = false;

         while(it.hasNext()) {
            Local jbLocal = (Local)it.next();
            if (jbLocal.getName().equals(lName)) {
               oops = true;
            }
         }

         if (!oops) {
            Local l = Jimple.v().newLocal(lName, RefType.v("java.lang.Class"));
            jb.getLocals().add(l);
            Chain units = jb.getUnits();
            units.insertBefore((Object)Jimple.v().newAssignStmt(l, Jimple.v().newStaticFieldRef(classCacher.makeRef())), target);
            IfStmt ifStmt;
            units.insertBefore((Object)(ifStmt = Jimple.v().newIfStmt(Jimple.v().newNeExpr(l, NullConstant.v()), (Unit)target)), target);
            units.insertBefore((Object)Jimple.v().newAssignStmt(l, Jimple.v().newStaticInvokeExpr(this.getClassFetcherFor(sc).makeRef(), Arrays.asList(StringConstant.v(sc.getName())))), target);
            units.insertBefore((Object)Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(classCacher.makeRef()), l), target);
            ifStmt.setTarget(target);
            return l;
         }

         lName = "_" + lName;
      }
   }

   public SootMethod getClassFetcherFor(SootClass c) {
      String methodName = "class$";

      while(true) {
         SootMethod m = c.getMethodByNameUnsafe(methodName);
         if (m == null) {
            return this.createClassFetcherFor(c, methodName);
         }

         if (m.getSignature().equals("<" + c.getName().replace('.', '$') + ": java.lang.Class " + methodName + "(java.lang.String)>")) {
            Body b = null;
            b = m.retrieveActiveBody();
            Iterator unitsIt = b.getUnits().iterator();
            if (unitsIt.hasNext()) {
               Stmt s = (Stmt)unitsIt.next();
               if (s instanceof IdentityStmt) {
                  IdentityStmt is = (IdentityStmt)s;
                  Value lo = is.getLeftOp();
                  Value ro = is.getRightOp();
                  if (ro instanceof ParameterRef) {
                     ParameterRef pr = (ParameterRef)ro;
                     if (pr.getIndex() == 0 && unitsIt.hasNext()) {
                        s = (Stmt)unitsIt.next();
                        if (s instanceof AssignStmt) {
                           AssignStmt as = (AssignStmt)s;
                           Value retVal = as.getLeftOp();
                           Value ie = as.getRightOp();
                           if (ie.toString().equals(".staticinvoke <java.lang.Class: java.lang.Class forName(java.lang.String)>(" + lo + ")") && unitsIt.hasNext()) {
                              s = (Stmt)unitsIt.next();
                              if (s instanceof ReturnStmt) {
                                 ReturnStmt rs = (ReturnStmt)s;
                                 if (rs.getOp().equivTo(retVal)) {
                                    return m;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         methodName = "_" + methodName;
      }
   }

   public SootMethod createClassFetcherFor(SootClass c, String methodName) {
      SootMethod method = Scene.v().makeSootMethod(methodName, Arrays.asList(RefType.v("java.lang.String")), RefType.v("java.lang.Class"), 8);
      c.addMethod(method);
      JimpleBody body = Jimple.v().newBody(method);
      method.setActiveBody(body);
      Chain units = body.getUnits();
      Local l_r0 = Jimple.v().newLocal("r0", RefType.v("java.lang.String"));
      Local l_r1 = Jimple.v().newLocal("r1", RefType.v("java.lang.ClassNotFoundException"));
      Local l_r2 = Jimple.v().newLocal("$r2", RefType.v("java.lang.Class"));
      Local l_r3 = Jimple.v().newLocal("$r3", RefType.v("java.lang.ClassNotFoundException"));
      Local l_r4 = Jimple.v().newLocal("$r4", RefType.v("java.lang.NoClassDefFoundError"));
      Local l_r5 = Jimple.v().newLocal("$r5", RefType.v("java.lang.String"));
      body.getLocals().add(l_r0);
      body.getLocals().add(l_r1);
      body.getLocals().add(l_r2);
      body.getLocals().add(l_r3);
      body.getLocals().add(l_r4);
      body.getLocals().add(l_r5);
      units.add(Jimple.v().newIdentityStmt(l_r0, Jimple.v().newParameterRef(RefType.v("java.lang.String"), 0)));
      AssignStmt asi;
      units.add(asi = Jimple.v().newAssignStmt(l_r2, Jimple.v().newStaticInvokeExpr(Scene.v().getMethod("<java.lang.Class: java.lang.Class forName(java.lang.String)>").makeRef(), Arrays.asList(l_r0))));
      units.add(Jimple.v().newReturnStmt(l_r2));
      IdentityStmt handlerStart;
      units.add(handlerStart = Jimple.v().newIdentityStmt(l_r3, Jimple.v().newCaughtExceptionRef()));
      units.add(Jimple.v().newAssignStmt(l_r1, l_r3));
      units.add(Jimple.v().newAssignStmt(l_r4, Jimple.v().newNewExpr(RefType.v("java.lang.NoClassDefFoundError"))));
      units.add(Jimple.v().newAssignStmt(l_r5, Jimple.v().newVirtualInvokeExpr(l_r1, Scene.v().getMethod("<java.lang.Throwable: java.lang.String getMessage()>").makeRef(), (List)(new LinkedList()))));
      units.add(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(l_r4, Scene.v().getMethod("<java.lang.NoClassDefFoundError: void <init>(java.lang.String)>").makeRef(), Arrays.asList(l_r5))));
      units.add(Jimple.v().newThrowStmt(l_r4));
      body.getTraps().add(Jimple.v().newTrap(Scene.v().getSootClass("java.lang.ClassNotFoundException"), (Unit)asi, (Unit)handlerStart, (Unit)handlerStart));
      return method;
   }

   public void synchronizeStmtOn(Stmt stmt, JimpleBody b, Local lock) {
      Chain units = b.getUnits();
      units.insertBefore((Object)Jimple.v().newEnterMonitorStmt(lock), stmt);
      Stmt exitMon = Jimple.v().newExitMonitorStmt(lock);
      units.insertAfter((Object)exitMon, stmt);
      Stmt newGoto = Jimple.v().newGotoStmt((Unit)((Stmt)units.getSuccOf(exitMon)));
      units.insertAfter((Object)newGoto, exitMon);
      List<Unit> l = new ArrayList();
      Local eRef = Jimple.v().newLocal("__exception", RefType.v("java.lang.Throwable"));
      b.getLocals().add(eRef);
      Stmt handlerStmt = Jimple.v().newIdentityStmt(eRef, Jimple.v().newCaughtExceptionRef());
      l.add(handlerStmt);
      l.add((Stmt)exitMon.clone());
      l.add(Jimple.v().newThrowStmt(eRef));
      units.insertAfter((List)l, newGoto);
      Trap newTrap = Jimple.v().newTrap(Scene.v().getSootClass("java.lang.Throwable"), (Unit)stmt, (Unit)((Stmt)units.getSuccOf(stmt)), (Unit)handlerStmt);
      b.getTraps().addFirst(newTrap);
   }
}
