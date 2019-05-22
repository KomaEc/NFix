package soot.dava.internal.AST;

import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.ASTAnalysis;
import soot.dava.toolkits.base.AST.ASTWalker;
import soot.dava.toolkits.base.AST.TryContentsFinder;
import soot.jimple.ConditionExpr;

public abstract class ASTControlFlowNode extends ASTLabeledNode {
   ASTCondition condition;

   public ASTControlFlowNode(SETNodeLabel label, ConditionExpr condition) {
      super(label);
      this.condition = new ASTBinaryCondition(condition);
   }

   public ASTControlFlowNode(SETNodeLabel label, ASTCondition condition) {
      super(label);
      this.condition = condition;
   }

   public ASTCondition get_Condition() {
      return this.condition;
   }

   public void set_Condition(ASTCondition condition) {
      this.condition = condition;
   }

   public void perform_Analysis(ASTAnalysis a) {
      if (this.condition instanceof ASTBinaryCondition) {
         ConditionExpr condExpr = ((ASTBinaryCondition)this.condition).getConditionExpr();
         ASTWalker.v().walk_value(a, condExpr);
      }

      if (a instanceof TryContentsFinder) {
         TryContentsFinder.v().add_ExceptionSet(this, TryContentsFinder.v().remove_CurExceptionSet());
      }

      this.perform_AnalysisOnSubBodies(a);
   }
}
