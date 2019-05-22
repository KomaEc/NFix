package soot.jimple.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;

public enum IdentityValidator implements BodyValidator {
   INSTANCE;

   public static IdentityValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exceptions) {
      boolean hasThisLocal = false;
      int paramCount = body.getMethod().getParameterCount();
      boolean[] parameterRefs = new boolean[paramCount];
      Iterator var6 = body.getUnits().iterator();

      while(true) {
         IdentityStmt id;
         do {
            Unit u;
            do {
               if (!var6.hasNext()) {
                  if (!body.getMethod().isStatic() && !hasThisLocal) {
                     exceptions.add(new ValidationException(body, String.format("The method %s is not static, but does not have a this local", body.getMethod().getSignature())));
                  }

                  for(int i = 0; i < paramCount; ++i) {
                     if (!parameterRefs[i]) {
                        exceptions.add(new ValidationException(body, String.format("There is no parameter local for parameter number %d", i)));
                     }
                  }

                  return;
               }

               u = (Unit)var6.next();
            } while(!(u instanceof IdentityStmt));

            id = (IdentityStmt)u;
            if (id.getRightOp() instanceof ThisRef) {
               hasThisLocal = true;
            }
         } while(!(id.getRightOp() instanceof ParameterRef));

         ParameterRef ref = (ParameterRef)id.getRightOp();
         if (ref.getIndex() < 0 || ref.getIndex() >= paramCount) {
            if (paramCount == 0) {
               exceptions.add(new ValidationException(id, "This method has no parameters, so no parameter reference is allowed"));
            } else {
               exceptions.add(new ValidationException(id, String.format("Parameter reference index must be between 0 and %d (inclusive)", paramCount - 1)));
            }

            return;
         }

         if (parameterRefs[ref.getIndex()]) {
            exceptions.add(new ValidationException(id, String.format("Only one local for parameter %d is allowed", ref.getIndex())));
         }

         parameterRefs[ref.getIndex()] = true;
      }
   }

   public boolean isBasicValidator() {
      return true;
   }
}
