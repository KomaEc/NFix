package polyglot.ast;

import java.util.List;
import polyglot.types.ConstructorInstance;
import polyglot.types.Flags;

public interface ConstructorDecl extends ProcedureDecl {
   Flags flags();

   ConstructorDecl flags(Flags var1);

   String name();

   ConstructorDecl name(String var1);

   List formals();

   ConstructorDecl formals(List var1);

   List throwTypes();

   ConstructorDecl throwTypes(List var1);

   ConstructorInstance constructorInstance();

   ConstructorDecl constructorInstance(ConstructorInstance var1);
}
