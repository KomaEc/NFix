package soot.jimple;

import java.util.List;
import soot.Local;

public interface EqualLocals {
   boolean isLocalEqualToAt(Local var1, Local var2, Stmt var3);

   List getCopiesAt(Stmt var1);
}
