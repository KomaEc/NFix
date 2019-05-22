package soot.jimple.toolkits.pointer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.Body;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class LocalMayAliasAnalysis extends ForwardFlowAnalysis<Unit, Set<Set<Value>>> {
   private Body body;

   public LocalMayAliasAnalysis(UnitGraph graph) {
      super(graph);
      this.body = graph.getBody();
      this.doAnalysis();
   }

   protected void flowThrough(Set<Set<Value>> source, Unit unit, Set<Set<Value>> target) {
      target.addAll(source);
      if (unit instanceof DefinitionStmt) {
         DefinitionStmt def = (DefinitionStmt)unit;
         Value left = def.getLeftOp();
         Value right = def.getRightOp();
         Set leftSet;
         if (right instanceof Constant) {
            leftSet = null;
            Iterator var11 = source.iterator();

            while(var11.hasNext()) {
               Set<Value> s = (Set)var11.next();
               if (s.contains(left)) {
                  leftSet = s;
                  break;
               }
            }

            if (leftSet == null) {
               throw new RuntimeException("internal error");
            }

            target.remove(leftSet);
            HashSet<Value> setWithoutLeft = new HashSet(leftSet);
            setWithoutLeft.remove(left);
            target.add(setWithoutLeft);
            target.add(Collections.singleton(left));
         } else {
            leftSet = null;
            Set<Value> rightSet = null;
            Iterator var9 = source.iterator();

            Set s;
            while(var9.hasNext()) {
               s = (Set)var9.next();
               if (s.contains(left)) {
                  leftSet = s;
                  break;
               }
            }

            var9 = source.iterator();

            while(true) {
               if (var9.hasNext()) {
                  s = (Set)var9.next();
                  if (!s.contains(right)) {
                     continue;
                  }

                  rightSet = s;
               }

               if (leftSet == null || rightSet == null) {
                  throw new RuntimeException("internal error");
               }

               target.remove(leftSet);
               target.remove(rightSet);
               HashSet<Value> union = new HashSet(leftSet);
               union.addAll(rightSet);
               target.add(union);
               break;
            }
         }
      }

   }

   protected void copy(Set<Set<Value>> source, Set<Set<Value>> target) {
      target.clear();
      target.addAll(source);
   }

   protected Set<Set<Value>> entryInitialFlow() {
      Set<Set<Value>> res = new HashSet();
      Iterator var2 = this.body.getUseAndDefBoxes().iterator();

      while(var2.hasNext()) {
         ValueBox vb = (ValueBox)var2.next();
         res.add(Collections.singleton(vb.getValue()));
      }

      return res;
   }

   protected void merge(Set<Set<Value>> source1, Set<Set<Value>> source2, Set<Set<Value>> target) {
      target.clear();
      target.addAll(source1);
      target.addAll(source2);
   }

   protected Set<Set<Value>> newInitialFlow() {
      return new HashSet();
   }

   public boolean mayAlias(Value v1, Value v2, Unit u) {
      Set<Set<Value>> res = (Set)this.getFlowBefore(u);
      Iterator var5 = res.iterator();

      Set set;
      do {
         if (!var5.hasNext()) {
            return false;
         }

         set = (Set)var5.next();
      } while(!set.contains(v1) || !set.contains(v2));

      return true;
   }

   public Set<Value> mayAliases(Value v, Unit u) {
      Set<Value> res = new HashSet();
      Set<Set<Value>> flow = (Set)this.getFlowBefore(u);
      Iterator var5 = flow.iterator();

      while(var5.hasNext()) {
         Set<Value> set = (Set)var5.next();
         if (set.contains(v)) {
            res.addAll(set);
         }
      }

      return res;
   }

   public Set<Value> mayAliasesAtExit(Value v) {
      Set<Value> res = new HashSet();
      Iterator var3 = this.graph.getTails().iterator();

      while(var3.hasNext()) {
         Unit u = (Unit)var3.next();
         Set<Set<Value>> flow = (Set)this.getFlowAfter(u);
         Iterator var6 = flow.iterator();

         while(var6.hasNext()) {
            Set<Value> set = (Set)var6.next();
            if (set.contains(v)) {
               res.addAll(set);
            }
         }
      }

      return res;
   }
}
