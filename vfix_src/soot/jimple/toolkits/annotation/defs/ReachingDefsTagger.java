package soot.jimple.toolkits.annotation.defs;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.tagkit.LinkTag;
import soot.toolkits.scalar.LocalDefs;

public class ReachingDefsTagger extends BodyTransformer {
   public ReachingDefsTagger(Singletons.Global g) {
   }

   public static ReachingDefsTagger v() {
      return G.v().soot_jimple_toolkits_annotation_defs_ReachingDefsTagger();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      LocalDefs ld = LocalDefs.Factory.newLocalDefs(b);
      Iterator var5 = b.getUnits().iterator();

      label31:
      while(var5.hasNext()) {
         Unit s = (Unit)var5.next();
         Iterator var7 = s.getUseBoxes().iterator();

         while(true) {
            Value v;
            do {
               if (!var7.hasNext()) {
                  continue label31;
               }

               ValueBox vbox = (ValueBox)var7.next();
               v = vbox.getValue();
            } while(!(v instanceof Local));

            Local l = (Local)v;
            Iterator var11 = ld.getDefsOfAt(l, s).iterator();

            while(var11.hasNext()) {
               Unit next = (Unit)var11.next();
               String info = l + " has reaching def: " + next;
               String className = b.getMethod().getDeclaringClass().getName();
               s.addTag(new LinkTag(info, next, className, "Reaching Defs"));
            }
         }
      }

   }
}
