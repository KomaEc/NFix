package soot.jimple.toolkits.annotation.parity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.IntegerType;
import soot.Local;
import soot.LongType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AddExpr;
import soot.jimple.ArithmeticConstant;
import soot.jimple.BinopExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.MulExpr;
import soot.jimple.SubExpr;
import soot.options.Options;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.toolkits.scalar.LiveLocals;

public class ParityAnalysis extends ForwardFlowAnalysis<Unit, Map<Value, ParityAnalysis.Parity>> {
   private UnitGraph g;
   private LiveLocals filter;

   public ParityAnalysis(UnitGraph g, LiveLocals filter) {
      super(g);
      this.g = g;
      this.filter = filter;
      this.filterUnitToBeforeFlow = new HashMap();
      this.buildBeforeFilterMap();
      this.filterUnitToAfterFlow = new HashMap();
      this.doAnalysis();
   }

   public ParityAnalysis(UnitGraph g) {
      super(g);
      this.g = g;
      this.doAnalysis();
   }

   private void buildBeforeFilterMap() {
      Iterator var1 = this.g.getBody().getUnits().iterator();

      while(var1.hasNext()) {
         Unit s = (Unit)var1.next();
         Map<Value, ParityAnalysis.Parity> map = new HashMap();
         Iterator var4 = this.filter.getLiveLocalsBefore(s).iterator();

         while(var4.hasNext()) {
            Local l = (Local)var4.next();
            map.put(l, ParityAnalysis.Parity.BOTTOM);
         }

         this.filterUnitToBeforeFlow.put(s, map);
      }

   }

   protected void merge(Map<Value, ParityAnalysis.Parity> inMap1, Map<Value, ParityAnalysis.Parity> inMap2, Map<Value, ParityAnalysis.Parity> outMap) {
      Iterator var4 = inMap1.keySet().iterator();

      while(true) {
         while(var4.hasNext()) {
            Value var1 = (Value)var4.next();
            ParityAnalysis.Parity inVal1 = (ParityAnalysis.Parity)inMap1.get(var1);
            ParityAnalysis.Parity inVal2 = (ParityAnalysis.Parity)inMap2.get(var1);
            if (inVal2 == null) {
               outMap.put(var1, inVal1);
            } else if (inVal1.equals(ParityAnalysis.Parity.BOTTOM)) {
               outMap.put(var1, inVal2);
            } else if (inVal2.equals(ParityAnalysis.Parity.BOTTOM)) {
               outMap.put(var1, inVal1);
            } else if (inVal1.equals(ParityAnalysis.Parity.EVEN) && inVal2.equals(ParityAnalysis.Parity.EVEN)) {
               outMap.put(var1, ParityAnalysis.Parity.EVEN);
            } else if (inVal1.equals(ParityAnalysis.Parity.ODD) && inVal2.equals(ParityAnalysis.Parity.ODD)) {
               outMap.put(var1, ParityAnalysis.Parity.ODD);
            } else {
               outMap.put(var1, ParityAnalysis.Parity.TOP);
            }
         }

         return;
      }
   }

   protected void copy(Map<Value, ParityAnalysis.Parity> sourceIn, Map<Value, ParityAnalysis.Parity> destOut) {
      destOut.clear();
      destOut.putAll(sourceIn);
   }

   private ParityAnalysis.Parity getParity(Map<Value, ParityAnalysis.Parity> in, Value val) {
      ParityAnalysis.Parity p;
      ParityAnalysis.Parity resVal2;
      if (val instanceof AddExpr | val instanceof SubExpr) {
         p = this.getParity(in, ((BinopExpr)val).getOp1());
         resVal2 = this.getParity(in, ((BinopExpr)val).getOp2());
         if (p.equals(ParityAnalysis.Parity.TOP) | resVal2.equals(ParityAnalysis.Parity.TOP)) {
            return ParityAnalysis.Parity.TOP;
         } else if (p.equals(ParityAnalysis.Parity.BOTTOM) | resVal2.equals(ParityAnalysis.Parity.BOTTOM)) {
            return ParityAnalysis.Parity.BOTTOM;
         } else {
            return p.equals(resVal2) ? ParityAnalysis.Parity.EVEN : ParityAnalysis.Parity.ODD;
         }
      } else if (val instanceof MulExpr) {
         p = this.getParity(in, ((BinopExpr)val).getOp1());
         resVal2 = this.getParity(in, ((BinopExpr)val).getOp2());
         if (p.equals(ParityAnalysis.Parity.TOP) | resVal2.equals(ParityAnalysis.Parity.TOP)) {
            return ParityAnalysis.Parity.TOP;
         } else if (p.equals(ParityAnalysis.Parity.BOTTOM) | resVal2.equals(ParityAnalysis.Parity.BOTTOM)) {
            return ParityAnalysis.Parity.BOTTOM;
         } else {
            return p.equals(resVal2) ? p : ParityAnalysis.Parity.EVEN;
         }
      } else if (val instanceof IntConstant) {
         int value = ((IntConstant)val).value;
         return ParityAnalysis.Parity.valueOf(value);
      } else if (val instanceof LongConstant) {
         long value = ((LongConstant)val).value;
         return ParityAnalysis.Parity.valueOf(value);
      } else {
         p = (ParityAnalysis.Parity)in.get(val);
         return p == null ? ParityAnalysis.Parity.TOP : p;
      }
   }

   protected void flowThrough(Map<Value, ParityAnalysis.Parity> in, Unit s, Map<Value, ParityAnalysis.Parity> out) {
      out.putAll(in);
      if (s instanceof DefinitionStmt) {
         Value left = ((DefinitionStmt)s).getLeftOp();
         if (left instanceof Local && (left.getType() instanceof IntegerType || left.getType() instanceof LongType)) {
            Value right = ((DefinitionStmt)s).getRightOp();
            out.put(left, this.getParity(out, right));
         }
      }

      Iterator var7 = s.getUseAndDefBoxes().iterator();

      while(var7.hasNext()) {
         ValueBox next = (ValueBox)var7.next();
         Value val = next.getValue();
         if (val instanceof ArithmeticConstant) {
            out.put(val, this.getParity(out, val));
         }
      }

      if (Options.v().interactive_mode()) {
         this.buildAfterFilterMap(s);
         this.updateAfterFilterMap(s);
      }

   }

   private void buildAfterFilterMap(Unit s) {
      Map<Value, ParityAnalysis.Parity> map = new HashMap();
      Iterator var3 = this.filter.getLiveLocalsAfter(s).iterator();

      while(var3.hasNext()) {
         Local local = (Local)var3.next();
         map.put(local, ParityAnalysis.Parity.BOTTOM);
      }

      this.filterUnitToAfterFlow.put(s, map);
   }

   protected Map<Value, ParityAnalysis.Parity> entryInitialFlow() {
      return this.newInitialFlow();
   }

   private void updateBeforeFilterMap() {
      Iterator var1 = this.filterUnitToBeforeFlow.keySet().iterator();

      while(var1.hasNext()) {
         Unit s = (Unit)var1.next();
         Map<Value, ParityAnalysis.Parity> allData = (Map)this.getFlowBefore(s);
         Map<Value, ParityAnalysis.Parity> filterData = (Map)this.filterUnitToBeforeFlow.get(s);
         this.filterUnitToBeforeFlow.put(s, this.updateFilter(allData, filterData));
      }

   }

   private void updateAfterFilterMap(Unit s) {
      Map<Value, ParityAnalysis.Parity> allData = (Map)this.getFlowAfter(s);
      Map<Value, ParityAnalysis.Parity> filterData = (Map)this.filterUnitToAfterFlow.get(s);
      this.filterUnitToAfterFlow.put(s, this.updateFilter(allData, filterData));
   }

   private Map<Value, ParityAnalysis.Parity> updateFilter(Map<Value, ParityAnalysis.Parity> allData, Map<Value, ParityAnalysis.Parity> filterData) {
      if (allData == null) {
         return filterData;
      } else {
         Iterator var3 = filterData.keySet().iterator();

         while(var3.hasNext()) {
            Value v = (Value)var3.next();
            ParityAnalysis.Parity d = (ParityAnalysis.Parity)allData.get(v);
            if (d == null) {
               filterData.remove(v);
            } else {
               filterData.put(v, d);
            }
         }

         return filterData;
      }
   }

   protected Map<Value, ParityAnalysis.Parity> newInitialFlow() {
      Map<Value, ParityAnalysis.Parity> initMap = new HashMap();
      Iterator var2 = this.g.getBody().getLocals().iterator();

      while(true) {
         Local l;
         Type t;
         do {
            if (!var2.hasNext()) {
               var2 = this.g.getBody().getUseAndDefBoxes().iterator();

               while(var2.hasNext()) {
                  ValueBox vb = (ValueBox)var2.next();
                  Value val = vb.getValue();
                  if (val instanceof ArithmeticConstant) {
                     initMap.put(val, this.getParity(initMap, val));
                  }
               }

               if (Options.v().interactive_mode()) {
                  this.updateBeforeFilterMap();
               }

               return initMap;
            }

            l = (Local)var2.next();
            t = l.getType();
         } while(!(t instanceof IntegerType) && !(t instanceof LongType));

         initMap.put(l, ParityAnalysis.Parity.BOTTOM);
      }
   }

   public static enum Parity {
      TOP,
      BOTTOM,
      EVEN,
      ODD;

      static ParityAnalysis.Parity valueOf(int v) {
         return v % 2 == 0 ? EVEN : ODD;
      }

      static ParityAnalysis.Parity valueOf(long v) {
         return v % 2L == 0L ? EVEN : ODD;
      }
   }
}
