package soot.jimple.toolkits.thread.mhp.findobject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.NewExpr;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.PAG;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.thread.mhp.pegcallgraph.PegCallGraph;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;

public class AllocNodesFinder {
   private final Set<AllocNode> allocNodes;
   private final Set<AllocNode> multiRunAllocNodes;
   private final Set<SootMethod> multiCalledMethods;
   PAG pag;

   public AllocNodesFinder(PegCallGraph pcg, CallGraph cg, PAG pag) {
      this.pag = pag;
      this.allocNodes = new HashSet();
      this.multiRunAllocNodes = new HashSet();
      this.multiCalledMethods = new HashSet();
      MultiCalledMethods mcm = new MultiCalledMethods(pcg, this.multiCalledMethods);
      this.find(mcm.getMultiCalledMethods(), pcg, cg);
   }

   private void find(Set<SootMethod> multiCalledMethods, PegCallGraph pcg, CallGraph callGraph) {
      Set clinitMethods = pcg.getClinitMethods();
      Iterator it = pcg.iterator();

      while(true) {
         label47:
         while(it.hasNext()) {
            SootMethod sm = (SootMethod)it.next();
            UnitGraph graph = new CompleteUnitGraph(sm.getActiveBody());
            Iterator iterator = graph.iterator();
            AllocNode allocNode;
            if (multiCalledMethods.contains(sm)) {
               while(true) {
                  while(true) {
                     if (!iterator.hasNext()) {
                        continue label47;
                     }

                     Unit unit = (Unit)iterator.next();
                     if (clinitMethods.contains(sm) && unit instanceof AssignStmt) {
                        AllocNode allocNode = this.pag.makeAllocNode("STRING_NODE", RefType.v("java.lang.String"), (SootMethod)null);
                        this.allocNodes.add(allocNode);
                        this.multiRunAllocNodes.add(allocNode);
                     } else if (unit instanceof DefinitionStmt) {
                        Value rightOp = ((DefinitionStmt)unit).getRightOp();
                        if (rightOp instanceof NewExpr) {
                           Type type = ((NewExpr)rightOp).getType();
                           allocNode = this.pag.makeAllocNode(rightOp, type, sm);
                           this.allocNodes.add(allocNode);
                           this.multiRunAllocNodes.add(allocNode);
                        }
                     }
                  }
               }
            } else {
               MultiRunStatementsFinder finder = new MultiRunStatementsFinder(graph, sm, multiCalledMethods, callGraph);
               FlowSet fs = finder.getMultiRunStatements();

               while(true) {
                  while(true) {
                     if (!iterator.hasNext()) {
                        continue label47;
                     }

                     Unit unit = (Unit)iterator.next();
                     if (clinitMethods.contains(sm) && unit instanceof AssignStmt) {
                        allocNode = this.pag.makeAllocNode("STRING_NODE", RefType.v("java.lang.String"), (SootMethod)null);
                        this.allocNodes.add(allocNode);
                     } else if (unit instanceof DefinitionStmt) {
                        Value rightOp = ((DefinitionStmt)unit).getRightOp();
                        if (rightOp instanceof NewExpr) {
                           Type type = ((NewExpr)rightOp).getType();
                           AllocNode allocNode = this.pag.makeAllocNode(rightOp, type, sm);
                           this.allocNodes.add(allocNode);
                           if (fs.contains(unit)) {
                              this.multiRunAllocNodes.add(allocNode);
                           }
                        }
                     }
                  }
               }
            }
         }

         return;
      }
   }

   public Set<AllocNode> getAllocNodes() {
      return this.allocNodes;
   }

   public Set<AllocNode> getMultiRunAllocNodes() {
      return this.multiRunAllocNodes;
   }

   public Set<SootMethod> getMultiCalledMethods() {
      return this.multiCalledMethods;
   }
}
