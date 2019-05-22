package polyglot.ast;

import java.util.List;
import polyglot.types.MethodInstance;

public interface Call extends Expr, ProcedureCall {
   Receiver target();

   Call target(Receiver var1);

   String name();

   Call name(String var1);

   boolean isTargetImplicit();

   Call targetImplicit(boolean var1);

   List arguments();

   ProcedureCall arguments(List var1);

   MethodInstance methodInstance();

   Call methodInstance(MethodInstance var1);
}
