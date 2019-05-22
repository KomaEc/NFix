package soot.validation;

import java.util.List;
import soot.SootClass;

public interface ClassValidator {
   void validate(SootClass var1, List<ValidationException> var2);

   boolean isBasicValidator();
}
