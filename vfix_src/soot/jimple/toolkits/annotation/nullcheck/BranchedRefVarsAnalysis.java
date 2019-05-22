package soot.jimple.toolkits.annotation.nullcheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.EquivalentValue;
import soot.Local;
import soot.NullType;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.EqExpr;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.MonitorStmt;
import soot.jimple.NeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArrayFlowUniverse;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.FlowUniverse;
import soot.toolkits.scalar.ForwardBranchedFlowAnalysis;

/** @deprecated */
@Deprecated
public class BranchedRefVarsAnalysis extends ForwardBranchedFlowAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(BranchedRefVarsAnalysis.class);
   private final boolean isNotConservative = false;
   private final boolean isBranched = true;
   private final boolean careForAliases = false;
   private final boolean careForMethodCalls = true;
   public static final int kBottom = 0;
   public static final int kNull = 1;
   public static final int kNonNull = 2;
   public static final int kTop = 99;
   protected FlowSet emptySet;
   protected FlowSet fullSet;
   protected Map<Unit, FlowSet> unitToGenerateSet;
   protected Map<Unit, FlowSet> unitToPreserveSet;
   protected Map<Unit, HashSet<Value>> unitToAnalyzedChecksSet;
   protected Map<Unit, HashSet<Value>> unitToArrayRefChecksSet;
   protected Map<Unit, HashSet<Value>> unitToInstanceFieldRefChecksSet;
   protected Map<Unit, HashSet<Value>> unitToInstanceInvokeExprChecksSet;
   protected Map<Unit, HashSet<Value>> unitToLengthExprChecksSet;
   protected List<EquivalentValue> refTypeLocals;
   protected List<EquivalentValue> refTypeInstFields;
   protected List<EquivalentValue> refTypeInstFieldBases;
   protected List<EquivalentValue> refTypeStaticFields;
   protected List<EquivalentValue> refTypeValues;
   protected FlowSet tempFlowSet = null;
   private final HashMap<Value, EquivalentValue> valueToEquivValue = new HashMap(2293, 0.7F);
   private final HashMap<EquivalentValue, RefIntPair> kRefBotttomPairs = new HashMap(2293, 0.7F);
   private final HashMap<EquivalentValue, RefIntPair> kRefNonNullPairs = new HashMap(2293, 0.7F);
   private final HashMap<EquivalentValue, RefIntPair> kRefNullPairs = new HashMap(2293, 0.7F);
   private final HashMap<EquivalentValue, RefIntPair> kRefTopPairs = new HashMap(2293, 0.7F);

   public EquivalentValue getEquivalentValue(Value v) {
      if (this.valueToEquivValue.containsKey(v)) {
         return (EquivalentValue)this.valueToEquivValue.get(v);
      } else {
         EquivalentValue ev = new EquivalentValue(v);
         this.valueToEquivValue.put(v, ev);
         return ev;
      }
   }

   public RefIntPair getKRefIntPair(EquivalentValue r, int v) {
      HashMap<EquivalentValue, RefIntPair> pairsMap = null;
      if (v == 2) {
         pairsMap = this.kRefNonNullPairs;
      } else if (v == 1) {
         pairsMap = this.kRefNullPairs;
      } else if (v == 99) {
         pairsMap = this.kRefTopPairs;
      } else {
         if (v != 0) {
            throw new RuntimeException("invalid constant (" + v + ")");
         }

         pairsMap = this.kRefBotttomPairs;
      }

      if (pairsMap.containsKey(r)) {
         return (RefIntPair)pairsMap.get(r);
      } else {
         RefIntPair pair = new RefIntPair(r, v, this);
         pairsMap.put(r, pair);
         return pair;
      }
   }

   private final boolean isAlwaysNull(Value r) {
      return r instanceof NullConstant || r.getType() instanceof NullType;
   }

   private final boolean isAlwaysTop(Value r) {
      return r instanceof InstanceFieldRef || r instanceof StaticFieldRef;
   }

   protected boolean isAlwaysNonNull(Value ro) {
      if (ro instanceof NewExpr) {
         return true;
      } else if (ro instanceof NewArrayExpr) {
         return true;
      } else if (ro instanceof NewMultiArrayExpr) {
         return true;
      } else if (ro instanceof ThisRef) {
         return true;
      } else if (ro instanceof CaughtExceptionRef) {
         return true;
      } else {
         return ro instanceof StringConstant;
      }
   }

   private final boolean isAnalyzedRef(Value r) {
      if (!this.isAlwaysNull(r) && !this.isAlwaysTop(r)) {
         if (!(r instanceof Local) && !(r instanceof InstanceFieldRef) && !(r instanceof StaticFieldRef)) {
            return false;
         } else {
            Type rType = r.getType();
            return rType instanceof RefType || rType instanceof ArrayType;
         }
      } else {
         return false;
      }
   }

   protected final int refInfo(EquivalentValue r, FlowSet fs) {
      boolean isNull = fs.contains(this.getKRefIntPair(r, 1));
      boolean isNonNull = fs.contains(this.getKRefIntPair(r, 2));
      if (isNull && isNonNull) {
         return 99;
      } else if (isNull) {
         return 1;
      } else {
         return isNonNull ? 2 : 0;
      }
   }

   protected final int refInfo(Value r, FlowSet fs) {
      return this.refInfo(this.getEquivalentValue(r), fs);
   }

   public int anyRefInfo(Value r, FlowSet f) {
      if (this.isAlwaysNull(r)) {
         return 1;
      } else if (this.isAlwaysTop(r)) {
         return 99;
      } else {
         return this.isAlwaysNonNull(r) ? 2 : this.refInfo(r, f);
      }
   }

   private final void uAddTopToFlowSet(EquivalentValue r, FlowSet genFS, FlowSet preFS) {
      RefIntPair nullPair = this.getKRefIntPair(r, 1);
      RefIntPair nullNonPair = this.getKRefIntPair(r, 2);
      if (genFS != preFS) {
         preFS.remove(nullPair, preFS);
         preFS.remove(nullNonPair, preFS);
      }

      genFS.add(nullPair, genFS);
      genFS.add(nullNonPair, genFS);
   }

   private final void uAddTopToFlowSet(Value r, FlowSet genFS, FlowSet preFS) {
      this.uAddTopToFlowSet(this.getEquivalentValue(r), genFS, preFS);
   }

   private final void uAddTopToFlowSet(Value r, FlowSet fs) {
      this.uAddTopToFlowSet(this.getEquivalentValue(r), fs, fs);
   }

   private final void uAddTopToFlowSet(EquivalentValue r, FlowSet fs) {
      this.uAddTopToFlowSet(r, fs, fs);
   }

   private final void uAddInfoToFlowSet(EquivalentValue r, int v, FlowSet genFS, FlowSet preFS) {
      byte kill;
      if (v == 1) {
         kill = 2;
      } else {
         if (v != 2) {
            throw new RuntimeException("invalid info");
         }

         kill = 1;
      }

      if (genFS != preFS) {
         preFS.remove(this.getKRefIntPair(r, kill), preFS);
      }

      genFS.remove(this.getKRefIntPair(r, kill), genFS);
      genFS.add(this.getKRefIntPair(r, v), genFS);
   }

   private final void uAddInfoToFlowSet(Value r, int v, FlowSet genF, FlowSet preF) {
      this.uAddInfoToFlowSet(this.getEquivalentValue(r), v, genF, preF);
   }

   private final void uAddInfoToFlowSet(Value r, int v, FlowSet fs) {
      this.uAddInfoToFlowSet(this.getEquivalentValue(r), v, fs, fs);
   }

   private final void uAddInfoToFlowSet(EquivalentValue r, int v, FlowSet fs) {
      this.uAddInfoToFlowSet(r, v, fs, fs);
   }

   private final void uListAddTopToFlowSet(List<EquivalentValue> refs, FlowSet genFS, FlowSet preFS) {
      Iterator it = refs.iterator();

      while(it.hasNext()) {
         this.uAddTopToFlowSet((EquivalentValue)it.next(), genFS, preFS);
      }

   }

   /** @deprecated */
   @Deprecated
   public BranchedRefVarsAnalysis(UnitGraph g) {
      super(g);
      this.initRefTypeLists();
      this.initUniverseSets();
      this.initUnitSets();
      this.doAnalysis();
   }

   private void initRefTypeLists() {
      this.refTypeLocals = new ArrayList();
      this.refTypeInstFields = new ArrayList();
      this.refTypeInstFieldBases = new ArrayList();
      this.refTypeStaticFields = new ArrayList();
      this.refTypeValues = new ArrayList();
      Iterator it = ((UnitGraph)this.graph).getBody().getLocals().iterator();

      while(true) {
         Local l;
         do {
            if (!it.hasNext()) {
               this.refTypeValues.addAll(this.refTypeLocals);
               this.refTypeValues.addAll(this.refTypeInstFields);
               this.refTypeValues.addAll(this.refTypeStaticFields);
               return;
            }

            l = (Local)((Local)it.next());
         } while(!(l.getType() instanceof RefType) && !(l.getType() instanceof ArrayType));

         this.refTypeLocals.add(this.getEquivalentValue(l));
      }
   }

   private void initRefTypeLists(ValueBox box) {
      Value val = box.getValue();
      Type opType = null;
      EquivalentValue esr;
      if (val instanceof InstanceFieldRef) {
         InstanceFieldRef ir = (InstanceFieldRef)val;
         opType = ir.getType();
         if (opType instanceof RefType || opType instanceof ArrayType) {
            esr = this.getEquivalentValue(ir);
            if (!this.refTypeInstFields.contains(esr)) {
               this.refTypeInstFields.add(esr);
               EquivalentValue eirbase = this.getEquivalentValue(ir.getBase());
               if (!this.refTypeInstFieldBases.contains(eirbase)) {
                  this.refTypeInstFieldBases.add(eirbase);
               }
            }
         }
      } else if (val instanceof StaticFieldRef) {
         StaticFieldRef sr = (StaticFieldRef)val;
         opType = sr.getType();
         if (opType instanceof RefType || opType instanceof ArrayType) {
            esr = this.getEquivalentValue(sr);
            if (!this.refTypeStaticFields.contains(esr)) {
               this.refTypeStaticFields.add(esr);
            }
         }
      }

   }

   private void initUniverseSets() {
      Object[] refTypeValuesArray = this.refTypeValues.toArray();
      int len = refTypeValuesArray.length;
      Object[] universeArray = new Object[2 * len];

      for(int i = 0; i < len; ++i) {
         int j = i * 2;
         EquivalentValue r = (EquivalentValue)refTypeValuesArray[i];
         universeArray[j] = this.getKRefIntPair(r, 1);
         universeArray[j + 1] = this.getKRefIntPair(r, 2);
      }

      FlowUniverse localUniverse = new ArrayFlowUniverse(universeArray);
      this.emptySet = new ArrayPackedSet(localUniverse);
      this.fullSet = this.emptySet.clone();
      ((ArrayPackedSet)this.emptySet).complement(this.fullSet);
      this.tempFlowSet = (FlowSet)this.newInitialFlow();
   }

   private void initUnitSets() {
      int cap = this.graph.size() * 2 + 1;
      float load = 0.7F;
      this.unitToGenerateSet = new HashMap(cap, load);
      this.unitToPreserveSet = new HashMap(cap, load);
      this.unitToAnalyzedChecksSet = new HashMap(cap, load);
      this.unitToArrayRefChecksSet = new HashMap(cap, load);
      this.unitToInstanceFieldRefChecksSet = new HashMap(cap, load);
      this.unitToInstanceInvokeExprChecksSet = new HashMap(cap, load);
      this.unitToLengthExprChecksSet = new HashMap(cap, load);
      Iterator unitIt = this.graph.iterator();

      while(unitIt.hasNext()) {
         Unit s = (Unit)unitIt.next();
         FlowSet genSet = this.emptySet.clone();
         FlowSet preSet = this.fullSet.clone();
         HashSet<Value> analyzedChecksSet = new HashSet(5, load);
         HashSet<Value> arrayRefChecksSet = new HashSet(5, load);
         HashSet<Value> instanceFieldRefChecksSet = new HashSet(5, load);
         HashSet<Value> instanceInvokeExprChecksSet = new HashSet(5, load);
         HashSet<Value> lengthExprChecksSet = new HashSet(5, load);
         if (((Stmt)s).containsInvokeExpr()) {
            this.uListAddTopToFlowSet(this.refTypeInstFields, genSet, preSet);
            this.uListAddTopToFlowSet(this.refTypeStaticFields, genSet, preSet);
         }

         Iterator boxIt = s.getDefBoxes().iterator();

         Value base;
         while(boxIt.hasNext()) {
            ValueBox box = (ValueBox)boxIt.next();
            base = box.getValue();
            if (this.isAnalyzedRef(base)) {
               this.uAddTopToFlowSet(base, genSet, preSet);
            }
         }

         Value boxValue;
         if (s instanceof DefinitionStmt) {
            DefinitionStmt as = (DefinitionStmt)s;
            boxValue = as.getRightOp();
            base = as.getLeftOp();
            if (boxValue instanceof CastExpr) {
               boxValue = ((CastExpr)boxValue).getOp();
            }

            if (this.isAnalyzedRef(base)) {
               if (this.isAlwaysNonNull(boxValue)) {
                  this.uAddInfoToFlowSet((Value)base, 2, genSet, preSet);
               } else if (this.isAlwaysNull(boxValue)) {
                  this.uAddInfoToFlowSet((Value)base, 1, genSet, preSet);
               } else if (this.isAlwaysTop(boxValue)) {
                  this.uAddTopToFlowSet(base, genSet, preSet);
               }
            }
         }

         boxIt = s.getUseBoxes().iterator();

         while(boxIt.hasNext()) {
            boxValue = ((ValueBox)boxIt.next()).getValue();
            base = null;
            if (boxValue instanceof InstanceFieldRef) {
               base = ((InstanceFieldRef)((InstanceFieldRef)boxValue)).getBase();
               instanceFieldRefChecksSet.add(base);
            } else if (boxValue instanceof ArrayRef) {
               base = ((ArrayRef)((ArrayRef)boxValue)).getBase();
               arrayRefChecksSet.add(base);
            } else if (boxValue instanceof InstanceInvokeExpr) {
               base = ((InstanceInvokeExpr)boxValue).getBase();
               instanceInvokeExprChecksSet.add(base);
            } else if (boxValue instanceof LengthExpr) {
               base = ((LengthExpr)boxValue).getOp();
               lengthExprChecksSet.add(base);
            } else if (s instanceof ThrowStmt) {
               base = ((ThrowStmt)s).getOp();
            } else if (s instanceof MonitorStmt) {
               base = ((MonitorStmt)s).getOp();
            }

            if (base != null && this.isAnalyzedRef(base)) {
               this.uAddInfoToFlowSet((Value)base, 2, genSet, preSet);
               analyzedChecksSet.add(base);
            }
         }

         boxIt = s.getDefBoxes().iterator();

         while(boxIt.hasNext()) {
            boxValue = ((ValueBox)boxIt.next()).getValue();
            base = null;
            if (boxValue instanceof InstanceFieldRef) {
               base = ((InstanceFieldRef)((InstanceFieldRef)boxValue)).getBase();
               instanceFieldRefChecksSet.add(base);
            } else if (boxValue instanceof ArrayRef) {
               base = ((ArrayRef)((ArrayRef)boxValue)).getBase();
               arrayRefChecksSet.add(base);
            } else if (boxValue instanceof InstanceInvokeExpr) {
               base = ((InstanceInvokeExpr)boxValue).getBase();
               instanceInvokeExprChecksSet.add(base);
            } else if (boxValue instanceof LengthExpr) {
               base = ((LengthExpr)boxValue).getOp();
               lengthExprChecksSet.add(base);
            } else if (s instanceof ThrowStmt) {
               base = ((ThrowStmt)s).getOp();
            } else if (s instanceof MonitorStmt) {
               base = ((MonitorStmt)s).getOp();
            }

            if (base != null && this.isAnalyzedRef(base)) {
               this.uAddInfoToFlowSet((Value)base, 2, genSet, preSet);
               analyzedChecksSet.add(base);
            }
         }

         this.unitToGenerateSet.put(s, genSet);
         this.unitToPreserveSet.put(s, preSet);
         this.unitToAnalyzedChecksSet.put(s, analyzedChecksSet);
         this.unitToArrayRefChecksSet.put(s, arrayRefChecksSet);
         this.unitToInstanceFieldRefChecksSet.put(s, instanceFieldRefChecksSet);
         this.unitToInstanceInvokeExprChecksSet.put(s, instanceInvokeExprChecksSet);
         this.unitToLengthExprChecksSet.put(s, lengthExprChecksSet);
      }

   }

   protected void flowThrough(Object inValue, Unit stmt, List outFallValue, List outBranchValues) {
      FlowSet in = (FlowSet)inValue;
      FlowSet out = this.tempFlowSet;
      FlowSet pre = (FlowSet)this.unitToPreserveSet.get(stmt);
      FlowSet gen = (FlowSet)this.unitToGenerateSet.get(stmt);
      in.intersection(pre, out);
      out.union(gen, out);
      Value op1;
      Value op2;
      if (stmt instanceof AssignStmt) {
         AssignStmt as = (AssignStmt)stmt;
         op1 = as.getRightOp();
         op2 = as.getLeftOp();
         if (op1 instanceof CastExpr) {
            op1 = ((CastExpr)op1).getOp();
         }

         if (this.isAnalyzedRef(op2) && this.isAnalyzedRef(op1)) {
            int roInfo = this.refInfo(op1, in);
            if (roInfo == 99) {
               this.uAddTopToFlowSet(op2, out);
            } else if (roInfo != 0) {
               this.uAddInfoToFlowSet(op2, roInfo, out);
            }
         }
      }

      Iterator it = outBranchValues.iterator();

      FlowSet fs;
      while(it.hasNext()) {
         fs = (FlowSet)((FlowSet)it.next());
         this.copy(out, fs);
      }

      it = outFallValue.iterator();

      while(it.hasNext()) {
         fs = (FlowSet)((FlowSet)it.next());
         this.copy(out, fs);
      }

      if (stmt instanceof IfStmt) {
         Value cond = ((IfStmt)stmt).getCondition();
         op1 = ((BinopExpr)cond).getOp1();
         op2 = ((BinopExpr)cond).getOp2();
         if (!this.isAlwaysTop(op1) && !this.isAlwaysTop(op2) && (this.isAnalyzedRef(op1) || this.isAnalyzedRef(op2))) {
            Value toGen = null;
            int toGenInfo = 0;
            int op1Info = this.anyRefInfo(op1, in);
            int op2Info = this.anyRefInfo(op2, in);
            boolean op1isKnown = op1Info == 1 || op1Info == 2;
            boolean op2isKnown = op2Info == 1 || op2Info == 2;
            if (op1isKnown) {
               if (!op2isKnown) {
                  toGen = op2;
                  toGenInfo = op1Info;
               }
            } else if (op2isKnown) {
               toGen = op1;
               toGenInfo = op2Info;
            }

            if (toGen != null && this.isAnalyzedRef(toGen)) {
               int fInfo = 0;
               int bInfo = 0;
               if (cond instanceof EqExpr) {
                  bInfo = toGenInfo;
                  if (toGenInfo == 1) {
                     fInfo = 2;
                  }
               } else {
                  if (!(cond instanceof NeExpr)) {
                     throw new RuntimeException("invalid condition");
                  }

                  fInfo = toGenInfo;
                  if (toGenInfo == 1) {
                     bInfo = 2;
                  }
               }

               Iterator it;
               FlowSet fs;
               if (fInfo != 0) {
                  it = outFallValue.iterator();

                  while(it.hasNext()) {
                     fs = (FlowSet)((FlowSet)it.next());
                     this.copy(out, fs);
                     this.uAddInfoToFlowSet(toGen, fInfo, fs);
                  }
               }

               if (bInfo != 0) {
                  it = outBranchValues.iterator();

                  while(it.hasNext()) {
                     fs = (FlowSet)((FlowSet)it.next());
                     this.copy(out, fs);
                     this.uAddInfoToFlowSet(toGen, bInfo, fs);
                  }
               }
            }
         }
      }

   }

   protected void merge(Object in1, Object in2, Object out) {
      FlowSet inSet1 = (FlowSet)in1;
      FlowSet inSet2 = (FlowSet)in2;
      FlowSet inSet1Copy = inSet1.clone();
      FlowSet inSet2Copy = inSet2.clone();
      FlowSet outSet = (FlowSet)out;
      inSet1.intersection(inSet2, outSet);
      Iterator it = this.refTypeValues.iterator();

      while(true) {
         while(true) {
            EquivalentValue r;
            int refInfoIn1;
            int refInfoIn2;
            do {
               if (!it.hasNext()) {
                  return;
               }

               r = (EquivalentValue)it.next();
               refInfoIn1 = this.refInfo(r, inSet1Copy);
               refInfoIn2 = this.refInfo(r, inSet2Copy);
            } while(refInfoIn1 == refInfoIn2);

            if (refInfoIn1 != 99 && refInfoIn2 != 99) {
               if (refInfoIn1 == 0) {
                  this.uAddInfoToFlowSet(r, refInfoIn2, outSet);
               } else if (refInfoIn2 == 0) {
                  this.uAddInfoToFlowSet(r, refInfoIn1, outSet);
               } else {
                  this.uAddTopToFlowSet(r, outSet);
               }
            } else {
               this.uAddTopToFlowSet(r, outSet);
            }
         }
      }
   }

   protected void copy(Object source, Object dest) {
      FlowSet sourceSet = (FlowSet)source;
      FlowSet destSet = (FlowSet)dest;
      sourceSet.copy(destSet);
   }

   protected Object newInitialFlow() {
      return this.emptySet.clone();
   }

   protected Object entryInitialFlow() {
      return this.fullSet.clone();
   }

   public boolean treatTrapHandlersAsEntries() {
      return true;
   }
}
