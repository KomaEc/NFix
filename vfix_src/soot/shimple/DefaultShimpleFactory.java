package soot.shimple;

import soot.Body;
import soot.PointsToAnalysis;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.pointer.SideEffectAnalysis;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.shimple.toolkits.graph.GlobalValueNumberer;
import soot.shimple.toolkits.graph.SimpleGlobalValueNumberer;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BlockGraphConverter;
import soot.toolkits.graph.CytronDominanceFrontier;
import soot.toolkits.graph.DominanceFrontier;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.graph.DominatorsFinder;
import soot.toolkits.graph.ExceptionalBlockGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.HashReversibleGraph;
import soot.toolkits.graph.ReversibleGraph;
import soot.toolkits.graph.SimpleDominatorsFinder;
import soot.toolkits.graph.UnitGraph;

public class DefaultShimpleFactory implements ShimpleFactory {
   protected final Body body;
   protected BlockGraph bg;
   protected UnitGraph ug;
   protected DominatorsFinder<Block> dFinder;
   protected DominatorTree<Block> dTree;
   protected DominanceFrontier<Block> dFrontier;
   protected PointsToAnalysis pta;
   protected CallGraph cg;
   protected SideEffectAnalysis sea;
   protected GlobalValueNumberer gvn;
   protected ReversibleGraph<Block> rbg;
   protected DominatorTree<Block> rdTree;
   protected DominanceFrontier<Block> rdFrontier;
   protected DominatorsFinder<Block> rdFinder;

   public DefaultShimpleFactory(Body body) {
      this.body = body;
   }

   public void clearCache() {
      this.bg = null;
      this.ug = null;
      this.dFinder = null;
      this.dTree = null;
      this.dFrontier = null;
      this.pta = null;
      this.cg = null;
      this.sea = null;
      this.gvn = null;
      this.rbg = null;
      this.rdTree = null;
      this.rdFinder = null;
      this.rdFrontier = null;
   }

   public Body getBody() {
      if (this.body == null) {
         throw new RuntimeException("Assertion failed: Call setBody() first.");
      } else {
         return this.body;
      }
   }

   public ReversibleGraph<Block> getReverseBlockGraph() {
      if (this.rbg != null) {
         return this.rbg;
      } else {
         BlockGraph bg = this.getBlockGraph();
         this.rbg = new HashReversibleGraph(bg);
         this.rbg.reverse();
         return this.rbg;
      }
   }

   public DominatorsFinder<Block> getReverseDominatorsFinder() {
      if (this.rdFinder != null) {
         return this.rdFinder;
      } else {
         this.rdFinder = new SimpleDominatorsFinder(this.getReverseBlockGraph());
         return this.rdFinder;
      }
   }

   public DominatorTree<Block> getReverseDominatorTree() {
      if (this.rdTree != null) {
         return this.rdTree;
      } else {
         this.rdTree = new DominatorTree(this.getReverseDominatorsFinder());
         return this.rdTree;
      }
   }

   public DominanceFrontier<Block> getReverseDominanceFrontier() {
      if (this.rdFrontier != null) {
         return this.rdFrontier;
      } else {
         this.rdFrontier = new CytronDominanceFrontier(this.getReverseDominatorTree());
         return this.rdFrontier;
      }
   }

   public BlockGraph getBlockGraph() {
      if (this.bg != null) {
         return this.bg;
      } else {
         this.bg = new ExceptionalBlockGraph((ExceptionalUnitGraph)this.getUnitGraph());
         BlockGraphConverter.addStartStopNodesTo(this.bg);
         return this.bg;
      }
   }

   public UnitGraph getUnitGraph() {
      if (this.ug != null) {
         return this.ug;
      } else {
         UnreachableCodeEliminator.v().transform(this.getBody());
         this.ug = new ExceptionalUnitGraph(this.getBody());
         return this.ug;
      }
   }

   public DominatorsFinder<Block> getDominatorsFinder() {
      if (this.dFinder != null) {
         return this.dFinder;
      } else {
         this.dFinder = new SimpleDominatorsFinder(this.getBlockGraph());
         return this.dFinder;
      }
   }

   public DominatorTree<Block> getDominatorTree() {
      if (this.dTree != null) {
         return this.dTree;
      } else {
         this.dTree = new DominatorTree(this.getDominatorsFinder());
         return this.dTree;
      }
   }

   public DominanceFrontier<Block> getDominanceFrontier() {
      if (this.dFrontier != null) {
         return this.dFrontier;
      } else {
         this.dFrontier = new CytronDominanceFrontier(this.getDominatorTree());
         return this.dFrontier;
      }
   }

   public GlobalValueNumberer getGlobalValueNumberer() {
      if (this.gvn != null) {
         return this.gvn;
      } else {
         this.gvn = new SimpleGlobalValueNumberer(this.getBlockGraph());
         return this.gvn;
      }
   }
}
