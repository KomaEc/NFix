package soot.dava.toolkits.base.AST.traversals;

import java.util.ArrayList;
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
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.FieldRef;
import soot.jimple.Stmt;

public class AllVariableUses extends DepthFirstAdapter {
   ASTMethodNode methodNode;
   HashMap<Local, List> localsToUses;
   HashMap<SootField, List> fieldsToUses;

   public AllVariableUses(ASTMethodNode node) {
      this.methodNode = node;
      this.init();
   }

   public AllVariableUses(boolean verbose, ASTMethodNode node) {
      super(verbose);
      this.methodNode = node;
      this.init();
   }

   public void init() {
      this.localsToUses = new HashMap();
      this.fieldsToUses = new HashMap();
   }

   public void inASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
      Local local = node.getLocal();
      this.addLocalUse(local, node);
   }

   public void inASTSwitchNode(ASTSwitchNode node) {
      Value val = node.get_Key();
      List<Value> localUses = new ArrayList();
      List<Value> fieldUses = new ArrayList();
      if (val instanceof Local) {
         localUses.add(val);
         System.out.println("Added " + val + " to local uses for switch");
      } else if (val instanceof FieldRef) {
         fieldUses.add(val);
         System.out.println("Added " + val + " to field uses for switch");
      } else {
         List useBoxes = val.getUseBoxes();
         List<Value> localsOrFieldRefs = this.getUsesFromBoxes(useBoxes);
         Iterator it = localsOrFieldRefs.iterator();

         while(it.hasNext()) {
            Value temp = (Value)it.next();
            if (temp instanceof Local) {
               localUses.add(temp);
               System.out.println("Added " + temp + " to local uses for switch");
            } else if (temp instanceof FieldRef) {
               fieldUses.add(temp);
               System.out.println("Added " + temp + " to field uses for switch");
            }
         }
      }

      Iterator it = localUses.iterator();

      while(it.hasNext()) {
         Local local = (Local)it.next();
         this.addLocalUse(local, node);
      }

      it = fieldUses.iterator();

      while(it.hasNext()) {
         FieldRef field = (FieldRef)it.next();
         SootField sootField = field.getField();
         this.addFieldUse(sootField, node);
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

   public void checkStatementUses(Stmt s, Object useNodeOrStatement) {
      List useBoxes = s.getUseBoxes();
      List<Value> uses = this.getUsesFromBoxes(useBoxes);
      Iterator it = uses.iterator();

      while(it.hasNext()) {
         Value temp = (Value)it.next();
         if (temp instanceof Local) {
            this.addLocalUse((Local)temp, useNodeOrStatement);
         } else if (temp instanceof FieldRef) {
            FieldRef field = (FieldRef)temp;
            SootField sootField = field.getField();
            this.addFieldUse(sootField, useNodeOrStatement);
         }
      }

   }

   public void checkConditionalUses(ASTCondition cond, ASTNode node) {
      List<Value> useList = this.getUseList(cond);
      Iterator it = useList.iterator();

      while(it.hasNext()) {
         Value temp = (Value)it.next();
         if (temp instanceof Local) {
            this.addLocalUse((Local)temp, node);
         } else if (temp instanceof FieldRef) {
            FieldRef field = (FieldRef)temp;
            SootField sootField = field.getField();
            this.addFieldUse(sootField, node);
         }
      }

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

   public List<Value> getUseList(ASTCondition cond) {
      ArrayList<Value> useList = new ArrayList();
      if (cond instanceof ASTAggregatedCondition) {
         useList.addAll(this.getUseList(((ASTAggregatedCondition)cond).getLeftOp()));
         useList.addAll(this.getUseList(((ASTAggregatedCondition)cond).getRightOp()));
         return useList;
      } else if (!(cond instanceof ASTUnaryCondition)) {
         if (cond instanceof ASTBinaryCondition) {
            Value val = ((ASTBinaryCondition)cond).getConditionExpr();
            List useBoxes = val.getUseBoxes();
            return this.getUsesFromBoxes(useBoxes);
         } else {
            throw new RuntimeException("Method getUseList in ASTUsesAndDefs encountered unknown condition type");
         }
      } else {
         List<Value> uses = new ArrayList();
         Value val = ((ASTUnaryCondition)cond).getValue();
         if (!(val instanceof Local) && !(val instanceof FieldRef)) {
            List useBoxes = val.getUseBoxes();
            uses = this.getUsesFromBoxes(useBoxes);
         } else {
            ((List)uses).add(val);
         }

         return (List)uses;
      }
   }

   private void addLocalUse(Local local, Object obj) {
      Object temp = this.localsToUses.get(local);
      ArrayList uses;
      if (temp == null) {
         uses = new ArrayList();
      } else {
         uses = (ArrayList)temp;
      }

      uses.add(obj);
      this.localsToUses.put(local, uses);
   }

   private void addFieldUse(SootField field, Object obj) {
      Object temp = this.fieldsToUses.get(field);
      ArrayList uses;
      if (temp == null) {
         uses = new ArrayList();
      } else {
         uses = (ArrayList)temp;
      }

      uses.add(obj);
      this.fieldsToUses.put(field, uses);
   }

   private List<Value> getUsesFromBoxes(List useBoxes) {
      ArrayList<Value> toReturn = new ArrayList();
      Iterator it = useBoxes.iterator();

      while(true) {
         Value val;
         do {
            if (!it.hasNext()) {
               return toReturn;
            }

            val = ((ValueBox)it.next()).getValue();
         } while(!(val instanceof Local) && !(val instanceof FieldRef));

         toReturn.add(val);
      }
   }

   public List getUsesForField(SootField field) {
      Object temp = this.fieldsToUses.get(field);
      return temp == null ? null : (List)temp;
   }

   public List getUsesForLocal(Local local) {
      Object temp = this.localsToUses.get(local);
      return temp == null ? null : (List)temp;
   }
}
