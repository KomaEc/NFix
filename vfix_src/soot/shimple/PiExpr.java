package soot.shimple;

import soot.Unit;
import soot.Value;
import soot.toolkits.scalar.ValueUnitPair;

public interface PiExpr extends ShimpleExpr {
   ValueUnitPair getArgBox();

   Value getValue();

   Unit getCondStmt();

   Object getTargetKey();

   void setValue(Value var1);

   void setCondStmt(Unit var1);

   void setTargetKey(Object var1);
}
