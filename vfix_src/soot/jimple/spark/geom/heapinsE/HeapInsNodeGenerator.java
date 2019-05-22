package soot.jimple.spark.geom.heapinsE;

import java.util.Iterator;
import soot.jimple.spark.geom.dataRep.CgEdge;
import soot.jimple.spark.geom.dataRep.PlainConstraint;
import soot.jimple.spark.geom.geomPA.DummyNode;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.geom.geomPA.IEncodingBroker;
import soot.jimple.spark.geom.geomPA.IVarAbstraction;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.toolkits.callgraph.Edge;

public class HeapInsNodeGenerator extends IEncodingBroker {
   private static final int[] full_convertor = new int[]{0, 1, 1, 1};

   public void initFlowGraph(GeomPointsTo ptAnalyzer) {
      int n_legal_cons = 0;
      Iterator var9 = ptAnalyzer.constraints.iterator();

      while(true) {
         PlainConstraint cons;
         do {
            if (!var9.hasNext()) {
               ptAnalyzer.ps.printf("Only %d (%.1f%%) constraints are needed for this run.\n", n_legal_cons, (double)n_legal_cons / (double)ptAnalyzer.n_init_constraints * 100.0D);
               return;
            }

            cons = (PlainConstraint)var9.next();
         } while(!cons.isActive);

         IVarAbstraction my_lhs = cons.getLHS().getRepresentative();
         IVarAbstraction my_rhs = cons.getRHS().getRepresentative();
         int nf1 = ptAnalyzer.getMethodIDFromPtr(my_lhs);
         int nf2 = ptAnalyzer.getMethodIDFromPtr(my_rhs);
         int code = (nf1 == 0 ? 1 : 0) << 1 | (nf2 == 0 ? 1 : 0);
         label114:
         switch(cons.type) {
         case 0:
            my_rhs.add_points_to_3((AllocNode)my_lhs.getWrappedNode(), (code & 1) == 1 ? 0L : 1L, code >> 1 == 1 ? 0L : 1L, (code & 1) == 1 ? ptAnalyzer.context_size[nf1] : ptAnalyzer.context_size[nf2]);
            ptAnalyzer.getWorklist().push(my_rhs);
            break;
         case 1:
            if (cons.interCallEdges != null) {
               Iterator it = cons.interCallEdges.iterator();

               while(true) {
                  while(true) {
                     while(true) {
                        CgEdge q;
                        do {
                           if (!it.hasNext()) {
                              break label114;
                           }

                           Edge sEdge = (Edge)it.next();
                           q = ptAnalyzer.getInternalEdgeFromSootEdge(sEdge);
                        } while(q.is_obsoleted);

                        int k;
                        if (nf2 == q.t) {
                           if (nf1 == 0) {
                              my_lhs.add_simple_constraint_3(my_rhs, 0L, q.map_offset, ptAnalyzer.max_context_size_block[q.s]);
                           } else if (q.s == q.t) {
                              my_lhs.add_simple_constraint_3(my_rhs, 1L, 1L, ptAnalyzer.context_size[nf1]);
                           } else {
                              for(k = 0; k < ptAnalyzer.block_num[nf1]; ++k) {
                                 my_lhs.add_simple_constraint_3(my_rhs, (long)k * ptAnalyzer.max_context_size_block[nf1] + 1L, q.map_offset, ptAnalyzer.max_context_size_block[nf1]);
                              }
                           }
                        } else if (q.s == q.t) {
                           my_lhs.add_simple_constraint_3(my_rhs, 1L, 1L, ptAnalyzer.context_size[nf2]);
                        } else {
                           for(k = 0; k < ptAnalyzer.block_num[nf2]; ++k) {
                              my_lhs.add_simple_constraint_3(my_rhs, q.map_offset, (long)k * ptAnalyzer.max_context_size_block[nf2] + 1L, ptAnalyzer.max_context_size_block[nf2]);
                           }
                        }
                     }
                  }
               }
            } else {
               my_lhs.add_simple_constraint_3(my_rhs, nf1 == 0 ? 0L : 1L, nf2 == 0 ? 0L : 1L, nf1 == 0 ? ptAnalyzer.context_size[nf2] : ptAnalyzer.context_size[nf1]);
               break;
            }
         case 2:
            cons.code = full_convertor[code];
            cons.otherSide = my_rhs;
            my_lhs.put_complex_constraint(cons);
            break;
         case 3:
            cons.code = full_convertor[code];
            cons.otherSide = my_lhs;
            my_rhs.put_complex_constraint(cons);
            break;
         default:
            throw new RuntimeException("Invalid node type");
         }

         ++n_legal_cons;
      }
   }

   public IVarAbstraction generateNode(Node vNode) {
      IVarAbstraction ret = null;
      if (!(vNode instanceof AllocNode) && !(vNode instanceof FieldRefNode)) {
         ret = new HeapInsNode(vNode);
      } else {
         ret = new DummyNode(vNode);
      }

      return (IVarAbstraction)ret;
   }

   public String getSignature() {
      return "HeapIns";
   }
}
