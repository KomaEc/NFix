package polyglot.ext.jl.types;

import polyglot.types.Resolver;
import polyglot.types.TypeSystem;
import polyglot.types.UnknownPackage;
import polyglot.util.InternalCompilerError;

public class UnknownPackage_c extends Package_c implements UnknownPackage {
   protected UnknownPackage_c() {
   }

   public UnknownPackage_c(TypeSystem ts) {
      super(ts);
   }

   public boolean isCanonical() {
      return false;
   }

   public String translate(Resolver c) {
      throw new InternalCompilerError("Cannot translate an unknown package.");
   }

   public String toString() {
      return "<unknown>";
   }
}
