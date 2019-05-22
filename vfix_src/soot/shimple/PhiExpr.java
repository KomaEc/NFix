package soot.shimple;

import java.util.List;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.toolkits.graph.Block;
import soot.toolkits.scalar.ValueUnitPair;
import soot.util.Switch;

public interface PhiExpr extends ShimpleExpr {
   List<ValueUnitPair> getArgs();

   List<Value> getValues();

   List<Unit> getPreds();

   int getArgCount();

   ValueUnitPair getArgBox(int var1);

   Value getValue(int var1);

   Unit getPred(int var1);

   int getArgIndex(Unit var1);

   ValueUnitPair getArgBox(Unit var1);

   Value getValue(Unit var1);

   int getArgIndex(Block var1);

   ValueUnitPair getArgBox(Block var1);

   Value getValue(Block var1);

   boolean setArg(int var1, Value var2, Unit var3);

   boolean setArg(int var1, Value var2, Block var3);

   boolean setValue(int var1, Value var2);

   boolean setValue(Unit var1, Value var2);

   boolean setValue(Block var1, Value var2);

   boolean setPred(int var1, Unit var2);

   boolean setPred(int var1, Block var2);

   boolean removeArg(int var1);

   boolean removeArg(Unit var1);

   boolean removeArg(Block var1);

   boolean removeArg(ValueUnitPair var1);

   boolean addArg(Value var1, Block var2);

   boolean addArg(Value var1, Unit var2);

   void setBlockId(int var1);

   int getBlockId();

   Type getType();

   void apply(Switch var1);
}
