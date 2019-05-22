package jas;

public class LookupswitchInsn extends Insn implements RuntimeConstants {
   public LookupswitchInsn(Label def, int[] match, Label[] target) {
      this.opc = 171;
      this.operand = new LookupswitchOperand(this, def, match, target);
   }
}
