package soot.dava.toolkits.base.AST.traversals;

import java.util.ArrayList;
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
import soot.dava.toolkits.base.AST.structuredAnalysis.DavaFlowSet;
import soot.dava.toolkits.base.AST.structuredAnalysis.ReachingCopies;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;

public class CopyPropagation extends DepthFirstAdapter {
   public static boolean DEBUG = false;
   ASTNode AST;
   ASTUsesAndDefs useDefs;
   ReachingCopies reachingCopies;
   ASTParentNodeFinder parentOf;
   boolean someCopyStmtModified = false;
   boolean ASTMODIFIED;

   public CopyPropagation(ASTNode AST) {
      this.AST = AST;
      this.ASTMODIFIED = false;
      this.setup();
   }

   public CopyPropagation(boolean verbose, ASTNode AST) {
      super(verbose);
      this.AST = AST;
      this.ASTMODIFIED = false;
      this.setup();
   }

   private void setup() {
      if (DEBUG) {
         System.out.println("computing usesAnd Defs");
      }

      this.useDefs = new ASTUsesAndDefs(this.AST);
      this.AST.apply(this.useDefs);
      if (DEBUG) {
         System.out.println("computing usesAnd Defs....done");
      }

      this.reachingCopies = new ReachingCopies(this.AST);
      this.parentOf = new ASTParentNodeFinder();
      this.AST.apply(this.parentOf);
   }

   public void outASTMethodNode(ASTMethodNode node) {
      if (this.ASTMODIFIED) {
         this.AST.apply(ClosestAbruptTargetFinder.v());
         CopyPropagation prop1 = new CopyPropagation(this.AST);
         this.AST.apply(prop1);
      }

   }

   public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
      Iterator var2 = node.getStatements().iterator();

      while(var2.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var2.next();
         Stmt s = as.get_Stmt();
         if (this.isCopyStmt(s)) {
            this.handleCopyStmt((DefinitionStmt)s);
         }
      }

   }

   public boolean isCopyStmt(Stmt s) {
      if (!(s instanceof DefinitionStmt)) {
         return false;
      } else {
         Value leftOp = ((DefinitionStmt)s).getLeftOp();
         Value rightOp = ((DefinitionStmt)s).getRightOp();
         return leftOp instanceof Local && rightOp instanceof Local;
      }
   }

   public void handleCopyStmt(DefinitionStmt copyStmt) {
      Local definedLocal = (Local)copyStmt.getLeftOp();
      Object temp = this.useDefs.getDUChain(copyStmt);
      ArrayList uses = new ArrayList();
      if (temp != null) {
         uses = (ArrayList)temp;
      }

      if (uses.size() != 0) {
         if (DEBUG) {
            System.out.println(">>>>The defined local:" + definedLocal + " is used in the following");
            System.out.println("\n numof uses:" + uses.size() + uses + ">>>>>>>>>>>>>>>\n\n");
         }

         Local leftLocal = (Local)copyStmt.getLeftOp();
         Local rightLocal = (Local)copyStmt.getRightOp();
         ReachingCopies.LocalPair localPair = this.reachingCopies.new LocalPair(leftLocal, rightLocal);
         Iterator useIt = uses.iterator();

         Object tempUse;
         while(useIt.hasNext()) {
            tempUse = useIt.next();
            DavaFlowSet reaching = this.reachingCopies.getReachingCopies(tempUse);
            if (!reaching.contains(localPair)) {
               return;
            }
         }

         for(useIt = uses.iterator(); useIt.hasNext(); this.replace(leftLocal, rightLocal, tempUse)) {
            tempUse = useIt.next();
            if (DEBUG) {
               System.out.println("copy stmt reached this use" + tempUse);
            }
         }

         this.removeStmt(copyStmt);
         if (this.someCopyStmtModified) {
            this.setup();
            this.someCopyStmtModified = false;
         }
      } else {
         this.removeStmt(copyStmt);
      }

   }

   public void removeStmt(Stmt stmt) {
      Object tempParent = this.parentOf.getParentOf(stmt);
      if (tempParent != null) {
         ASTNode parent = (ASTNode)tempParent;
         if (!(parent instanceof ASTStatementSequenceNode)) {
            throw new RuntimeException("Found a stmt whose parent is not an ASTStatementSequenceNode");
         } else {
            ASTStatementSequenceNode parentNode = (ASTStatementSequenceNode)parent;
            ArrayList<AugmentedStmt> newSequence = new ArrayList();
            Iterator var6 = parentNode.getStatements().iterator();

            while(var6.hasNext()) {
               AugmentedStmt as = (AugmentedStmt)var6.next();
               Stmt s = as.get_Stmt();
               if (s.toString().compareTo(stmt.toString()) != 0) {
                  newSequence.add(as);
               }
            }

            parentNode.setStatements(newSequence);
            this.ASTMODIFIED = true;
         }
      }
   }

   public void modifyUses(Local from, Local to, ASTCondition cond) {
      if (cond instanceof ASTAggregatedCondition) {
         this.modifyUses(from, to, ((ASTAggregatedCondition)cond).getLeftOp());
         this.modifyUses(from, to, ((ASTAggregatedCondition)cond).getRightOp());
      } else {
         ValueBox valBox;
         Value tempVal;
         Local local;
         Iterator it;
         if (cond instanceof ASTUnaryCondition) {
            Value val = ((ASTUnaryCondition)cond).getValue();
            if (val instanceof Local) {
               Local local = (Local)val;
               if (local.getName().compareTo(from.getName()) == 0) {
                  ((ASTUnaryCondition)cond).setValue(to);
                  this.ASTMODIFIED = true;
               }
            } else {
               it = val.getUseBoxes().iterator();

               while(it.hasNext()) {
                  valBox = (ValueBox)it.next();
                  tempVal = valBox.getValue();
                  if (tempVal instanceof Local) {
                     local = (Local)tempVal;
                     if (local.getName().compareTo(from.getName()) == 0) {
                        valBox.setValue(to);
                        this.ASTMODIFIED = true;
                     }
                  }
               }
            }
         } else {
            if (!(cond instanceof ASTBinaryCondition)) {
               throw new RuntimeException("Method getUseList in CopyPropagation encountered unknown condition type");
            }

            Value val = ((ASTBinaryCondition)cond).getConditionExpr();
            it = val.getUseBoxes().iterator();

            while(it.hasNext()) {
               valBox = (ValueBox)it.next();
               tempVal = valBox.getValue();
               if (tempVal instanceof Local) {
                  local = (Local)tempVal;
                  if (local.getName().compareTo(from.getName()) == 0) {
                     valBox.setValue(to);
                     this.ASTMODIFIED = true;
                  }
               }
            }
         }
      }

   }

   public void modifyUseBoxes(Local from, Local to, List useBoxes) {
      Iterator it = useBoxes.iterator();

      while(it.hasNext()) {
         ValueBox valBox = (ValueBox)it.next();
         Value tempVal = valBox.getValue();
         if (tempVal instanceof Local) {
            Local local = (Local)tempVal;
            if (local.getName().compareTo(from.getName()) == 0) {
               valBox.setValue(to);
               this.ASTMODIFIED = true;
            }
         }
      }

   }

   public void replace(Local from, Local to, Object use) {
      if (use instanceof Stmt) {
         Stmt s = (Stmt)use;
         if (this.isCopyStmt(s)) {
            this.someCopyStmtModified = true;
         }

         List useBoxes = s.getUseBoxes();
         if (DEBUG) {
            System.out.println("Printing uses for stmt" + useBoxes);
         }

         this.modifyUseBoxes(from, to, useBoxes);
      } else {
         if (!(use instanceof ASTNode)) {
            throw new RuntimeException("Encountered an unknown use in copyPropagation method replace");
         }

         if (use instanceof ASTSwitchNode) {
            ASTSwitchNode temp = (ASTSwitchNode)use;
            Value val = temp.get_Key();
            if (val instanceof Local) {
               if (((Local)val).getName().compareTo(from.getName()) == 0) {
                  this.ASTMODIFIED = true;
                  temp.set_Key(to);
               }
            } else {
               List useBoxes = val.getUseBoxes();
               this.modifyUseBoxes(from, to, useBoxes);
            }
         } else if (use instanceof ASTSynchronizedBlockNode) {
            ASTSynchronizedBlockNode temp = (ASTSynchronizedBlockNode)use;
            Local local = temp.getLocal();
            if (local.getName().compareTo(from.getName()) == 0) {
               temp.setLocal(to);
               this.ASTMODIFIED = true;
            }
         } else {
            ASTCondition cond;
            if (use instanceof ASTIfNode) {
               if (DEBUG) {
                  System.out.println("Use is an instanceof if node");
               }

               ASTIfNode temp = (ASTIfNode)use;
               cond = temp.get_Condition();
               this.modifyUses(from, to, cond);
            } else if (use instanceof ASTIfElseNode) {
               ASTIfElseNode temp = (ASTIfElseNode)use;
               cond = temp.get_Condition();
               this.modifyUses(from, to, cond);
            } else if (use instanceof ASTWhileNode) {
               ASTWhileNode temp = (ASTWhileNode)use;
               cond = temp.get_Condition();
               this.modifyUses(from, to, cond);
            } else if (use instanceof ASTDoWhileNode) {
               ASTDoWhileNode temp = (ASTDoWhileNode)use;
               cond = temp.get_Condition();
               this.modifyUses(from, to, cond);
            } else {
               if (!(use instanceof ASTForLoopNode)) {
                  throw new RuntimeException("Encountered an unknown ASTNode in copyPropagation method replace");
               }

               ASTForLoopNode temp = (ASTForLoopNode)use;
               Iterator var19 = temp.getInit().iterator();

               Stmt s;
               AugmentedStmt as;
               while(var19.hasNext()) {
                  as = (AugmentedStmt)var19.next();
                  s = as.get_Stmt();
                  this.replace(from, to, s);
               }

               var19 = temp.getUpdate().iterator();

               while(var19.hasNext()) {
                  as = (AugmentedStmt)var19.next();
                  s = as.get_Stmt();
                  this.replace(from, to, s);
               }

               cond = temp.get_Condition();
               this.modifyUses(from, to, cond);
            }
         }
      }

   }
}
