package soot.toolkits.exceptions;

import soot.Unit;
import soot.baf.ThrowInst;
import soot.jimple.ThrowStmt;

public interface ThrowAnalysis {
   ThrowableSet mightThrow(Unit var1);

   ThrowableSet mightThrowExplicitly(ThrowInst var1);

   ThrowableSet mightThrowExplicitly(ThrowStmt var1);

   ThrowableSet mightThrowImplicitly(ThrowInst var1);

   ThrowableSet mightThrowImplicitly(ThrowStmt var1);
}
