package soot.jimple;

import java.util.List;
import soot.Unit;

public interface TableSwitchStmt extends SwitchStmt {
   void setLowIndex(int var1);

   void setHighIndex(int var1);

   int getLowIndex();

   int getHighIndex();

   void setTargets(List<? extends Unit> var1);
}
