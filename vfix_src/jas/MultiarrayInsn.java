package jas;

public class MultiarrayInsn extends Insn implements RuntimeConstants {
   public MultiarrayInsn(CP cpe, int sz) {
      this.opc = 197;
      this.operand = new MultiarrayOperand(cpe, sz);
   }
}
