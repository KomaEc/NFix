package soot.toolkits.graph.pdg;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.options.Options;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.graph.ExceptionalBlockGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.UnitGraph;

public class RegionAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(RegionAnalysis.class);
   protected SootClass m_class = null;
   protected SootMethod m_method = null;
   protected Body m_methodBody;
   protected UnitGraph m_cfg;
   protected UnitGraph m_reverseCFG;
   protected BlockGraph m_blockCFG;
   protected BlockGraph m_reverseBlockCFG;
   protected Hashtable<Integer, Region> m_regions = new Hashtable();
   protected List<Region> m_regionsList = null;
   private int m_regCount = 0;
   private MHGDominatorTree<Block> m_dom;
   private MHGDominatorTree<Block> m_pdom;
   protected Region m_topLevelRegion = null;
   protected Hashtable<Block, Region> m_block2region = null;

   public RegionAnalysis(UnitGraph cfg, SootMethod m, SootClass c) {
      this.m_methodBody = cfg.getBody();
      this.m_cfg = cfg;
      this.m_method = m;
      this.m_class = c;
      if (Options.v().verbose()) {
         logger.debug("[RegionAnalysis]~~~~~~~~~~~~~~~ Begin Region Analsis for method: " + m.getName() + " ~~~~~~~~~~~~~~~~~~~~");
      }

      this.findWeakRegions();
      if (Options.v().verbose()) {
         logger.debug("[RegionAnalysis]~~~~~~~~~~~~~~~ End:" + m.getName() + " ~~~~~~~~~~~~~~~~~~~~");
      }

   }

   private void findWeakRegions() {
      if (this.m_cfg instanceof ExceptionalUnitGraph) {
         this.m_blockCFG = new ExceptionalBlockGraph((ExceptionalUnitGraph)this.m_cfg);
      } else if (this.m_cfg instanceof EnhancedUnitGraph) {
         this.m_blockCFG = new EnhancedBlockGraph((EnhancedUnitGraph)this.m_cfg);
      } else {
         if (!(this.m_cfg instanceof BriefUnitGraph)) {
            throw new RuntimeException("Unsupported CFG passed into the RegionAnalyis constructor!");
         }

         this.m_blockCFG = new BriefBlockGraph((BriefUnitGraph)this.m_cfg);
      }

      this.m_dom = new MHGDominatorTree(new MHGDominatorsFinder(this.m_blockCFG));

      try {
         this.m_pdom = new MHGDominatorTree(new MHGPostDominatorsFinder(this.m_blockCFG));
         if (Options.v().verbose()) {
            logger.debug("[RegionAnalysis] PostDominator tree: ");
         }

         this.m_regCount = -1;
         if (this.m_blockCFG.getHeads().size() == 1) {
            ++this.m_regCount;
            this.m_regions.put(this.m_regCount, this.createRegion(this.m_regCount));
            this.weakRegionDFS2((Block)this.m_blockCFG.getHeads().get(0), this.m_regCount);
         } else if (this.m_blockCFG.getTails().size() == 1) {
            ++this.m_regCount;
            this.m_regions.put(this.m_regCount, this.createRegion(this.m_regCount));
            this.weakRegionDFS((Block)this.m_blockCFG.getTails().get(0), this.m_regCount);
         } else {
            if (Options.v().verbose()) {
               logger.warn("RegionAnalysis: the CFG is multi-headed and tailed, so, the results of this analysis might not be reliable!");
            }

            for(int i = 0; i < this.m_blockCFG.getTails().size(); ++i) {
               ++this.m_regCount;
               this.m_regions.put(this.m_regCount, this.createRegion(this.m_regCount));
               this.weakRegionDFS((Block)this.m_blockCFG.getTails().get(i), this.m_regCount);
            }
         }
      } catch (RuntimeException var2) {
         logger.debug("[RegionAnalysis] Exception in findWeakRegions: " + var2);
      }

   }

   private void weakRegionDFS(Block v, int r) {
      try {
         ((Region)this.m_regions.get(r)).add(v);
         DominatorNode<Block> parentOfV = this.m_dom.getParentOf(this.m_dom.getDode(v));
         Block u2 = parentOfV == null ? null : (Block)parentOfV.getGode();
         List<DominatorNode<Block>> children = this.m_pdom.getChildrenOf(this.m_pdom.getDode(v));

         for(int i = 0; i < children.size(); ++i) {
            DominatorNode<Block> w = (DominatorNode)children.get(i);
            Block u1 = (Block)w.getGode();
            if (u2 != null && u1.equals(u2)) {
               this.weakRegionDFS((Block)w.getGode(), r);
            } else {
               ++this.m_regCount;
               this.m_regions.put(this.m_regCount, this.createRegion(this.m_regCount));
               this.weakRegionDFS((Block)w.getGode(), this.m_regCount);
            }
         }
      } catch (RuntimeException var9) {
         logger.debug((String)"[RegionAnalysis] Exception in weakRegionDFS: ", (Throwable)var9);
         logger.debug("v is  " + v.toShortString() + " in region " + r);
         G.v().out.flush();
      }

   }

   private void weakRegionDFS2(Block v, int r) {
      ((Region)this.m_regions.get(r)).add2Back(v);
      DominatorNode<Block> parentOfV = this.m_pdom.getParentOf(this.m_pdom.getDode(v));
      Block u2 = parentOfV == null ? null : (Block)parentOfV.getGode();
      List<DominatorNode<Block>> children = this.m_dom.getChildrenOf(this.m_dom.getDode(v));

      for(int i = 0; i < children.size(); ++i) {
         DominatorNode<Block> w = (DominatorNode)children.get(i);
         Block u1 = (Block)w.getGode();
         if (u2 != null && u1.equals(u2)) {
            this.weakRegionDFS2((Block)w.getGode(), r);
         } else {
            ++this.m_regCount;
            this.m_regions.put(this.m_regCount, this.createRegion(this.m_regCount));
            this.weakRegionDFS2((Block)w.getGode(), this.m_regCount);
         }
      }

   }

   public List<Region> getRegions() {
      if (this.m_regionsList == null) {
         this.m_regionsList = new ArrayList(this.m_regions.values());
      }

      return this.m_regionsList;
   }

   public Hashtable<Unit, Region> getUnit2RegionMap() {
      Hashtable<Unit, Region> unit2region = new Hashtable();
      List<Region> regions = this.getRegions();
      Iterator itr = regions.iterator();

      while(itr.hasNext()) {
         Region r = (Region)itr.next();
         List<Unit> units = r.getUnits();
         Iterator itr1 = units.iterator();

         while(itr1.hasNext()) {
            Unit u = (Unit)itr1.next();
            unit2region.put(u, r);
         }
      }

      return unit2region;
   }

   public Hashtable<Block, Region> getBlock2RegionMap() {
      if (this.m_block2region == null) {
         this.m_block2region = new Hashtable();
         List<Region> regions = this.getRegions();
         Iterator itr = regions.iterator();

         while(itr.hasNext()) {
            Region r = (Region)itr.next();
            List<Block> blocks = r.getBlocks();
            Iterator itr1 = blocks.iterator();

            while(itr1.hasNext()) {
               Block u = (Block)itr1.next();
               this.m_block2region.put(u, r);
            }
         }
      }

      return this.m_block2region;
   }

   public BlockGraph getBlockCFG() {
      return this.m_blockCFG;
   }

   public DominatorTree<Block> getPostDominatorTree() {
      return this.m_pdom;
   }

   public DominatorTree<Block> getDominatorTree() {
      return this.m_dom;
   }

   public void reset() {
      this.m_regions.clear();
      this.m_regionsList.clear();
      this.m_regionsList = null;
      this.m_block2region.clear();
      this.m_block2region = null;
      this.m_regCount = 0;
   }

   protected Region createRegion(int id) {
      Region region = new Region(id, this.m_method, this.m_class, this.m_cfg);
      if (id == 0) {
         this.m_topLevelRegion = region;
      }

      return region;
   }

   public Region getTopLevelRegion() {
      return this.m_topLevelRegion;
   }

   public static String CFGtoString(DirectedGraph<Block> cfg, boolean blockDetail) {
      String s = "";
      s = s + "Headers: " + cfg.getHeads().size() + " " + cfg.getHeads();
      Iterator it = cfg.iterator();

      Block node;
      while(it.hasNext()) {
         node = (Block)it.next();
         s = s + "Node = " + node.toShortString() + "\n";
         s = s + "Preds:\n";

         Iterator succsIt;
         for(succsIt = cfg.getPredsOf(node).iterator(); succsIt.hasNext(); s = s + ((Block)succsIt.next()).toShortString() + "\n") {
            s = s + "     ";
         }

         s = s + "Succs:\n";

         for(succsIt = cfg.getSuccsOf(node).iterator(); succsIt.hasNext(); s = s + ((Block)succsIt.next()).toShortString() + "\n") {
            s = s + "     ";
         }
      }

      if (blockDetail) {
         s = s + "Blocks Detail:";

         for(it = cfg.iterator(); it.hasNext(); s = s + node + "\n") {
            node = (Block)it.next();
         }
      }

      return s;
   }
}
