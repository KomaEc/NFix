package soot.jimple.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;
import soot.util.Chain;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;

public enum IdentityStatementsValidator implements BodyValidator {
   INSTANCE;

   public static IdentityStatementsValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exceptions) {
      SootMethod method = body.getMethod();
      if (!method.isAbstract()) {
         Chain<Unit> units = body.getUnits().getNonPatchingChain();
         boolean foundNonThisOrParamIdentityStatement = false;
         boolean firstStatement = true;

         for(Iterator var7 = units.iterator(); var7.hasNext(); firstStatement = false) {
            Unit unit = (Unit)var7.next();
            if (unit instanceof IdentityStmt) {
               IdentityStmt identityStmt = (IdentityStmt)unit;
               if (identityStmt.getRightOp() instanceof ThisRef) {
                  if (method.isStatic()) {
                     exceptions.add(new ValidationException(identityStmt, "@this-assignment in a static method!"));
                  }

                  if (!firstStatement) {
                     exceptions.add(new ValidationException(identityStmt, "@this-assignment statement should precede all other statements\n method: " + method));
                  }
               } else if (identityStmt.getRightOp() instanceof ParameterRef) {
                  if (foundNonThisOrParamIdentityStatement) {
                     exceptions.add(new ValidationException(identityStmt, "@param-assignment statements should precede all non-identity statements\n method: " + method));
                  }
               } else {
                  foundNonThisOrParamIdentityStatement = true;
               }
            } else {
               foundNonThisOrParamIdentityStatement = true;
            }
         }

      }
   }

   public boolean isBasicValidator() {
      return true;
   }
}
