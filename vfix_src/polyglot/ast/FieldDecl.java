package polyglot.ast;

import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.InitializerInstance;
import polyglot.types.Type;

public interface FieldDecl extends ClassMember {
   Type declType();

   Flags flags();

   FieldDecl flags(Flags var1);

   TypeNode type();

   FieldDecl type(TypeNode var1);

   String name();

   FieldDecl name(String var1);

   Expr init();

   FieldDecl init(Expr var1);

   FieldInstance fieldInstance();

   FieldDecl fieldInstance(FieldInstance var1);

   InitializerInstance initializerInstance();

   FieldDecl initializerInstance(InitializerInstance var1);
}
