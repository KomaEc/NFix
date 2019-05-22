package soot.dexpler;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootMethodRef;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LengthExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.toolkits.scalar.LocalCreation;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.LocalDefs;

public class DexNullArrayRefTransformer extends BodyTransformer {
   public static DexNullArrayRefTransformer v() {
      return new DexNullArrayRefTransformer();
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      ExceptionalUnitGraph g = new ExceptionalUnitGraph(body, DalvikThrowAnalysis.v());
      LocalDefs defs = LocalDefs.Factory.newLocalDefs((UnitGraph)g);
      LocalCreation lc = new LocalCreation(body.getLocals(), "ex");
      boolean changed = false;
      Iterator unitIt = body.getUnits().snapshotIterator();

      while(true) {
         while(unitIt.hasNext()) {
            Stmt s = (Stmt)unitIt.next();
            if (s.containsArrayRef()) {
               Value base = s.getArrayRef().getBase();
               if (this.isAlwaysNullBefore(s, (Local)base, defs)) {
                  this.createThrowStmt(body, s, lc);
                  changed = true;
               }
            } else if (s instanceof AssignStmt) {
               AssignStmt ass = (AssignStmt)s;
               Value rightOp = ass.getRightOp();
               if (rightOp instanceof LengthExpr) {
                  LengthExpr l = (LengthExpr)ass.getRightOp();
                  Value base = l.getOp();
                  if (base instanceof IntConstant) {
                     IntConstant ic = (IntConstant)base;
                     if (ic.value == 0) {
                        this.createThrowStmt(body, s, lc);
                        changed = true;
                     }
                  } else if (base == NullConstant.v() || this.isAlwaysNullBefore(s, (Local)base, defs)) {
                     this.createThrowStmt(body, s, lc);
                     changed = true;
                  }
               }
            }
         }

         if (changed) {
            UnreachableCodeEliminator.v().transform(body);
         }

         return;
      }
   }

   private boolean isAlwaysNullBefore(Stmt s, Local base, LocalDefs defs) {
      List<Unit> baseDefs = defs.getDefsOfAt(base, s);
      if (baseDefs.isEmpty()) {
         return true;
      } else {
         Iterator var5 = baseDefs.iterator();

         DefinitionStmt defStmt;
         do {
            if (!var5.hasNext()) {
               return true;
            }

            Unit u = (Unit)var5.next();
            if (!(u instanceof DefinitionStmt)) {
               return false;
            }

            defStmt = (DefinitionStmt)u;
         } while(defStmt.getRightOp() == NullConstant.v());

         return false;
      }
   }

   private void createThrowStmt(Body body, Unit oldStmt, LocalCreation lc) {
      RefType tp = RefType.v("java.lang.NullPointerException");
      Local lcEx = lc.newLocal(tp);
      SootMethodRef constructorRef = Scene.v().makeConstructorRef(tp.getSootClass(), Collections.singletonList(RefType.v("java.lang.String")));
      Stmt newExStmt = Jimple.v().newAssignStmt(lcEx, Jimple.v().newNewExpr(tp));
      body.getUnits().insertBefore((Unit)newExStmt, (Unit)oldStmt);
      Stmt invConsStmt = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(lcEx, constructorRef, Collections.singletonList(StringConstant.v("Invalid array reference replaced by Soot"))));
      body.getUnits().insertBefore((Unit)invConsStmt, (Unit)oldStmt);
      body.getUnits().swapWith((Unit)oldStmt, (Unit)Jimple.v().newThrowStmt(lcEx));
   }
}
