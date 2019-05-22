package soot.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.UnitBox;

public enum UnitBoxesValidator implements BodyValidator {
   INSTANCE;

   public static UnitBoxesValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exception) {
      Iterator var3 = body.getAllUnitBoxes().iterator();

      UnitBox ub;
      do {
         if (!var3.hasNext()) {
            return;
         }

         ub = (UnitBox)var3.next();
      } while(body.getUnits().contains(ub.getUnit()));

      throw new RuntimeException("Unitbox points outside unitChain! to unit : " + ub.getUnit() + " in " + body.getMethod());
   }

   public boolean isBasicValidator() {
      return true;
   }
}
