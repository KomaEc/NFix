package soot.validation;

import java.util.List;
import soot.SootClass;

public enum ClassFlagsValidator implements ClassValidator {
   INSTANCE;

   public static ClassFlagsValidator v() {
      return INSTANCE;
   }

   public void validate(SootClass sc, List<ValidationException> exceptions) {
      if (sc.isInterface() && sc.isEnum()) {
         exceptions.add(new ValidationException(sc, "Class is both an interface and an enum"));
      }

      if (sc.isSynchronized()) {
         exceptions.add(new ValidationException(sc, "Classes cannot be synchronized"));
      }

   }

   public boolean isBasicValidator() {
      return true;
   }
}
