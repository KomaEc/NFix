package soot.jimple.toolkits.annotation.nullcheck;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.Immediate;
import soot.Local;
import soot.RefLikeType;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.MonitorStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JCastExpr;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;

public class NullnessAssumptionAnalysis extends BackwardFlowAnalysis {
   protected static final Object BOTTOM = new Object() {
      public String toString() {
         return "bottom";
      }
   };
   protected static final Object NULL = new Object() {
      public String toString() {
         return "null";
      }
   };
   protected static final Object NON_NULL = new Object() {
      public String toString() {
         return "non-null";
      }
   };
   protected static final Object TOP = new Object() {
      public String toString() {
         return "top";
      }
   };

   public NullnessAssumptionAnalysis(UnitGraph graph) {
      super(graph);
      this.doAnalysis();
   }

   protected void flowThrough(Object inValue, Object unit, Object outValue) {
      NullnessAssumptionAnalysis.AnalysisInfo in = (NullnessAssumptionAnalysis.AnalysisInfo)inValue;
      NullnessAssumptionAnalysis.AnalysisInfo out = new NullnessAssumptionAnalysis.AnalysisInfo(in);
      Stmt s = (Stmt)unit;
      if (s instanceof MonitorStmt) {
         MonitorStmt monitorStmt = (MonitorStmt)s;
         out.put(monitorStmt.getOp(), NON_NULL);
      }

      if (s.containsArrayRef()) {
         ArrayRef arrayRef = s.getArrayRef();
         this.handleArrayRef(arrayRef, out);
      }

      if (s.containsFieldRef()) {
         FieldRef fieldRef = s.getFieldRef();
         this.handleFieldRef(fieldRef, out);
      }

      if (s.containsInvokeExpr()) {
         InvokeExpr invokeExpr = s.getInvokeExpr();
         this.handleInvokeExpr(invokeExpr, out);
      }

      Iterator outIter = out.entrySet().iterator();

      while(outIter.hasNext()) {
         java.util.Map.Entry entry = (java.util.Map.Entry)outIter.next();
         Value v = (Value)entry.getKey();
         if (this.isAlwaysNonNull(v)) {
            entry.setValue(NON_NULL);
         }
      }

      if (s instanceof DefinitionStmt) {
         NullnessAssumptionAnalysis.AnalysisInfo temp = new NullnessAssumptionAnalysis.AnalysisInfo(out);
         DefinitionStmt defStmt = (DefinitionStmt)s;
         if (defStmt.getLeftOp().getType() instanceof RefLikeType) {
            this.handleRefTypeAssignment(defStmt, temp, out);
         }
      }

      outIter = out.keySet().iterator();

      while(outIter.hasNext()) {
         Value v = (Value)outIter.next();
         if (!(v instanceof Local)) {
            outIter.remove();
         }
      }

      this.copy(out, outValue);
   }

   protected boolean isAlwaysNonNull(Value v) {
      return false;
   }

   private void handleArrayRef(ArrayRef arrayRef, NullnessAssumptionAnalysis.AnalysisInfo out) {
      Value array = arrayRef.getBase();
      out.put(array, NON_NULL);
   }

   private void handleFieldRef(FieldRef fieldRef, NullnessAssumptionAnalysis.AnalysisInfo out) {
      if (fieldRef instanceof InstanceFieldRef) {
         InstanceFieldRef instanceFieldRef = (InstanceFieldRef)fieldRef;
         Value base = instanceFieldRef.getBase();
         out.put(base, NON_NULL);
      }

   }

   private void handleInvokeExpr(InvokeExpr invokeExpr, NullnessAssumptionAnalysis.AnalysisInfo out) {
      if (invokeExpr instanceof InstanceInvokeExpr) {
         InstanceInvokeExpr instanceInvokeExpr = (InstanceInvokeExpr)invokeExpr;
         Value base = instanceInvokeExpr.getBase();
         out.put(base, NON_NULL);
      }

   }

   private void handleRefTypeAssignment(DefinitionStmt assignStmt, NullnessAssumptionAnalysis.AnalysisInfo rhsInfo, NullnessAssumptionAnalysis.AnalysisInfo out) {
      Value left = assignStmt.getLeftOp();
      Value right = assignStmt.getRightOp();
      if (right instanceof JCastExpr) {
         JCastExpr castExpr = (JCastExpr)right;
         right = castExpr.getOp();
      }

      rhsInfo.put(right, BOTTOM);
      out.put(left, rhsInfo.get(right));
   }

   protected void copy(Object source, Object dest) {
      Map s = (Map)source;
      Map d = (Map)dest;
      d.clear();
      d.putAll(s);
   }

   protected Object entryInitialFlow() {
      return new NullnessAssumptionAnalysis.AnalysisInfo();
   }

   protected void merge(Object in1, Object in2, Object out) {
      NullnessAssumptionAnalysis.AnalysisInfo left = (NullnessAssumptionAnalysis.AnalysisInfo)in1;
      NullnessAssumptionAnalysis.AnalysisInfo right = (NullnessAssumptionAnalysis.AnalysisInfo)in2;
      NullnessAssumptionAnalysis.AnalysisInfo res = (NullnessAssumptionAnalysis.AnalysisInfo)out;
      Set values = new HashSet();
      values.addAll(left.keySet());
      values.addAll(right.keySet());
      res.clear();

      Value v;
      Object result;
      for(Iterator keyIter = values.iterator(); keyIter.hasNext(); res.put(v, result)) {
         v = (Value)keyIter.next();
         Set<Object> leftAndRight = new HashSet();
         leftAndRight.add(left.get(v));
         leftAndRight.add(right.get(v));
         if (leftAndRight.contains(BOTTOM)) {
            result = BOTTOM;
         } else if (leftAndRight.contains(NON_NULL)) {
            if (leftAndRight.contains(NULL)) {
               result = BOTTOM;
            } else {
               result = NON_NULL;
            }
         } else if (leftAndRight.contains(NULL)) {
            result = NULL;
         } else {
            result = BOTTOM;
         }
      }

   }

   protected Object newInitialFlow() {
      return new NullnessAssumptionAnalysis.AnalysisInfo();
   }

   public boolean isAssumedNullBefore(Unit s, Immediate i) {
      NullnessAssumptionAnalysis.AnalysisInfo ai = (NullnessAssumptionAnalysis.AnalysisInfo)this.getFlowBefore(s);
      return ai.get(i) == NULL;
   }

   public boolean isAssumedNonNullBefore(Unit s, Immediate i) {
      NullnessAssumptionAnalysis.AnalysisInfo ai = (NullnessAssumptionAnalysis.AnalysisInfo)this.getFlowBefore(s);
      return ai.get(i) == NON_NULL;
   }

   protected static class AnalysisInfo extends HashMap {
      public AnalysisInfo() {
      }

      public AnalysisInfo(Map m) {
         super(m);
      }

      public Object get(Object key) {
         Object object = super.get(key);
         return object == null ? NullnessAssumptionAnalysis.BOTTOM : object;
      }
   }
}
