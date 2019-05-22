package soot.jimple.toolkits.invoke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Hierarchy;
import soot.Local;
import soot.PhaseOptions;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.TrapManager;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityRef;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.scalar.LocalNameStandardizer;
import soot.util.Chain;

public class SiteInliner {
   public String getDefaultOptions() {
      return "insert-null-checks insert-redundant-casts";
   }

   public static void inlineSites(List sites) {
      inlineSites(sites, new HashMap());
   }

   public static void inlineSites(List sites, Map options) {
      Iterator it = sites.iterator();

      while(it.hasNext()) {
         List l = (List)it.next();
         SootMethod inlinee = (SootMethod)l.get(0);
         Stmt toInline = (Stmt)l.get(1);
         SootMethod container = (SootMethod)l.get(2);
         inlineSite(inlinee, toInline, container, options);
      }

   }

   public static void inlineSite(SootMethod inlinee, Stmt toInline, SootMethod container) {
      inlineSite(inlinee, toInline, container, new HashMap());
   }

   public static List inlineSite(SootMethod inlinee, Stmt toInline, SootMethod container, Map options) {
      boolean enableNullPointerCheckInsertion = PhaseOptions.getBoolean(options, "insert-null-checks");
      boolean enableRedundantCastInsertion = PhaseOptions.getBoolean(options, "insert-redundant-casts");
      Hierarchy hierarchy = Scene.v().getActiveHierarchy();
      JimpleBody containerB = (JimpleBody)container.getActiveBody();
      Chain<Unit> containerUnits = containerB.getUnits();
      if (!inlinee.getDeclaringClass().isApplicationClass() && !inlinee.getDeclaringClass().isLibraryClass()) {
         return null;
      } else {
         Body inlineeB = inlinee.getActiveBody();
         Chain<Unit> inlineeUnits = inlineeB.getUnits();
         InvokeExpr ie = toInline.getInvokeExpr();
         Value thisToAdd = null;
         if (ie instanceof InstanceInvokeExpr) {
            thisToAdd = ((InstanceInvokeExpr)ie).getBase();
         }

         boolean caught = true;
         if (enableRedundantCastInsertion && ie instanceof InstanceInvokeExpr && caught) {
            SootClass localType = ((RefType)((InstanceInvokeExpr)ie).getBase().getType()).getSootClass();
            SootClass parameterType = inlinee.getDeclaringClass();
            if (localType.isInterface() || hierarchy.isClassSuperclassOf(localType, parameterType)) {
               Local castee = Jimple.v().newLocal("__castee", parameterType.getType());
               containerB.getLocals().add(castee);
               containerB.getUnits().insertBefore((Unit)Jimple.v().newAssignStmt(castee, Jimple.v().newCastExpr(((InstanceInvokeExpr)ie).getBase(), parameterType.getType())), (Unit)toInline);
               thisToAdd = castee;
            }
         }

         if (enableNullPointerCheckInsertion && ie instanceof InstanceInvokeExpr) {
            caught = TrapManager.isExceptionCaughtAt(Scene.v().getSootClass("java.lang.NullPointerException"), toInline, containerB);
            if (caught) {
               Stmt insertee = Jimple.v().newIfStmt(Jimple.v().newNeExpr(((InstanceInvokeExpr)ie).getBase(), NullConstant.v()), (Unit)toInline);
               containerB.getUnits().insertBefore((Unit)insertee, (Unit)toInline);
               ((IfStmt)insertee).setTarget(toInline);
               ThrowManager.addThrowAfter(containerB, insertee);
            } else {
               Stmt throwPoint = ThrowManager.getNullPointerExceptionThrower(containerB);
               containerB.getUnits().insertBefore((Unit)Jimple.v().newIfStmt(Jimple.v().newEqExpr(((InstanceInvokeExpr)ie).getBase(), NullConstant.v()), (Unit)throwPoint), (Unit)toInline);
            }
         }

         if (inlinee.isSynchronized()) {
            if (ie instanceof InstanceInvokeExpr) {
               SynchronizerManager.v().synchronizeStmtOn(toInline, containerB, (Local)((InstanceInvokeExpr)ie).getBase());
            } else if (!container.getDeclaringClass().isInterface()) {
               Local l = SynchronizerManager.v().addStmtsToFetchClassBefore(containerB, toInline);
               SynchronizerManager.v().synchronizeStmtOn(toInline, containerB, l);
            }
         }

         Stmt exitPoint = (Stmt)containerUnits.getSuccOf(toInline);
         HashMap<Local, Local> oldLocalsToNew = new HashMap();
         HashMap<Stmt, Stmt> oldUnitsToNew = new HashMap();
         Stmt cursor = toInline;
         Iterator i = inlineeUnits.iterator();

         Stmt newBegin;
         while(i.hasNext()) {
            Stmt curr = (Stmt)i.next();
            newBegin = (Stmt)curr.clone();
            if (newBegin == null) {
               throw new RuntimeException("getting null from clone!");
            }

            newBegin.addAllTagsOf(curr);
            containerUnits.insertAfter((Object)newBegin, cursor);
            cursor = newBegin;
            oldUnitsToNew.put(curr, newBegin);
         }

         i = inlineeB.getLocals().iterator();

         while(i.hasNext()) {
            Local l = (Local)i.next();
            Local lPrime = (Local)l.clone();
            if (lPrime == null) {
               throw new RuntimeException("getting null from local clone!");
            }

            containerB.getLocals().add(lPrime);
            oldLocalsToNew.put(l, lPrime);
         }

         Iterator it = containerUnits.iterator(containerUnits.getSuccOf(toInline), containerUnits.getPredOf(exitPoint));

         Iterator var37;
         while(it.hasNext()) {
            Stmt patchee = (Stmt)it.next();
            var37 = patchee.getUseAndDefBoxes().iterator();

            while(var37.hasNext()) {
               ValueBox box = (ValueBox)var37.next();
               if (box.getValue() instanceof Local) {
                  Local lPrime = (Local)oldLocalsToNew.get(box.getValue());
                  if (lPrime == null) {
                     throw new RuntimeException("local has no clone!");
                  }

                  box.setValue(lPrime);
               }
            }

            var37 = patchee.getUnitBoxes().iterator();

            while(var37.hasNext()) {
               UnitBox box = (UnitBox)var37.next();
               Unit uPrime = (Unit)oldUnitsToNew.get(box.getUnit());
               if (uPrime == null) {
                  throw new RuntimeException("inlined stmt has no clone!");
               }

               box.setUnit(uPrime);
            }
         }

         Trap prevTrap = null;

         Trap trap;
         Stmt s;
         for(i = inlineeB.getTraps().iterator(); i.hasNext(); prevTrap = trap) {
            Trap t = (Trap)i.next();
            newBegin = (Stmt)oldUnitsToNew.get(t.getBeginUnit());
            s = (Stmt)oldUnitsToNew.get(t.getEndUnit());
            Stmt newHandler = (Stmt)oldUnitsToNew.get(t.getHandlerUnit());
            if (newBegin == null || s == null || newHandler == null) {
               throw new RuntimeException("couldn't map trap!");
            }

            trap = Jimple.v().newTrap(t.getException(), (Unit)newBegin, (Unit)s, (Unit)newHandler);
            if (prevTrap == null) {
               containerB.getTraps().addFirst(trap);
            } else {
               containerB.getTraps().insertAfter((Object)trap, prevTrap);
            }
         }

         it = containerUnits.iterator(containerUnits.getSuccOf(toInline), containerUnits.getPredOf(exitPoint));
         ArrayList cuCopy = new ArrayList();

         while(it.hasNext()) {
            cuCopy.add(it.next());
         }

         var37 = cuCopy.iterator();

         while(var37.hasNext()) {
            Unit u = (Unit)var37.next();
            s = (Stmt)u;
            if (s instanceof IdentityStmt) {
               IdentityRef rhs = (IdentityRef)((IdentityStmt)s).getRightOp();
               if (!(rhs instanceof CaughtExceptionRef)) {
                  if (rhs instanceof ThisRef) {
                     if (!(ie instanceof InstanceInvokeExpr)) {
                        throw new RuntimeException("thisref with no receiver!");
                     }

                     containerUnits.swapWith(s, Jimple.v().newAssignStmt(((IdentityStmt)s).getLeftOp(), (Value)thisToAdd));
                  } else if (rhs instanceof ParameterRef) {
                     ParameterRef pref = (ParameterRef)rhs;
                     containerUnits.swapWith(s, Jimple.v().newAssignStmt(((IdentityStmt)s).getLeftOp(), ie.getArg(pref.getIndex())));
                  }
               }
            } else if (s instanceof ReturnStmt) {
               if (toInline instanceof InvokeStmt) {
                  containerUnits.swapWith(s, Jimple.v().newGotoStmt((Unit)exitPoint));
               } else {
                  if (!(toInline instanceof AssignStmt)) {
                     throw new RuntimeException("invoking stmt neither InvokeStmt nor AssignStmt!??!?!");
                  }

                  Value ro = ((ReturnStmt)s).getOp();
                  Value lhs = ((AssignStmt)toInline).getLeftOp();
                  AssignStmt as = Jimple.v().newAssignStmt(lhs, ro);
                  containerUnits.insertBefore((Object)as, s);
                  containerUnits.swapWith(s, Jimple.v().newGotoStmt((Unit)exitPoint));
               }
            } else if (s instanceof ReturnVoidStmt) {
               containerUnits.swapWith(s, Jimple.v().newGotoStmt((Unit)exitPoint));
            }
         }

         List<Unit> newStmts = new ArrayList();
         i = containerUnits.iterator(containerUnits.getSuccOf(toInline), containerUnits.getPredOf(exitPoint));

         while(i.hasNext()) {
            newStmts.add(i.next());
         }

         containerUnits.remove(toInline);
         LocalNameStandardizer.v().transform(containerB, "ji.lns");
         return newStmts;
      }
   }
}
