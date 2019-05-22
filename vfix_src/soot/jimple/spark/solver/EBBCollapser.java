package soot.jimple.spark.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Type;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;

public class EBBCollapser {
   private static final Logger logger = LoggerFactory.getLogger(EBBCollapser.class);
   protected int numCollapsed = 0;
   protected PAG pag;

   public void collapse() {
      boolean verbose = this.pag.getOpts().verbose();
      if (verbose) {
         logger.debug("Total VarNodes: " + this.pag.getVarNodeNumberer().size() + ". Collapsing EBBs...");
      }

      this.collapseAlloc();
      this.collapseLoad();
      this.collapseSimple();
      if (verbose) {
         logger.debug("" + this.numCollapsed + " nodes were collapsed.");
      }

   }

   public EBBCollapser(PAG pag) {
      this.pag = pag;
   }

   protected void collapseAlloc() {
      boolean ofcg = this.pag.getOnFlyCallGraph() != null;
      Iterator var2 = this.pag.allocSources().iterator();

      while(var2.hasNext()) {
         Object object = var2.next();
         AllocNode n = (AllocNode)object;
         Node[] succs = this.pag.allocLookup(n);
         VarNode firstSucc = null;
         Node[] var7 = succs;
         int var8 = succs.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            Node element0 = var7[var9];
            VarNode succ = (VarNode)element0;
            if (this.pag.allocInvLookup(succ).length <= 1 && this.pag.loadInvLookup(succ).length <= 0 && this.pag.simpleInvLookup(succ).length <= 0 && (!ofcg || !succ.isInterProcTarget())) {
               if (firstSucc == null) {
                  firstSucc = succ;
               } else if (firstSucc.getType().equals(succ.getType())) {
                  firstSucc.mergeWith(succ);
                  ++this.numCollapsed;
               }
            }
         }
      }

   }

   protected void collapseSimple() {
      boolean ofcg = this.pag.getOnFlyCallGraph() != null;
      TypeManager typeManager = this.pag.getTypeManager();

      boolean change;
      do {
         change = false;
         Iterator nIt = (new ArrayList(this.pag.simpleSources())).iterator();

         while(nIt.hasNext()) {
            VarNode n = (VarNode)nIt.next();
            Type nType = n.getType();
            Node[] succs = this.pag.simpleLookup(n);
            Node[] var8 = succs;
            int var9 = succs.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               Node element = var8[var10];
               VarNode succ = (VarNode)element;
               Type sType = succ.getType();
               if (typeManager.castNeverFails(nType, sType) && this.pag.allocInvLookup(succ).length <= 0 && this.pag.loadInvLookup(succ).length <= 0 && this.pag.simpleInvLookup(succ).length <= 1 && (!ofcg || !succ.isInterProcTarget() && !n.isInterProcSource())) {
                  n.mergeWith(succ);
                  change = true;
                  ++this.numCollapsed;
               }
            }
         }
      } while(change);

   }

   protected void collapseLoad() {
      boolean ofcg = this.pag.getOnFlyCallGraph() != null;
      TypeManager typeManager = this.pag.getTypeManager();
      Iterator nIt = (new ArrayList(this.pag.loadSources())).iterator();

      while(nIt.hasNext()) {
         FieldRefNode n = (FieldRefNode)nIt.next();
         Type nType = n.getType();
         Node[] succs = this.pag.loadLookup(n);
         Node firstSucc = null;
         HashMap<Type, VarNode> typeToSucc = new HashMap();
         Node[] var9 = succs;
         int var10 = succs.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            Node element = var9[var11];
            VarNode succ = (VarNode)element;
            Type sType = succ.getType();
            if (this.pag.allocInvLookup(succ).length <= 0 && this.pag.loadInvLookup(succ).length <= 1 && this.pag.simpleInvLookup(succ).length <= 0 && (!ofcg || !succ.isInterProcTarget())) {
               if (typeManager.castNeverFails(nType, sType)) {
                  if (firstSucc == null) {
                     firstSucc = succ;
                  } else {
                     firstSucc.mergeWith(succ);
                     ++this.numCollapsed;
                  }
               } else {
                  VarNode rep = (VarNode)typeToSucc.get(succ.getType());
                  if (rep == null) {
                     typeToSucc.put(succ.getType(), succ);
                  } else {
                     rep.mergeWith(succ);
                     ++this.numCollapsed;
                  }
               }
            }
         }
      }

   }
}
