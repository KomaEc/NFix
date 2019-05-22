package soot.jimple.spark.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.util.LargeNumberedMap;

public final class PropCycle extends Propagator {
   private static final Logger logger = LoggerFactory.getLogger(PropCycle.class);
   private PAG pag;
   private OnFlyCallGraph ofcg;
   private Integer currentIteration;
   private final LargeNumberedMap<VarNode, Integer> varNodeToIteration;

   public PropCycle(PAG pag) {
      this.pag = pag;
      this.varNodeToIteration = new LargeNumberedMap(pag.getVarNodeNumberer());
   }

   public final void propagate() {
      this.ofcg = this.pag.getOnFlyCallGraph();
      boolean verbose = this.pag.getOpts().verbose();
      Collection<VarNode> bases = new HashSet();
      Iterator var3 = this.pag.getFieldRefNodeNumberer().iterator();

      while(var3.hasNext()) {
         FieldRefNode frn = (FieldRefNode)var3.next();
         bases.add(frn.getBase());
      }

      Collection<VarNode> bases = new ArrayList(bases);
      int iteration = 0;
      boolean finalIter = false;

      boolean changed;
      do {
         changed = false;
         ++iteration;
         this.currentIteration = new Integer(iteration);
         if (verbose) {
            logger.debug("Iteration: " + iteration);
         }

         Iterator var6;
         VarNode v;
         for(var6 = bases.iterator(); var6.hasNext(); changed |= this.computeP2Set((VarNode)v.getReplacement(), new ArrayList())) {
            v = (VarNode)var6.next();
         }

         if (this.ofcg != null) {
            throw new RuntimeException("NYI");
         }

         if (verbose) {
            logger.debug("Processing stores");
         }

         var6 = this.pag.storeSources().iterator();

         while(var6.hasNext()) {
            Object object = var6.next();
            final VarNode src = (VarNode)object;
            Node[] targets = this.pag.storeLookup(src);
            Node[] var10 = targets;
            int var11 = targets.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               Node element0 = var10[var12];
               final FieldRefNode target = (FieldRefNode)element0;
               changed |= target.getBase().makeP2Set().forall(new P2SetVisitor() {
                  public final void visit(Node n) {
                     AllocDotField nDotF = PropCycle.this.pag.makeAllocDotField((AllocNode)n, target.getField());
                     nDotF.makeP2Set().addAll(src.getP2Set(), (PointsToSetInternal)null);
                  }
               });
            }
         }

         if (!changed && !finalIter) {
            finalIter = true;
            if (verbose) {
               logger.debug("Doing full graph");
            }

            bases = new ArrayList(this.pag.getVarNodeNumberer().size());
            var6 = this.pag.getVarNodeNumberer().iterator();

            while(var6.hasNext()) {
               v = (VarNode)var6.next();
               bases.add(v);
            }

            changed = true;
         }
      } while(changed);

   }

   private boolean computeP2Set(final VarNode v, ArrayList<VarNode> path) {
      boolean ret = false;
      if (path.contains(v)) {
         return false;
      } else {
         Integer vnIteration = (Integer)this.varNodeToIteration.get(v);
         if (this.currentIteration != null && vnIteration != null && this.currentIteration == vnIteration) {
            return false;
         } else {
            this.varNodeToIteration.put(v, this.currentIteration);
            path.add(v);
            Node[] srcs;
            Node[] var6;
            int var7;
            int var8;
            Node element;
            if (v.getP2Set().isEmpty()) {
               srcs = this.pag.allocInvLookup(v);
               var6 = srcs;
               var7 = srcs.length;

               for(var8 = 0; var8 < var7; ++var8) {
                  element = var6[var8];
                  ret |= v.makeP2Set().add(element);
               }
            }

            srcs = this.pag.simpleInvLookup(v);
            var6 = srcs;
            var7 = srcs.length;

            for(var8 = 0; var8 < var7; ++var8) {
               element = var6[var8];
               VarNode src = (VarNode)element;
               ret |= this.computeP2Set(src, path);
               ret |= v.makeP2Set().addAll(src.getP2Set(), (PointsToSetInternal)null);
            }

            srcs = this.pag.loadInvLookup(v);
            var6 = srcs;
            var7 = srcs.length;

            for(var8 = 0; var8 < var7; ++var8) {
               element = var6[var8];
               final FieldRefNode src = (FieldRefNode)element;
               ret |= src.getBase().getP2Set().forall(new P2SetVisitor() {
                  public final void visit(Node n) {
                     AllocNode an = (AllocNode)n;
                     AllocDotField adf = PropCycle.this.pag.makeAllocDotField(an, src.getField());
                     this.returnValue |= v.makeP2Set().addAll(adf.getP2Set(), (PointsToSetInternal)null);
                  }
               });
            }

            path.remove(path.size() - 1);
            return ret;
         }
      }
   }
}
