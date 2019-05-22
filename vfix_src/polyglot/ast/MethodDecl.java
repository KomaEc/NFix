package polyglot.ast;

import java.util.List;
import polyglot.types.Flags;
import polyglot.types.MethodInstance;

public interface MethodDecl extends ProcedureDecl {
   Flags flags();

   MethodDecl flags(Flags var1);

   TypeNode returnType();

   MethodDecl returnType(TypeNode var1);

   String name();

   MethodDecl name(String var1);

   List formals();

   MethodDecl formals(List var1);

   List throwTypes();

   MethodDecl throwTypes(List var1);

   MethodInstance methodInstance();

   MethodDecl methodInstance(MethodInstance var1);
}
