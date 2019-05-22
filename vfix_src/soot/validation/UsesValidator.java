package soot.validation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.toolkits.exceptions.PedanticThrowAnalysis;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.LocalDefs;

public enum UsesValidator implements BodyValidator {
   INSTANCE;

   public static UsesValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exception) {
      ThrowAnalysis throwAnalysis = PedanticThrowAnalysis.v();
      UnitGraph g = new ExceptionalUnitGraph(body, throwAnalysis, false);
      LocalDefs ld = LocalDefs.Factory.newLocalDefs((UnitGraph)g, true);
      Collection<Local> locals = body.getLocals();
      Iterator var7 = body.getUnits().iterator();

      while(var7.hasNext()) {
         Unit u = (Unit)var7.next();
         Iterator var9 = u.getUseBoxes().iterator();

         while(var9.hasNext()) {
            ValueBox box = (ValueBox)var9.next();
            Value v = box.getValue();
            if (v instanceof Local) {
               Local l = (Local)v;
               if (!locals.contains(l)) {
                  String msg = "Local " + v + " is referenced here but not in body's local-chain. (" + body.getMethod() + ")";
                  exception.add(new ValidationException(u, msg, msg));
               }

               if (ld.getDefsOfAt(l, u).isEmpty()) {
                  assert this.graphEdgesAreValid(g, u) : "broken graph found: " + u;

                  exception.add(new ValidationException(u, "There is no path from a definition of " + v + " to this statement.", "(" + body.getMethod() + ") no defs for value: " + l + "!"));
               }
            }
         }
      }

   }

   private boolean graphEdgesAreValid(UnitGraph g, Unit u) {
      Iterator var3 = g.getPredsOf(u).iterator();

      Unit p;
      do {
         if (!var3.hasNext()) {
            return true;
         }

         p = (Unit)var3.next();
      } while(g.getSuccsOf(p).contains(u));

      return false;
   }

   public boolean isBasicValidator() {
      return false;
   }
}
