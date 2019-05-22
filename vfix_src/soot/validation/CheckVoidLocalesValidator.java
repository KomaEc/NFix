package soot.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.VoidType;

public enum CheckVoidLocalesValidator implements BodyValidator {
   INSTANCE;

   public static CheckVoidLocalesValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exception) {
      Iterator var3 = body.getLocals().iterator();

      while(var3.hasNext()) {
         Local l = (Local)var3.next();
         if (l.getType() instanceof VoidType) {
            exception.add(new ValidationException(l, "Local " + l + " in " + body.getMethod() + " defined with void type"));
         }
      }

   }

   public boolean isBasicValidator() {
      return false;
   }
}
