package soot.toolkits.exceptions;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.solver.IDESolver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.FastHierarchy;
import soot.G;
import soot.IntegerType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.PatchingChain;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.baf.AddInst;
import soot.baf.AndInst;
import soot.baf.ArrayLengthInst;
import soot.baf.ArrayReadInst;
import soot.baf.ArrayWriteInst;
import soot.baf.CmpInst;
import soot.baf.CmpgInst;
import soot.baf.CmplInst;
import soot.baf.DivInst;
import soot.baf.Dup1Inst;
import soot.baf.Dup1_x1Inst;
import soot.baf.Dup1_x2Inst;
import soot.baf.Dup2Inst;
import soot.baf.Dup2_x1Inst;
import soot.baf.Dup2_x2Inst;
import soot.baf.DynamicInvokeInst;
import soot.baf.EnterMonitorInst;
import soot.baf.ExitMonitorInst;
import soot.baf.FieldGetInst;
import soot.baf.FieldPutInst;
import soot.baf.GotoInst;
import soot.baf.IdentityInst;
import soot.baf.IfCmpEqInst;
import soot.baf.IfCmpGeInst;
import soot.baf.IfCmpGtInst;
import soot.baf.IfCmpLeInst;
import soot.baf.IfCmpLtInst;
import soot.baf.IfCmpNeInst;
import soot.baf.IfEqInst;
import soot.baf.IfGeInst;
import soot.baf.IfGtInst;
import soot.baf.IfLeInst;
import soot.baf.IfLtInst;
import soot.baf.IfNeInst;
import soot.baf.IfNonNullInst;
import soot.baf.IfNullInst;
import soot.baf.IncInst;
import soot.baf.InstSwitch;
import soot.baf.InstanceCastInst;
import soot.baf.InstanceOfInst;
import soot.baf.InterfaceInvokeInst;
import soot.baf.JSRInst;
import soot.baf.LoadInst;
import soot.baf.LookupSwitchInst;
import soot.baf.MulInst;
import soot.baf.NegInst;
import soot.baf.NewArrayInst;
import soot.baf.NewInst;
import soot.baf.NewMultiArrayInst;
import soot.baf.NopInst;
import soot.baf.OrInst;
import soot.baf.PopInst;
import soot.baf.PrimitiveCastInst;
import soot.baf.PushInst;
import soot.baf.RemInst;
import soot.baf.ReturnInst;
import soot.baf.ReturnVoidInst;
import soot.baf.ShlInst;
import soot.baf.ShrInst;
import soot.baf.SpecialInvokeInst;
import soot.baf.StaticGetInst;
import soot.baf.StaticInvokeInst;
import soot.baf.StaticPutInst;
import soot.baf.StoreInst;
import soot.baf.SubInst;
import soot.baf.SwapInst;
import soot.baf.TableSwitchInst;
import soot.baf.ThrowInst;
import soot.baf.UshrInst;
import soot.baf.VirtualInvokeInst;
import soot.baf.XorInst;
import soot.grimp.GrimpValueSwitch;
import soot.grimp.NewInvokeExpr;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.BreakpointStmt;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.EqExpr;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GotoStmt;
import soot.jimple.GtExpr;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.LtExpr;
import soot.jimple.MethodHandle;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.ParameterRef;
import soot.jimple.RemExpr;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StmtSwitch;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import soot.shimple.PhiExpr;
import soot.shimple.ShimpleValueSwitch;

public class UnitThrowAnalysis extends AbstractThrowAnalysis {
   protected final ThrowableSet.Manager mgr;
   private final ThrowableSet implicitThrowExceptions;
   protected final boolean isInterproc;
   public static UnitThrowAnalysis interproceduralAnalysis = null;
   protected final LoadingCache<SootMethod, ThrowableSet> methodToThrowSet;
   private static final IntConstant INT_CONSTANT_ZERO = IntConstant.v(0);
   private static final LongConstant LONG_CONSTANT_ZERO = LongConstant.v(0L);

   public UnitThrowAnalysis(Singletons.Global g) {
      this(false);
   }

   protected UnitThrowAnalysis() {
      this(false);
   }

   public static UnitThrowAnalysis v() {
      return G.v().soot_toolkits_exceptions_UnitThrowAnalysis();
   }

   protected UnitThrowAnalysis(boolean isInterproc) {
      this.mgr = ThrowableSet.Manager.v();
      this.implicitThrowExceptions = ThrowableSet.Manager.v().VM_ERRORS.add(ThrowableSet.Manager.v().NULL_POINTER_EXCEPTION).add(ThrowableSet.Manager.v().ILLEGAL_MONITOR_STATE_EXCEPTION);
      this.methodToThrowSet = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<SootMethod, ThrowableSet>() {
         public ThrowableSet load(SootMethod sm) throws Exception {
            return UnitThrowAnalysis.this.mightThrow(sm, new HashSet());
         }
      });
      this.isInterproc = isInterproc;
   }

   public static UnitThrowAnalysis interproc() {
      if (interproceduralAnalysis == null) {
         interproceduralAnalysis = new UnitThrowAnalysis(true);
      }

      return interproceduralAnalysis;
   }

   protected ThrowableSet defaultResult() {
      return this.mgr.VM_ERRORS;
   }

   protected UnitThrowAnalysis.UnitSwitch unitSwitch() {
      return new UnitThrowAnalysis.UnitSwitch();
   }

   protected UnitThrowAnalysis.ValueSwitch valueSwitch() {
      return new UnitThrowAnalysis.ValueSwitch();
   }

   public ThrowableSet mightThrow(Unit u) {
      UnitThrowAnalysis.UnitSwitch sw = this.unitSwitch();
      u.apply(sw);
      return sw.getResult();
   }

   public ThrowableSet mightThrowImplicitly(ThrowInst t) {
      return this.implicitThrowExceptions;
   }

   public ThrowableSet mightThrowImplicitly(ThrowStmt t) {
      return this.implicitThrowExceptions;
   }

   protected ThrowableSet mightThrow(Value v) {
      UnitThrowAnalysis.ValueSwitch sw = this.valueSwitch();
      v.apply(sw);
      return sw.getResult();
   }

   protected ThrowableSet mightThrow(SootMethodRef m) {
      SootMethod sm = m.tryResolve();
      return sm != null ? this.mightThrow(sm) : this.mgr.ALL_THROWABLES;
   }

   protected ThrowableSet mightThrow(SootMethod sm) {
      return !this.isInterproc ? ThrowableSet.Manager.v().ALL_THROWABLES : (ThrowableSet)this.methodToThrowSet.getUnchecked(sm);
   }

   private ThrowableSet mightThrow(SootMethod sm, Set<SootMethod> doneSet) {
      if (!doneSet.add(sm)) {
         return ThrowableSet.Manager.v().EMPTY;
      } else if (!sm.hasActiveBody()) {
         return ThrowableSet.Manager.v().EMPTY;
      } else {
         PatchingChain<Unit> units = sm.getActiveBody().getUnits();
         Map<Unit, Collection<Trap>> unitToTraps = sm.getActiveBody().getTraps().isEmpty() ? null : new HashMap();
         Iterator var5 = sm.getActiveBody().getTraps().iterator();

         Iterator unitIt;
         Unit u;
         while(var5.hasNext()) {
            Trap t = (Trap)var5.next();

            Object unitsForTrap;
            for(unitIt = units.iterator(t.getBeginUnit(), units.getPredOf(t.getEndUnit())); unitIt.hasNext(); ((Collection)unitsForTrap).add(t)) {
               u = (Unit)unitIt.next();
               unitsForTrap = (Collection)unitToTraps.get(u);
               if (unitsForTrap == null) {
                  unitsForTrap = new ArrayList();
                  unitToTraps.put(u, unitsForTrap);
               }
            }
         }

         ThrowableSet methodSet = ThrowableSet.Manager.v().EMPTY;
         if (sm.hasActiveBody()) {
            Body methodBody = sm.getActiveBody();
            unitIt = methodBody.getUnits().iterator();

            while(true) {
               do {
                  if (!unitIt.hasNext()) {
                     return methodSet;
                  }

                  u = (Unit)unitIt.next();
               } while(!(u instanceof Stmt));

               Stmt stmt = (Stmt)u;
               ThrowableSet curStmtSet;
               if (stmt.containsInvokeExpr()) {
                  InvokeExpr inv = stmt.getInvokeExpr();
                  curStmtSet = this.mightThrow(inv.getMethod(), doneSet);
               } else {
                  curStmtSet = this.mightThrow(u);
               }

               if (unitToTraps != null) {
                  Collection<Trap> trapsForUnit = (Collection)unitToTraps.get(stmt);
                  ThrowableSet.Pair p;
                  if (trapsForUnit != null) {
                     for(Iterator var12 = trapsForUnit.iterator(); var12.hasNext(); curStmtSet = curStmtSet.remove(p.getCaught())) {
                        Trap t = (Trap)var12.next();
                        p = curStmtSet.whichCatchableAs(t.getException().getType());
                     }
                  }
               }

               methodSet = methodSet.add(curStmtSet);
            }
         } else {
            return methodSet;
         }
      }
   }

   protected class ValueSwitch implements GrimpValueSwitch, ShimpleValueSwitch {
      private ThrowableSet result = UnitThrowAnalysis.this.defaultResult();

      ThrowableSet getResult() {
         return this.result;
      }

      public void caseDoubleConstant(DoubleConstant c) {
      }

      public void caseFloatConstant(FloatConstant c) {
      }

      public void caseIntConstant(IntConstant c) {
      }

      public void caseLongConstant(LongConstant c) {
      }

      public void caseNullConstant(NullConstant c) {
      }

      public void caseStringConstant(StringConstant c) {
      }

      public void caseClassConstant(ClassConstant c) {
      }

      public void caseMethodHandle(MethodHandle handle) {
      }

      public void caseAddExpr(AddExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseAndExpr(AndExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseCmpExpr(CmpExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseCmpgExpr(CmpgExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseCmplExpr(CmplExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseDivExpr(DivExpr expr) {
         this.caseBinopDivExpr(expr);
      }

      public void caseEqExpr(EqExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseNeExpr(NeExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseGeExpr(GeExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseGtExpr(GtExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseLeExpr(LeExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseLtExpr(LtExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseMulExpr(MulExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseOrExpr(OrExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseRemExpr(RemExpr expr) {
         this.caseBinopDivExpr(expr);
      }

      public void caseShlExpr(ShlExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseShrExpr(ShrExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseUshrExpr(UshrExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseSubExpr(SubExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseXorExpr(XorExpr expr) {
         this.caseBinopExpr(expr);
      }

      public void caseInterfaceInvokeExpr(InterfaceInvokeExpr expr) {
         this.caseInstanceInvokeExpr(expr);
      }

      public void caseSpecialInvokeExpr(SpecialInvokeExpr expr) {
         this.caseInstanceInvokeExpr(expr);
      }

      public void caseStaticInvokeExpr(StaticInvokeExpr expr) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);

         for(int i = 0; i < expr.getArgCount(); ++i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getArg(i)));
         }

         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getMethodRef()));
      }

      public void caseVirtualInvokeExpr(VirtualInvokeExpr expr) {
         this.caseInstanceInvokeExpr(expr);
      }

      public void caseDynamicInvokeExpr(DynamicInvokeExpr expr) {
      }

      public void caseCastExpr(CastExpr expr) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
         Type fromType = expr.getOp().getType();
         Type toType = expr.getCastType();
         if (toType instanceof RefLikeType) {
            FastHierarchy h = Scene.v().getOrMakeFastHierarchy();
            if (fromType == null || fromType instanceof UnknownType || !(fromType instanceof NullType) && !h.canStoreType(fromType, toType)) {
               this.result = this.result.add(UnitThrowAnalysis.this.mgr.CLASS_CAST_EXCEPTION);
            }
         }

         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getOp()));
      }

      public void caseInstanceOfExpr(InstanceOfExpr expr) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getOp()));
      }

      public void caseNewArrayExpr(NewArrayExpr expr) {
         if (expr.getBaseType() instanceof RefLikeType) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
         }

         Value count = expr.getSize();
         if (!(count instanceof IntConstant) || ((IntConstant)count).lessThan(UnitThrowAnalysis.INT_CONSTANT_ZERO).equals(UnitThrowAnalysis.INT_CONSTANT_ZERO)) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NEGATIVE_ARRAY_SIZE_EXCEPTION);
         }

         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(count));
      }

      public void caseNewMultiArrayExpr(NewMultiArrayExpr expr) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);

         for(int i = 0; i < expr.getSizeCount(); ++i) {
            Value count = expr.getSize(i);
            if (!(count instanceof IntConstant) || ((IntConstant)count).lessThan(UnitThrowAnalysis.INT_CONSTANT_ZERO).equals(UnitThrowAnalysis.INT_CONSTANT_ZERO)) {
               this.result = this.result.add(UnitThrowAnalysis.this.mgr.NEGATIVE_ARRAY_SIZE_EXCEPTION);
            }

            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(count));
         }

      }

      public void caseNewExpr(NewExpr expr) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);

         ValueBox box;
         for(Iterator i = expr.getUseBoxes().iterator(); i.hasNext(); this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(box.getValue()))) {
            box = (ValueBox)i.next();
         }

      }

      public void caseLengthExpr(LengthExpr expr) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getOp()));
      }

      public void caseNegExpr(NegExpr expr) {
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getOp()));
      }

      public void caseArrayRef(ArrayRef ref) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION);
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(ref.getBase()));
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(ref.getIndex()));
      }

      public void caseStaticFieldRef(StaticFieldRef ref) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);
      }

      public void caseInstanceFieldRef(InstanceFieldRef ref) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_FIELD_ERRORS);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(ref.getBase()));
      }

      public void caseParameterRef(ParameterRef v) {
      }

      public void caseCaughtExceptionRef(CaughtExceptionRef v) {
      }

      public void caseThisRef(ThisRef v) {
      }

      public void caseLocal(Local l) {
      }

      public void caseNewInvokeExpr(NewInvokeExpr e) {
         this.caseStaticInvokeExpr(e);
      }

      public void casePhiExpr(PhiExpr e) {
         ValueBox box;
         for(Iterator i = e.getUseBoxes().iterator(); i.hasNext(); this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(box.getValue()))) {
            box = (ValueBox)i.next();
         }

      }

      public void defaultCase(Object obj) {
      }

      private void caseBinopExpr(BinopExpr expr) {
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getOp1()));
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getOp2()));
      }

      private void caseBinopDivExpr(BinopExpr expr) {
         Value divisor = expr.getOp2();
         Type divisorType = divisor.getType();
         if (divisorType instanceof UnknownType) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARITHMETIC_EXCEPTION);
         } else if (divisorType instanceof IntegerType && (!(divisor instanceof IntConstant) || ((IntConstant)divisor).equals(UnitThrowAnalysis.INT_CONSTANT_ZERO))) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARITHMETIC_EXCEPTION);
         } else if (divisorType == LongType.v() && (!(divisor instanceof LongConstant) || ((LongConstant)divisor).equals(UnitThrowAnalysis.LONG_CONSTANT_ZERO))) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARITHMETIC_EXCEPTION);
         }

         this.caseBinopExpr(expr);
      }

      private void caseInstanceInvokeExpr(InstanceInvokeExpr expr) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_METHOD_ERRORS);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);

         for(int i = 0; i < expr.getArgCount(); ++i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getArg(i)));
         }

         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getBase()));
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getMethodRef()));
      }
   }

   protected class UnitSwitch implements InstSwitch, StmtSwitch {
      protected ThrowableSet result = UnitThrowAnalysis.this.defaultResult();

      ThrowableSet getResult() {
         return this.result;
      }

      public void caseReturnVoidInst(ReturnVoidInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.ILLEGAL_MONITOR_STATE_EXCEPTION);
      }

      public void caseReturnInst(ReturnInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.ILLEGAL_MONITOR_STATE_EXCEPTION);
      }

      public void caseNopInst(NopInst i) {
      }

      public void caseGotoInst(GotoInst i) {
      }

      public void caseJSRInst(JSRInst i) {
      }

      public void casePushInst(PushInst i) {
      }

      public void casePopInst(PopInst i) {
      }

      public void caseIdentityInst(IdentityInst i) {
      }

      public void caseStoreInst(StoreInst i) {
      }

      public void caseLoadInst(LoadInst i) {
      }

      public void caseArrayWriteInst(ArrayWriteInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION);
         if (i.getOpType() instanceof RefType) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARRAY_STORE_EXCEPTION);
         }

      }

      public void caseArrayReadInst(ArrayReadInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION);
      }

      public void caseIfNullInst(IfNullInst i) {
      }

      public void caseIfNonNullInst(IfNonNullInst i) {
      }

      public void caseIfEqInst(IfEqInst i) {
      }

      public void caseIfNeInst(IfNeInst i) {
      }

      public void caseIfGtInst(IfGtInst i) {
      }

      public void caseIfGeInst(IfGeInst i) {
      }

      public void caseIfLtInst(IfLtInst i) {
      }

      public void caseIfLeInst(IfLeInst i) {
      }

      public void caseIfCmpEqInst(IfCmpEqInst i) {
      }

      public void caseIfCmpNeInst(IfCmpNeInst i) {
      }

      public void caseIfCmpGtInst(IfCmpGtInst i) {
      }

      public void caseIfCmpGeInst(IfCmpGeInst i) {
      }

      public void caseIfCmpLtInst(IfCmpLtInst i) {
      }

      public void caseIfCmpLeInst(IfCmpLeInst i) {
      }

      public void caseStaticGetInst(StaticGetInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);
      }

      public void caseStaticPutInst(StaticPutInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);
      }

      public void caseFieldGetInst(FieldGetInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_FIELD_ERRORS);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
      }

      public void caseFieldPutInst(FieldPutInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_FIELD_ERRORS);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
      }

      public void caseInstanceCastInst(InstanceCastInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.CLASS_CAST_EXCEPTION);
      }

      public void caseInstanceOfInst(InstanceOfInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
      }

      public void casePrimitiveCastInst(PrimitiveCastInst i) {
      }

      public void caseDynamicInvokeInst(DynamicInvokeInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_METHOD_ERRORS);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);
         this.result = this.result.add(ThrowableSet.Manager.v().ALL_THROWABLES);
      }

      public void caseStaticInvokeInst(StaticInvokeInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(i.getMethodRef()));
      }

      public void caseVirtualInvokeInst(VirtualInvokeInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_METHOD_ERRORS);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(i.getMethodRef()));
      }

      public void caseInterfaceInvokeInst(InterfaceInvokeInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_METHOD_ERRORS);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(i.getMethodRef()));
      }

      public void caseSpecialInvokeInst(SpecialInvokeInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_METHOD_ERRORS);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(i.getMethodRef()));
      }

      public void caseThrowInst(ThrowInst i) {
         this.result = UnitThrowAnalysis.this.mightThrowImplicitly(i);
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrowExplicitly(i));
      }

      public void caseAddInst(AddInst i) {
      }

      public void caseAndInst(AndInst i) {
      }

      public void caseOrInst(OrInst i) {
      }

      public void caseXorInst(XorInst i) {
      }

      public void caseArrayLengthInst(ArrayLengthInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
      }

      public void caseCmpInst(CmpInst i) {
      }

      public void caseCmpgInst(CmpgInst i) {
      }

      public void caseCmplInst(CmplInst i) {
      }

      public void caseDivInst(DivInst i) {
         if (i.getOpType() instanceof IntegerType || i.getOpType() == LongType.v()) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARITHMETIC_EXCEPTION);
         }

      }

      public void caseIncInst(IncInst i) {
      }

      public void caseMulInst(MulInst i) {
      }

      public void caseRemInst(RemInst i) {
         if (i.getOpType() instanceof IntegerType || i.getOpType() == LongType.v()) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARITHMETIC_EXCEPTION);
         }

      }

      public void caseSubInst(SubInst i) {
      }

      public void caseShlInst(ShlInst i) {
      }

      public void caseShrInst(ShrInst i) {
      }

      public void caseUshrInst(UshrInst i) {
      }

      public void caseNewInst(NewInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);
      }

      public void caseNegInst(NegInst i) {
      }

      public void caseSwapInst(SwapInst i) {
      }

      public void caseDup1Inst(Dup1Inst i) {
      }

      public void caseDup2Inst(Dup2Inst i) {
      }

      public void caseDup1_x1Inst(Dup1_x1Inst i) {
      }

      public void caseDup1_x2Inst(Dup1_x2Inst i) {
      }

      public void caseDup2_x1Inst(Dup2_x1Inst i) {
      }

      public void caseDup2_x2Inst(Dup2_x2Inst i) {
      }

      public void caseNewArrayInst(NewArrayInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NEGATIVE_ARRAY_SIZE_EXCEPTION);
      }

      public void caseNewMultiArrayInst(NewMultiArrayInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NEGATIVE_ARRAY_SIZE_EXCEPTION);
      }

      public void caseLookupSwitchInst(LookupSwitchInst i) {
      }

      public void caseTableSwitchInst(TableSwitchInst i) {
      }

      public void caseEnterMonitorInst(EnterMonitorInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
      }

      public void caseExitMonitorInst(ExitMonitorInst i) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.ILLEGAL_MONITOR_STATE_EXCEPTION);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
      }

      public void caseAssignStmt(AssignStmt s) {
         Value lhs = s.getLeftOp();
         if (lhs instanceof ArrayRef && (lhs.getType() instanceof UnknownType || lhs.getType() instanceof RefType)) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARRAY_STORE_EXCEPTION);
         }

         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getLeftOp()));
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getRightOp()));
      }

      public void caseBreakpointStmt(BreakpointStmt s) {
      }

      public void caseEnterMonitorStmt(EnterMonitorStmt s) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getOp()));
      }

      public void caseExitMonitorStmt(ExitMonitorStmt s) {
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.ILLEGAL_MONITOR_STATE_EXCEPTION);
         this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getOp()));
      }

      public void caseGotoStmt(GotoStmt s) {
      }

      public void caseIdentityStmt(IdentityStmt s) {
      }

      public void caseIfStmt(IfStmt s) {
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getCondition()));
      }

      public void caseInvokeStmt(InvokeStmt s) {
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow((Value)s.getInvokeExpr()));
      }

      public void caseLookupSwitchStmt(LookupSwitchStmt s) {
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getKey()));
      }

      public void caseNopStmt(NopStmt s) {
      }

      public void caseRetStmt(RetStmt s) {
      }

      public void caseReturnStmt(ReturnStmt s) {
      }

      public void caseReturnVoidStmt(ReturnVoidStmt s) {
      }

      public void caseTableSwitchStmt(TableSwitchStmt s) {
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getKey()));
      }

      public void caseThrowStmt(ThrowStmt s) {
         this.result = UnitThrowAnalysis.this.mightThrowImplicitly(s);
         this.result = this.result.add(UnitThrowAnalysis.this.mightThrowExplicitly(s));
      }

      public void defaultCase(Object obj) {
      }
   }
}
