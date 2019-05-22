package soot.jimple.validation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityStmt;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;

public enum JimpleTrapValidator implements BodyValidator {
   INSTANCE;

   public static JimpleTrapValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exceptions) {
      Set<Unit> caughtUnits = new HashSet();
      Iterator var4 = body.getTraps().iterator();

      IdentityStmt id;
      while(var4.hasNext()) {
         Trap trap = (Trap)var4.next();
         caughtUnits.add(trap.getHandlerUnit());
         if (!(trap.getHandlerUnit() instanceof IdentityStmt)) {
            exceptions.add(new ValidationException(trap, "Trap handler does not start with caught exception reference"));
         } else {
            id = (IdentityStmt)trap.getHandlerUnit();
            if (!(id.getRightOp() instanceof CaughtExceptionRef)) {
               exceptions.add(new ValidationException(trap, "Trap handler does not start with caught exception reference"));
            }
         }
      }

      var4 = body.getUnits().iterator();

      while(var4.hasNext()) {
         Unit u = (Unit)var4.next();
         if (u instanceof IdentityStmt) {
            id = (IdentityStmt)u;
            if (id.getRightOp() instanceof CaughtExceptionRef && !caughtUnits.contains(id)) {
               exceptions.add(new ValidationException(id, "Could not find a corresponding trap using this statement as handler", "Body of method " + body.getMethod().getSignature() + " contains a caught exception reference, but not a corresponding trap using this statement as handler"));
            }
         }
      }

   }

   public boolean isBasicValidator() {
      return true;
   }
}
