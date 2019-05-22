package soot.jimple;

public abstract class ArithmeticConstant extends NumericConstant {
   public abstract ArithmeticConstant and(ArithmeticConstant var1);

   public abstract ArithmeticConstant or(ArithmeticConstant var1);

   public abstract ArithmeticConstant xor(ArithmeticConstant var1);

   public abstract ArithmeticConstant shiftLeft(ArithmeticConstant var1);

   public abstract ArithmeticConstant shiftRight(ArithmeticConstant var1);

   public abstract ArithmeticConstant unsignedShiftRight(ArithmeticConstant var1);
}
