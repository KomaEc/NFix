package soot.jimple.toolkits.typing.fast;

import java.util.Collection;
import soot.Type;
import soot.Value;
import soot.jimple.Stmt;

public interface IEvalFunction {
   Collection<Type> eval(Typing var1, Value var2, Stmt var3);
}
