package soot.jimple.toolkits.callgraph;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.Local;
import soot.NullType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.shimple.PhiExpr;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class ConstantArrayAnalysis extends ForwardFlowAnalysis<Unit, ConstantArrayAnalysis.ArrayState> {
   private Map<Local, Integer> localToInt = new HashMap();
   private Map<Type, Integer> typeToInt = new HashMap();
   private Map<Integer, Integer> sizeToInt = new HashMap();
   private Map<Integer, Type> rvTypeToInt = new HashMap();
   private Map<Integer, Integer> rvSizeToInt = new HashMap();
   private int size;
   private int typeSize;
   private int szSize;

   public ConstantArrayAnalysis(DirectedGraph<Unit> graph, Body b) {
      super(graph);
      Iterator var3 = b.getLocals().iterator();

      while(var3.hasNext()) {
         Local l = (Local)var3.next();
         this.localToInt.put(l, this.size++);
      }

      var3 = b.getUnits().iterator();

      while(var3.hasNext()) {
         Unit u = (Unit)var3.next();
         Stmt s = (Stmt)u;
         if (s instanceof DefinitionStmt) {
            Type ty = ((DefinitionStmt)s).getRightOp().getType();
            if (!this.typeToInt.containsKey(ty)) {
               int key = this.typeSize++;
               this.typeToInt.put(ty, key);
               this.rvTypeToInt.put(key, ty);
            }

            if (((DefinitionStmt)s).getRightOp() instanceof NewArrayExpr) {
               NewArrayExpr nae = (NewArrayExpr)((DefinitionStmt)s).getRightOp();
               if (nae.getSize() instanceof IntConstant) {
                  int sz = ((IntConstant)nae.getSize()).value;
                  if (!this.sizeToInt.containsKey(sz)) {
                     int key = this.szSize++;
                     this.sizeToInt.put(sz, key);
                     this.rvSizeToInt.put(key, sz);
                  }
               }
            }
         }
      }

      this.doAnalysis();
   }

   protected void flowThrough(ConstantArrayAnalysis.ArrayState in, Unit d, ConstantArrayAnalysis.ArrayState out) {
      out.active.clear();
      out.active.or(in.active);
      out.state = (ConstantArrayAnalysis.ArrayTypesInternal[])Arrays.copyOf(in.state, in.state.length);
      if (d instanceof DefinitionStmt) {
         DefinitionStmt ds = (DefinitionStmt)d;
         Value rhs = ds.getRightOp();
         Value lhs = ds.getLeftOp();
         int localRef;
         int index;
         int localRef;
         int lhsRef;
         if (rhs instanceof NewArrayExpr) {
            Local l = (Local)lhs;
            lhsRef = (Integer)this.localToInt.get(l);
            NewArrayExpr nae = (NewArrayExpr)rhs;
            out.active.set(lhsRef);
            if (!(nae.getSize() instanceof IntConstant)) {
               out.state[lhsRef] = null;
            } else {
               int arraySize = ((IntConstant)nae.getSize()).value;
               out.state[lhsRef] = new ConstantArrayAnalysis.ArrayTypesInternal();
               out.state[lhsRef].sizeState.set((Integer)this.sizeToInt.get(arraySize));
               out.state[lhsRef].typeState = new BitSet[arraySize];
               out.state[lhsRef].mustAssign = new BitSet(arraySize);

               for(index = 0; index < arraySize; ++index) {
                  out.state[lhsRef].typeState[index] = new BitSet(this.typeSize);
               }
            }
         } else if (lhs instanceof Local && lhs.getType() instanceof ArrayType && rhs instanceof NullConstant) {
            localRef = (Integer)this.localToInt.get(lhs);
            out.active.clear(localRef);
            out.state[localRef] = null;
         } else if (lhs instanceof Local && rhs instanceof Local && in.state[(Integer)this.localToInt.get(rhs)] != null && in.active.get((Integer)this.localToInt.get(rhs))) {
            localRef = (Integer)this.localToInt.get(lhs);
            lhsRef = (Integer)this.localToInt.get(rhs);
            out.active.set(localRef);
            out.state[localRef] = in.state[lhsRef];
            out.state[lhsRef] = null;
         } else if (lhs instanceof Local && rhs instanceof PhiExpr) {
            PhiExpr rPhi = (PhiExpr)rhs;
            lhsRef = (Integer)this.localToInt.get(lhs);
            out.state[lhsRef] = null;
            localRef = 0;

            List phiValues;
            for(phiValues = rPhi.getValues(); localRef < phiValues.size(); ++localRef) {
               Value v = (Value)phiValues.get(localRef);
               int argRef = (Integer)this.localToInt.get(v);
               if (in.active.get(argRef)) {
                  out.active.set(lhsRef);
                  if (in.state[argRef] == null) {
                     out.state[lhsRef] = null;
                     break;
                  }

                  if (out.state[lhsRef] == null) {
                     out.state[lhsRef] = in.state[argRef];
                  } else {
                     out.state[lhsRef] = this.mergeTypeStates(in.state[argRef], out.state[lhsRef]);
                  }

                  out.state[argRef] = null;
               }
            }

            while(localRef < phiValues.size()) {
               index = (Integer)this.localToInt.get(phiValues.get(localRef));
               out.state[index] = null;
               ++localRef;
            }
         } else if (lhs instanceof ArrayRef) {
            ArrayRef ar = (ArrayRef)lhs;
            Value indexVal = ar.getIndex();
            localRef = (Integer)this.localToInt.get(ar.getBase());
            if (!(indexVal instanceof IntConstant)) {
               out.state[localRef] = null;
               out.active.set(localRef);
            } else if (out.state[localRef] != null) {
               Type assignType = rhs.getType();
               index = ((IntConstant)indexVal).value;

               assert index < out.state[localRef].typeState.length;

               out.deepCloneLocalValueSlot(localRef, index);

               assert out.state[localRef].typeState[index] != null : d;

               out.state[localRef].typeState[index].set((Integer)this.typeToInt.get(assignType));
               out.state[localRef].mustAssign.set(index);
            }
         } else if (lhs instanceof Local) {
            Local defLocal = (Local)lhs;
            localRef = (Integer)this.localToInt.get(defLocal);
            out.active.set(localRef);
            out.state[localRef] = null;
         }

         Iterator var20 = rhs.getUseBoxes().iterator();

         while(var20.hasNext()) {
            ValueBox b = (ValueBox)var20.next();
            if (this.localToInt.containsKey(b.getValue())) {
               localRef = (Integer)this.localToInt.get(b.getValue());
               out.state[localRef] = null;
               out.active.set(localRef);
            }
         }

         if (this.localToInt.containsKey(rhs)) {
            localRef = (Integer)this.localToInt.get(rhs);
            out.state[localRef] = null;
            out.active.set(localRef);
         }
      } else {
         Iterator var13 = d.getUseBoxes().iterator();

         while(var13.hasNext()) {
            ValueBox b = (ValueBox)var13.next();
            if (this.localToInt.containsKey(b.getValue())) {
               int localRef = (Integer)this.localToInt.get(b.getValue());
               out.state[localRef] = null;
               out.active.set(localRef);
            }
         }
      }

   }

   protected ConstantArrayAnalysis.ArrayState newInitialFlow() {
      return new ConstantArrayAnalysis.ArrayState();
   }

   protected void merge(ConstantArrayAnalysis.ArrayState in1, ConstantArrayAnalysis.ArrayState in2, ConstantArrayAnalysis.ArrayState out) {
      out.active.clear();
      out.active.or(in1.active);
      out.active.or(in2.active);
      BitSet in2_excl = (BitSet)in2.active.clone();
      in2_excl.andNot(in1.active);

      int i;
      for(i = in1.active.nextSetBit(0); i >= 0; i = in1.active.nextSetBit(i + 1)) {
         if (in1.state[i] == null) {
            out.state[i] = null;
         } else if (in2.active.get(i)) {
            if (in2.state[i] == null) {
               out.state[i] = null;
            } else {
               out.state[i] = this.mergeTypeStates(in1.state[i], in2.state[i]);
            }
         } else {
            out.state[i] = in1.state[i];
         }
      }

      for(i = in2_excl.nextSetBit(0); i >= 0; i = in2_excl.nextSetBit(i + 1)) {
         out.state[i] = in2.state[i];
      }

   }

   private ConstantArrayAnalysis.ArrayTypesInternal mergeTypeStates(ConstantArrayAnalysis.ArrayTypesInternal a1, ConstantArrayAnalysis.ArrayTypesInternal a2) {
      assert a1 != null && a2 != null;

      ConstantArrayAnalysis.ArrayTypesInternal toRet = new ConstantArrayAnalysis.ArrayTypesInternal();
      toRet.sizeState.or(a1.sizeState);
      toRet.sizeState.or(a2.sizeState);
      int maxSize = Math.max(a1.typeState.length, a2.typeState.length);
      int commonSize = Math.min(a1.typeState.length, a2.typeState.length);
      toRet.mustAssign = new BitSet(maxSize);
      toRet.typeState = new BitSet[maxSize];

      int i;
      for(i = 0; i < commonSize; ++i) {
         toRet.typeState[i] = new BitSet(this.typeSize);
         toRet.typeState[i].or(a1.typeState[i]);
         toRet.typeState[i].or(a2.typeState[i]);
         toRet.mustAssign.set(i, a1.mustAssign.get(i) && a2.mustAssign.get(i));
      }

      for(i = commonSize; i < maxSize; ++i) {
         if (a1.typeState.length > i) {
            toRet.typeState[i] = (BitSet)a1.typeState[i].clone();
            toRet.mustAssign.set(i, a1.mustAssign.get(i));
         } else {
            toRet.mustAssign.set(i, a2.mustAssign.get(i));
            toRet.typeState[i] = (BitSet)a2.typeState[i].clone();
         }
      }

      return toRet;
   }

   protected void copy(ConstantArrayAnalysis.ArrayState source, ConstantArrayAnalysis.ArrayState dest) {
      dest.active = source.active;
      dest.state = source.state;
   }

   public boolean isConstantBefore(Stmt s, Local arrayLocal) {
      ConstantArrayAnalysis.ArrayState flowResults = (ConstantArrayAnalysis.ArrayState)this.getFlowBefore(s);
      int varRef = (Integer)this.localToInt.get(arrayLocal);
      return flowResults.active.get(varRef) && flowResults.state[varRef] != null;
   }

   public ConstantArrayAnalysis.ArrayTypes getArrayTypesBefore(Stmt s, Local arrayLocal) {
      if (!this.isConstantBefore(s, arrayLocal)) {
         return null;
      } else {
         ConstantArrayAnalysis.ArrayTypes toRet = new ConstantArrayAnalysis.ArrayTypes();
         int varRef = (Integer)this.localToInt.get(arrayLocal);
         ConstantArrayAnalysis.ArrayTypesInternal ati = ((ConstantArrayAnalysis.ArrayState)this.getFlowBefore(s)).state[varRef];
         toRet.possibleSizes = new HashSet();
         toRet.possibleTypes = new Set[ati.typeState.length];

         int i;
         for(i = ati.sizeState.nextSetBit(0); i >= 0; i = ati.sizeState.nextSetBit(i + 1)) {
            toRet.possibleSizes.add(this.rvSizeToInt.get(i));
         }

         for(i = 0; i < toRet.possibleTypes.length; ++i) {
            toRet.possibleTypes[i] = new HashSet();

            for(int j = ati.typeState[i].nextSetBit(0); j >= 0; j = ati.typeState[i].nextSetBit(j + 1)) {
               toRet.possibleTypes[i].add(this.rvTypeToInt.get(j));
            }

            if (!ati.mustAssign.get(i)) {
               toRet.possibleTypes[i].add(NullType.v());
            }
         }

         return toRet;
      }
   }

   public class ArrayState {
      ConstantArrayAnalysis.ArrayTypesInternal[] state;
      BitSet active;

      public ArrayState() {
         this.state = new ConstantArrayAnalysis.ArrayTypesInternal[ConstantArrayAnalysis.this.size];
         this.active = new BitSet(ConstantArrayAnalysis.this.size);
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof ConstantArrayAnalysis.ArrayState)) {
            return false;
         } else {
            ConstantArrayAnalysis.ArrayState otherState = (ConstantArrayAnalysis.ArrayState)obj;
            return otherState.active.equals(this.active) && Arrays.equals(this.state, otherState.state);
         }
      }

      public void deepCloneLocalValueSlot(int localRef, int index) {
         this.state[localRef] = (ConstantArrayAnalysis.ArrayTypesInternal)this.state[localRef].clone();
         this.state[localRef].typeState[index] = (BitSet)this.state[localRef].typeState[index].clone();
      }
   }

   public static class ArrayTypes {
      public Set<Integer> possibleSizes;
      public Set<Type>[] possibleTypes;

      public String toString() {
         return "ArrayTypes [possibleSizes=" + this.possibleSizes + ", possibleTypes=" + Arrays.toString(this.possibleTypes) + "]";
      }
   }

   private class ArrayTypesInternal implements Cloneable {
      BitSet mustAssign;
      BitSet[] typeState;
      BitSet sizeState;

      private ArrayTypesInternal() {
         this.sizeState = new BitSet(ConstantArrayAnalysis.this.szSize);
      }

      public Object clone() {
         try {
            ConstantArrayAnalysis.ArrayTypesInternal s = (ConstantArrayAnalysis.ArrayTypesInternal)super.clone();
            s.sizeState = (BitSet)s.sizeState.clone();
            s.typeState = (BitSet[])s.typeState.clone();
            s.mustAssign = (BitSet)s.mustAssign.clone();
            return s;
         } catch (CloneNotSupportedException var3) {
            throw new InternalError();
         }
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof ConstantArrayAnalysis.ArrayTypesInternal)) {
            return false;
         } else {
            ConstantArrayAnalysis.ArrayTypesInternal otherTypes = (ConstantArrayAnalysis.ArrayTypesInternal)obj;
            return otherTypes.sizeState.equals(this.sizeState) && Arrays.equals(this.typeState, otherTypes.typeState) && this.mustAssign.equals(otherTypes.mustAssign);
         }
      }

      // $FF: synthetic method
      ArrayTypesInternal(Object x1) {
         this();
      }
   }
}
