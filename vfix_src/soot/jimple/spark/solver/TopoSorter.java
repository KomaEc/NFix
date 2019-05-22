package soot.jimple.spark.solver;

import java.util.HashSet;
import java.util.Iterator;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;

public class TopoSorter {
   protected boolean ignoreTypes;
   protected PAG pag;
   protected int nextFinishNumber = 1;
   protected HashSet<VarNode> visited;

   public void sort() {
      Iterator var1 = this.pag.getVarNodeNumberer().iterator();

      while(var1.hasNext()) {
         VarNode v = (VarNode)var1.next();
         this.dfsVisit(v);
      }

      this.visited = null;
   }

   public TopoSorter(PAG pag, boolean ignoreTypes) {
      this.pag = pag;
      this.ignoreTypes = ignoreTypes;
      this.visited = new HashSet();
   }

   protected void dfsVisit(VarNode n) {
      if (!this.visited.contains(n)) {
         this.visited.add(n);
         Node[] succs = this.pag.simpleLookup(n);
         Node[] var3 = succs;
         int var4 = succs.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Node element = var3[var5];
            if (this.ignoreTypes || this.pag.getTypeManager().castNeverFails(n.getType(), element.getType())) {
               this.dfsVisit((VarNode)element);
            }
         }

         n.setFinishingNumber(this.nextFinishNumber++);
      }
   }
}
