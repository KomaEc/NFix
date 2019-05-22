package soot.jimple;

import java.util.List;
import soot.SootMethodRef;
import soot.Value;

public interface DynamicInvokeExpr extends InvokeExpr {
   SootMethodRef getBootstrapMethodRef();

   List<Value> getBootstrapArgs();

   Value getBootstrapArg(int var1);

   int getBootstrapArgCount();

   int getHandleTag();
}
