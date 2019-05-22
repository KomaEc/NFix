package soot.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;

public enum CheckEscapingValidator implements BodyValidator {
   INSTANCE;

   public static CheckEscapingValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exception) {
      Iterator var3 = body.getUnits().iterator();

      while(true) {
         Stmt stmt;
         do {
            Unit u;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               u = (Unit)var3.next();
            } while(!(u instanceof Stmt));

            stmt = (Stmt)u;
         } while(!stmt.containsInvokeExpr());

         InvokeExpr iexpr = stmt.getInvokeExpr();
         SootMethodRef ref = iexpr.getMethodRef();
         if (ref.name().contains("'") || ref.declaringClass().getName().contains("'")) {
            throw new ValidationException(stmt, "Escaped name in signature found");
         }

         Iterator var8 = ref.parameterTypes().iterator();

         while(var8.hasNext()) {
            Type paramType = (Type)var8.next();
            if (paramType.toString().contains("'")) {
               throw new ValidationException(stmt, "Escaped name in signature found");
            }
         }
      }
   }

   public boolean isBasicValidator() {
      return false;
   }
}
