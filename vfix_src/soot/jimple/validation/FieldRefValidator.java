package soot.jimple.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.ResolutionFailedException;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.util.Chain;
import soot.validation.BodyValidator;
import soot.validation.UnitValidationException;
import soot.validation.ValidationException;

public enum FieldRefValidator implements BodyValidator {
   INSTANCE;

   public static FieldRefValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exceptions) {
      SootMethod method = body.getMethod();
      if (!method.isAbstract()) {
         Chain<Unit> units = body.getUnits().getNonPatchingChain();
         Iterator var5 = units.iterator();

         while(var5.hasNext()) {
            Unit unit = (Unit)var5.next();
            Stmt s = (Stmt)unit;
            if (s.containsFieldRef()) {
               FieldRef fr = s.getFieldRef();
               SootField field;
               if (fr instanceof StaticFieldRef) {
                  StaticFieldRef v = (StaticFieldRef)fr;

                  try {
                     field = v.getField();
                     if (field == null) {
                        exceptions.add(new UnitValidationException(unit, body, "Resolved field is null: " + fr.toString()));
                     } else if (!field.isStatic() && !field.isPhantom()) {
                        exceptions.add(new UnitValidationException(unit, body, "Trying to get a static field which is non-static: " + v));
                     }
                  } catch (ResolutionFailedException var12) {
                     exceptions.add(new UnitValidationException(unit, body, "Trying to get a static field which is non-static: " + v));
                  }
               } else {
                  if (!(fr instanceof InstanceFieldRef)) {
                     throw new RuntimeException("unknown field ref");
                  }

                  InstanceFieldRef v = (InstanceFieldRef)fr;

                  try {
                     field = v.getField();
                     if (field == null) {
                        exceptions.add(new UnitValidationException(unit, body, "Resolved field is null: " + fr.toString()));
                     } else if (field.isStatic() && !field.isPhantom()) {
                        exceptions.add(new UnitValidationException(unit, body, "Trying to get an instance field which is static: " + v));
                     }
                  } catch (ResolutionFailedException var11) {
                     exceptions.add(new UnitValidationException(unit, body, "Trying to get an instance field which is static: " + v));
                  }
               }
            }
         }

      }
   }

   public boolean isBasicValidator() {
      return true;
   }
}
