package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Timers;
import soot.Trap;
import soot.Unit;
import soot.UnitBox;
import soot.options.Options;
import soot.toolkits.graph.PseudoTopologicalOrderer;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.graph.interaction.FlowInfo;
import soot.toolkits.graph.interaction.InteractionHandler;
import soot.util.Chain;

public abstract class ForwardBranchedFlowAnalysis<A> extends BranchedFlowAnalysis<Unit, A> {
   private static final Logger logger = LoggerFactory.getLogger(ForwardBranchedFlowAnalysis.class);

   public ForwardBranchedFlowAnalysis(UnitGraph graph) {
      super(graph);
   }

   protected boolean isForward() {
      return true;
   }

   private void accumulateAfterFlowSets(Unit s, A[] flowRepositories, List<Object> previousAfterFlows) {
      int repCount = 0;
      previousAfterFlows.clear();
      if (s.fallsThrough()) {
         this.copy(((List)this.unitToAfterFallFlow.get(s)).get(0), flowRepositories[repCount]);
         previousAfterFlows.add(flowRepositories[repCount++]);
      }

      if (s.branches()) {
         List<A> l = this.getBranchFlowAfter(s);
         Iterator it = l.iterator();

         while(it.hasNext()) {
            A fs = it.next();
            this.copy(fs, flowRepositories[repCount]);
            previousAfterFlows.add(flowRepositories[repCount++]);
         }
      }

   }

   protected void doAnalysis() {
      final Map<Unit, Integer> numbers = new HashMap();
      List<Unit> orderedUnits = (new PseudoTopologicalOrderer()).newList(this.graph, false);
      int i = 1;

      for(Iterator var4 = orderedUnits.iterator(); var4.hasNext(); ++i) {
         Unit u = (Unit)var4.next();
         numbers.put(u, new Integer(i));
      }

      TreeSet<Unit> changedUnits = new TreeSet(new Comparator<Unit>() {
         public int compare(Unit o1, Unit o2) {
            Integer i1 = (Integer)numbers.get(o1);
            Integer i2 = (Integer)numbers.get(o2);
            return i1 - i2;
         }
      });
      Map<Unit, ArrayList<A>> unitToIncomingFlowSets = new HashMap(this.graph.size() * 2 + 1, 0.7F);
      List<Unit> heads = this.graph.getHeads();
      int numNodes = this.graph.size();
      int numComputations = 0;
      int maxBranchSize = 0;
      Iterator var9 = this.graph.iterator();

      Unit s;
      while(var9.hasNext()) {
         s = (Unit)var9.next();
         unitToIncomingFlowSets.put(s, new ArrayList());
      }

      Chain<Unit> sl = ((UnitGraph)this.graph).getBody().getUnits();
      Iterator var25 = this.graph.iterator();

      Unit s;
      Unit succ;
      while(var25.hasNext()) {
         s = (Unit)var25.next();
         changedUnits.add(s);
         this.unitToBeforeFlow.put(s, this.newInitialFlow());
         ArrayList l;
         if (s.fallsThrough()) {
            l = new ArrayList();
            l.add(this.newInitialFlow());
            this.unitToAfterFallFlow.put(s, l);
            Unit succ = (Unit)sl.getSuccOf(s);
            if (succ != null) {
               List<A> l = (List)unitToIncomingFlowSets.get(sl.getSuccOf(s));
               l.addAll(l);
            }
         } else {
            this.unitToAfterFallFlow.put(s, new ArrayList());
         }

         l = new ArrayList();
         if (s.branches()) {
            Iterator var34 = s.getUnitBoxes().iterator();

            while(var34.hasNext()) {
               UnitBox ub = (UnitBox)var34.next();
               A f = this.newInitialFlow();
               l.add(f);
               succ = ub.getUnit();
               List<A> incList = (List)unitToIncomingFlowSets.get(succ);
               incList.add(f);
            }
         }

         this.unitToAfterBranchFlow.put(s, l);
         if (s.getUnitBoxes().size() > maxBranchSize) {
            maxBranchSize = s.getUnitBoxes().size();
         }
      }

      var9 = heads.iterator();

      while(var9.hasNext()) {
         s = (Unit)var9.next();
         this.unitToBeforeFlow.put(s, this.entryInitialFlow());
      }

      if (this.treatTrapHandlersAsEntries()) {
         var9 = ((UnitGraph)this.graph).getBody().getTraps().iterator();

         while(var9.hasNext()) {
            Trap trap = (Trap)var9.next();
            s = trap.getHandlerUnit();
            this.unitToBeforeFlow.put(s, this.entryInitialFlow());
         }
      }

      List<Object> previousAfterFlows = new ArrayList();
      List<Object> afterFlows = new ArrayList();
      A[] flowRepositories = (Object[])(new Object[maxBranchSize + 1]);

      for(int i = 0; i < maxBranchSize + 1; ++i) {
         flowRepositories[i] = this.newInitialFlow();
      }

      A[] previousFlowRepositories = (Object[])(new Object[maxBranchSize + 1]);

      for(int i = 0; i < maxBranchSize + 1; ++i) {
         previousFlowRepositories[i] = this.newInitialFlow();
      }

      while(true) {
         Unit s;
         do {
            if (changedUnits.isEmpty()) {
               Timers var10000 = Timers.v();
               var10000.totalFlowNodes += numNodes;
               var10000 = Timers.v();
               var10000.totalFlowComputations += numComputations;
               return;
            }

            s = (Unit)changedUnits.first();
            changedUnits.remove(s);
            boolean isHead = heads.contains(s);
            this.accumulateAfterFlowSets(s, previousFlowRepositories, previousAfterFlows);
            List<A> afterFallFlow = (List)unitToIncomingFlowSets.get(s);
            A beforeFlow = this.getFlowBefore(s);
            Object savedFlow;
            if (afterFallFlow.size() == 1) {
               this.copy(afterFallFlow.get(0), beforeFlow);
            } else if (afterFallFlow.size() != 0) {
               Iterator<A> predIt = afterFallFlow.iterator();
               this.copy(predIt.next(), beforeFlow);

               while(predIt.hasNext()) {
                  A otherBranchFlow = predIt.next();
                  savedFlow = this.newInitialFlow();
                  this.merge(s, beforeFlow, otherBranchFlow, savedFlow);
                  this.copy(savedFlow, beforeFlow);
               }
            }

            if (isHead && afterFallFlow.size() != 0) {
               this.mergeInto(s, beforeFlow, this.entryInitialFlow());
            }

            afterFallFlow = (List)this.unitToAfterFallFlow.get(s);
            List<A> afterBranchFlow = this.getBranchFlowAfter(s);
            if (Options.v().interactive_mode()) {
               InteractionHandler ih = InteractionHandler.v();
               savedFlow = this.newInitialFlow();
               this.copy(beforeFlow, savedFlow);
               FlowInfo<A, Unit> fi = new FlowInfo(savedFlow, s, true);
               if (ih.getStopUnitList() != null && ih.getStopUnitList().contains(s)) {
                  ih.handleStopAtNodeEvent(s);
               }

               ih.handleBeforeAnalysisEvent(fi);
            }

            this.flowThrough(beforeFlow, s, afterFallFlow, afterBranchFlow);
            if (Options.v().interactive_mode()) {
               List<A> l = new ArrayList();
               if (!afterFallFlow.isEmpty()) {
                  l.addAll(afterFallFlow);
               }

               if (!afterBranchFlow.isEmpty()) {
                  l.addAll(afterBranchFlow);
               }

               FlowInfo<List<A>, Unit> fi = new FlowInfo(l, s, false);
               InteractionHandler.v().handleAfterAnalysisEvent(fi);
            }

            ++numComputations;
            this.accumulateAfterFlowSets(s, flowRepositories, afterFlows);
         } while(afterFlows.equals(previousAfterFlows));

         Iterator var39 = this.graph.getSuccsOf(s).iterator();

         while(var39.hasNext()) {
            succ = (Unit)var39.next();
            changedUnits.add(succ);
         }
      }
   }
}
