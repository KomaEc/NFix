package soot.jimple.toolkits.pointer;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.EquivalentValue;
import soot.Local;
import soot.RefLikeType;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.CastExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.queue.QueueReader;

public class LocalMustAliasAnalysis extends ForwardFlowAnalysis<Unit, HashMap<Value, Integer>> {
   protected Set<Value> localsAndFieldRefs;
   protected transient Map<Value, Integer> rhsToNumber;
   protected transient Map<Unit, Map<Value, Integer>> mergePointToValueToNumber;
   protected int nextNumber;
   protected SootMethod container;

   public LocalMustAliasAnalysis(UnitGraph g) {
      this(g, false);
   }

   public LocalMustAliasAnalysis(UnitGraph g, boolean tryTrackFieldAssignments) {
      super(g);
      this.nextNumber = 1;
      this.container = g.getBody().getMethod();
      this.localsAndFieldRefs = new HashSet();
      Iterator var3 = g.getBody().getLocals().iterator();

      while(var3.hasNext()) {
         Local l = (Local)var3.next();
         if (l.getType() instanceof RefLikeType) {
            this.localsAndFieldRefs.add(l);
         }
      }

      if (tryTrackFieldAssignments) {
         this.localsAndFieldRefs.addAll(this.trackableFields());
      }

      this.rhsToNumber = new HashMap();
      this.mergePointToValueToNumber = new HashMap();
      this.doAnalysis();
      this.rhsToNumber = null;
      this.mergePointToValueToNumber = null;
   }

   private Set<Value> trackableFields() {
      Set<Value> usedFieldRefs = new HashSet();
      Iterator var2 = this.graph.iterator();

      Iterator var6;
      while(var2.hasNext()) {
         Unit unit = (Unit)var2.next();
         Stmt s = (Stmt)unit;
         List<ValueBox> useBoxes = s.getUseBoxes();
         var6 = useBoxes.iterator();

         while(var6.hasNext()) {
            ValueBox useBox = (ValueBox)var6.next();
            Value val = useBox.getValue();
            if (val instanceof FieldRef) {
               FieldRef fieldRef = (FieldRef)val;
               if (fieldRef.getType() instanceof RefLikeType) {
                  usedFieldRefs.add(new EquivalentValue(fieldRef));
               }
            }
         }
      }

      if (!usedFieldRefs.isEmpty()) {
         if (!Scene.v().hasCallGraph()) {
            throw new IllegalStateException("No call graph found!");
         } else {
            CallGraph cg = Scene.v().getCallGraph();
            ReachableMethods reachableMethods = new ReachableMethods(cg, Collections.singletonList(this.container));
            reachableMethods.update();
            QueueReader iterator = reachableMethods.listener();

            while(true) {
               SootMethod m;
               do {
                  do {
                     if (!iterator.hasNext()) {
                        return usedFieldRefs;
                     }

                     m = (SootMethod)iterator.next();
                  } while(!m.hasActiveBody());
               } while(m.getName().equals("<clinit>") && m.getDeclaringClass().equals(this.container.getDeclaringClass()));

               var6 = m.getActiveBody().getUnits().iterator();

               while(var6.hasNext()) {
                  Unit u = (Unit)var6.next();
                  List<ValueBox> defBoxes = u.getDefBoxes();
                  Iterator var18 = defBoxes.iterator();

                  while(var18.hasNext()) {
                     ValueBox defBox = (ValueBox)var18.next();
                     Value value = defBox.getValue();
                     if (value instanceof FieldRef) {
                        usedFieldRefs.remove(new EquivalentValue(value));
                     }
                  }
               }
            }
         }
      } else {
         return usedFieldRefs;
      }
   }

   protected void merge(Unit succUnit, HashMap<Value, Integer> inMap1, HashMap<Value, Integer> inMap2, HashMap<Value, Integer> outMap) {
      Iterator var5 = this.localsAndFieldRefs.iterator();

      while(var5.hasNext()) {
         Value l = (Value)var5.next();
         Integer i1 = (Integer)inMap1.get(l);
         Integer i2 = (Integer)inMap2.get(l);
         if (i1 == null) {
            outMap.put(l, i2);
         } else if (i2 == null) {
            outMap.put(l, i1);
         } else if (i1.equals(i2)) {
            outMap.put(l, i1);
         } else {
            Map<Value, Integer> valueToNumber = (Map)this.mergePointToValueToNumber.get(succUnit);
            Integer number = null;
            if (valueToNumber == null) {
               valueToNumber = new HashMap();
               this.mergePointToValueToNumber.put(succUnit, valueToNumber);
            } else {
               number = (Integer)((Map)valueToNumber).get(l);
            }

            if (number == null) {
               number = this.nextNumber++;
               ((Map)valueToNumber).put(l, number);
            }

            outMap.put(l, number);
         }
      }

   }

   protected void flowThrough(HashMap<Value, Integer> in, Unit u, HashMap<Value, Integer> out) {
      Stmt s = (Stmt)u;
      out.clear();
      out.putAll(in);
      if (s instanceof DefinitionStmt) {
         DefinitionStmt ds = (DefinitionStmt)s;
         Value lhs = ds.getLeftOp();
         Value rhs = ds.getRightOp();
         if (rhs instanceof CastExpr) {
            CastExpr castExpr = (CastExpr)rhs;
            rhs = castExpr.getOp();
         }

         if ((lhs instanceof Local || lhs instanceof FieldRef && this.localsAndFieldRefs.contains(new EquivalentValue(lhs))) && lhs.getType() instanceof RefLikeType) {
            if (rhs instanceof Local) {
               Integer val = (Integer)in.get(rhs);
               if (val != null) {
                  out.put(lhs, val);
               }
            } else if (rhs instanceof ThisRef) {
               out.put(lhs, thisRefNumber());
            } else if (rhs instanceof ParameterRef) {
               out.put(lhs, parameterRefNumber((ParameterRef)rhs));
            } else {
               out.put(lhs, this.numberOfRhs(rhs));
            }
         }
      } else {
         assert s.getDefBoxes().isEmpty();
      }

   }

   private Integer numberOfRhs(Value rhs) {
      EquivalentValue equivValue = new EquivalentValue((Value)rhs);
      if (this.localsAndFieldRefs.contains(equivValue)) {
         rhs = equivValue;
      }

      Integer num = (Integer)this.rhsToNumber.get(rhs);
      if (num == null) {
         num = this.nextNumber++;
         this.rhsToNumber.put(rhs, num);
      }

      return num;
   }

   public static int thisRefNumber() {
      return 0;
   }

   public static int parameterRefNumber(ParameterRef r) {
      return -1 - r.getIndex();
   }

   protected void copy(HashMap<Value, Integer> sourceMap, HashMap<Value, Integer> destMap) {
      destMap.clear();
      destMap.putAll(sourceMap);
   }

   protected HashMap<Value, Integer> entryInitialFlow() {
      return new HashMap();
   }

   protected HashMap<Value, Integer> newInitialFlow() {
      return new HashMap();
   }

   public String instanceKeyString(Local l, Stmt s) {
      Object ln = ((HashMap)this.getFlowBefore(s)).get(l);
      return ln == null ? null : ln.toString();
   }

   public boolean hasInfoOn(Local l, Stmt s) {
      HashMap<Value, Integer> flowBefore = (HashMap)this.getFlowBefore(s);
      return flowBefore != null;
   }

   public boolean mustAlias(Local l1, Stmt s1, Local l2, Stmt s2) {
      Object l1n = ((HashMap)this.getFlowBefore(s1)).get(l1);
      Object l2n = ((HashMap)this.getFlowBefore(s2)).get(l2);
      if (l1n != null && l2n != null) {
         return l1n == l2n;
      } else {
         return false;
      }
   }

   protected void merge(HashMap<Value, Integer> in1, HashMap<Value, Integer> in2, HashMap<Value, Integer> out) {
      out.putAll(in1);
      Iterator var4 = in2.keySet().iterator();

      while(var4.hasNext()) {
         Value val = (Value)var4.next();
         Integer i1 = (Integer)in1.get(val);
         Integer i2 = (Integer)in2.get(val);
         if (!i2.equals(i1)) {
            throw new RuntimeException("Merge of different IDs not supported");
         }

         out.put(val, i2);
      }

   }
}
