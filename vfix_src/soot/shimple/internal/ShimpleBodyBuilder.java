package soot.shimple.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.DefinitionStmt;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.base.Aggregator;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.scalar.LocalNameStandardizer;
import soot.jimple.toolkits.scalar.NopEliminator;
import soot.jimple.toolkits.scalar.UnconditionalBranchFolder;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.options.ShimpleOptions;
import soot.shimple.DefaultShimpleFactory;
import soot.shimple.PhiExpr;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.shimple.ShimpleFactory;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.scalar.UnusedLocalEliminator;

public class ShimpleBodyBuilder {
   protected ShimpleBody body;
   protected ShimpleFactory sf;
   protected DominatorTree<Block> dt;
   protected BlockGraph cfg;
   protected List<Local> origLocals;
   public PhiNodeManager phi;
   public PiNodeManager pi;
   ShimpleOptions options;
   protected Map<String, Local> newLocals;
   protected Map<Local, Local> newLocalsToOldLocal;
   protected int[] assignmentCounters;
   protected Stack<Integer>[] namingStacks;

   public ShimpleBodyBuilder(ShimpleBody body) {
      NopEliminator.v().transform(body);
      this.body = body;
      this.sf = new DefaultShimpleFactory(body);
      this.sf.clearCache();
      this.phi = new PhiNodeManager(body, this.sf);
      this.pi = new PiNodeManager(body, false, this.sf);
      this.options = body.getOptions();
      this.makeUniqueLocalNames();
   }

   public void update() {
      this.cfg = this.sf.getBlockGraph();
      this.dt = this.sf.getDominatorTree();
      this.origLocals = new ArrayList(this.body.getLocals());
   }

   public void transform() {
      this.phi.insertTrivialPhiNodes();
      boolean change = false;
      if (this.options.extended()) {
         for(change = this.pi.insertTrivialPiNodes(); change && this.phi.insertTrivialPhiNodes(); change = this.pi.insertTrivialPiNodes()) {
         }
      }

      this.renameLocals();
      this.phi.trimExceptionalPhiNodes();
      this.makeUniqueLocalNames();
   }

   public void preElimOpt() {
   }

   public void postElimOpt() {
      boolean optElim = this.options.node_elim_opt();
      if (optElim) {
         DeadAssignmentEliminator.v().transform(this.body);
         UnreachableCodeEliminator.v().transform(this.body);
         UnconditionalBranchFolder.v().transform(this.body);
         Aggregator.v().transform(this.body);
         UnusedLocalEliminator.v().transform(this.body);
      }

   }

   public void eliminatePhiNodes() {
      if (this.phi.doEliminatePhiNodes()) {
         this.makeUniqueLocalNames();
      }

   }

   public void eliminatePiNodes() {
      boolean optElim = this.options.node_elim_opt();
      this.pi.eliminatePiNodes(optElim);
   }

   public void renameLocals() {
      this.update();
      this.newLocals = new HashMap();
      this.newLocalsToOldLocal = new HashMap();
      this.assignmentCounters = new int[this.origLocals.size()];
      this.namingStacks = new Stack[this.origLocals.size()];

      for(int i = 0; i < this.namingStacks.length; ++i) {
         this.namingStacks[i] = new Stack();
      }

      List<Block> heads = this.cfg.getHeads();
      if (!heads.isEmpty()) {
         if (heads.size() != 1) {
            throw new RuntimeException("Assertion failed:  Only one head expected.");
         } else {
            Block entry = (Block)heads.get(0);
            this.renameLocalsSearch(entry);
         }
      }
   }

   public void renameLocalsSearch(Block block) {
      List<Local> lhsLocals = new ArrayList();
      Iterator lhsLocalsIt = block.iterator();

      Local phiArg;
      while(lhsLocalsIt.hasNext()) {
         Unit unit = (Unit)lhsLocalsIt.next();
         List<ValueBox> useBoxes = new ArrayList();
         if (!Shimple.isPhiNode(unit)) {
            useBoxes.addAll(unit.getUseBoxes());
         }

         Iterator var6 = useBoxes.iterator();

         ValueBox lhsLocalBox;
         int localIndex;
         while(var6.hasNext()) {
            lhsLocalBox = (ValueBox)var6.next();
            Value use = lhsLocalBox.getValue();
            localIndex = this.indexOfLocal(use);
            if (localIndex != -1) {
               phiArg = (Local)use;
               if (!this.namingStacks[localIndex].empty()) {
                  Integer subscript = (Integer)this.namingStacks[localIndex].peek();
                  Local renamedLocal = this.fetchNewLocal(phiArg, subscript);
                  lhsLocalBox.setValue(renamedLocal);
               }
            }
         }

         if (unit instanceof DefinitionStmt) {
            DefinitionStmt defStmt = (DefinitionStmt)unit;
            Value lhsValue = defStmt.getLeftOp();
            if (this.origLocals.contains(lhsValue)) {
               lhsLocalBox = defStmt.getLeftOpBox();
               Local lhsLocal = (Local)lhsValue;
               lhsLocals.add(lhsLocal);
               localIndex = this.indexOfLocal(lhsLocal);
               if (localIndex == -1) {
                  throw new RuntimeException("Assertion failed.");
               }

               Integer subscript = this.assignmentCounters[localIndex];
               Local newLhsLocal = this.fetchNewLocal(lhsLocal, subscript);
               lhsLocalBox.setValue(newLhsLocal);
               this.namingStacks[localIndex].push(subscript);
               int var10002 = this.assignmentCounters[localIndex]++;
            }
         }
      }

      lhsLocalsIt = this.cfg.getSuccsOf(block).iterator();

      while(true) {
         Block succ;
         do {
            if (!lhsLocalsIt.hasNext()) {
               DominatorNode<Block> node = this.dt.getDode(block);
               Iterator childrenIt = this.dt.getChildrenOf(node).iterator();

               while(childrenIt.hasNext()) {
                  DominatorNode<Block> childNode = (DominatorNode)childrenIt.next();
                  this.renameLocalsSearch((Block)childNode.getGode());
               }

               lhsLocalsIt = lhsLocals.iterator();

               while(lhsLocalsIt.hasNext()) {
                  Local lhsLocal = (Local)lhsLocalsIt.next();
                  int lhsLocalIndex = this.indexOfLocal(lhsLocal);
                  if (lhsLocalIndex == -1) {
                     throw new RuntimeException("Assertion failed.");
                  }

                  this.namingStacks[lhsLocalIndex].pop();
               }

               return;
            }

            succ = (Block)lhsLocalsIt.next();
         } while(block.getHead() == null && block.getTail() == null);

         Iterator var19 = succ.iterator();

         while(var19.hasNext()) {
            Unit unit = (Unit)var19.next();
            PhiExpr phiExpr = Shimple.getPhiExpr(unit);
            if (phiExpr != null) {
               int argIndex = phiExpr.getArgIndex(block);
               if (argIndex == -1) {
                  throw new RuntimeException("Assertion failed.");
               }

               ValueBox phiArgBox = phiExpr.getArgBox(argIndex);
               phiArg = (Local)phiArgBox.getValue();
               int localIndex = this.indexOfLocal(phiArg);
               if (localIndex == -1) {
                  throw new RuntimeException("Assertion failed.");
               }

               if (!this.namingStacks[localIndex].empty()) {
                  Integer subscript = (Integer)this.namingStacks[localIndex].peek();
                  Local newPhiArg = this.fetchNewLocal(phiArg, subscript);
                  phiArgBox.setValue(newPhiArg);
               }
            }
         }
      }
   }

   protected Local fetchNewLocal(Local local, Integer subscript) {
      Local oldLocal = local;
      if (!this.origLocals.contains(local)) {
         oldLocal = (Local)this.newLocalsToOldLocal.get(local);
      }

      if (subscript == 0) {
         return oldLocal;
      } else {
         String name = oldLocal.getName() + "_" + subscript;
         Local newLocal = (Local)this.newLocals.get(name);
         if (newLocal == null) {
            newLocal = new JimpleLocal(name, oldLocal.getType());
            this.newLocals.put(name, newLocal);
            this.newLocalsToOldLocal.put(newLocal, oldLocal);
            this.body.getLocals().add(newLocal);
         }

         return (Local)newLocal;
      }
   }

   protected int indexOfLocal(Value local) {
      int localIndex = this.origLocals.indexOf(local);
      if (localIndex == -1) {
         Local oldLocal = (Local)this.newLocalsToOldLocal.get(local);
         localIndex = this.origLocals.indexOf(oldLocal);
      }

      return localIndex;
   }

   public void makeUniqueLocalNames() {
      if (this.options.standard_local_names()) {
         LocalNameStandardizer.v().transform(this.body);
      } else {
         Set<String> localNames = new HashSet();
         Iterator localsIt = this.body.getLocals().iterator();

         while(localsIt.hasNext()) {
            Local local = (Local)localsIt.next();
            String localName = local.getName();
            if (localNames.contains(localName)) {
               String uniqueName = this.makeUniqueLocalName(localName, localNames);
               local.setName(uniqueName);
               localNames.add(uniqueName);
            } else {
               localNames.add(localName);
            }
         }

      }
   }

   public String makeUniqueLocalName(String dupName, Set<String> localNames) {
      int counter = 1;

      String newName;
      for(newName = dupName; localNames.contains(newName); newName = dupName + "_" + counter++) {
      }

      return newName;
   }
}
