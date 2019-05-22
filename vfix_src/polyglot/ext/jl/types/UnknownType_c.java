package polyglot.ext.jl.types;

import polyglot.types.Resolver;
import polyglot.types.TypeSystem;
import polyglot.types.UnknownType;
import polyglot.util.InternalCompilerError;

public class UnknownType_c extends Type_c implements UnknownType {
   protected UnknownType_c() {
   }

   public UnknownType_c(TypeSystem ts) {
      super(ts);
   }

   public boolean isCanonical() {
      return false;
   }

   public String translate(Resolver c) {
      throw new InternalCompilerError("Cannot translate an unknown type.");
   }

   public String toString() {
      return "<unknown>";
   }
}
