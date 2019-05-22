package jas;

public class InvokedynamicInsn extends Insn implements RuntimeConstants {
   public InvokedynamicInsn(CP cpe, int nargs) {
      this.opc = 185;
      this.operand = new InvokeinterfaceOperand(cpe, nargs);
   }
}
