package polyglot.ext.jl.types;

import polyglot.types.LazyClassInitializer;
import polyglot.types.ParsedClassType;
import polyglot.types.TypeSystem;

public class LazyClassInitializer_c implements LazyClassInitializer {
   TypeSystem ts;

   public LazyClassInitializer_c(TypeSystem ts) {
      this.ts = ts;
   }

   public boolean fromClassFile() {
      return false;
   }

   public void initConstructors(ParsedClassType ct) {
   }

   public void initMethods(ParsedClassType ct) {
   }

   public void initFields(ParsedClassType ct) {
   }

   public void initMemberClasses(ParsedClassType ct) {
   }

   public void initInterfaces(ParsedClassType ct) {
   }
}
