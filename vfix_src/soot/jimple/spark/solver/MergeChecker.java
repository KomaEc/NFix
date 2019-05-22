package soot.jimple.spark.solver;

import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.FastHierarchy;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.util.HashMultiMap;
import soot.util.MultiMap;

public class MergeChecker {
   private static final Logger logger = LoggerFactory.getLogger(MergeChecker.class);
   protected PAG pag;
   protected MultiMap<SparkField, VarNode> fieldToBase = new HashMultiMap();

   public MergeChecker(PAG pag) {
      this.pag = pag;
   }

   public void check() {
      Iterator var1 = this.pag.allocSources().iterator();

      Object object;
      while(var1.hasNext()) {
         object = var1.next();
         this.handleAllocNode((AllocNode)object);
      }

      var1 = this.pag.simpleSources().iterator();

      while(var1.hasNext()) {
         object = var1.next();
         this.handleSimples((VarNode)object);
      }

      var1 = this.pag.loadSources().iterator();

      while(var1.hasNext()) {
         object = var1.next();
         this.handleLoads((FieldRefNode)object);
      }

      var1 = this.pag.storeSources().iterator();

      while(var1.hasNext()) {
         object = var1.next();
         this.handleStores((VarNode)object);
      }

      var1 = this.pag.loadSources().iterator();

      FieldRefNode fr;
      while(var1.hasNext()) {
         object = var1.next();
         fr = (FieldRefNode)object;
         this.fieldToBase.put(fr.getField(), fr.getBase());
      }

      var1 = this.pag.storeInvSources().iterator();

      while(var1.hasNext()) {
         object = var1.next();
         fr = (FieldRefNode)object;
         this.fieldToBase.put(fr.getField(), fr.getBase());
      }

      var1 = this.pag.getVarNodeNumberer().iterator();

      while(var1.hasNext()) {
         VarNode src = (VarNode)var1.next();
         Iterator var8 = src.getAllFieldRefs().iterator();

         while(var8.hasNext()) {
            FieldRefNode fr = (FieldRefNode)var8.next();
            Iterator var5 = this.fieldToBase.get(fr.getField()).iterator();

            while(var5.hasNext()) {
               VarNode dst = (VarNode)var5.next();
               if (src.getP2Set().hasNonEmptyIntersection(dst.getP2Set())) {
                  FieldRefNode fr2 = dst.dot(fr.getField());
                  if (fr2.getReplacement() != fr.getReplacement()) {
                     logger.debug("Check failure: " + fr + " should be merged with " + fr2);
                  }
               }
            }
         }
      }

   }

   protected void checkAll(final Node container, PointsToSetInternal nodes, final Node upstream) {
      nodes.forall(new P2SetVisitor() {
         public final void visit(Node n) {
            MergeChecker.this.checkNode(container, n, upstream);
         }
      });
   }

   protected void checkNode(Node container, Node n, Node upstream) {
      if (container.getReplacement() != container) {
         throw new RuntimeException("container " + container + " is illegal");
      } else if (upstream.getReplacement() != upstream) {
         throw new RuntimeException("upstream " + upstream + " is illegal");
      } else {
         PointsToSetInternal p2set = container.getP2Set();
         FastHierarchy fh = this.pag.getTypeManager().getFastHierarchy();
         if (!p2set.contains(n) && (fh == null || container.getType() == null || fh.canStoreType(n.getType(), container.getType()))) {
            logger.debug("Check failure: " + container + " does not have " + n + "; upstream is " + upstream);
         }

      }
   }

   protected void handleAllocNode(AllocNode src) {
      Node[] targets = this.pag.allocLookup(src);
      Node[] var3 = targets;
      int var4 = targets.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Node element = var3[var5];
         this.checkNode(element, src, src);
      }

   }

   protected void handleSimples(VarNode src) {
      PointsToSetInternal srcSet = src.getP2Set();
      if (!srcSet.isEmpty()) {
         Node[] simpleTargets = this.pag.simpleLookup(src);
         Node[] var4 = simpleTargets;
         int var5 = simpleTargets.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Node element = var4[var6];
            this.checkAll(element, srcSet, src);
         }

      }
   }

   protected void handleStores(VarNode src) {
      PointsToSetInternal srcSet = src.getP2Set();
      if (!srcSet.isEmpty()) {
         Node[] storeTargets = this.pag.storeLookup(src);
         Node[] var4 = storeTargets;
         int var5 = storeTargets.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Node element = var4[var6];
            FieldRefNode fr = (FieldRefNode)element;
            this.checkAll(fr, srcSet, src);
         }

      }
   }

   protected void handleLoads(FieldRefNode src) {
      Node[] loadTargets = this.pag.loadLookup(src);
      PointsToSetInternal set = src.getP2Set();
      if (!set.isEmpty()) {
         Node[] var4 = loadTargets;
         int var5 = loadTargets.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Node element = var4[var6];
            VarNode target = (VarNode)element;
            this.checkAll(target, set, src);
         }

      }
   }
}
