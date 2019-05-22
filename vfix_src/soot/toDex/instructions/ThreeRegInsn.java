package soot.toDex.instructions;

import soot.toDex.Register;

public interface ThreeRegInsn extends TwoRegInsn {
   int REG_C_IDX = 2;

   Register getRegC();
}
