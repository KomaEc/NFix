package soot.jimple.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.SootMethod;
import soot.Type;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;

public enum TypesValidator implements BodyValidator {
   INSTANCE;

   public static TypesValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exceptions) {
      SootMethod method = body.getMethod();
      Iterator var4;
      if (method != null) {
         if (!method.getReturnType().isAllowedInFinalCode()) {
            exceptions.add(new ValidationException(method, "Return type not allowed in final code: " + method.getReturnType(), "return type not allowed in final code:" + method.getReturnType() + "\n method: " + method));
         }

         var4 = method.getParameterTypes().iterator();

         while(var4.hasNext()) {
            Type t = (Type)var4.next();
            if (!t.isAllowedInFinalCode()) {
               exceptions.add(new ValidationException(method, "Parameter type not allowed in final code: " + t, "parameter type not allowed in final code:" + t + "\n method: " + method));
            }
         }
      }

      var4 = body.getLocals().iterator();

      while(var4.hasNext()) {
         Local l = (Local)var4.next();
         Type t = l.getType();
         if (!t.isAllowedInFinalCode()) {
            exceptions.add(new ValidationException(l, "Local type not allowed in final code: " + t, "(" + method + ") local type not allowed in final code: " + t + " local: " + l));
         }
      }

   }

   public boolean isBasicValidator() {
      return true;
   }
}
