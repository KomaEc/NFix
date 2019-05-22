package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Value;
import soot.dava.internal.AST.ASTAggregatedCondition;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;

public class ForLoopCreationHelper {
   ASTStatementSequenceNode stmtSeqNode;
   ASTWhileNode whileNode;
   ASTStatementSequenceNode newStmtSeqNode;
   ASTForLoopNode forNode;
   Map<String, Integer> varToStmtMap;
   List<AugmentedStmt> myStmts;
   boolean removeLast = false;

   public ForLoopCreationHelper(ASTStatementSequenceNode stmtSeqNode, ASTWhileNode whileNode) {
      this.stmtSeqNode = stmtSeqNode;
      this.whileNode = whileNode;
      this.varToStmtMap = new HashMap();
   }

   public List<Object> createNewBody(List<Object> oldSubBody, int nodeNumber) {
      List<Object> newSubBody = new ArrayList();
      if (oldSubBody.size() <= nodeNumber) {
         return null;
      } else {
         Iterator<Object> oldIt = oldSubBody.iterator();

         for(int index = 0; index != nodeNumber; ++index) {
            newSubBody.add(oldIt.next());
         }

         ASTNode temp = (ASTNode)oldIt.next();
         if (!(temp instanceof ASTStatementSequenceNode)) {
            return null;
         } else {
            temp = (ASTNode)oldIt.next();
            if (!(temp instanceof ASTWhileNode)) {
               return null;
            } else {
               if (this.newStmtSeqNode != null) {
                  newSubBody.add(this.newStmtSeqNode);
               }

               newSubBody.add(this.forNode);

               while(oldIt.hasNext()) {
                  newSubBody.add(oldIt.next());
               }

               return newSubBody;
            }
         }
      }
   }

   private List<String> getDefs() {
      if (this.stmtSeqNode == null) {
         return null;
      } else {
         List<String> toReturn = new ArrayList();
         int stmtNum = 0;

         for(Iterator var3 = this.stmtSeqNode.getStatements().iterator(); var3.hasNext(); ++stmtNum) {
            AugmentedStmt as = (AugmentedStmt)var3.next();
            Stmt s = as.get_Stmt();
            if (s instanceof DefinitionStmt) {
               Value left = ((DefinitionStmt)s).getLeftOp();
               toReturn.add(left.toString());
               this.varToStmtMap.put(left.toString(), new Integer(stmtNum));
            } else {
               toReturn = new ArrayList();
               this.varToStmtMap = new HashMap();
            }
         }

         return toReturn;
      }
   }

   private List<String> getCondUses() {
      if (this.whileNode == null) {
         return null;
      } else {
         ASTCondition cond = this.whileNode.get_Condition();
         return this.getCond(cond);
      }
   }

   private List<String> getCond(ASTCondition cond) {
      List<String> toReturn = new ArrayList();
      if (cond instanceof ASTUnaryCondition) {
         toReturn.add(((ASTUnaryCondition)cond).toString());
      } else if (cond instanceof ASTBinaryCondition) {
         ConditionExpr condExpr = ((ASTBinaryCondition)cond).getConditionExpr();
         toReturn.add(condExpr.getOp1().toString());
         toReturn.add(condExpr.getOp2().toString());
      } else if (cond instanceof ASTAggregatedCondition) {
         toReturn.addAll(this.getCond(((ASTAggregatedCondition)cond).getLeftOp()));
         toReturn.addAll(this.getCond(((ASTAggregatedCondition)cond).getRightOp()));
      }

      return toReturn;
   }

   private List<String> getCommonVars(List<String> defs, List<String> condUses) {
      List<String> toReturn = new ArrayList();
      Iterator defIt = defs.iterator();

      while(true) {
         while(defIt.hasNext()) {
            String defString = (String)defIt.next();
            Iterator condIt = condUses.iterator();

            while(condIt.hasNext()) {
               String condString = (String)condIt.next();
               if (condString.compareTo(defString) == 0) {
                  toReturn.add(defString);
                  break;
               }
            }
         }

         return toReturn;
      }
   }

   public boolean checkPattern() {
      List<String> defs = this.getDefs();
      if (defs == null) {
         return false;
      } else if (defs.size() == 0) {
         return false;
      } else {
         List<String> condUses = this.getCondUses();
         if (condUses == null) {
            return false;
         } else if (condUses.size() == 0) {
            return false;
         } else {
            List<String> commonVars = this.getCommonVars(defs, condUses);
            List<AugmentedStmt> update = this.getUpdate(defs, condUses, commonVars);
            if (update != null && update.size() != 0) {
               if (commonVars != null && commonVars.size() != 0) {
                  List<AugmentedStmt> init = this.createNewStmtSeqNodeAndGetInit(commonVars);
                  if (init.size() == 0) {
                     return false;
                  } else {
                     ASTCondition condition = this.whileNode.get_Condition();
                     List<Object> body = (List)this.whileNode.get_SubBodies().get(0);
                     SETNodeLabel label = this.whileNode.get_Label();
                     if (this.removeLast) {
                        this.myStmts.remove(this.myStmts.size() - 1);
                        this.removeLast = false;
                     }

                     this.forNode = new ASTForLoopNode(label, init, condition, update, body);
                     return true;
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         }
      }
   }

   private List<AugmentedStmt> getUpdate(List<String> defs, List<String> condUses, List<String> commonUses) {
      List<AugmentedStmt> toReturn = new ArrayList();
      List<Object> subBodies = this.whileNode.get_SubBodies();
      if (subBodies.size() != 1) {
         return toReturn;
      } else {
         List subBody = (List)subBodies.get(0);
         Iterator it = subBody.iterator();

         while(true) {
            ASTNode temp;
            do {
               if (!it.hasNext()) {
                  return toReturn;
               }

               temp = (ASTNode)it.next();
            } while(it.hasNext());

            if (!(temp instanceof ASTStatementSequenceNode)) {
               return null;
            }

            List<AugmentedStmt> stmts = ((ASTStatementSequenceNode)temp).getStatements();
            AugmentedStmt last = (AugmentedStmt)stmts.get(stmts.size() - 1);
            Stmt lastStmt = last.get_Stmt();
            if (!(lastStmt instanceof DefinitionStmt)) {
               return null;
            }

            Value left = ((DefinitionStmt)lastStmt).getLeftOp();
            Iterator defIt = defs.iterator();

            while(defIt.hasNext()) {
               String defString = (String)defIt.next();
               if (left.toString().compareTo(defString) == 0) {
                  toReturn.add(last);
                  this.myStmts = stmts;
                  this.removeLast = true;
                  Iterator<String> coIt = commonUses.iterator();
                  boolean matched = false;

                  while(coIt.hasNext()) {
                     if (defString.compareTo((String)coIt.next()) == 0) {
                        matched = true;
                     }
                  }

                  if (!matched) {
                     commonUses.add(defString);
                  }

                  return toReturn;
               }
            }

            Iterator condIt = condUses.iterator();

            while(condIt.hasNext()) {
               String condString = (String)condIt.next();
               if (left.toString().compareTo(condString) == 0) {
                  toReturn.add(last);
                  this.myStmts = stmts;
                  this.removeLast = true;
                  Iterator<String> coIt = commonUses.iterator();
                  boolean matched = false;

                  while(coIt.hasNext()) {
                     if (condString.compareTo((String)coIt.next()) == 0) {
                        matched = true;
                     }
                  }

                  if (!matched) {
                     commonUses.add(condString);
                  }

                  return toReturn;
               }
            }
         }
      }
   }

   private List<AugmentedStmt> createNewStmtSeqNodeAndGetInit(List<String> commonVars) {
      int currentLowestPosition = 999;
      Iterator var3 = commonVars.iterator();

      while(var3.hasNext()) {
         String temp = (String)var3.next();
         Integer tempInt = (Integer)this.varToStmtMap.get(temp);
         if (tempInt != null && tempInt < currentLowestPosition) {
            currentLowestPosition = tempInt;
         }
      }

      List<AugmentedStmt> stmts = new ArrayList();
      List<AugmentedStmt> statements = this.stmtSeqNode.getStatements();
      Iterator<AugmentedStmt> stmtIt = statements.iterator();

      for(int stmtNum = 0; stmtNum < currentLowestPosition && stmtIt.hasNext(); ++stmtNum) {
         stmts.add(stmtIt.next());
      }

      if (stmts.size() > 0) {
         this.newStmtSeqNode = new ASTStatementSequenceNode(stmts);
      } else {
         this.newStmtSeqNode = null;
      }

      ArrayList init = new ArrayList();

      while(stmtIt.hasNext()) {
         init.add(stmtIt.next());
      }

      return init;
   }
}
