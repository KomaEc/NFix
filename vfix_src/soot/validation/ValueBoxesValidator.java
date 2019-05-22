package soot.validation;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.Body;
import soot.Unit;
import soot.ValueBox;

public enum ValueBoxesValidator implements BodyValidator {
   INSTANCE;

   public static ValueBoxesValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exception) {
      Set<ValueBox> set = Collections.newSetFromMap(new IdentityHashMap());
      Iterator var4 = body.getUseAndDefBoxes().iterator();

      while(true) {
         ValueBox vb;
         do {
            if (!var4.hasNext()) {
               return;
            }

            vb = (ValueBox)var4.next();
         } while(set.add(vb));

         exception.add(new ValidationException(vb, "Aliased value box : " + vb + " in " + body.getMethod()));
         Iterator var6 = body.getUnits().iterator();

         while(var6.hasNext()) {
            Unit u = (Unit)var6.next();
            System.err.println(u);
         }
      }
   }

   public boolean isBasicValidator() {
      return false;
   }
}
