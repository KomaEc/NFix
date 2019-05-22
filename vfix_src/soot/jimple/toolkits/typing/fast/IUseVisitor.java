package soot.jimple.toolkits.typing.fast;

import soot.Type;
import soot.Value;
import soot.jimple.Stmt;

public interface IUseVisitor {
   Value visit(Value var1, Type var2, Stmt var3);

   boolean finish();
}
