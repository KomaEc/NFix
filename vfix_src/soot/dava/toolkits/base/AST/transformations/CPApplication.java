package soot.dava.toolkits.base.AST.transformations;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.Local;
import soot.SootField;
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
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.structuredAnalysis.CP;
import soot.dava.toolkits.base.AST.structuredAnalysis.CPFlowSet;
import soot.dava.toolkits.base.AST.structuredAnalysis.CPHelper;
import soot.jimple.FieldRef;
import soot.jimple.Stmt;

public class CPApplication extends DepthFirstAdapter {
   CP cp = null;
   String className = null;

   public CPApplication(ASTMethodNode AST, HashMap<String, Object> constantValueFields, HashMap<String, SootField> classNameFieldNameToSootFieldMapping) {
      this.className = AST.getDavaBody().getMethod().getDeclaringClass().getName();
      this.cp = new CP(AST, constantValueFields, classNameFieldNameToSootFieldMapping);
   }

   public CPApplication(boolean verbose, ASTMethodNode AST, HashMap<String, Object> constantValueFields, HashMap<String, SootField> classNameFieldNameToSootFieldMapping) {
      super(verbose);
      this.className = AST.getDavaBody().getMethod().getDeclaringClass().getName();
      this.cp = new CP(AST, constantValueFields, classNameFieldNameToSootFieldMapping);
   }

   public void inASTSwitchNode(ASTSwitchNode node) {
      Object obj = this.cp.getBeforeSet(node);
      if (obj != null) {
         if (obj instanceof CPFlowSet) {
            CPFlowSet beforeSet = (CPFlowSet)obj;
            Value key = node.get_Key();
            if (key instanceof Local) {
               Local useLocal = (Local)key;
               Object value = beforeSet.contains(this.className, useLocal.toString());
               if (value != null) {
                  Value newValue = CPHelper.createConstant(value);
                  if (newValue != null) {
                     node.set_Key(newValue);
                  }
               }
            } else if (key instanceof FieldRef) {
               FieldRef useField = (FieldRef)key;
               SootField usedSootField = useField.getField();
               Object value = beforeSet.contains(usedSootField.getDeclaringClass().getName(), usedSootField.getName().toString());
               if (value != null) {
                  Value newValue = CPHelper.createConstant(value);
                  if (newValue != null) {
                     node.set_Key(newValue);
                  }
               }
            }

         }
      }
   }

   public void inASTForLoopNode(ASTForLoopNode node) {
      Iterator var2 = node.getInit().iterator();

      while(var2.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var2.next();
         Stmt s = as.get_Stmt();
         List useBoxes = s.getUseBoxes();
         Object obj = this.cp.getBeforeSet(s);
         if (obj != null && obj instanceof CPFlowSet) {
            CPFlowSet beforeSet = (CPFlowSet)obj;
            this.substituteUses(useBoxes, beforeSet);
         }
      }

      Object obj = this.cp.getAfterSet(node);
      if (obj != null) {
         if (obj instanceof CPFlowSet) {
            CPFlowSet afterSet = (CPFlowSet)obj;
            ASTCondition cond = node.get_Condition();
            this.changedCondition(cond, afterSet);
            Iterator var12 = node.getUpdate().iterator();

            while(var12.hasNext()) {
               AugmentedStmt as = (AugmentedStmt)var12.next();
               Stmt s = as.get_Stmt();
               List useBoxes = s.getUseBoxes();
               this.substituteUses(useBoxes, afterSet);
            }

         }
      }
   }

   public void inASTWhileNode(ASTWhileNode node) {
      Object obj = this.cp.getAfterSet(node);
      if (obj != null) {
         if (obj instanceof CPFlowSet) {
            CPFlowSet afterSet = (CPFlowSet)obj;
            ASTCondition cond = node.get_Condition();
            this.changedCondition(cond, afterSet);
         }
      }
   }

   public void inASTDoWhileNode(ASTDoWhileNode node) {
      Object obj = this.cp.getAfterSet(node);
      if (obj != null) {
         if (obj instanceof CPFlowSet) {
            CPFlowSet afterSet = (CPFlowSet)obj;
            ASTCondition cond = node.get_Condition();
            this.changedCondition(cond, afterSet);
         }
      }
   }

   public void inASTIfNode(ASTIfNode node) {
      Object obj = this.cp.getBeforeSet(node);
      if (obj != null) {
         if (obj instanceof CPFlowSet) {
            CPFlowSet beforeSet = (CPFlowSet)obj;
            ASTCondition cond = node.get_Condition();
            this.changedCondition(cond, beforeSet);
         }
      }
   }

   public void inASTIfElseNode(ASTIfElseNode node) {
      Object obj = this.cp.getBeforeSet(node);
      if (obj != null) {
         if (obj instanceof CPFlowSet) {
            CPFlowSet beforeSet = (CPFlowSet)obj;
            ASTCondition cond = node.get_Condition();
            this.changedCondition(cond, beforeSet);
         }
      }
   }

   public ASTCondition changedCondition(ASTCondition cond, CPFlowSet set) {
      if (cond instanceof ASTAggregatedCondition) {
         ASTCondition left = this.changedCondition(((ASTAggregatedCondition)cond).getLeftOp(), set);
         ASTCondition right = this.changedCondition(((ASTAggregatedCondition)cond).getRightOp(), set);
         ((ASTAggregatedCondition)cond).setLeftOp(left);
         ((ASTAggregatedCondition)cond).setRightOp(right);
         return cond;
      } else if (cond instanceof ASTUnaryCondition) {
         Value val = ((ASTUnaryCondition)cond).getValue();
         if (val instanceof Local) {
            Object value = set.contains(this.className, ((Local)val).toString());
            if (value != null) {
               Value newValue = CPHelper.createConstant(value);
               if (newValue != null) {
                  ((ASTUnaryCondition)cond).setValue(newValue);
               }
            }
         } else if (val instanceof FieldRef) {
            FieldRef useField = (FieldRef)val;
            SootField usedSootField = useField.getField();
            Object value = set.contains(usedSootField.getDeclaringClass().getName(), usedSootField.getName().toString());
            if (value != null) {
               Value newValue = CPHelper.createConstant(value);
               if (newValue != null) {
                  ((ASTUnaryCondition)cond).setValue(newValue);
               }
            }
         } else {
            this.substituteUses(val.getUseBoxes(), set);
         }

         return cond;
      } else if (cond instanceof ASTBinaryCondition) {
         Value val = ((ASTBinaryCondition)cond).getConditionExpr();
         this.substituteUses(val.getUseBoxes(), set);
         return cond;
      } else {
         throw new RuntimeException("Method getUseList in ASTUsesAndDefs encountered unknown condition type");
      }
   }

   public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
      Iterator var2 = node.getStatements().iterator();

      while(var2.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var2.next();
         Stmt s = as.get_Stmt();
         List useBoxes = s.getUseBoxes();
         Object obj = this.cp.getBeforeSet(s);
         if (obj != null && obj instanceof CPFlowSet) {
            CPFlowSet beforeSet = (CPFlowSet)obj;
            this.substituteUses(useBoxes, beforeSet);
         }
      }

   }

   public void substituteUses(List useBoxes, CPFlowSet beforeSet) {
      Iterator useIt = useBoxes.iterator();

      while(useIt.hasNext()) {
         Object useObj = useIt.next();
         Value use = ((ValueBox)useObj).getValue();
         if (use instanceof Local) {
            Local useLocal = (Local)use;
            Object value = beforeSet.contains(this.className, useLocal.toString());
            if (value != null) {
               Value newValue = CPHelper.createConstant(value);
               if (newValue != null) {
                  ((ValueBox)useObj).setValue(newValue);
               }
            }
         } else if (use instanceof FieldRef) {
            FieldRef useField = (FieldRef)use;
            SootField usedSootField = useField.getField();
            Object value = beforeSet.contains(usedSootField.getDeclaringClass().getName(), usedSootField.getName().toString());
            if (value != null) {
               Value newValue = CPHelper.createConstant(value);
               if (newValue != null) {
                  ((ValueBox)useObj).setValue(newValue);
               }
            }
         }
      }

   }
}
