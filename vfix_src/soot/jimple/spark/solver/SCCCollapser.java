package soot.jimple.spark.solver;

import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Type;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.PointsToSetInternal;

public class SCCCollapser {
   private static final Logger logger = LoggerFactory.getLogger(SCCCollapser.class);
   protected int numCollapsed = 0;
   protected PAG pag;
   protected HashSet<VarNode> visited = new HashSet();
   protected boolean ignoreTypes;
   protected TypeManager typeManager;

   public void collapse() {
      boolean verbose = this.pag.getOpts().verbose();
      if (verbose) {
         logger.debug("Total VarNodes: " + this.pag.getVarNodeNumberer().size() + ". Collapsing SCCs...");
      }

      (new TopoSorter(this.pag, this.ignoreTypes)).sort();
      TreeSet<VarNode> s = new TreeSet();
      Iterator var3 = this.pag.getVarNodeNumberer().iterator();

      VarNode v;
      while(var3.hasNext()) {
         v = (VarNode)var3.next();
         s.add(v);
      }

      var3 = s.iterator();

      while(var3.hasNext()) {
         v = (VarNode)var3.next();
         this.dfsVisit(v, v);
      }

      if (verbose) {
         logger.debug("" + this.numCollapsed + " nodes were collapsed.");
      }

      this.visited = null;
   }

   public SCCCollapser(PAG pag, boolean ignoreTypes) {
      this.pag = pag;
      this.ignoreTypes = ignoreTypes;
      this.typeManager = pag.getTypeManager();
   }

   protected final void dfsVisit(VarNode v, VarNode rootOfSCC) {
      if (!this.visited.contains(v)) {
         this.visited.add(v);
         Node[] succs = this.pag.simpleInvLookup(v);
         Node[] var4 = succs;
         int var5 = succs.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Node element = var4[var6];
            if (this.ignoreTypes || this.typeManager.castNeverFails(element.getType(), v.getType())) {
               this.dfsVisit((VarNode)element, rootOfSCC);
            }
         }

         if (v != rootOfSCC) {
            if (!this.ignoreTypes) {
               if (this.typeManager.castNeverFails(v.getType(), rootOfSCC.getType()) && this.typeManager.castNeverFails(rootOfSCC.getType(), v.getType())) {
                  rootOfSCC.mergeWith(v);
                  ++this.numCollapsed;
               }
            } else {
               if (this.typeManager.castNeverFails(v.getType(), rootOfSCC.getType())) {
                  rootOfSCC.mergeWith(v);
               } else if (this.typeManager.castNeverFails(rootOfSCC.getType(), v.getType())) {
                  v.mergeWith(rootOfSCC);
               } else {
                  rootOfSCC.getReplacement().setType((Type)null);
                  PointsToSetInternal set = rootOfSCC.getP2Set();
                  if (set != null) {
                     set.setType((Type)null);
                  }

                  rootOfSCC.mergeWith(v);
               }

               ++this.numCollapsed;
            }
         }

      }
   }
}
