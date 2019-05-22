package soot.JastAddJ;

public class CONSTANT_Float_Info extends CONSTANT_Info {
   public float value;

   public CONSTANT_Float_Info(BytecodeParser parser) {
      super(parser);
      this.value = this.p.readFloat();
   }

   public String toString() {
      return "FloatInfo: " + Float.toString(this.value);
   }

   public Expr expr() {
      return Literal.buildFloatLiteral(this.value);
   }
}
