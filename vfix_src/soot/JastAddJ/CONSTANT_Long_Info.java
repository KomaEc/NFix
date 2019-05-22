package soot.JastAddJ;

public class CONSTANT_Long_Info extends CONSTANT_Info {
   public long value;

   public CONSTANT_Long_Info(BytecodeParser parser) {
      super(parser);
      this.value = this.p.readLong();
   }

   public String toString() {
      return "LongInfo: " + Long.toString(this.value);
   }

   public Expr expr() {
      return Literal.buildLongLiteral(this.value);
   }
}
