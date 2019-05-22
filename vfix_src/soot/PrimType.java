package soot;

public abstract class PrimType extends Type {
   public abstract RefType boxedType();

   public boolean isAllowedInFinalCode() {
      return true;
   }
}
