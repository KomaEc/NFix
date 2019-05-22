package polyglot.ast;

import polyglot.types.CodeInstance;

public interface CodeDecl extends ClassMember {
   Block body();

   CodeDecl body(Block var1);

   CodeInstance codeInstance();
}
