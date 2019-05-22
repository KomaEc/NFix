package polyglot.ext.jl.types;

import polyglot.types.Package;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.types.UnknownQualifier;

public class UnknownQualifier_c extends TypeObject_c implements UnknownQualifier {
   public UnknownQualifier_c(TypeSystem ts) {
      super(ts);
   }

   public boolean isCanonical() {
      return false;
   }

   public boolean isPackage() {
      return false;
   }

   public boolean isType() {
      return false;
   }

   public Package toPackage() {
      return null;
   }

   public Type toType() {
      return null;
   }

   public String toString() {
      return "<unknown>";
   }
}
