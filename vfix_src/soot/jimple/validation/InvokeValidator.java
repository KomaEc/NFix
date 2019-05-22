package soot.jimple.validation;

import java.util.List;
import soot.Body;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;

public enum InvokeValidator implements BodyValidator {
   INSTANCE;

   public static InvokeValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exceptions) {
   }

   public boolean isBasicValidator() {
      return false;
   }
}
