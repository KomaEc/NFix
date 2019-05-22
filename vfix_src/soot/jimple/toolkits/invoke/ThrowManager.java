package soot.jimple.toolkits.invoke;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import soot.Body;
import soot.Hierarchy;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Trap;
import soot.TrapManager;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.util.Chain;

public class ThrowManager {
   public static Stmt getNullPointerExceptionThrower(JimpleBody b) {
      Chain<Unit> units = b.getUnits();
      Set<Unit> trappedUnits = TrapManager.getTrappedUnitsOf(b);

      Stmt s;
      for(s = (Stmt)units.getLast(); s != units.getFirst(); s = (Stmt)units.getPredOf(s)) {
         if (!trappedUnits.contains(s) && s instanceof ThrowStmt) {
            Value throwee = ((ThrowStmt)s).getOp();
            if (!(throwee instanceof Constant)) {
               if (s == units.getFirst()) {
                  break;
               }

               Stmt prosInvoke = (Stmt)units.getPredOf(s);
               if (prosInvoke instanceof InvokeStmt) {
                  if (prosInvoke == units.getFirst()) {
                     break;
                  }

                  Stmt prosNew = (Stmt)units.getPredOf(prosInvoke);
                  if (prosNew instanceof AssignStmt) {
                     InvokeExpr ie = ((InvokeStmt)prosInvoke).getInvokeExpr();
                     if (ie instanceof SpecialInvokeExpr && ((SpecialInvokeExpr)ie).getBase() == throwee && ie.getMethodRef().name().equals("<init>")) {
                        Value lo = ((AssignStmt)prosNew).getLeftOp();
                        Value ro = ((AssignStmt)prosNew).getRightOp();
                        if (lo == throwee && ro instanceof NewExpr) {
                           Type newType = ((NewExpr)ro).getBaseType();
                           if (newType.equals(RefType.v("java.lang.NullPointerException"))) {
                              return prosNew;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      s = (Stmt)units.getLast();
      return addThrowAfter(b, s);
   }

   static Stmt addThrowAfter(JimpleBody b, Stmt target) {
      Chain<Unit> units = b.getUnits();
      Collection<Local> locals = b.getLocals();
      int i = 0;
      boolean canAddI = false;

      do {
         canAddI = true;
         Iterator localIt = locals.iterator();

         while(localIt.hasNext()) {
            Local l = (Local)localIt.next();
            if (l.getName().equals("__throwee" + i)) {
               canAddI = false;
            }
         }

         if (!canAddI) {
            ++i;
         }
      } while(!canAddI);

      Local l = Jimple.v().newLocal("__throwee" + i, RefType.v("java.lang.NullPointerException"));
      b.getLocals().add(l);
      Stmt newStmt = Jimple.v().newAssignStmt(l, Jimple.v().newNewExpr(RefType.v("java.lang.NullPointerException")));
      Stmt invStmt = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(l, Scene.v().getMethod("<java.lang.NullPointerException: void <init>()>").makeRef()));
      Stmt throwStmt = Jimple.v().newThrowStmt(l);
      units.insertAfter((Object)newStmt, target);
      units.insertAfter((Object)invStmt, newStmt);
      units.insertAfter((Object)throwStmt, invStmt);
      return newStmt;
   }

   static boolean isExceptionCaughtAt(SootClass e, Stmt stmt, Body b) {
      Hierarchy h = new Hierarchy();
      Iterator trapsIt = b.getTraps().iterator();

      while(true) {
         Trap t;
         do {
            if (!trapsIt.hasNext()) {
               return false;
            }

            t = (Trap)trapsIt.next();
         } while(!h.isClassSubclassOfIncluding(e, t.getException()));

         Iterator it = b.getUnits().iterator(t.getBeginUnit(), t.getEndUnit());

         while(it.hasNext()) {
            if (stmt.equals(it.next())) {
               return true;
            }
         }
      }
   }
}
