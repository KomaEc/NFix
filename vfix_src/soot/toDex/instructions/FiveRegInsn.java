package soot.toDex.instructions;

import soot.toDex.Register;

public interface FiveRegInsn extends Insn {
   int REG_D_IDX = 0;
   int REG_E_IDX = 1;
   int REG_F_IDX = 2;
   int REG_G_IDX = 3;
   int REG_A_IDX = 4;

   Register getRegD();

   Register getRegE();

   Register getRegF();

   Register getRegG();

   Register getRegA();
}
