package soot.JastAddJ;

public class CONSTANT_Utf8_Info extends CONSTANT_Info {
   public String string;

   public CONSTANT_Utf8_Info(BytecodeParser parser) {
      super(parser);
      this.string = this.p.readUTF();
   }

   public String toString() {
      return "Utf8Info: " + this.string;
   }

   public Expr expr() {
      return Literal.buildStringLiteral(this.string);
   }

   public String string() {
      return this.string;
   }
}
