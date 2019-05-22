package polyglot.ast;

import java.util.List;
import polyglot.types.ConstructorInstance;
import polyglot.util.Enum;

public interface ConstructorCall extends Stmt, ProcedureCall {
   ConstructorCall.Kind SUPER = new ConstructorCall.Kind("super");
   ConstructorCall.Kind THIS = new ConstructorCall.Kind("this");

   Expr qualifier();

   ConstructorCall qualifier(Expr var1);

   ConstructorCall.Kind kind();

   ConstructorCall kind(ConstructorCall.Kind var1);

   List arguments();

   ProcedureCall arguments(List var1);

   ConstructorInstance constructorInstance();

   ConstructorCall constructorInstance(ConstructorInstance var1);

   public static class Kind extends Enum {
      public Kind(String name) {
         super(name);
      }
   }
}
