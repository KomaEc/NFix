package polyglot.ast;

import polyglot.types.Flags;
import polyglot.types.InitializerInstance;

public interface Initializer extends CodeDecl {
   Flags flags();

   Initializer flags(Flags var1);

   InitializerInstance initializerInstance();

   Initializer initializerInstance(InitializerInstance var1);
}
