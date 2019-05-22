package soot.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.PatchingChain;
import soot.Trap;
import soot.Unit;

public enum TrapsValidator implements BodyValidator {
   INSTANCE;

   public static TrapsValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exception) {
      PatchingChain<Unit> units = body.getUnits();
      Iterator var4 = body.getTraps().iterator();

      while(var4.hasNext()) {
         Trap t = (Trap)var4.next();
         if (!units.contains(t.getBeginUnit())) {
            exception.add(new ValidationException(t.getBeginUnit(), "begin not in chain in " + body.getMethod()));
         }

         if (!units.contains(t.getEndUnit())) {
            exception.add(new ValidationException(t.getEndUnit(), "end not in chain in " + body.getMethod()));
         }

         if (!units.contains(t.getHandlerUnit())) {
            exception.add(new ValidationException(t.getHandlerUnit(), "handler not in chain in " + body.getMethod()));
         }
      }

   }

   public boolean isBasicValidator() {
      return true;
   }
}
