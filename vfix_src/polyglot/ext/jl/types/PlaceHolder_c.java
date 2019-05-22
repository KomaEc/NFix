package polyglot.ext.jl.types;

import polyglot.types.PlaceHolder;
import polyglot.types.Resolver;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;

public class PlaceHolder_c implements PlaceHolder {
   String name;

   protected PlaceHolder_c() {
   }

   public PlaceHolder_c(Type t) {
      if (t.isClass()) {
         this.name = t.typeSystem().getTransformedClassName(t.toClass());
      } else {
         throw new InternalCompilerError("Cannot serialize " + t + ".");
      }
   }

   public TypeObject resolve(TypeSystem ts) {
      try {
         return ts.systemResolver().find(this.name);
      } catch (SemanticException var3) {
         throw new InternalCompilerError(var3);
      }
   }

   public String translate(Resolver c) {
      throw new InternalCompilerError("Cannot translate place holder type.");
   }

   public String toString() {
      return "PlaceHolder(" + this.name + ")";
   }
}
