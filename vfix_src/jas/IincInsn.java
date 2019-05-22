package jas;

public class IincInsn extends Insn implements RuntimeConstants {
   public IincInsn(int vindex, int increment) {
      this.opc = 132;
      this.operand = new IincOperand(vindex, increment);
   }
}
