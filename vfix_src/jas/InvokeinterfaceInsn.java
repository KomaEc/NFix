package jas;

public class InvokeinterfaceInsn extends Insn implements RuntimeConstants {
   public InvokeinterfaceInsn(CP cpe, int nargs) {
      this.opc = 185;
      this.operand = new InvokeinterfaceOperand(cpe, nargs);
   }
}
