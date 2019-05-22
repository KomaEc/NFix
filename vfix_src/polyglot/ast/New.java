package polyglot.ast;

import java.util.List;
import polyglot.types.ConstructorInstance;
import polyglot.types.ParsedClassType;

public interface New extends Expr, ProcedureCall {
   ParsedClassType anonType();

   New anonType(ParsedClassType var1);

   ConstructorInstance constructorInstance();

   New constructorInstance(ConstructorInstance var1);

   Expr qualifier();

   New qualifier(Expr var1);

   TypeNode objectType();

   New objectType(TypeNode var1);

   List arguments();

   ProcedureCall arguments(List var1);

   ClassBody body();

   New body(ClassBody var1);
}
