package soot.dava.toolkits.base.AST.traversals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.Local;
import soot.Value;
import soot.ValueBox;
import soot.dava.internal.AST.ASTAggregatedCondition;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.structuredAnalysis.ReachingDefs;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;

public class ASTUsesAndDefs extends DepthFirstAdapter {
   public static boolean DEBUG = false;
   HashMap<Object, List<DefinitionStmt>> uD = new HashMap();
   HashMap<Object, List> dU = new HashMap();
   ReachingDefs reaching;

   public ASTUsesAndDefs(ASTNode AST) {
      this.reaching = new ReachingDefs(AST);
   }

   public ASTUsesAndDefs(boolean verbose, ASTNode AST) {
      super(verbose);
      this.reaching = new ReachingDefs(AST);
   }

   private List<Value> getUsesFromBoxes(List useBoxes) {
      ArrayList<Value> toReturn = new ArrayList();
      Iterator it = useBoxes.iterator();

      while(it.hasNext()) {
         Value val = ((ValueBox)it.next()).getValue();
         if (val instanceof Local) {
            toReturn.add(val);
         }
      }

      return toReturn;
   }

   public void checkStatementUses(Stmt s, Object useNodeOrStatement) {
      List useBoxes = s.getUseBoxes();
      List<Value> uses = this.getUsesFromBoxes(useBoxes);
      Iterator it = uses.iterator();

      while(it.hasNext()) {
         Local local = (Local)it.next();
         this.createUDDUChain(local, useNodeOrStatement);
      }

      if (s instanceof DefinitionStmt && this.dU.get(s) == null) {
         this.dU.put(s, new ArrayList());
      }

   }

   public void createUDDUChain(Local local, Object useNodeOrStatement) {
      List<DefinitionStmt> reachingDefs = this.reaching.getReachingDefs(local, useNodeOrStatement);
      if (DEBUG) {
         System.out.println("Reaching def for:" + local + " are:" + reachingDefs);
      }

      Object tempObj = this.uD.get(useNodeOrStatement);
      if (tempObj != null) {
         List<DefinitionStmt> tempList = (List)tempObj;
         tempList.addAll(reachingDefs);
         this.uD.put(useNodeOrStatement, tempList);
      } else {
         this.uD.put(useNodeOrStatement, reachingDefs);
      }

      Iterator defIt = reachingDefs.iterator();

      while(defIt.hasNext()) {
         Object defStmt = defIt.next();
         Object useObj = this.dU.get(defStmt);
         List<Object> uses = null;
         if (useObj == null) {
            uses = new ArrayList();
         } else {
            uses = (List)useObj;
         }

         ((List)uses).add(useNodeOrStatement);
         this.dU.put(defStmt, uses);
      }

   }

   public List<Value> getUseList(ASTCondition cond) {
      ArrayList<Value> useList = new ArrayList();
      if (cond instanceof ASTAggregatedCondition) {
         useList.addAll(this.getUseList(((ASTAggregatedCondition)cond).getLeftOp()));
         useList.addAll(this.getUseList(((ASTAggregatedCondition)cond).getRightOp()));
         return useList;
      } else if (cond instanceof ASTUnaryCondition) {
         List<Value> uses = new ArrayList();
         Value val = ((ASTUnaryCondition)cond).getValue();
         if (val instanceof Local) {
            if (DEBUG) {
               System.out.println("adding local from unary condition as a use" + val);
            }

            ((List)uses).add(val);
         } else {
            List useBoxes = val.getUseBoxes();
            uses = this.getUsesFromBoxes(useBoxes);
         }

         return (List)uses;
      } else if (cond instanceof ASTBinaryCondition) {
         Value val = ((ASTBinaryCondition)cond).getConditionExpr();
         List useBoxes = val.getUseBoxes();
         return this.getUsesFromBoxes(useBoxes);
      } else {
         throw new RuntimeException("Method getUseList in ASTUsesAndDefs encountered unknown condition type");
      }
   }

   public void checkConditionalUses(ASTCondition cond, ASTNode node) {
      List<Value> useList = this.getUseList(cond);
      Iterator it = useList.iterator();

      while(it.hasNext()) {
         Local local = (Local)it.next();
         this.createUDDUChain(local, node);
      }

   }

   public void inASTSwitchNode(ASTSwitchNode node) {
      Value val = node.get_Key();
      List<Value> uses = new ArrayList();
      if (val instanceof Local) {
         ((List)uses).add(val);
      } else {
         List useBoxes = val.getUseBoxes();
         uses = this.getUsesFromBoxes(useBoxes);
      }

      Iterator it = ((List)uses).iterator();

      while(it.hasNext()) {
         Local local = (Local)it.next();
         this.createUDDUChain(local, node);
      }

   }

   public void inASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
      Local local = node.getLocal();
      this.createUDDUChain(local, node);
   }

   public void inASTIfNode(ASTIfNode node) {
      ASTCondition cond = node.get_Condition();
      this.checkConditionalUses(cond, node);
   }

   public void inASTIfElseNode(ASTIfElseNode node) {
      ASTCondition cond = node.get_Condition();
      this.checkConditionalUses(cond, node);
   }

   public void inASTWhileNode(ASTWhileNode node) {
      ASTCondition cond = node.get_Condition();
      this.checkConditionalUses(cond, node);
   }

   public void inASTDoWhileNode(ASTDoWhileNode node) {
      ASTCondition cond = node.get_Condition();
      this.checkConditionalUses(cond, node);
   }

   public void inASTForLoopNode(ASTForLoopNode node) {
      Iterator var2 = node.getInit().iterator();

      while(var2.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var2.next();
         Stmt s = as.get_Stmt();
         this.checkStatementUses(s, node);
      }

      ASTCondition cond = node.get_Condition();
      this.checkConditionalUses(cond, node);
      Iterator var7 = node.getUpdate().iterator();

      while(var7.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var7.next();
         Stmt s = as.get_Stmt();
         this.checkStatementUses(s, node);
      }

   }

   public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
      Iterator var2 = node.getStatements().iterator();

      while(var2.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var2.next();
         Stmt s = as.get_Stmt();
         this.checkStatementUses(s, s);
      }

   }

   public List getUDChain(Object node) {
      return (List)this.uD.get(node);
   }

   public List getDUChain(Object node) {
      return (List)this.dU.get(node);
   }

   public HashMap<Object, List> getDUHashMap() {
      return this.dU;
   }

   public void outASTMethodNode(ASTMethodNode node) {
   }

   public void print() {
      System.out.println("\n\n\nPRINTING uD dU CHAINS ______________________________");
      Iterator it = this.dU.keySet().iterator();

      while(true) {
         Object obj;
         do {
            if (!it.hasNext()) {
               System.out.println("END --------PRINTING uD dU CHAINS ______________________________");
               return;
            }

            DefinitionStmt s = (DefinitionStmt)it.next();
            System.out.println("*****The def  " + s + " has following uses:");
            obj = this.dU.get(s);
         } while(obj == null);

         ArrayList list = (ArrayList)obj;
         Iterator tempIt = list.iterator();

         while(tempIt.hasNext()) {
            Object tempUse = tempIt.next();
            System.out.println("-----------Use  " + tempUse);
            System.out.println("----------------Defs of this use:   " + this.uD.get(tempUse));
         }
      }
   }
}
