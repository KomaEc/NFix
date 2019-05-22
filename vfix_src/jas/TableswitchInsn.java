package jas;

public class TableswitchInsn extends Insn implements RuntimeConstants {
   public TableswitchInsn(int min, int max, Label def, Label[] j) {
      this.opc = 170;
      this.operand = new TableswitchOperand(this, min, max, def, j);
   }
}
