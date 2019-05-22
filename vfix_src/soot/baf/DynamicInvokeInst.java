package soot.baf;

import java.util.List;
import soot.SootMethodRef;
import soot.Value;

public interface DynamicInvokeInst extends MethodArgInst {
   SootMethodRef getBootstrapMethodRef();

   List<Value> getBootstrapArgs();

   int getHandleTag();
}
