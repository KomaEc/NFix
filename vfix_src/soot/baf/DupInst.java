package soot.baf;

import java.util.List;
import soot.Type;

public interface DupInst extends Inst {
   List<Type> getOpTypes();

   List<Type> getUnderTypes();
}
