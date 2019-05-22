package soot.JastAddJ;

public class CONSTANT_Info {
   protected BytecodeParser p;

   public CONSTANT_Info(BytecodeParser parser) {
      this.p = parser;
   }

   public Expr expr() {
      throw new Error("CONSTANT_info.expr() should not be computed for " + this.getClass().getName());
   }

   public Expr exprAsBoolean() {
      return this.expr();
   }
}
