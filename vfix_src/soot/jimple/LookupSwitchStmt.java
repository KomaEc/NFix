package soot.jimple;

import java.util.List;
import soot.Unit;

public interface LookupSwitchStmt extends SwitchStmt {
   void setLookupValues(List<IntConstant> var1);

   void setLookupValue(int var1, int var2);

   int getLookupValue(int var1);

   List<IntConstant> getLookupValues();

   int getTargetCount();

   void setTargets(Unit[] var1);
}
