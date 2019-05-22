package soot.jimple.toolkits.pointer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.Local;
import soot.RefLikeType;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.toolkits.graph.StronglyConnectedComponentsFast;
import soot.toolkits.graph.UnitGraph;

public class StrongLocalMustAliasAnalysis extends LocalMustAliasAnalysis {
   protected Set<Integer> invalidInstanceKeys = new HashSet();

   public StrongLocalMustAliasAnalysis(UnitGraph g) {
      super(g);
      StronglyConnectedComponentsFast<Unit> sccAnalysis = new StronglyConnectedComponentsFast(g);
      Iterator var3 = sccAnalysis.getTrueComponents().iterator();

      while(var3.hasNext()) {
         List<Unit> scc = (List)var3.next();
         Iterator var5 = scc.iterator();

         while(var5.hasNext()) {
            Unit unit = (Unit)var5.next();
            Iterator var7 = unit.getDefBoxes().iterator();

            while(var7.hasNext()) {
               ValueBox vb = (ValueBox)var7.next();
               Value defValue = vb.getValue();
               if (defValue instanceof Local) {
                  Local defLocal = (Local)defValue;
                  if (defLocal.getType() instanceof RefLikeType) {
                     Object instanceKey = ((HashMap)this.getFlowBefore(unit)).get(defLocal);
                     Integer intKey;
                     if (instanceKey instanceof Integer) {
                        intKey = (Integer)instanceKey;
                        this.invalidInstanceKeys.add(intKey);
                     }

                     instanceKey = ((HashMap)this.getFlowAfter(unit)).get(defLocal);
                     if (instanceKey instanceof Integer) {
                        intKey = (Integer)instanceKey;
                        this.invalidInstanceKeys.add(intKey);
                     }
                  }
               }
            }
         }
      }

   }

   public boolean mustAlias(Local l1, Stmt s1, Local l2, Stmt s2) {
      Object l1n = ((HashMap)this.getFlowBefore(s1)).get(l1);
      Object l2n = ((HashMap)this.getFlowBefore(s2)).get(l2);
      if (l1n != null && l2n != null && !this.invalidInstanceKeys.contains(l1n) && !this.invalidInstanceKeys.contains(l2n)) {
         return l1n == l2n;
      } else {
         return false;
      }
   }

   public String instanceKeyString(Local l, Stmt s) {
      Object ln = ((HashMap)this.getFlowBefore(s)).get(l);
      return this.invalidInstanceKeys.contains(ln) ? "UNKNOWN" : super.instanceKeyString(l, s);
   }
}
