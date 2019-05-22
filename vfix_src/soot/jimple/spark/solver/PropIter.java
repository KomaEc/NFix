package soot.jimple.spark.solver;

import java.util.Iterator;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Type;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ClassConstantNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.NewInstanceNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.util.queue.QueueReader;

public final class PropIter extends Propagator {
   private static final Logger logger = LoggerFactory.getLogger(PropIter.class);
   protected PAG pag;

   public PropIter(PAG pag) {
      this.pag = pag;
   }

   public final void propagate() {
      OnFlyCallGraph ofcg = this.pag.getOnFlyCallGraph();
      (new TopoSorter(this.pag, false)).sort();
      Iterator var2 = this.pag.allocSources().iterator();

      while(var2.hasNext()) {
         Object object = var2.next();
         this.handleAllocNode((AllocNode)object);
      }

      int var9 = 1;

      boolean change;
      do {
         change = false;
         TreeSet<VarNode> simpleSources = new TreeSet(this.pag.simpleSources());
         if (this.pag.getOpts().verbose()) {
            logger.debug("Iteration " + var9++);
         }

         Iterator var5;
         VarNode object;
         for(var5 = simpleSources.iterator(); var5.hasNext(); change |= this.handleSimples(object)) {
            object = (VarNode)var5.next();
         }

         if (ofcg != null) {
            QueueReader<Node> addedEdges = this.pag.edgeReader();
            Iterator var12 = this.pag.getVarNodeNumberer().iterator();

            while(var12.hasNext()) {
               VarNode src = (VarNode)var12.next();
               ofcg.updatedNode(src);
            }

            ofcg.build();

            while(addedEdges.hasNext()) {
               Node addedSrc = (Node)addedEdges.next();
               Node addedTgt = (Node)addedEdges.next();
               change = true;
               if (addedSrc instanceof VarNode) {
                  PointsToSetInternal p2set = ((VarNode)addedSrc).getP2Set();
                  if (p2set != null) {
                     p2set.unFlushNew();
                  }
               } else if (addedSrc instanceof AllocNode) {
                  ((VarNode)addedTgt).makeP2Set().add(addedSrc);
               }
            }

            if (change) {
               (new TopoSorter(this.pag, false)).sort();
            }
         }

         FieldRefNode object;
         for(var5 = this.pag.loadSources().iterator(); var5.hasNext(); change |= this.handleLoads(object)) {
            object = (FieldRefNode)var5.next();
         }

         for(var5 = this.pag.storeSources().iterator(); var5.hasNext(); change |= this.handleStores(object)) {
            object = (VarNode)var5.next();
         }

         NewInstanceNode object;
         for(var5 = this.pag.assignInstanceSources().iterator(); var5.hasNext(); change |= this.handleNewInstances(object)) {
            object = (NewInstanceNode)var5.next();
         }
      } while(change);

   }

   protected final boolean handleAllocNode(AllocNode src) {
      boolean ret = false;
      Node[] targets = this.pag.allocLookup(src);
      Node[] var4 = targets;
      int var5 = targets.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Node element = var4[var6];
         ret |= element.makeP2Set().add(src);
      }

      return ret;
   }

   protected final boolean handleSimples(VarNode src) {
      boolean ret = false;
      PointsToSetInternal srcSet = src.getP2Set();
      if (srcSet.isEmpty()) {
         return false;
      } else {
         Node[] simpleTargets = this.pag.simpleLookup(src);
         Node[] newInstances = simpleTargets;
         int var6 = simpleTargets.length;

         int var7;
         for(var7 = 0; var7 < var6; ++var7) {
            Node element = newInstances[var7];
            ret |= element.makeP2Set().addAll(srcSet, (PointsToSetInternal)null);
         }

         newInstances = this.pag.newInstanceLookup(src);
         Node[] var10 = newInstances;
         var7 = newInstances.length;

         for(int var11 = 0; var11 < var7; ++var11) {
            Node element = var10[var11];
            ret |= element.makeP2Set().addAll(srcSet, (PointsToSetInternal)null);
         }

         return ret;
      }
   }

   protected final boolean handleStores(VarNode src) {
      boolean ret = false;
      final PointsToSetInternal srcSet = src.getP2Set();
      if (srcSet.isEmpty()) {
         return false;
      } else {
         Node[] storeTargets = this.pag.storeLookup(src);
         Node[] var5 = storeTargets;
         int var6 = storeTargets.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Node element = var5[var7];
            FieldRefNode fr = (FieldRefNode)element;
            final SparkField f = fr.getField();
            ret |= fr.getBase().getP2Set().forall(new P2SetVisitor() {
               public final void visit(Node n) {
                  AllocDotField nDotF = PropIter.this.pag.makeAllocDotField((AllocNode)n, f);
                  if (nDotF.makeP2Set().addAll(srcSet, (PointsToSetInternal)null)) {
                     this.returnValue = true;
                  }

               }
            });
         }

         return ret;
      }
   }

   protected final boolean handleLoads(FieldRefNode src) {
      boolean ret = false;
      final Node[] loadTargets = this.pag.loadLookup(src);
      final SparkField f = src.getField();
      ret |= src.getBase().getP2Set().forall(new P2SetVisitor() {
         public final void visit(Node n) {
            AllocDotField nDotF = ((AllocNode)n).dot(f);
            if (nDotF != null) {
               PointsToSetInternal set = nDotF.getP2Set();
               if (!set.isEmpty()) {
                  Node[] var4 = loadTargets;
                  int var5 = var4.length;

                  for(int var6 = 0; var6 < var5; ++var6) {
                     Node element = var4[var6];
                     VarNode target = (VarNode)element;
                     if (target.makeP2Set().addAll(set, (PointsToSetInternal)null)) {
                        this.returnValue = true;
                     }
                  }

               }
            }
         }
      });
      return ret;
   }

   protected final boolean handleNewInstances(final NewInstanceNode src) {
      boolean ret = false;
      Node[] newInstances = this.pag.assignInstanceLookup(src);
      Node[] var4 = newInstances;
      int var5 = newInstances.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         final Node instance = var4[var6];
         ret = src.getP2Set().forall(new P2SetVisitor() {
            public void visit(Node n) {
               if (n instanceof ClassConstantNode) {
                  ClassConstantNode ccn = (ClassConstantNode)n;
                  Type ccnType = ccn.getClassConstant().toSootType();
                  SootClass targetClass = ((RefType)ccnType).getSootClass();
                  if (targetClass.resolvingLevel() == 0) {
                     Scene.v().forceResolve(targetClass.getName(), 2);
                  }

                  instance.makeP2Set().add(PropIter.this.pag.makeAllocNode(src.getValue(), ccnType, ccn.getMethod()));
               }

            }
         });
      }

      return ret;
   }
}
