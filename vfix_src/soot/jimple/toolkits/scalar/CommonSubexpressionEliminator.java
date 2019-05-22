package soot.jimple.toolkits.scalar;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.EquivalentValue;
import soot.G;
import soot.Local;
import soot.PhaseOptions;
import soot.Scene;
import soot.SideEffectTester;
import soot.Singletons;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.NaiveSideEffectTester;
import soot.jimple.Stmt;
import soot.jimple.toolkits.pointer.PASideEffectTester;
import soot.options.Options;
import soot.tagkit.StringTag;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;

public class CommonSubexpressionEliminator extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(CommonSubexpressionEliminator.class);

   public CommonSubexpressionEliminator(Singletons.Global g) {
   }

   public static CommonSubexpressionEliminator v() {
      return G.v().soot_jimple_toolkits_scalar_CommonSubexpressionEliminator();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      int counter = 0;
      Iterator<Local> localsIt = b.getLocals().iterator();
      HashSet localNames = new HashSet(b.getLocals().size());

      while(localsIt.hasNext()) {
         localNames.add(((Local)localsIt.next()).getName());
      }

      Object sideEffect;
      if (Scene.v().hasCallGraph() && !PhaseOptions.getBoolean(options, "naive-side-effect")) {
         sideEffect = new PASideEffectTester();
      } else {
         sideEffect = new NaiveSideEffectTester();
      }

      ((SideEffectTester)sideEffect).newMethod(b.getMethod());
      if (Options.v().verbose()) {
         logger.debug("[" + b.getMethod().getName() + "]     Eliminating common subexpressions " + (sideEffect instanceof NaiveSideEffectTester ? "(naively)" : "") + "...");
      }

      AvailableExpressions ae = new FastAvailableExpressions(b, (SideEffectTester)sideEffect);
      Chain<Unit> units = b.getUnits();
      Iterator unitsIt = units.snapshotIterator();

      label63:
      while(true) {
         Stmt s;
         Chain availExprs;
         Value v;
         EquivalentValue ev;
         do {
            do {
               if (!unitsIt.hasNext()) {
                  if (Options.v().verbose()) {
                     logger.debug("[" + b.getMethod().getName() + "]     Eliminating common subexpressions done!");
                  }

                  return;
               }

               s = (Stmt)unitsIt.next();
            } while(!(s instanceof AssignStmt));

            availExprs = ae.getAvailableEquivsBefore(s);
            v = ((AssignStmt)s).getRightOp();
            ev = new EquivalentValue(v);
         } while(!availExprs.contains(ev));

         List availPairs = ae.getAvailablePairsBefore(s);
         Iterator availIt = availPairs.iterator();

         while(true) {
            UnitValueBoxPair up;
            do {
               if (!availIt.hasNext()) {
                  continue label63;
               }

               up = (UnitValueBoxPair)availIt.next();
            } while(!up.getValueBox().getValue().equivTo(v));

            String newName = "$cseTmp" + counter;
            ++counter;

            while(localNames.contains(newName)) {
               newName = "$cseTmp" + counter;
               ++counter;
            }

            Local l = Jimple.v().newLocal(newName, Type.toMachineType(v.getType()));
            b.getLocals().add(l);
            AssignStmt origCalc = (AssignStmt)up.getUnit();
            Value origLHS = origCalc.getLeftOp();
            origCalc.setLeftOp(l);
            Unit copier = Jimple.v().newAssignStmt(origLHS, l);
            units.insertAfter((Object)copier, origCalc);
            ((AssignStmt)s).setRightOp(l);
            copier.addTag(new StringTag("Common sub-expression"));
            s.addTag(new StringTag("Common sub-expression"));
         }
      }
   }
}
