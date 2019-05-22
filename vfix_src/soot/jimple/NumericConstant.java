package soot.jimple;

public abstract class NumericConstant extends Constant {
   public abstract NumericConstant add(NumericConstant var1);

   public abstract NumericConstant subtract(NumericConstant var1);

   public abstract NumericConstant multiply(NumericConstant var1);

   public abstract NumericConstant divide(NumericConstant var1);

   public abstract NumericConstant remainder(NumericConstant var1);

   public abstract NumericConstant equalEqual(NumericConstant var1);

   public abstract NumericConstant notEqual(NumericConstant var1);

   public abstract NumericConstant lessThan(NumericConstant var1);

   public abstract NumericConstant lessThanOrEqual(NumericConstant var1);

   public abstract NumericConstant greaterThan(NumericConstant var1);

   public abstract NumericConstant greaterThanOrEqual(NumericConstant var1);

   public abstract NumericConstant negate();
}
