package soot.jimple.spark.solver;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Type;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ClassConstantNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.NewInstanceNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.EmptyPointsToSet;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.util.HashMultiMap;
import soot.util.LargeNumberedMap;
import soot.util.MultiMap;
import soot.util.queue.QueueReader;

public final class PropAlias extends Propagator {
   private static final Logger logger = LoggerFactory.getLogger(PropAlias.class);
   protected final Set<VarNode> varNodeWorkList = new TreeSet();
   protected Set<VarNode> aliasWorkList;
   protected Set<FieldRefNode> fieldRefWorkList = new HashSet();
   protected Set<FieldRefNode> outFieldRefWorkList = new HashSet();
   protected PAG pag;
   protected MultiMap<SparkField, VarNode> fieldToBase = new HashMultiMap();
   protected MultiMap<FieldRefNode, FieldRefNode> aliasEdges = new HashMultiMap();
   protected LargeNumberedMap<FieldRefNode, PointsToSetInternal> loadSets;
   protected OnFlyCallGraph ofcg;

   public PropAlias(PAG pag) {
      this.pag = pag;
      this.loadSets = new LargeNumberedMap(pag.getFieldRefNodeNumberer());
   }

   public final void propagate() {
      this.ofcg = this.pag.getOnFlyCallGraph();
      (new TopoSorter(this.pag, false)).sort();
      Iterator var1 = this.pag.loadSources().iterator();

      Object object;
      FieldRefNode src;
      while(var1.hasNext()) {
         object = var1.next();
         src = (FieldRefNode)object;
         this.fieldToBase.put(src.getField(), src.getBase());
      }

      var1 = this.pag.storeInvSources().iterator();

      while(var1.hasNext()) {
         object = var1.next();
         src = (FieldRefNode)object;
         this.fieldToBase.put(src.getField(), src.getBase());
      }

      var1 = this.pag.allocSources().iterator();

      while(var1.hasNext()) {
         object = var1.next();
         this.handleAllocNode((AllocNode)object);
      }

      boolean verbose = this.pag.getOpts().verbose();

      label117:
      do {
         if (verbose) {
            logger.debug("Worklist has " + this.varNodeWorkList.size() + " nodes.");
         }

         this.aliasWorkList = new HashSet();

         while(!this.varNodeWorkList.isEmpty()) {
            VarNode src = (VarNode)this.varNodeWorkList.iterator().next();
            this.varNodeWorkList.remove(src);
            this.aliasWorkList.add(src);
            this.handleVarNode(src);
         }

         if (verbose) {
            logger.debug("Now handling field references");
         }

         Iterator var13 = this.aliasWorkList.iterator();

         Iterator var4;
         FieldRefNode srcFr;
         while(var13.hasNext()) {
            VarNode src = (VarNode)var13.next();
            var4 = src.getAllFieldRefs().iterator();

            while(var4.hasNext()) {
               srcFr = (FieldRefNode)var4.next();
               SparkField field = srcFr.getField();
               Iterator var7 = this.fieldToBase.get(field).iterator();

               while(var7.hasNext()) {
                  VarNode dst = (VarNode)var7.next();
                  if (src.getP2Set().hasNonEmptyIntersection(dst.getP2Set())) {
                     FieldRefNode dstFr = dst.dot(field);
                     this.aliasEdges.put(srcFr, dstFr);
                     this.aliasEdges.put(dstFr, srcFr);
                     this.fieldRefWorkList.add(srcFr);
                     this.fieldRefWorkList.add(dstFr);
                     if (this.makeP2Set(dstFr).addAll(srcFr.getP2Set().getOldSet(), (PointsToSetInternal)null)) {
                        this.outFieldRefWorkList.add(dstFr);
                     }

                     if (this.makeP2Set(srcFr).addAll(dstFr.getP2Set().getOldSet(), (PointsToSetInternal)null)) {
                        this.outFieldRefWorkList.add(srcFr);
                     }
                  }
               }
            }
         }

         var13 = this.fieldRefWorkList.iterator();

         while(var13.hasNext()) {
            src = (FieldRefNode)var13.next();
            var4 = this.aliasEdges.get(src).iterator();

            while(var4.hasNext()) {
               srcFr = (FieldRefNode)var4.next();
               if (this.makeP2Set(srcFr).addAll(src.getP2Set().getNewSet(), (PointsToSetInternal)null)) {
                  this.outFieldRefWorkList.add(srcFr);
               }
            }

            src.getP2Set().flushNew();
         }

         this.fieldRefWorkList = new HashSet();
         var13 = this.outFieldRefWorkList.iterator();

         while(true) {
            PointsToSetInternal set;
            do {
               if (!var13.hasNext()) {
                  this.outFieldRefWorkList = new HashSet();
                  continue label117;
               }

               src = (FieldRefNode)var13.next();
               set = this.getP2Set(src).getNewSet();
            } while(set.isEmpty());

            Node[] targets = this.pag.loadLookup(src);
            Node[] var17 = targets;
            int var18 = targets.length;

            for(int var19 = 0; var19 < var18; ++var19) {
               Node element0 = var17[var19];
               VarNode target = (VarNode)element0;
               if (target.makeP2Set().addAll(set, (PointsToSetInternal)null)) {
                  this.addToWorklist(target);
               }
            }

            this.getP2Set(src).flushNew();
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
            this.addToWorklist((VarNode)element);
            ret = true;
         }
      }

      return ret;
   }

   protected final boolean handleVarNode(VarNode src) {
      boolean ret = false;
      if (src.getReplacement() != src) {
         throw new RuntimeException("Got bad node " + src + " with rep " + src.getReplacement());
      } else {
         PointsToSetInternal newP2Set = src.getP2Set().getNewSet();
         if (newP2Set.isEmpty()) {
            return false;
         } else {
            if (this.ofcg != null) {
               QueueReader<Node> addedEdges = this.pag.edgeReader();
               this.ofcg.updatedNode(src);
               this.ofcg.build();

               while(addedEdges.hasNext()) {
                  Node addedSrc = (Node)addedEdges.next();
                  Node addedTgt = (Node)addedEdges.next();
                  ret = true;
                  final VarNode base;
                  if (addedSrc instanceof VarNode) {
                     VarNode edgeSrc = (VarNode)addedSrc;
                     if (addedTgt instanceof VarNode) {
                        base = (VarNode)addedTgt;
                        if (base.makeP2Set().addAll(edgeSrc.getP2Set(), (PointsToSetInternal)null)) {
                           this.addToWorklist(base);
                        }
                     } else if (addedTgt instanceof NewInstanceNode) {
                        NewInstanceNode edgeTgt = (NewInstanceNode)addedTgt.getReplacement();
                        if (edgeTgt.makeP2Set().addAll(edgeSrc.getP2Set(), (PointsToSetInternal)null)) {
                           Node[] var9 = this.pag.assignInstanceLookup(edgeTgt);
                           int var10 = var9.length;

                           for(int var11 = 0; var11 < var10; ++var11) {
                              Node element = var9[var11];
                              this.addToWorklist((VarNode)element);
                           }
                        }
                     }
                  } else if (addedSrc instanceof AllocNode) {
                     AllocNode edgeSrc = (AllocNode)addedSrc;
                     base = (VarNode)addedTgt;
                     if (base.makeP2Set().add(edgeSrc)) {
                        this.addToWorklist(base);
                     }
                  } else if (addedSrc instanceof NewInstanceNode && addedTgt instanceof VarNode) {
                     final NewInstanceNode edgeSrc = (NewInstanceNode)addedSrc.getReplacement();
                     base = (VarNode)addedTgt.getReplacement();
                     addedSrc.getP2Set().forall(new P2SetVisitor() {
                        public void visit(Node n) {
                           if (n instanceof ClassConstantNode) {
                              ClassConstantNode ccn = (ClassConstantNode)n;
                              Type ccnType = ccn.getClassConstant().toSootType();
                              SootClass targetClass = ((RefType)ccnType).getSootClass();
                              if (targetClass.resolvingLevel() == 0) {
                                 Scene.v().forceResolve(targetClass.getName(), 2);
                              }

                              base.makeP2Set().add(PropAlias.this.pag.makeAllocNode(edgeSrc.getValue(), ccnType, ccn.getMethod()));
                              PropAlias.this.addToWorklist(base);
                           }

                        }
                     });
                  }

                  FieldRefNode frn = null;
                  if (addedSrc instanceof FieldRefNode) {
                     frn = (FieldRefNode)addedSrc;
                  }

                  if (addedTgt instanceof FieldRefNode) {
                     frn = (FieldRefNode)addedTgt;
                  }

                  if (frn != null) {
                     base = frn.getBase();
                     if (this.fieldToBase.put(frn.getField(), base)) {
                        this.aliasWorkList.add(base);
                     }
                  }
               }
            }

            Node[] simpleTargets = this.pag.simpleLookup(src);
            Node[] storeTargets = simpleTargets;
            int var15 = simpleTargets.length;

            int var21;
            for(var21 = 0; var21 < var15; ++var21) {
               Node element = storeTargets[var21];
               if (element.makeP2Set().addAll(newP2Set, (PointsToSetInternal)null)) {
                  this.addToWorklist((VarNode)element);
                  ret = true;
               }
            }

            storeTargets = this.pag.storeLookup(src);
            Node[] var16 = storeTargets;
            var21 = storeTargets.length;

            for(int var24 = 0; var24 < var21; ++var24) {
               Node element = var16[var24];
               FieldRefNode fr = (FieldRefNode)element;
               if (fr.makeP2Set().addAll(newP2Set, (PointsToSetInternal)null)) {
                  this.fieldRefWorkList.add(fr);
                  ret = true;
               }
            }

            src.getP2Set().flushNew();
            return ret;
         }
      }
   }

   protected final PointsToSetInternal makeP2Set(FieldRefNode n) {
      PointsToSetInternal ret = (PointsToSetInternal)this.loadSets.get(n);
      if (ret == null) {
         ret = this.pag.getSetFactory().newSet((Type)null, this.pag);
         this.loadSets.put(n, ret);
      }

      return ret;
   }

   protected final PointsToSetInternal getP2Set(FieldRefNode n) {
      PointsToSetInternal ret = (PointsToSetInternal)this.loadSets.get(n);
      return (PointsToSetInternal)(ret == null ? EmptyPointsToSet.v() : ret);
   }

   private boolean addToWorklist(VarNode n) {
      if (n.getReplacement() != n) {
         throw new RuntimeException("Adding bad node " + n + " with rep " + n.getReplacement());
      } else {
         return this.varNodeWorkList.add(n);
      }
   }
}
