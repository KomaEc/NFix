package soot.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.toolkits.exceptions.ThrowAnalysisFactory;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.InitAnalysis;

public enum CheckInitValidator implements BodyValidator {
   INSTANCE;

   public static CheckInitValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exception) {
      ExceptionalUnitGraph g = new ExceptionalUnitGraph(body, ThrowAnalysisFactory.checkInitThrowAnalysis(), false);
      InitAnalysis analysis = new InitAnalysis(g);
      Iterator var5 = body.getUnits().iterator();

      while(var5.hasNext()) {
         Unit s = (Unit)var5.next();
         FlowSet<Local> init = (FlowSet)analysis.getFlowBefore(s);
         Iterator var8 = s.getUseBoxes().iterator();

         while(var8.hasNext()) {
            ValueBox vBox = (ValueBox)var8.next();
            Value v = vBox.getValue();
            if (v instanceof Local) {
               Local l = (Local)v;
               if (!init.contains(l)) {
                  throw new ValidationException(s, "Local variable $1 is not definitively defined at this point".replace("$1", l.getName()), "Warning: Local variable " + l + " not definitely defined at " + s + " in " + body.getMethod(), false);
               }
            }
         }
      }

   }

   public boolean isBasicValidator() {
      return false;
   }
}
