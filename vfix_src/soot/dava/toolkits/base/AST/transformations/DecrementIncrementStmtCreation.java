package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import soot.Value;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DDecrementStmt;
import soot.dava.internal.javaRep.DIncrementStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.AddExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;

public class DecrementIncrementStmtCreation extends DepthFirstAdapter {
   public DecrementIncrementStmtCreation() {
   }

   public DecrementIncrementStmtCreation(boolean verbose) {
      super(verbose);
   }

   public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
      Iterator var2 = node.getStatements().iterator();

      while(var2.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var2.next();
         Stmt s = as.get_Stmt();
         if (s instanceof DefinitionStmt) {
            Value left = ((DefinitionStmt)s).getLeftOp();
            Value right = ((DefinitionStmt)s).getRightOp();
            Value op1;
            Value op2;
            DDecrementStmt newStmt;
            DIncrementStmt newStmt;
            if (right instanceof SubExpr) {
               op1 = ((SubExpr)right).getOp1();
               op2 = ((SubExpr)right).getOp2();
               if (left.toString().compareTo(op1.toString()) == 0 && op2 instanceof IntConstant) {
                  if (((IntConstant)op2).value == 1) {
                     newStmt = new DDecrementStmt(left, right);
                     as.set_Stmt(newStmt);
                  } else if (((IntConstant)op2).value == -1) {
                     newStmt = new DIncrementStmt(left, right);
                     as.set_Stmt(newStmt);
                  }
               }
            } else if (right instanceof AddExpr) {
               op1 = ((AddExpr)right).getOp1();
               op2 = ((AddExpr)right).getOp2();
               if (left.toString().compareTo(op1.toString()) == 0 && op2 instanceof IntConstant) {
                  if (((IntConstant)op2).value == 1) {
                     newStmt = new DIncrementStmt(left, right);
                     as.set_Stmt(newStmt);
                  } else if (((IntConstant)op2).value == -1) {
                     newStmt = new DDecrementStmt(left, right);
                     as.set_Stmt(newStmt);
                  }
               }
            }
         }
      }

   }
}
