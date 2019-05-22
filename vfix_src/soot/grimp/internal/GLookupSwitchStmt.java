package soot.grimp.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.IntConstant;
import soot.jimple.Stmt;
import soot.jimple.internal.JLookupSwitchStmt;

public class GLookupSwitchStmt extends JLookupSwitchStmt {
   private static UnitBox[] getTargetBoxesArray(List targets) {
      UnitBox[] targetBoxes = new UnitBox[targets.size()];

      for(int i = 0; i < targetBoxes.length; ++i) {
         targetBoxes[i] = Grimp.v().newStmtBox((Stmt)targets.get(i));
      }

      return targetBoxes;
   }

   public GLookupSwitchStmt(Value key, List lookupValues, List targets, Unit defaultTarget) {
      super(Grimp.v().newExprBox(key), lookupValues, getTargetBoxesArray(targets), Grimp.v().newStmtBox(defaultTarget));
   }

   public Object clone() {
      int lookupValueCount = this.getLookupValues().size();
      List clonedLookupValues = new ArrayList(lookupValueCount);

      for(int i = 0; i < lookupValueCount; ++i) {
         clonedLookupValues.add(i, IntConstant.v(this.getLookupValue(i)));
      }

      return new GLookupSwitchStmt(Grimp.cloneIfNecessary(this.getKey()), clonedLookupValues, this.getTargets(), this.getDefaultTarget());
   }
}
