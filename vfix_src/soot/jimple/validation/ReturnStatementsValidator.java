package soot.jimple.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Unit;
import soot.jimple.GotoStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ThrowStmt;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;

public enum ReturnStatementsValidator implements BodyValidator {
   INSTANCE;

   public static ReturnStatementsValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exceptions) {
      Iterator var3 = body.getUnits().iterator();

      Unit u;
      do {
         if (!var3.hasNext()) {
            Unit last = body.getUnits().getLast();
            if (!(last instanceof GotoStmt) && !(last instanceof ThrowStmt)) {
               exceptions.add(new ValidationException(body.getMethod(), "The method does not contain a return statement", "Body of method " + body.getMethod().getSignature() + " does not contain a return statement"));
               return;
            }

            return;
         }

         u = (Unit)var3.next();
      } while(!(u instanceof ReturnStmt) && !(u instanceof ReturnVoidStmt) && !(u instanceof RetStmt) && !(u instanceof ThrowStmt));

   }

   public boolean isBasicValidator() {
      return true;
   }
}
