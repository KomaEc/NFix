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
import soot.jimple.Jimple;
import soot.jimple.NaiveSideEffectTester;
import soot.jimple.toolkits.graph.CriticalEdgeRemover;
import soot.jimple.toolkits.graph.LoopConditionUnroller;
import soot.jimple.toolkits.pointer.PASideEffectTester;
import soot.jimple.toolkits.scalar.LocalCreation;
import soot.options.LCMOptions;
import soot.options.Options;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.CollectionFlowUniverse;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.FlowUniverse;
import soot.util.Chain;
import soot.util.UnitMap;

public class LazyCodeMotion extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(LazyCodeMotion.class);
   private static final String PREFIX = "$lcm";

   public LazyCodeMotion(Singletons.Global g) {
   }

   public static LazyCodeMotion v() {
      return G.v().soot_jimple_toolkits_scalar_pre_LazyCodeMotion();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> opts) {
      LCMOptions options = new LCMOptions(opts);
      HashMap<EquivalentValue, Local> expToHelper = new HashMap();
      Chain<Unit> unitChain = b.getUnits();
      if (Options.v().verbose()) {
         logger.debug("[" + b.getMethod().getName() + "] Performing Lazy Code Motion...");
      }

      if (options.unroll()) {
         (new LoopConditionUnroller()).transform(b, phaseName + ".lcu");
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
      FlowUniverse<EquivalentValue> universe = new CollectionFlowUniverse(unitToEquivRhs.values());
      BoundedFlowSet<EquivalentValue> set = new ArrayPackedSet(universe);
      Object sideEffect;
      if (Scene.v().hasCallGraph() && !options.naive_side_effect()) {
         sideEffect = new PASideEffectTester();
      } else {
         sideEffect = new NaiveSideEffectTester();
      }

      ((SideEffectTester)sideEffect).newMethod(b.getMethod());
      UpSafetyAnalysis upSafe;
      if (options.safety() == 1) {
         upSafe = new UpSafetyAnalysis(graph, unitToNoExceptionEquivRhs, (SideEffectTester)sideEffect, set);
      } else {
         upSafe = new UpSafetyAnalysis(graph, unitToEquivRhs, (SideEffectTester)sideEffect, set);
      }

      DownSafetyAnalysis downSafe;
      if (options.safety() == 3) {
         downSafe = new DownSafetyAnalysis(graph, unitToEquivRhs, (SideEffectTester)sideEffect, set);
      } else {
         downSafe = new DownSafetyAnalysis(graph, unitToNoExceptionEquivRhs, (SideEffectTester)sideEffect, set);
         Iterator unitIt = unitChain.iterator();

         while(unitIt.hasNext()) {
            Unit currentUnit = (Unit)unitIt.next();
            EquivalentValue rhs = (EquivalentValue)unitToEquivRhs.get(currentUnit);
            if (rhs != null) {
               ((FlowSet)downSafe.getFlowBefore(currentUnit)).add(rhs);
            }
         }
      }

      EarliestnessComputation earliest = new EarliestnessComputation(graph, upSafe, downSafe, (SideEffectTester)sideEffect, set);
      DelayabilityAnalysis delay = new DelayabilityAnalysis(graph, earliest, unitToEquivRhs, set);
      LatestComputation latest = new LatestComputation(graph, delay, unitToEquivRhs, set);
      NotIsolatedAnalysis notIsolated = new NotIsolatedAnalysis(graph, latest, unitToEquivRhs, set);
      LocalCreation localCreation = new LocalCreation(b.getLocals(), "$lcm");
      Iterator unitIt = unitChain.snapshotIterator();

      FlowSet latestSet;
      FlowSet notIsolatedSet;
      Unit currentUnit;
      while(unitIt.hasNext()) {
         currentUnit = (Unit)unitIt.next();
         FlowSet<EquivalentValue> latestSet = latest.getFlowBefore(currentUnit);
         latestSet = (FlowSet)notIsolated.getFlowAfter(currentUnit);
         notIsolatedSet = latestSet.clone();
         notIsolatedSet.intersection(latestSet, notIsolatedSet);
         Iterator var25 = notIsolatedSet.iterator();

         while(var25.hasNext()) {
            EquivalentValue equiVal = (EquivalentValue)var25.next();
            Local helper = (Local)expToHelper.get(equiVal);
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
            latestSet = latest.getFlowBefore(currentUnit);
            notIsolatedSet = (FlowSet)notIsolated.getFlowAfter(currentUnit);
            if (!latestSet.contains(rhs) && notIsolatedSet.contains(rhs)) {
               Local helper = (Local)expToHelper.get(rhs);

               try {
                  if (helper != null) {
                     ((AssignStmt)currentUnit).setRightOp(helper);
                  }
               } catch (RuntimeException var30) {
                  logger.debug("Error on " + b.getMethod().getName());
                  logger.debug("" + currentUnit.toString());
                  logger.debug("" + latestSet);
                  logger.debug("" + notIsolatedSet);
                  throw var30;
               }
            }
         }
      }

      if (Options.v().verbose()) {
         logger.debug("[" + b.getMethod().getName() + "]     Lazy Code Motion done.");
      }

   }
}
