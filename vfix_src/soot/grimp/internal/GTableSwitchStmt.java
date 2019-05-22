package soot.grimp.internal;

import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.Stmt;
import soot.jimple.internal.JTableSwitchStmt;

public class GTableSwitchStmt extends JTableSwitchStmt {
   private static UnitBox[] getTargetBoxesArray(List targets) {
      UnitBox[] targetBoxes = new UnitBox[targets.size()];

      for(int i = 0; i < targetBoxes.length; ++i) {
         targetBoxes[i] = Grimp.v().newStmtBox((Stmt)targets.get(i));
      }

      return targetBoxes;
   }

   public GTableSwitchStmt(Value key, int lowIndex, int highIndex, List targets, Unit defaultTarget) {
      super(Grimp.v().newExprBox(key), lowIndex, highIndex, getTargetBoxesArray(targets), Grimp.v().newStmtBox(defaultTarget));
   }

   public Object clone() {
      return new GTableSwitchStmt(Grimp.cloneIfNecessary(this.getKey()), this.getLowIndex(), this.getHighIndex(), this.getTargets(), this.getDefaultTarget());
   }
}
