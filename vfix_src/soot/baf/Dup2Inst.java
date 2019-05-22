package soot.baf;

import soot.Type;

public interface Dup2Inst extends DupInst {
   Type getOp1Type();

   Type getOp2Type();
}
