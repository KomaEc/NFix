package polyglot.ext.jl.types;

import polyglot.types.NullType;
import polyglot.types.Resolver;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;

public class NullType_c extends Type_c implements NullType {
   protected NullType_c() {
   }

   public NullType_c(TypeSystem ts) {
      super(ts);
   }

   public String translate(Resolver c) {
      throw new InternalCompilerError("Cannot translate a null type.");
   }

   public String toString() {
      return "type(null)";
   }

   public boolean equalsImpl(TypeObject t) {
      return t instanceof NullType;
   }

   public int hashCode() {
      return 6060842;
   }

   public boolean isCanonical() {
      return true;
   }

   public boolean isNull() {
      return true;
   }

   public NullType toNull() {
      return this;
   }

   public boolean descendsFromImpl(Type ancestor) {
      if (ancestor.isNull()) {
         return false;
      } else {
         return ancestor.isReference();
      }
   }

   public boolean isImplicitCastValidImpl(Type toType) {
      return toType.isNull() || toType.isReference();
   }

   public boolean isCastValidImpl(Type toType) {
      return toType.isNull() || toType.isReference();
   }
}
