package polyglot.types;

import java.util.List;

public interface ProcedureInstance extends CodeInstance {
   List formalTypes();

   List throwTypes();

   String signature();

   String designator();

   boolean moreSpecific(ProcedureInstance var1);

   boolean hasFormals(List var1);

   boolean throwsSubset(ProcedureInstance var1);

   boolean callValid(List var1);

   boolean moreSpecificImpl(ProcedureInstance var1);

   boolean hasFormalsImpl(List var1);

   boolean throwsSubsetImpl(ProcedureInstance var1);

   boolean callValidImpl(List var1);
}
