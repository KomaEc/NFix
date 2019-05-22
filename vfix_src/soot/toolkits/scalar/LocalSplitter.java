package soot.toolkits.scalar;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Scene;
import soot.Singletons;
import soot.Timers;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.LocalBitSetPacker;

public class LocalSplitter extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(LocalSplitter.class);
   protected ThrowAnalysis throwAnalysis;
   protected boolean omitExceptingUnitEdges;

   public LocalSplitter(Singletons.Global g) {
   }

   public LocalSplitter(ThrowAnalysis ta) {
      this(ta, false);
   }

   public LocalSplitter(ThrowAnalysis ta, boolean omitExceptingUnitEdges) {
      this.throwAnalysis = ta;
      this.omitExceptingUnitEdges = omitExceptingUnitEdges;
   }

   public static LocalSplitter v() {
      return G.v().soot_toolkits_scalar_LocalSplitter();
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      if (Options.v().verbose()) {
         logger.debug("[" + body.getMethod().getName() + "] Splitting locals...");
      }

      if (Options.v().time()) {
         Timers.v().splitTimer.start();
      }

      if (Options.v().time()) {
         Timers.v().splitPhase1Timer.start();
      }

      if (this.throwAnalysis == null) {
         this.throwAnalysis = Scene.v().getDefaultThrowAnalysis();
      }

      if (!this.omitExceptingUnitEdges) {
         this.omitExceptingUnitEdges = Options.v().omit_excepting_unit_edges();
      }

      LocalBitSetPacker localPacker = new LocalBitSetPacker(body);
      localPacker.pack();
      ExceptionalUnitGraph graph = new ExceptionalUnitGraph(body, this.throwAnalysis, this.omitExceptingUnitEdges);
      LocalDefs defs = LocalDefs.Factory.newLocalDefs((UnitGraph)graph, true);
      LocalUses uses = LocalUses.Factory.newLocalUses((UnitGraph)graph, defs);
      if (Options.v().time()) {
         Timers.v().splitPhase1Timer.end();
      }

      if (Options.v().time()) {
         Timers.v().splitPhase2Timer.start();
      }

      Set<Unit> visited = new HashSet();
      BitSet localsToSplit = new BitSet(localPacker.getLocalCount());
      BitSet localsVisited = new BitSet(localPacker.getLocalCount());
      Iterator var11 = body.getUnits().iterator();

      Unit s;
      Local oldLocal;
      while(var11.hasNext()) {
         s = (Unit)var11.next();
         if (!s.getDefBoxes().isEmpty() && ((ValueBox)s.getDefBoxes().get(0)).getValue() instanceof Local) {
            oldLocal = (Local)((ValueBox)s.getDefBoxes().get(0)).getValue();
            if (localsVisited.get(oldLocal.getNumber())) {
               localsToSplit.set(oldLocal.getNumber());
            }

            localsVisited.set(oldLocal.getNumber());
         }
      }

      int w = 0;
      var11 = body.getUnits().iterator();

      while(true) {
         do {
            do {
               do {
                  do {
                     if (!var11.hasNext()) {
                        localPacker.unpack();
                        if (Options.v().time()) {
                           Timers.v().splitPhase2Timer.end();
                        }

                        if (Options.v().time()) {
                           Timers.v().splitTimer.end();
                        }

                        return;
                     }

                     s = (Unit)var11.next();
                  } while(s.getDefBoxes().isEmpty());

                  if (s.getDefBoxes().size() > 1) {
                     throw new RuntimeException("stmt with more than 1 defbox!");
                  }
               } while(!(((ValueBox)s.getDefBoxes().get(0)).getValue() instanceof Local));
            } while(visited.remove(s));

            oldLocal = (Local)((ValueBox)s.getDefBoxes().get(0)).getValue();
         } while(!localsToSplit.get(oldLocal.getNumber()));

         Local newLocal = (Local)oldLocal.clone();
         StringBuilder var10001 = (new StringBuilder()).append(newLocal.getName()).append('#');
         ++w;
         newLocal.setName(var10001.append(w).toString());
         body.getLocals().add(newLocal);
         Deque<Unit> queue = new ArrayDeque();
         queue.addFirst(s);

         do {
            Unit head = (Unit)queue.removeFirst();
            if (visited.add(head)) {
               Iterator var17 = uses.getUsesOf(head).iterator();

               while(var17.hasNext()) {
                  UnitValueBoxPair use = (UnitValueBoxPair)var17.next();
                  ValueBox vb = use.valueBox;
                  Value v = vb.getValue();
                  if (v != newLocal && v instanceof Local) {
                     Local l = (Local)v;
                     queue.addAll(defs.getDefsOfAt(l, use.unit));
                     vb.setValue(newLocal);
                  }
               }

               var17 = head.getDefBoxes().iterator();

               while(var17.hasNext()) {
                  ValueBox vb = (ValueBox)var17.next();
                  Value v = vb.getValue();
                  if (v instanceof Local) {
                     vb.setValue(newLocal);
                  }
               }
            }
         } while(!queue.isEmpty());

         visited.remove(s);
      }
   }
}
