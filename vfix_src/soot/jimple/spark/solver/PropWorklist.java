package soot.jimple.spark.solver;

import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;
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

public final class PropWorklist extends Propagator {
   private static final Logger logger = LoggerFactory.getLogger(PropWorklist.class);
   protected final Set<VarNode> varNodeWorkList = new TreeSet();
   protected PAG pag;
   protected OnFlyCallGraph ofcg;

   public PropWorklist(PAG pag) {
      this.pag = pag;
   }

   public final void propagate() {
      this.ofcg = this.pag.getOnFlyCallGraph();
      (new TopoSorter(this.pag, false)).sort();
      Iterator var1 = this.pag.allocSources().iterator();

      while(var1.hasNext()) {
         AllocNode object = (AllocNode)var1.next();
         this.handleAllocNode(object);
      }

      boolean verbose = this.pag.getOpts().verbose();

      do {
         if (verbose) {
            logger.debug("Worklist has " + this.varNodeWorkList.size() + " nodes.");
         }

         while(!this.varNodeWorkList.isEmpty()) {
            VarNode src = (VarNode)this.varNodeWorkList.iterator().next();
            this.varNodeWorkList.remove(src);
            this.handleVarNode(src);
         }

         if (verbose) {
            logger.debug("Now handling field references");
         }

         Iterator var13 = this.pag.storeSources().iterator();

         while(var13.hasNext()) {
            Object object = var13.next();
            final VarNode src = (VarNode)object;
            Node[] targets = this.pag.storeLookup(src);
            Node[] var6 = targets;
            int var7 = targets.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Node element0 = var6[var8];
               final FieldRefNode target = (FieldRefNode)element0;
               target.getBase().makeP2Set().forall(new P2SetVisitor() {
                  public final void visit(Node n) {
                     AllocDotField nDotF = PropWorklist.this.pag.makeAllocDotField((AllocNode)n, target.getField());
                     if (PropWorklist.this.ofcg != null) {
                        PropWorklist.this.ofcg.updatedFieldRef(nDotF, src.getP2Set());
                     }

                     nDotF.makeP2Set().addAll(src.getP2Set(), (PointsToSetInternal)null);
                  }
               });
            }
         }

         HashSet<Object[]> edgesToPropagate = new HashSet();
         Iterator var15 = this.pag.loadSources().iterator();

         while(var15.hasNext()) {
            Object object = var15.next();
            this.handleFieldRefNode((FieldRefNode)object, edgesToPropagate);
         }

         Set<PointsToSetInternal> nodesToFlush = Collections.newSetFromMap(new IdentityHashMap());

         Iterator var18;
         PointsToSetInternal nDotF;
         for(var18 = edgesToPropagate.iterator(); var18.hasNext(); nodesToFlush.add(nDotF)) {
            Object[] pair = (Object[])var18.next();
            nDotF = (PointsToSetInternal)pair[0];
            PointsToSetInternal newP2Set = nDotF.getNewSet();
            VarNode loadTarget = (VarNode)pair[1];
            if (loadTarget.makeP2Set().addAll(newP2Set, (PointsToSetInternal)null)) {
               this.varNodeWorkList.add(loadTarget);
            }
         }

         var18 = nodesToFlush.iterator();

         while(var18.hasNext()) {
            PointsToSetInternal nDotF = (PointsToSetInternal)var18.next();
            nDotF.flushNew();
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
            this.varNodeWorkList.add((VarNode)element);
            ret = true;
         }
      }

      return ret;
   }

   protected final boolean handleVarNode(VarNode src) {
      boolean ret = false;
      boolean flush = true;
      if (src.getReplacement() != src) {
         throw new RuntimeException("Got bad node " + src + " with rep " + src.getReplacement());
      } else {
         final PointsToSetInternal newP2Set = src.getP2Set().getNewSet();
         if (newP2Set.isEmpty()) {
            return false;
         } else {
            Node[] p;
            if (this.ofcg != null) {
               QueueReader<Node> addedEdges = this.pag.edgeReader();
               this.ofcg.updatedNode(src);
               this.ofcg.build();

               label134:
               while(true) {
                  while(true) {
                     while(true) {
                        if (!addedEdges.hasNext()) {
                           break label134;
                        }

                        Node addedSrc = (Node)addedEdges.next();
                        Node addedTgt = (Node)addedEdges.next();
                        ret = true;
                        VarNode edgeTgt;
                        final VarNode edgeTgt;
                        if (addedSrc instanceof VarNode) {
                           edgeTgt = (VarNode)addedSrc.getReplacement();
                           if (addedTgt instanceof VarNode) {
                              edgeTgt = (VarNode)addedTgt.getReplacement();
                              if (edgeTgt.makeP2Set().addAll(edgeTgt.getP2Set(), (PointsToSetInternal)null)) {
                                 this.varNodeWorkList.add(edgeTgt);
                                 if (edgeTgt == src) {
                                    flush = false;
                                 }
                              }
                           } else if (addedTgt instanceof NewInstanceNode) {
                              NewInstanceNode edgeTgt = (NewInstanceNode)addedTgt.getReplacement();
                              if (edgeTgt.makeP2Set().addAll(edgeTgt.getP2Set(), (PointsToSetInternal)null)) {
                                 p = this.pag.assignInstanceLookup(edgeTgt);
                                 int var11 = p.length;

                                 for(int var12 = 0; var12 < var11; ++var12) {
                                    Node element = p[var12];
                                    this.varNodeWorkList.add((VarNode)element);
                                    if (element == src) {
                                       flush = false;
                                    }
                                 }
                              }
                           }
                        } else if (addedSrc instanceof AllocNode) {
                           edgeTgt = (VarNode)addedTgt.getReplacement();
                           if (edgeTgt.makeP2Set().add(addedSrc)) {
                              this.varNodeWorkList.add(edgeTgt);
                              if (edgeTgt == src) {
                                 flush = false;
                              }
                           }
                        } else if (addedSrc instanceof NewInstanceNode && addedTgt instanceof VarNode) {
                           final NewInstanceNode edgeSrc = (NewInstanceNode)addedSrc.getReplacement();
                           edgeTgt = (VarNode)addedTgt.getReplacement();
                           addedSrc.getP2Set().forall(new P2SetVisitor() {
                              public void visit(Node n) {
                                 if (n instanceof ClassConstantNode) {
                                    ClassConstantNode ccn = (ClassConstantNode)n;
                                    Type ccnType = ccn.getClassConstant().toSootType();
                                    SootClass targetClass = ((RefType)ccnType).getSootClass();
                                    if (targetClass.resolvingLevel() == 0) {
                                       Scene.v().forceResolve(targetClass.getName(), 2);
                                    }

                                    edgeTgt.makeP2Set().add(PropWorklist.this.pag.makeAllocNode(edgeSrc.getValue(), ccnType, ccn.getMethod()));
                                    PropWorklist.this.varNodeWorkList.add(edgeTgt);
                                 }

                              }
                           });
                           if (edgeTgt.makeP2Set().add(addedSrc) && edgeTgt == src) {
                              flush = false;
                           }
                        }
                     }
                  }
               }
            }

            Node[] simpleTargets = this.pag.simpleLookup(src);
            Node[] storeTargets = simpleTargets;
            int var16 = simpleTargets.length;

            int var20;
            for(var20 = 0; var20 < var16; ++var20) {
               Node element = storeTargets[var20];
               if (element.makeP2Set().addAll(newP2Set, (PointsToSetInternal)null)) {
                  this.varNodeWorkList.add((VarNode)element);
                  if (element == src) {
                     flush = false;
                  }

                  ret = true;
               }
            }

            storeTargets = this.pag.storeLookup(src);
            Node[] var17 = storeTargets;
            var20 = storeTargets.length;

            for(int var24 = 0; var24 < var20; ++var24) {
               Node element = var17[var24];
               FieldRefNode fr = (FieldRefNode)element;
               final SparkField f = fr.getField();
               ret |= fr.getBase().getP2Set().forall(new P2SetVisitor() {
                  public final void visit(Node n) {
                     AllocDotField nDotF = PropWorklist.this.pag.makeAllocDotField((AllocNode)n, f);
                     if (nDotF.makeP2Set().addAll(newP2Set, (PointsToSetInternal)null)) {
                        this.returnValue = true;
                     }

                  }
               });
            }

            final HashSet<Node[]> storesToPropagate = new HashSet();
            final HashSet<Node[]> loadsToPropagate = new HashSet();
            Iterator var27 = src.getAllFieldRefs().iterator();

            while(var27.hasNext()) {
               FieldRefNode fr = (FieldRefNode)var27.next();
               final SparkField field = fr.getField();
               final Node[] storeSources = this.pag.storeInvLookup(fr);
               if (storeSources.length > 0) {
                  newP2Set.forall(new P2SetVisitor() {
                     public final void visit(Node n) {
                        AllocDotField nDotF = PropWorklist.this.pag.makeAllocDotField((AllocNode)n, field);
                        Node[] var3 = storeSources;
                        int var4 = var3.length;

                        for(int var5 = 0; var5 < var4; ++var5) {
                           Node element = var3[var5];
                           Node[] pair = new Node[]{element, nDotF.getReplacement()};
                           storesToPropagate.add(pair);
                        }

                     }
                  });
               }

               final Node[] loadTargets = this.pag.loadLookup(fr);
               if (loadTargets.length > 0) {
                  newP2Set.forall(new P2SetVisitor() {
                     public final void visit(Node n) {
                        AllocDotField nDotF = PropWorklist.this.pag.makeAllocDotField((AllocNode)n, field);
                        if (nDotF != null) {
                           Node[] var3 = loadTargets;
                           int var4 = var3.length;

                           for(int var5 = 0; var5 < var4; ++var5) {
                              Node element = var3[var5];
                              Node[] pair = new Node[]{nDotF.getReplacement(), element};
                              loadsToPropagate.add(pair);
                           }
                        }

                     }
                  });
               }
            }

            if (flush) {
               src.getP2Set().flushNew();
            }

            var27 = storesToPropagate.iterator();

            while(var27.hasNext()) {
               p = (Node[])var27.next();
               VarNode storeSource = (VarNode)p[0];
               AllocDotField nDotF = (AllocDotField)p[1];
               if (nDotF.makeP2Set().addAll(storeSource.getP2Set(), (PointsToSetInternal)null)) {
                  ret = true;
               }
            }

            var27 = loadsToPropagate.iterator();

            while(var27.hasNext()) {
               p = (Node[])var27.next();
               AllocDotField nDotF = (AllocDotField)p[0];
               VarNode loadTarget = (VarNode)p[1];
               if (loadTarget.makeP2Set().addAll(nDotF.getP2Set(), (PointsToSetInternal)null)) {
                  this.varNodeWorkList.add(loadTarget);
                  ret = true;
               }
            }

            return ret;
         }
      }
   }

   protected final void handleFieldRefNode(FieldRefNode src, final HashSet<Object[]> edgesToPropagate) {
      final Node[] loadTargets = this.pag.loadLookup(src);
      if (loadTargets.length != 0) {
         final SparkField field = src.getField();
         src.getBase().getP2Set().forall(new P2SetVisitor() {
            public final void visit(Node n) {
               AllocDotField nDotF = PropWorklist.this.pag.makeAllocDotField((AllocNode)n, field);
               if (nDotF != null) {
                  PointsToSetInternal p2Set = nDotF.getP2Set();
                  if (!p2Set.getNewSet().isEmpty()) {
                     Node[] var4 = loadTargets;
                     int var5 = var4.length;

                     for(int var6 = 0; var6 < var5; ++var6) {
                        Node element = var4[var6];
                        Object[] pair = new Object[]{p2Set, element};
                        edgesToPropagate.add(pair);
                     }
                  }
               }

            }
         });
      }
   }
}
