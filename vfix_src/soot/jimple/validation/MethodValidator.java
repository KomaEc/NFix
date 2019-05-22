package soot.jimple.validation;

import java.util.List;
import soot.Body;
import soot.SootMethod;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;

public enum MethodValidator implements BodyValidator {
   INSTANCE;

   public static MethodValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exceptions) {
      SootMethod method = body.getMethod();
      if (!method.isAbstract()) {
         if (method.isStaticInitializer() && !method.isStatic()) {
            exceptions.add(new ValidationException(method, "<clinit> should be static! Static initializer without 'static'('0x8') modifier will cause problem when running on android platform: \"<clinit> is not flagged correctly wrt/ static\"!"));
         }

      }
   }

   public boolean isBasicValidator() {
      return true;
   }
}
