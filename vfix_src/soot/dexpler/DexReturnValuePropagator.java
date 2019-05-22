package soot.dexpler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.ReturnStmt;
import soot.jimple.toolkits.scalar.LocalCreation;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;

public class DexReturnValuePropagator extends BodyTransformer {
   public static DexReturnValuePropagator v() {
      return new DexReturnValuePropagator();
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      ExceptionalUnitGraph graph = new ExceptionalUnitGraph(body, DalvikThrowAnalysis.v(), true);
      LocalDefs localDefs = LocalDefs.Factory.newLocalDefs((UnitGraph)graph);
      LocalUses localUses = null;
      LocalCreation localCreation = null;
      Iterator var8 = body.getUnits().iterator();

      while(var8.hasNext()) {
         Unit u = (Unit)var8.next();
         if (u instanceof ReturnStmt) {
            ReturnStmt retStmt = (ReturnStmt)u;
            if (retStmt.getOp() instanceof Local) {
               List<Unit> defs = localDefs.getDefsOfAt((Local)retStmt.getOp(), retStmt);
               if (defs.size() == 1 && defs.get(0) instanceof AssignStmt) {
                  AssignStmt assign = (AssignStmt)defs.get(0);
                  Value rightOp = assign.getRightOp();
                  Value leftOp = assign.getLeftOp();
                  if (rightOp instanceof Local) {
                     if (!this.isRedefined((Local)rightOp, u, assign, graph)) {
                        retStmt.setOp(rightOp);
                     }
                  } else if (rightOp instanceof Constant) {
                     retStmt.setOp(rightOp);
                  } else if (rightOp instanceof FieldRef) {
                     if (localUses == null) {
                        localUses = LocalUses.Factory.newLocalUses(body, localDefs);
                     }

                     if (localUses.getUsesOf(assign).size() == 1) {
                        if (localCreation == null) {
                           localCreation = new LocalCreation(body.getLocals(), "ret");
                        }

                        Local newLocal = localCreation.newLocal(leftOp.getType());
                        assign.setLeftOp(newLocal);
                        retStmt.setOp(newLocal);
                     }
                  }
               }
            }
         }
      }

   }

   private boolean isRedefined(Local l, Unit unitUse, AssignStmt unitDef, UnitGraph graph) {
      List<Unit> workList = new ArrayList();
      workList.add(unitUse);
      HashSet doneSet = new HashSet();

      while(true) {
         Unit curStmt;
         do {
            if (workList.isEmpty()) {
               return false;
            }

            curStmt = (Unit)workList.remove(0);
         } while(!doneSet.add(curStmt));

         Iterator var8 = graph.getPredsOf(curStmt).iterator();

         while(var8.hasNext()) {
            Unit u = (Unit)var8.next();
            if (u != unitDef) {
               if (u instanceof DefinitionStmt) {
                  DefinitionStmt defStmt = (DefinitionStmt)u;
                  if (defStmt.getLeftOp() == l) {
                     return true;
                  }
               }

               workList.add(u);
            }
         }
      }
   }
}
