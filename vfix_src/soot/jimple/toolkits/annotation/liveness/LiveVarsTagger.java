package soot.jimple.toolkits.annotation.liveness;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.tagkit.ColorTag;
import soot.tagkit.StringTag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.LiveLocals;
import soot.toolkits.scalar.SimpleLiveLocals;

public class LiveVarsTagger extends BodyTransformer {
   public LiveVarsTagger(Singletons.Global g) {
   }

   public static LiveVarsTagger v() {
      return G.v().soot_jimple_toolkits_annotation_liveness_LiveVarsTagger();
   }

   protected void internalTransform(Body b, String phaseName, Map options) {
      LiveLocals sll = new SimpleLiveLocals(new ExceptionalUnitGraph(b));
      Iterator it = b.getUnits().iterator();

      while(it.hasNext()) {
         Stmt s = (Stmt)it.next();
         Iterator liveLocalsIt = sll.getLiveLocalsAfter(s).iterator();

         while(liveLocalsIt.hasNext()) {
            Value v = (Value)liveLocalsIt.next();
            s.addTag(new StringTag("Live Variable: " + v, "Live Variable"));
            Iterator usesIt = s.getUseBoxes().iterator();

            while(usesIt.hasNext()) {
               ValueBox use = (ValueBox)usesIt.next();
               if (use.getValue().equals(v)) {
                  use.addTag(new ColorTag(1, "Live Variable"));
               }
            }

            Iterator defsIt = s.getDefBoxes().iterator();

            while(defsIt.hasNext()) {
               ValueBox def = (ValueBox)defsIt.next();
               if (def.getValue().equals(v)) {
                  def.addTag(new ColorTag(1, "Live Variable"));
               }
            }
         }
      }

   }
}
