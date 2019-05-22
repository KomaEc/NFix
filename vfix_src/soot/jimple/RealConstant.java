package soot.jimple;

public abstract class RealConstant extends NumericConstant {
   public abstract IntConstant cmpl(RealConstant var1);

   public abstract IntConstant cmpg(RealConstant var1);
}
