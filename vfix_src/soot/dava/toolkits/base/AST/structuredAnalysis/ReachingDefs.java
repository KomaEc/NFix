package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Local;
import soot.Value;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTUnaryBinaryCondition;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.toolkits.base.AST.traversals.AllDefinitionsFinder;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;

public class ReachingDefs extends StructuredAnalysis<Stmt> {
   Object toAnalyze;

   public ReachingDefs(Object analyze) {
      this.toAnalyze = analyze;
      this.process(analyze, new DavaFlowSet());
   }

   public DavaFlowSet<Stmt> emptyFlowSet() {
      return new DavaFlowSet();
   }

   public DavaFlowSet<Stmt> newInitialFlow() {
      DavaFlowSet<Stmt> initial = new DavaFlowSet();
      AllDefinitionsFinder defFinder = new AllDefinitionsFinder();
      ((ASTNode)this.toAnalyze).apply(defFinder);
      List<DefinitionStmt> allDefs = defFinder.getAllDefs();
      Iterator var4 = allDefs.iterator();

      while(var4.hasNext()) {
         DefinitionStmt def = (DefinitionStmt)var4.next();
         initial.add(def);
      }

      return initial;
   }

   public void setMergeType() {
      this.MERGETYPE = 1;
   }

   public DavaFlowSet<Stmt> cloneFlowSet(DavaFlowSet<Stmt> flowSet) {
      return flowSet.clone();
   }

   public DavaFlowSet<Stmt> processUnaryBinaryCondition(ASTUnaryBinaryCondition cond, DavaFlowSet<Stmt> inSet) {
      return inSet;
   }

   public DavaFlowSet<Stmt> processSynchronizedLocal(Local local, DavaFlowSet<Stmt> inSet) {
      return inSet;
   }

   public DavaFlowSet<Stmt> processSwitchKey(Value key, DavaFlowSet<Stmt> inSet) {
      return inSet;
   }

   public DavaFlowSet<Stmt> processStatement(Stmt s, DavaFlowSet<Stmt> inSet) {
      if (inSet == this.NOPATH) {
         return inSet;
      } else {
         if (s instanceof DefinitionStmt) {
            DavaFlowSet<Stmt> toReturn = this.cloneFlowSet(inSet);
            Value leftOp = ((DefinitionStmt)s).getLeftOp();
            if (leftOp instanceof Local) {
               this.kill(toReturn, (Local)leftOp);
               this.gen(toReturn, (DefinitionStmt)s);
               return toReturn;
            }
         }

         return inSet;
      }
   }

   public void gen(DavaFlowSet<Stmt> in, DefinitionStmt s) {
      in.add(s);
   }

   public void kill(DavaFlowSet<Stmt> in, Local redefined) {
      String redefinedLocalName = redefined.getName();
      Iterator listIt = in.iterator();

      while(listIt.hasNext()) {
         DefinitionStmt tempStmt = (DefinitionStmt)listIt.next();
         Value leftOp = tempStmt.getLeftOp();
         if (leftOp instanceof Local) {
            String storedLocalName = ((Local)leftOp).getName();
            if (redefinedLocalName.compareTo(storedLocalName) == 0) {
               listIt.remove();
            }
         }
      }

   }

   public List<DefinitionStmt> getReachingDefs(Local local, Object node) {
      ArrayList<DefinitionStmt> toReturn = new ArrayList();
      DavaFlowSet<Stmt> beforeSet = null;
      if (!(node instanceof ASTWhileNode) && !(node instanceof ASTDoWhileNode) && !(node instanceof ASTUnconditionalLoopNode) && !(node instanceof ASTForLoopNode)) {
         beforeSet = this.getBeforeSet(node);
      } else {
         beforeSet = this.getAfterSet(node);
      }

      if (beforeSet == null) {
         throw new RuntimeException("Could not get reaching defs of node");
      } else {
         Iterator var5 = beforeSet.iterator();

         while(var5.hasNext()) {
            Object temp = var5.next();
            if (!(temp instanceof DefinitionStmt)) {
               throw new RuntimeException("Not an instanceof DefinitionStmt" + temp);
            }

            DefinitionStmt stmt = (DefinitionStmt)temp;
            Value leftOp = stmt.getLeftOp();
            if (leftOp.toString().compareTo(local.toString()) == 0) {
               toReturn.add(stmt);
            }
         }

         return toReturn;
      }
   }

   public void reachingDefsToString(Object node) {
      DavaFlowSet<Stmt> beforeSet = null;
      if (!(node instanceof ASTWhileNode) && !(node instanceof ASTDoWhileNode) && !(node instanceof ASTUnconditionalLoopNode) && !(node instanceof ASTForLoopNode)) {
         beforeSet = this.getBeforeSet(node);
      } else {
         beforeSet = this.getAfterSet(node);
      }

      if (beforeSet == null) {
         throw new RuntimeException("Could not get reaching defs of node");
      } else {
         Iterator var3 = beforeSet.iterator();

         while(var3.hasNext()) {
            Object o = var3.next();
            System.out.println("Reaching def:" + o);
         }

      }
   }
}
