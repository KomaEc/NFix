package soot.dava.toolkits.base.AST.transformations;

import soot.BooleanType;
import soot.Type;
import soot.Value;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.javaRep.DIntConstant;
import soot.dava.internal.javaRep.DNotExpr;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.ConditionExpr;
import soot.jimple.EqExpr;
import soot.jimple.NeExpr;

public class BooleanConditionSimplification extends DepthFirstAdapter {
   public BooleanConditionSimplification(boolean verbose) {
      super(verbose);
   }

   public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
   }

   public BooleanConditionSimplification() {
   }

   public void outASTIfNode(ASTIfNode node) {
      ASTCondition condition = node.get_Condition();
      if (condition instanceof ASTBinaryCondition) {
         ConditionExpr condExpr = ((ASTBinaryCondition)condition).getConditionExpr();
         Value unary = this.checkBooleanUse(condExpr);
         if (unary != null) {
            node.set_Condition(new ASTUnaryCondition(unary));
         }
      }

   }

   public void outASTIfElseNode(ASTIfElseNode node) {
      ASTCondition condition = node.get_Condition();
      if (condition instanceof ASTBinaryCondition) {
         ConditionExpr condExpr = ((ASTBinaryCondition)condition).getConditionExpr();
         Value unary = this.checkBooleanUse(condExpr);
         if (unary != null) {
            node.set_Condition(new ASTUnaryCondition(unary));
         }
      }

   }

   public void outASTWhileNode(ASTWhileNode node) {
      ASTCondition condition = node.get_Condition();
      if (condition instanceof ASTBinaryCondition) {
         ConditionExpr condExpr = ((ASTBinaryCondition)condition).getConditionExpr();
         Value unary = this.checkBooleanUse(condExpr);
         if (unary != null) {
            node.set_Condition(new ASTUnaryCondition(unary));
         }
      }

   }

   public void outASTDoWhileNode(ASTDoWhileNode node) {
      ASTCondition condition = node.get_Condition();
      if (condition instanceof ASTBinaryCondition) {
         ConditionExpr condExpr = ((ASTBinaryCondition)condition).getConditionExpr();
         Value unary = this.checkBooleanUse(condExpr);
         if (unary != null) {
            node.set_Condition(new ASTUnaryCondition(unary));
         }
      }

   }

   private Value checkBooleanUse(ConditionExpr condition) {
      if (condition instanceof NeExpr || condition instanceof EqExpr) {
         Value op1 = condition.getOp1();
         Value op2 = condition.getOp2();
         Type op2Type;
         if (op1 instanceof DIntConstant) {
            op2Type = ((DIntConstant)op1).type;
            if (op2Type instanceof BooleanType) {
               return this.decideCondition(op2, ((DIntConstant)op1).toString(), condition);
            }
         } else {
            if (!(op2 instanceof DIntConstant)) {
               return null;
            }

            op2Type = ((DIntConstant)op2).type;
            if (op2Type instanceof BooleanType) {
               return this.decideCondition(op1, ((DIntConstant)op2).toString(), condition);
            }
         }
      }

      return null;
   }

   private Value decideCondition(Value A, String truthString, ConditionExpr condition) {
      int truthValue = false;
      boolean notEqual = false;
      if (truthString.compareTo("false") == 0) {
         truthValue = false;
      } else {
         if (truthString.compareTo("true") != 0) {
            throw new RuntimeException();
         }

         truthValue = true;
      }

      if (condition instanceof NeExpr) {
         notEqual = true;
      } else {
         if (!(condition instanceof EqExpr)) {
            throw new RuntimeException();
         }

         notEqual = false;
      }

      if (notEqual && !truthValue) {
         return A;
      } else if (notEqual && truthValue) {
         return (Value)(A instanceof DNotExpr ? ((DNotExpr)A).getOp() : new DNotExpr(A));
      } else if (!notEqual && !truthValue) {
         return (Value)(A instanceof DNotExpr ? ((DNotExpr)A).getOp() : new DNotExpr(A));
      } else if (!notEqual && truthValue) {
         return A;
      } else {
         throw new RuntimeException();
      }
   }
}
