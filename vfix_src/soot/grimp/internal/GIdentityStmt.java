package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JIdentityStmt;

public class GIdentityStmt extends JIdentityStmt {
   public GIdentityStmt(Value local, Value identityValue) {
      super(Grimp.v().newLocalBox(local), Grimp.v().newIdentityRefBox(identityValue));
   }

   public Object clone() {
      return new GIdentityStmt(Grimp.cloneIfNecessary(this.getLeftOp()), Grimp.cloneIfNecessary(this.getRightOp()));
   }
}
