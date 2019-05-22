package soot.validation;

import java.util.List;
import soot.Body;

public interface BodyValidator {
   void validate(Body var1, List<ValidationException> var2);

   boolean isBasicValidator();
}
