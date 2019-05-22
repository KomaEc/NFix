package soot.jimple;

import java.util.List;
import soot.ArrayType;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;

public interface NewMultiArrayExpr extends Expr, AnyNewExpr {
   ArrayType getBaseType();

   void setBaseType(ArrayType var1);

   ValueBox getSizeBox(int var1);

   int getSizeCount();

   Value getSize(int var1);

   List<Value> getSizes();

   void setSize(int var1, Value var2);

   Type getType();

   void apply(Switch var1);
}
