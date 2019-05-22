package polyglot.ast;

import java.util.List;
import polyglot.types.Flags;
import polyglot.types.ProcedureInstance;

public interface ProcedureDecl extends CodeDecl {
   Flags flags();

   String name();

   List formals();

   List throwTypes();

   ProcedureInstance procedureInstance();
}
