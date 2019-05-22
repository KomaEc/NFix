package polyglot.ast;

import java.util.List;
import polyglot.types.ProcedureInstance;

public interface ProcedureCall extends Term {
   List arguments();

   ProcedureCall arguments(List var1);

   ProcedureInstance procedureInstance();
}
