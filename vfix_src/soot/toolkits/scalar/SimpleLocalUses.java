package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Local;
import soot.Timers;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.graph.UnitGraph;

public class SimpleLocalUses implements LocalUses {
   private static final Logger logger = LoggerFactory.getLogger(SimpleLocalUses.class);
   final Body body;
   private Map<Unit, List<UnitValueBoxPair>> unitToUses;

   public SimpleLocalUses(UnitGraph graph, LocalDefs localDefs) {
      this(graph.getBody(), localDefs);
   }

   public SimpleLocalUses(Body body, LocalDefs localDefs) {
      this.body = body;
      Options options = Options.v();
      if (options.time()) {
         Timers.v().usesTimer.start();
         Timers.v().usePhase1Timer.start();
      }

      if (options.verbose()) {
         logger.debug("[" + body.getMethod().getName() + "]     Constructing SimpleLocalUses...");
      }

      this.unitToUses = new HashMap(body.getUnits().size() * 2 + 1, 0.7F);
      if (options.time()) {
         Timers.v().usePhase1Timer.end();
         Timers.v().usePhase2Timer.start();
      }

      Iterator var4 = body.getUnits().iterator();

      label60:
      while(var4.hasNext()) {
         Unit unit = (Unit)var4.next();
         Iterator var6 = unit.getUseBoxes().iterator();

         while(true) {
            UnitValueBoxPair newPair;
            List defs;
            do {
               ValueBox useBox;
               Value v;
               do {
                  if (!var6.hasNext()) {
                     continue label60;
                  }

                  useBox = (ValueBox)var6.next();
                  v = useBox.getValue();
               } while(!(v instanceof Local));

               Local l = (Local)v;
               newPair = new UnitValueBoxPair(unit, useBox);
               defs = localDefs.getDefsOfAt(l, unit);
            } while(defs == null);

            Object lst;
            for(Iterator var12 = defs.iterator(); var12.hasNext(); ((List)lst).add(newPair)) {
               Unit def = (Unit)var12.next();
               lst = (List)this.unitToUses.get(def);
               if (lst == null) {
                  this.unitToUses.put(def, lst = new ArrayList());
               }
            }
         }
      }

      if (options.time()) {
         Timers.v().usePhase2Timer.end();
         Timers.v().usesTimer.end();
      }

      if (options.verbose()) {
         logger.debug("[" + body.getMethod().getName() + "]     finished SimpleLocalUses...");
      }

   }

   public List<UnitValueBoxPair> getUsesOf(Unit s) {
      List<UnitValueBoxPair> l = (List)this.unitToUses.get(s);
      return l == null ? Collections.emptyList() : Collections.unmodifiableList(l);
   }

   public Set<Local> getUsedVariables() {
      Set<Local> res = new HashSet();
      Iterator var2 = this.unitToUses.values().iterator();

      while(var2.hasNext()) {
         List<UnitValueBoxPair> vals = (List)var2.next();
         Iterator var4 = vals.iterator();

         while(var4.hasNext()) {
            UnitValueBoxPair val = (UnitValueBoxPair)var4.next();
            res.add((Local)val.valueBox.getValue());
         }
      }

      return res;
   }

   public Set<Local> getUnusedVariables() {
      Set<Local> res = new HashSet(this.body.getLocals());
      res.retainAll(this.getUsedVariables());
      return res;
   }
}
