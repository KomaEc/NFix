package soot.jimple.spark.solver;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;

public final class PropMerge extends Propagator {
   private static final Logger logger = LoggerFactory.getLogger(PropMerge.class);
   protected final Set<Node> varNodeWorkList = new TreeSet();
   protected PAG pag;

   public PropMerge(PAG pag) {
      this.pag = pag;
   }

   public final void propagate() {
      (new TopoSorter(this.pag, false)).sort();
      Iterator var1 = this.pag.allocSources().iterator();

      while(var1.hasNext()) {
         Object object = var1.next();
         this.handleAllocNode((AllocNode)object);
      }

      boolean verbose = this.pag.getOpts().verbose();

      do {
         if (verbose) {
            logger.debug("Worklist has " + this.varNodeWorkList.size() + " nodes.");
         }

         int iter = 0;

         while(!this.varNodeWorkList.isEmpty()) {
            VarNode src = (VarNode)this.varNodeWorkList.iterator().next();
            this.varNodeWorkList.remove(src);
            this.handleVarNode(src);
            if (verbose) {
               ++iter;
               if (iter >= 1000) {
                  iter = 0;
                  logger.debug("Worklist has " + this.varNodeWorkList.size() + " nodes.");
               }
            }
         }

         if (verbose) {
            logger.debug("Now handling field references");
         }

         Iterator var14 = this.pag.storeSources().iterator();

         Object object;
         Node[] targets;
         Node[] var7;
         int var8;
         int var9;
         Node element0;
         while(var14.hasNext()) {
            object = var14.next();
            VarNode src = (VarNode)object;
            targets = this.pag.storeLookup(src);
            var7 = targets;
            var8 = targets.length;

            for(var9 = 0; var9 < var8; ++var9) {
               element0 = var7[var9];
               FieldRefNode fr = (FieldRefNode)element0;
               fr.makeP2Set().addAll(src.getP2Set(), (PointsToSetInternal)null);
            }
         }

         var14 = this.pag.loadSources().iterator();

         while(var14.hasNext()) {
            object = var14.next();
            FieldRefNode src = (FieldRefNode)object;
            if (src != src.getReplacement()) {
               throw new RuntimeException("shouldn't happen");
            }

            targets = this.pag.loadLookup(src);
            var7 = targets;
            var8 = targets.length;

            for(var9 = 0; var9 < var8; ++var9) {
               element0 = var7[var9];
               VarNode target = (VarNode)element0;
               if (target.makeP2Set().addAll(src.getP2Set(), (PointsToSetInternal)null)) {
                  this.varNodeWorkList.add(target);
               }
            }
         }
      } while(!this.varNodeWorkList.isEmpty());

   }

   protected final boolean handleAllocNode(AllocNode src) {
      boolean ret = false;
      Node[] targets = this.pag.allocLookup(src);
      Node[] var4 = targets;
      int var5 = targets.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Node element = var4[var6];
         if (element.makeP2Set().add(src)) {
            this.varNodeWorkList.add(element);
            ret = true;
         }
      }

      return ret;
   }

   protected final boolean handleVarNode(VarNode src) {
      boolean ret = false;
      if (src.getReplacement() != src) {
         return ret;
      } else {
         PointsToSetInternal newP2Set = src.getP2Set();
         if (newP2Set.isEmpty()) {
            return false;
         } else {
            Node[] simpleTargets = this.pag.simpleLookup(src);
            Node[] storeTargets = simpleTargets;
            int var6 = simpleTargets.length;

            int var7;
            for(var7 = 0; var7 < var6; ++var7) {
               Node element = storeTargets[var7];
               if (element.makeP2Set().addAll(newP2Set, (PointsToSetInternal)null)) {
                  this.varNodeWorkList.add(element);
                  ret = true;
               }
            }

            storeTargets = this.pag.storeLookup(src);
            Node[] var11 = storeTargets;
            var7 = storeTargets.length;

            for(int var14 = 0; var14 < var7; ++var14) {
               Node element = var11[var14];
               FieldRefNode fr = (FieldRefNode)element;
               if (fr.makeP2Set().addAll(newP2Set, (PointsToSetInternal)null)) {
                  ret = true;
               }
            }

            final FieldRefNode fr;
            final SparkField field;
            for(Iterator var12 = src.getAllFieldRefs().iterator(); var12.hasNext(); ret |= newP2Set.forall(new P2SetVisitor() {
               public final void visit(Node n) {
                  AllocDotField nDotF = PropMerge.this.pag.makeAllocDotField((AllocNode)n, field);
                  Node nDotFNode = nDotF.getReplacement();
                  if (nDotFNode != fr) {
                     fr.mergeWith(nDotFNode);
                     this.returnValue = true;
                  }

               }
            })) {
               fr = (FieldRefNode)var12.next();
               field = fr.getField();
            }

            return ret;
         }
      }
   }
}
