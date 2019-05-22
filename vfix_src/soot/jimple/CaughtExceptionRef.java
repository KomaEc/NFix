package soot.jimple;

import soot.Type;
import soot.util.Switch;

public interface CaughtExceptionRef extends IdentityRef {
   Type getType();

   void apply(Switch var1);
}
