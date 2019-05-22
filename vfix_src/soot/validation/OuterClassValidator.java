package soot.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.SootClass;

public enum OuterClassValidator implements ClassValidator {
   INSTANCE;

   public static OuterClassValidator v() {
      return INSTANCE;
   }

   public void validate(SootClass sc, List<ValidationException> exceptions) {
      Set<SootClass> outerClasses = new HashSet();

      for(SootClass curClass = sc; curClass != null; curClass = curClass.hasOuterClass() ? curClass.getOuterClass() : null) {
         if (!outerClasses.add(curClass)) {
            exceptions.add(new ValidationException(curClass, "Circular outer class chain"));
            break;
         }
      }

   }

   public boolean isBasicValidator() {
      return true;
   }
}
