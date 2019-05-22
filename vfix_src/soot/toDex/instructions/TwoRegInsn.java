package soot.toDex.instructions;

import soot.toDex.Register;

public interface TwoRegInsn extends OneRegInsn {
   int REG_B_IDX = 1;

   Register getRegB();
}
