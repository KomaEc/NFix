package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.List;
import soot.G;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTOrCondition;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.Stmt;

public class OrAggregatorTwo extends DepthFirstAdapter {
   public OrAggregatorTwo() {
      this.DEBUG = false;
   }

   public OrAggregatorTwo(boolean verbose) {
      super(verbose);
      this.DEBUG = false;
   }

   public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
   }

   public void outASTIfElseNode(ASTIfElseNode node) {
      List<Object> ifBody = node.getIfBody();
      List<Object> elseBody = node.getElseBody();
      List<Object> innerIfBody = this.checkElseHasOnlyIf(elseBody);
      if (innerIfBody == null) {
         this.matchPatternTwo(node);
      } else if (ifBody.toString().compareTo(innerIfBody.toString()) != 0) {
         this.matchPatternTwo(node);
      } else {
         ASTCondition leftCond = node.get_Condition();
         ASTCondition rightCond = this.getRightCond(elseBody);
         ASTCondition newCond = new ASTOrCondition(leftCond, rightCond);
         node.set_Condition(newCond);
         node.replaceElseBody(new ArrayList());
         G.v().ASTTransformations_modified = true;
      }
   }

   public ASTCondition getRightCond(List<Object> elseBody) {
      ASTIfNode innerIfNode = (ASTIfNode)elseBody.get(0);
      return innerIfNode.get_Condition();
   }

   public List<Object> checkElseHasOnlyIf(List<Object> elseBody) {
      if (elseBody.size() != 1) {
         return null;
      } else {
         ASTNode temp = (ASTNode)elseBody.get(0);
         if (!(temp instanceof ASTIfNode)) {
            return null;
         } else {
            ASTIfNode innerIfNode = (ASTIfNode)temp;
            List<Object> innerIfBody = innerIfNode.getIfBody();
            return innerIfBody;
         }
      }
   }

   public void matchPatternTwo(ASTIfElseNode node) {
      this.debug("OrAggregatorTwo", "matchPatternTwo", "Did not match patternOne...trying patternTwo");
      List<Object> ifBody = node.getIfBody();
      if (ifBody.size() == 1) {
         ASTNode onlyNode = (ASTNode)ifBody.get(0);
         if (onlyNode instanceof ASTStatementSequenceNode) {
            ASTStatementSequenceNode stmtNode = (ASTStatementSequenceNode)onlyNode;
            List<AugmentedStmt> statements = stmtNode.getStatements();
            if (statements.size() == 1) {
               AugmentedStmt as = (AugmentedStmt)statements.get(0);
               Stmt stmt = as.get_Stmt();
               if (stmt instanceof DAbruptStmt) {
                  DAbruptStmt abStmt = (DAbruptStmt)stmt;
                  if (abStmt.is_Break() || abStmt.is_Continue()) {
                     ASTCondition cond = node.get_Condition();
                     cond.flip();
                     List<Object> elseBody = node.getElseBody();
                     SETNodeLabel label = node.get_Label();
                     node.replace(label, cond, elseBody, ifBody);
                     this.debug("", "", "REVERSED CONDITIONS AND BODIES");
                     this.debug("", "", "elseBody is" + elseBody);
                     this.debug("", "", "ifBody is" + ifBody);
                     G.v().ASTIfElseFlipped = true;
                  }
               }
            }
         }
      }
   }
}
