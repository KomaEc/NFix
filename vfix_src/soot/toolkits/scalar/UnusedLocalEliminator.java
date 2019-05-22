package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.util.Chain;

public class UnusedLocalEliminator extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(UnusedLocalEliminator.class);

   public UnusedLocalEliminator(Singletons.Global g) {
   }

   public static UnusedLocalEliminator v() {
      return G.v().soot_toolkits_scalar_UnusedLocalEliminator();
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      if (Options.v().verbose()) {
         logger.debug("[" + body.getMethod().getName() + "] Eliminating unused locals...");
      }

      int i = 0;
      int n = body.getLocals().size();
      int[] oldNumbers = new int[n];
      Chain<Local> locals = body.getLocals();

      for(Iterator var8 = locals.iterator(); var8.hasNext(); ++i) {
         Local local = (Local)var8.next();
         oldNumbers[i] = local.getNumber();
         local.setNumber(i);
      }

      boolean[] usedLocals = new boolean[n];
      Iterator var16 = body.getUnits().iterator();

      while(var16.hasNext()) {
         Unit s = (Unit)var16.next();
         Iterator var11 = s.getUseBoxes().iterator();

         ValueBox vb;
         Value v;
         Local l;
         while(var11.hasNext()) {
            vb = (ValueBox)var11.next();
            v = vb.getValue();
            if (v instanceof Local) {
               l = (Local)v;

               assert locals.contains(l);

               usedLocals[l.getNumber()] = true;
            }
         }

         var11 = s.getDefBoxes().iterator();

         while(var11.hasNext()) {
            vb = (ValueBox)var11.next();
            v = vb.getValue();
            if (v instanceof Local) {
               l = (Local)v;

               assert locals.contains(l);

               usedLocals[l.getNumber()] = true;
            }
         }
      }

      List<Local> keep = new ArrayList(body.getLocalCount());
      Iterator var18 = locals.iterator();

      while(var18.hasNext()) {
         Local local = (Local)var18.next();
         int lno = local.getNumber();
         local.setNumber(oldNumbers[lno]);
         if (usedLocals[lno]) {
            keep.add(local);
         }
      }

      body.getLocals().clear();
      body.getLocals().addAll(keep);
   }
}
