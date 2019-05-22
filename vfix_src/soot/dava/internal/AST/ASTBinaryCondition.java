package soot.dava.internal.AST;

import soot.UnitPrinter;
import soot.dava.toolkits.base.AST.analysis.Analysis;
import soot.dava.toolkits.base.misc.ConditionFlipper;
import soot.jimple.ConditionExpr;
import soot.jimple.Jimple;

public class ASTBinaryCondition extends ASTUnaryBinaryCondition {
   ConditionExpr condition;

   public ASTBinaryCondition(ConditionExpr condition) {
      this.condition = condition;
   }

   public ConditionExpr getConditionExpr() {
      return this.condition;
   }

   public void apply(Analysis a) {
      a.caseASTBinaryCondition(this);
   }

   public String toString() {
      return this.condition.toString();
   }

   public void toString(UnitPrinter up) {
      Jimple.v().newConditionExprBox(this.condition).toString(up);
   }

   public void flip() {
      this.condition = ConditionFlipper.flip(this.condition);
   }

   public boolean isNotted() {
      return true;
   }
}
