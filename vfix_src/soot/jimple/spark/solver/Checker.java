package soot.jimple.spark.solver;

import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.FastHierarchy;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;

public class Checker {
   private static final Logger logger = LoggerFactory.getLogger(Checker.class);
   protected PAG pag;

   public Checker(PAG pag) {
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

   }

   protected void checkAll(final Node container, PointsToSetInternal nodes, final Node upstream) {
      nodes.forall(new P2SetVisitor() {
         public final void visit(Node n) {
            Checker.this.checkNode(container, n, upstream);
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

   protected void handleStores(final VarNode src) {
      final PointsToSetInternal srcSet = src.getP2Set();
      if (!srcSet.isEmpty()) {
         Node[] storeTargets = this.pag.storeLookup(src);
         Node[] var4 = storeTargets;
         int var5 = storeTargets.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Node element = var4[var6];
            FieldRefNode fr = (FieldRefNode)element;
            final SparkField f = fr.getField();
            fr.getBase().getP2Set().forall(new P2SetVisitor() {
               public final void visit(Node n) {
                  AllocDotField nDotF = Checker.this.pag.makeAllocDotField((AllocNode)n, f);
                  Checker.this.checkAll(nDotF, srcSet, src);
               }
            });
         }

      }
   }

   protected void handleLoads(final FieldRefNode src) {
      final Node[] loadTargets = this.pag.loadLookup(src);
      final SparkField f = src.getField();
      src.getBase().getP2Set().forall(new P2SetVisitor() {
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
                     Checker.this.checkAll(target, set, src);
                  }

               }
            }
         }
      });
   }
}
