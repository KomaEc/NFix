package soot.JastAddJ;

public class CONSTANT_Integer_Info extends CONSTANT_Info {
   public int value;

   public CONSTANT_Integer_Info(BytecodeParser parser) {
      super(parser);
      this.value = this.p.readInt();
   }

   public String toString() {
      return "IntegerInfo: " + Integer.toString(this.value);
   }

   public Expr expr() {
      return Literal.buildIntegerLiteral(this.value);
   }

   public Expr exprAsBoolean() {
      return Literal.buildBooleanLiteral(this.value == 0);
   }
}
