package soot.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.Value;
import soot.ValueBox;

public enum LocalsValidator implements BodyValidator {
   INSTANCE;

   public static LocalsValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exception) {
      Iterator var3 = body.getUseBoxes().iterator();

      ValueBox vb;
      while(var3.hasNext()) {
         vb = (ValueBox)var3.next();
         this.validateLocal(body, vb, exception);
      }

      var3 = body.getDefBoxes().iterator();

      while(var3.hasNext()) {
         vb = (ValueBox)var3.next();
         this.validateLocal(body, vb, exception);
      }

   }

   private void validateLocal(Body body, ValueBox vb, List<ValidationException> exception) {
      Value value;
      if ((value = vb.getValue()) instanceof Local && !body.getLocals().contains(value)) {
         exception.add(new ValidationException(value, "Local not in chain : " + value + " in " + body.getMethod()));
      }

   }

   public boolean isBasicValidator() {
      return true;
   }
}
