package soot.jimple.toolkits.annotation.nullcheck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.EquivalentValue;
import soot.Unit;
import soot.Value;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.FlowSet;

/** @deprecated */
@Deprecated
public class LocalRefVarsAnalysisWrapper {
   private static final boolean computeChecks = true;
   private static final boolean discardKTop = true;
   Map<Unit, List<RefIntPair>> unitToVarsBefore;
   Map<Unit, List<RefIntPair>> unitToVarsAfterFall;
   Map<Unit, List<List<RefIntPair>>> unitToListsOfVarsAfterBranches;
   Map<Unit, List<Object>> unitToVarsNeedCheck;
   Map<Unit, List<RefIntPair>> unitToVarsDontNeedCheck;
   BranchedRefVarsAnalysis analysis;

   private final List<RefIntPair> buildList(FlowSet set) {
      List<RefIntPair> lst = new ArrayList();
      Iterator it = this.analysis.refTypeValues.iterator();

      while(it.hasNext()) {
         EquivalentValue r = (EquivalentValue)it.next();
         int refInfo = this.analysis.refInfo(r, set);
         if (refInfo != 99) {
            lst.add(this.analysis.getKRefIntPair(r, refInfo));
         }
      }

      return lst;
   }

   public LocalRefVarsAnalysisWrapper(ExceptionalUnitGraph graph) {
      this.analysis = new BranchedRefVarsAnalysis(graph);
      this.unitToVarsBefore = new HashMap(graph.size() * 2 + 1, 0.7F);
      this.unitToVarsAfterFall = new HashMap(graph.size() * 2 + 1, 0.7F);
      this.unitToListsOfVarsAfterBranches = new HashMap(graph.size() * 2 + 1, 0.7F);
      this.unitToVarsNeedCheck = new HashMap(graph.size() * 2 + 1, 0.7F);
      this.unitToVarsDontNeedCheck = new HashMap(graph.size() * 2 + 1, 0.7F);
      Iterator unitIt = graph.iterator();

      while(unitIt.hasNext()) {
         Unit s = (Unit)unitIt.next();
         FlowSet set = (FlowSet)this.analysis.getFallFlowAfter(s);
         this.unitToVarsAfterFall.put(s, Collections.unmodifiableList(this.buildList(set)));
         List branchesFlowsets = this.analysis.getBranchFlowAfter(s);
         List<List<RefIntPair>> needCheckVars = new ArrayList(branchesFlowsets.size());
         Iterator it = branchesFlowsets.iterator();

         while(it.hasNext()) {
            set = (FlowSet)it.next();
            needCheckVars.add(Collections.unmodifiableList(this.buildList(set)));
         }

         this.unitToListsOfVarsAfterBranches.put(s, needCheckVars);
         set = (FlowSet)this.analysis.getFlowBefore(s);
         this.unitToVarsBefore.put(s, Collections.unmodifiableList(this.buildList(set)));
         ArrayList<RefIntPair> dontNeedCheckVars = new ArrayList();
         needCheckVars = new ArrayList();
         HashSet allChecksSet = new HashSet(5, 0.7F);
         allChecksSet.addAll((Collection)this.analysis.unitToArrayRefChecksSet.get(s));
         allChecksSet.addAll((Collection)this.analysis.unitToInstanceFieldRefChecksSet.get(s));
         allChecksSet.addAll((Collection)this.analysis.unitToInstanceInvokeExprChecksSet.get(s));
         allChecksSet.addAll((Collection)this.analysis.unitToLengthExprChecksSet.get(s));
         Iterator it = allChecksSet.iterator();

         while(it.hasNext()) {
            Value v = (Value)it.next();
            int vInfo = this.analysis.anyRefInfo(v, set);
            if (vInfo == 99) {
               needCheckVars.add(v);
            } else if (vInfo == 0) {
               needCheckVars.add(this.analysis.getKRefIntPair(new EquivalentValue(v), vInfo));
            } else {
               dontNeedCheckVars.add(this.analysis.getKRefIntPair(new EquivalentValue(v), vInfo));
            }
         }

         this.unitToVarsNeedCheck.put(s, Collections.unmodifiableList(needCheckVars));
         this.unitToVarsDontNeedCheck.put(s, Collections.unmodifiableList(dontNeedCheckVars));
      }

   }

   public List getVarsBefore(Unit s) {
      return (List)this.unitToVarsBefore.get(s);
   }

   public List getVarsAfterFall(Unit s) {
      return (List)this.unitToVarsAfterFall.get(s);
   }

   public List getListsOfVarsAfterBranch(Unit s) {
      return (List)this.unitToListsOfVarsAfterBranches.get(s);
   }

   public List getVarsNeedCheck(Unit s) {
      return (List)this.unitToVarsNeedCheck.get(s);
   }

   public List getVarsDontNeedCheck(Unit s) {
      return (List)this.unitToVarsDontNeedCheck.get(s);
   }
}
