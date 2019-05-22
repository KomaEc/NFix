package soot.jimple.toolkits.scalar.pre;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.EquivalentValue;
import soot.G;
import soot.Local;
import soot.Scene;
import soot.SideEffectTester;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.NaiveSideEffectTester;
import soot.jimple.toolkits.graph.CriticalEdgeRemover;
import soot.jimple.toolkits.pointer.PASideEffectTester;
import soot.jimple.toolkits.scalar.LocalCreation;
import soot.options.BCMOptions;
import soot.options.Options;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;
import soot.util.UnitMap;

public class BusyCodeMotion extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(BusyCodeMotion.class);
   private static final String PREFIX = "$bcm";

   public BusyCodeMotion(Singletons.Global g) {
   }

   public static BusyCodeMotion v() {
      return G.v().soot_jimple_toolkits_scalar_pre_BusyCodeMotion();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> opts) {
      BCMOptions options = new BCMOptions(opts);
      HashMap<EquivalentValue, Local> expToHelper = new HashMap();
      Chain<Unit> unitChain = b.getUnits();
      if (Options.v().verbose()) {
         logger.debug("[" + b.getMethod().getName() + "]     performing Busy Code Motion...");
      }

      CriticalEdgeRemover.v().transform(b, phaseName + ".cer");
      UnitGraph graph = new BriefUnitGraph(b);
      Map<Unit, EquivalentValue> unitToEquivRhs = new UnitMap<EquivalentValue>(b, graph.size() + 1, 0.7F) {
         protected EquivalentValue mapTo(Unit unit) {
            Value tmp = SootFilter.noInvokeRhs(unit);
            Value tmp2 = SootFilter.binop(tmp);
            if (tmp2 == null) {
               tmp2 = SootFilter.concreteRef(tmp);
            }

            return SootFilter.equiVal(tmp2);
         }
      };
      Map<Unit, EquivalentValue> unitToNoExceptionEquivRhs = new UnitMap<EquivalentValue>(b, graph.size() + 1, 0.7F) {
         protected EquivalentValue mapTo(Unit unit) {
            Value tmp = SootFilter.binopRhs(unit);
            tmp = SootFilter.noExceptionThrowing(tmp);
            return SootFilter.equiVal(tmp);
         }
      };
      Object sideEffect;
      if (Scene.v().hasCallGraph() && !options.naive_side_effect()) {
         sideEffect = new PASideEffectTester();
      } else {
         sideEffect = new NaiveSideEffectTester();
      }

      ((SideEffectTester)sideEffect).newMethod(b.getMethod());
      UpSafetyAnalysis upSafe = new UpSafetyAnalysis(graph, unitToEquivRhs, (SideEffectTester)sideEffect);
      DownSafetyAnalysis downSafe = new DownSafetyAnalysis(graph, unitToNoExceptionEquivRhs, (SideEffectTester)sideEffect);
      EarliestnessComputation earliest = new EarliestnessComputation(graph, upSafe, downSafe, (SideEffectTester)sideEffect);
      LocalCreation localCreation = new LocalCreation(b.getLocals(), "$bcm");
      Iterator unitIt = unitChain.snapshotIterator();

      Unit currentUnit;
      while(unitIt.hasNext()) {
         currentUnit = (Unit)unitIt.next();
         Iterator var17 = earliest.getFlowBefore(currentUnit).iterator();

         while(var17.hasNext()) {
            EquivalentValue equiVal = (EquivalentValue)var17.next();
            Local helper = (Local)expToHelper.get(equiVal);
            if (currentUnit instanceof IdentityStmt) {
               currentUnit = this.getFirstNonIdentityStmt(b);
            }

            if (helper == null) {
               helper = localCreation.newLocal(equiVal.getType());
               expToHelper.put(equiVal, helper);
            }

            Value insertValue = Jimple.cloneIfNecessary(equiVal.getValue());
            Unit firstComp = Jimple.v().newAssignStmt(helper, insertValue);
            unitChain.insertBefore((Object)firstComp, currentUnit);
         }
      }

      unitIt = unitChain.iterator();

      while(unitIt.hasNext()) {
         currentUnit = (Unit)unitIt.next();
         EquivalentValue rhs = (EquivalentValue)unitToEquivRhs.get(currentUnit);
         if (rhs != null) {
            Local helper = (Local)expToHelper.get(rhs);
            if (helper != null) {
               ((AssignStmt)currentUnit).setRightOp(helper);
            }
         }
      }

      if (Options.v().verbose()) {
         logger.debug("[" + b.getMethod().getName() + "]     Busy Code Motion done!");
      }

   }

   private Unit getFirstNonIdentityStmt(Body b) {
      Iterator var2 = b.getUnits().iterator();

      Unit u;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         u = (Unit)var2.next();
      } while(u instanceof IdentityStmt);

      return u;
   }
}
