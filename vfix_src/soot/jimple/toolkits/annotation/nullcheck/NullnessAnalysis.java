package soot.jimple.toolkits.annotation.nullcheck;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.Immediate;
import soot.Local;
import soot.RefLikeType;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.MonitorStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.jimple.internal.AbstractBinopExpr;
import soot.jimple.internal.JCastExpr;
import soot.jimple.internal.JEqExpr;
import soot.jimple.internal.JIfStmt;
import soot.jimple.internal.JInstanceOfExpr;
import soot.jimple.internal.JNeExpr;
import soot.shimple.PhiExpr;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardBranchedFlowAnalysis;

public class NullnessAnalysis extends ForwardBranchedFlowAnalysis<NullnessAnalysis.AnalysisInfo> {
   protected static final int BOTTOM = 0;
   protected static final int NULL = 1;
   protected static final int NON_NULL = 2;
   protected static final int TOP = 3;
   protected final HashMap<Value, Integer> valueToIndex = new HashMap();
   protected int used = 0;

   public NullnessAnalysis(UnitGraph graph) {
      super(graph);
      this.doAnalysis();
   }

   protected void flowThrough(NullnessAnalysis.AnalysisInfo in, Unit u, List<NullnessAnalysis.AnalysisInfo> fallOut, List<NullnessAnalysis.AnalysisInfo> branchOuts) {
      NullnessAnalysis.AnalysisInfo out = new NullnessAnalysis.AnalysisInfo(in);
      NullnessAnalysis.AnalysisInfo outBranch = new NullnessAnalysis.AnalysisInfo(in);
      Stmt s = (Stmt)u;
      if (s instanceof JIfStmt) {
         JIfStmt ifStmt = (JIfStmt)s;
         this.handleIfStmt(ifStmt, in, out, outBranch);
      } else if (s instanceof MonitorStmt) {
         MonitorStmt monitorStmt = (MonitorStmt)s;
         out.put(monitorStmt.getOp(), 2);
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

      if (s instanceof DefinitionStmt) {
         DefinitionStmt defStmt = (DefinitionStmt)s;
         if (defStmt.getLeftOp().getType() instanceof RefLikeType) {
            this.handleRefTypeAssignment(defStmt, out);
         }
      }

      Iterator it = fallOut.iterator();

      while(it.hasNext()) {
         this.copy(out, (NullnessAnalysis.AnalysisInfo)it.next());
      }

      it = branchOuts.iterator();

      while(it.hasNext()) {
         this.copy(outBranch, (NullnessAnalysis.AnalysisInfo)it.next());
      }

   }

   protected boolean isAlwaysNonNull(Value v) {
      return false;
   }

   private void handleIfStmt(JIfStmt ifStmt, NullnessAnalysis.AnalysisInfo in, NullnessAnalysis.AnalysisInfo out, NullnessAnalysis.AnalysisInfo outBranch) {
      Value condition = ifStmt.getCondition();
      if (condition instanceof JInstanceOfExpr) {
         JInstanceOfExpr expr = (JInstanceOfExpr)condition;
         this.handleInstanceOfExpression(expr, in, out, outBranch);
      } else if (condition instanceof JEqExpr || condition instanceof JNeExpr) {
         AbstractBinopExpr eqExpr = (AbstractBinopExpr)condition;
         this.handleEqualityOrNonEqualityCheck(eqExpr, in, out, outBranch);
      }

   }

   private void handleEqualityOrNonEqualityCheck(AbstractBinopExpr eqExpr, NullnessAnalysis.AnalysisInfo in, NullnessAnalysis.AnalysisInfo out, NullnessAnalysis.AnalysisInfo outBranch) {
      Value left = eqExpr.getOp1();
      Value right = eqExpr.getOp2();
      Value val = null;
      if (left == NullConstant.v()) {
         if (right != NullConstant.v()) {
            val = right;
         }
      } else if (right == NullConstant.v() && left != NullConstant.v()) {
         val = left;
      }

      if (val != null && val instanceof Local) {
         if (eqExpr instanceof JEqExpr) {
            this.handleEquality(val, out, outBranch);
         } else {
            if (!(eqExpr instanceof JNeExpr)) {
               throw new IllegalStateException("unexpected condition: " + eqExpr.getClass());
            }

            this.handleNonEquality(val, out, outBranch);
         }
      }

   }

   private void handleNonEquality(Value val, NullnessAnalysis.AnalysisInfo out, NullnessAnalysis.AnalysisInfo outBranch) {
      out.put(val, 1);
      outBranch.put(val, 2);
   }

   private void handleEquality(Value val, NullnessAnalysis.AnalysisInfo out, NullnessAnalysis.AnalysisInfo outBranch) {
      out.put(val, 2);
      outBranch.put(val, 1);
   }

   private void handleInstanceOfExpression(JInstanceOfExpr expr, NullnessAnalysis.AnalysisInfo in, NullnessAnalysis.AnalysisInfo out, NullnessAnalysis.AnalysisInfo outBranch) {
      Value op = expr.getOp();
      outBranch.put(op, 2);
   }

   private void handleArrayRef(ArrayRef arrayRef, NullnessAnalysis.AnalysisInfo out) {
      Value array = arrayRef.getBase();
      out.put(array, 2);
   }

   private void handleFieldRef(FieldRef fieldRef, NullnessAnalysis.AnalysisInfo out) {
      if (fieldRef instanceof InstanceFieldRef) {
         InstanceFieldRef instanceFieldRef = (InstanceFieldRef)fieldRef;
         Value base = instanceFieldRef.getBase();
         out.put(base, 2);
      }

   }

   private void handleInvokeExpr(InvokeExpr invokeExpr, NullnessAnalysis.AnalysisInfo out) {
      if (invokeExpr instanceof InstanceInvokeExpr) {
         InstanceInvokeExpr instanceInvokeExpr = (InstanceInvokeExpr)invokeExpr;
         Value base = instanceInvokeExpr.getBase();
         out.put(base, 2);
      }

   }

   private void handleRefTypeAssignment(DefinitionStmt assignStmt, NullnessAnalysis.AnalysisInfo out) {
      Value left = assignStmt.getLeftOp();
      Value right = assignStmt.getRightOp();
      if (right instanceof JCastExpr) {
         JCastExpr castExpr = (JCastExpr)right;
         right = castExpr.getOp();
      }

      if (!this.isAlwaysNonNull(right) && !(right instanceof NewExpr) && !(right instanceof NewArrayExpr) && !(right instanceof NewMultiArrayExpr) && !(right instanceof ThisRef) && !(right instanceof StringConstant) && !(right instanceof ClassConstant) && !(right instanceof CaughtExceptionRef)) {
         if (right == NullConstant.v()) {
            out.put(left, 1);
         } else if (left instanceof Local && right instanceof Local) {
            out.put(left, out.get(right));
         } else if (left instanceof Local && right instanceof PhiExpr) {
            this.handlePhiExpr(out, left, (PhiExpr)right);
         } else {
            out.put(left, 3);
         }
      } else {
         out.put(left, 2);
      }

   }

   private void handlePhiExpr(NullnessAnalysis.AnalysisInfo out, Value left, PhiExpr right) {
      int curr = 0;
      Iterator var5 = right.getValues().iterator();

      while(var5.hasNext()) {
         Value v = (Value)var5.next();
         int nullness = out.get(v);
         if (nullness != 0) {
            if (nullness == 3) {
               out.put(left, 3);
               return;
            }

            if (nullness == 1) {
               if (curr == 0) {
                  curr = 1;
               } else if (curr != 1) {
                  out.put(left, 3);
                  return;
               }
            } else if (nullness == 2) {
               if (curr == 0) {
                  curr = 2;
               } else if (curr != 2) {
                  out.put(left, 3);
                  return;
               }
            }
         }
      }

      out.put(left, curr);
   }

   protected void copy(NullnessAnalysis.AnalysisInfo s, NullnessAnalysis.AnalysisInfo d) {
      d.clear();
      d.or(s);
   }

   protected void merge(NullnessAnalysis.AnalysisInfo in1, NullnessAnalysis.AnalysisInfo in2, NullnessAnalysis.AnalysisInfo out) {
      out.clear();
      out.or(in1);
      out.or(in2);
   }

   protected NullnessAnalysis.AnalysisInfo newInitialFlow() {
      return new NullnessAnalysis.AnalysisInfo();
   }

   public boolean isAlwaysNullBefore(Unit s, Immediate i) {
      return ((NullnessAnalysis.AnalysisInfo)this.getFlowBefore(s)).get(i) == 1;
   }

   public boolean isAlwaysNonNullBefore(Unit s, Immediate i) {
      return ((NullnessAnalysis.AnalysisInfo)this.getFlowBefore(s)).get(i) == 2;
   }

   protected class AnalysisInfo extends BitSet {
      private static final long serialVersionUID = -9200043127757823764L;

      public AnalysisInfo() {
         super(NullnessAnalysis.this.used);
      }

      public AnalysisInfo(NullnessAnalysis.AnalysisInfo other) {
         super(NullnessAnalysis.this.used);
         this.or(other);
      }

      public int get(Value key) {
         if (!NullnessAnalysis.this.valueToIndex.containsKey(key)) {
            return 0;
         } else {
            int index = (Integer)NullnessAnalysis.this.valueToIndex.get(key);
            int result = this.get(index) ? 2 : 0;
            result += this.get(index + 1) ? 1 : 0;
            return result;
         }
      }

      public void put(Value key, int val) {
         int index;
         if (!NullnessAnalysis.this.valueToIndex.containsKey(key)) {
            index = NullnessAnalysis.this.used;
            NullnessAnalysis var10000 = NullnessAnalysis.this;
            var10000.used += 2;
            NullnessAnalysis.this.valueToIndex.put(key, index);
         } else {
            index = (Integer)NullnessAnalysis.this.valueToIndex.get(key);
         }

         this.set(index, (val & 2) == 2);
         this.set(index + 1, (val & 1) == 1);
      }
   }
}
