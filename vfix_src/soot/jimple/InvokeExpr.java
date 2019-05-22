package soot.jimple;

import java.util.List;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Value;
import soot.ValueBox;

public interface InvokeExpr extends Expr {
   void setMethodRef(SootMethodRef var1);

   SootMethodRef getMethodRef();

   SootMethod getMethod();

   List<Value> getArgs();

   Value getArg(int var1);

   int getArgCount();

   void setArg(int var1, Value var2);

   ValueBox getArgBox(int var1);
}
