package soot.jbco.bafTransformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.IntegerType;
import soot.Local;
import soot.LongType;
import soot.PatchingChain;
import soot.RefLikeType;
import soot.RefType;
import soot.SootMethod;
import soot.StmtAddressType;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.VoidType;
import soot.baf.AddInst;
import soot.baf.AndInst;
import soot.baf.ArrayLengthInst;
import soot.baf.ArrayReadInst;
import soot.baf.ArrayWriteInst;
import soot.baf.BafBody;
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
import soot.baf.Inst;
import soot.baf.InstSwitch;
import soot.baf.InstanceCastInst;
import soot.baf.InstanceOfInst;
import soot.baf.InterfaceInvokeInst;
import soot.baf.JSRInst;
import soot.baf.LoadInst;
import soot.baf.LookupSwitchInst;
import soot.baf.MethodArgInst;
import soot.baf.MulInst;
import soot.baf.NegInst;
import soot.baf.NewArrayInst;
import soot.baf.NewInst;
import soot.baf.NewMultiArrayInst;
import soot.baf.NopInst;
import soot.baf.OpTypeArgInst;
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
import soot.baf.TargetArgInst;
import soot.baf.ThrowInst;
import soot.baf.UshrInst;
import soot.baf.VirtualInvokeInst;
import soot.baf.XorInst;
import soot.baf.internal.AbstractOpTypeInst;
import soot.baf.internal.BPopInst;
import soot.jbco.util.Debugger;
import soot.toolkits.graph.BriefUnitGraph;
import soot.util.Chain;

public class StackTypeHeightCalculator {
   private static final Logger logger = LoggerFactory.getLogger(StackTypeHeightCalculator.class);
   public static StackTypeHeightCalculator.StackEffectSwitch sw = new StackTypeHeightCalculator().new StackEffectSwitch();
   public static BriefUnitGraph bug = null;

   public static Map<Unit, Stack<Type>> calculateStackHeights(Body b, Map<Local, Local> b2JLocs) {
      sw.bafToJLocals = b2JLocs;
      return calculateStackHeights(b, true);
   }

   public static Map<Unit, Stack<Type>> calculateStackHeights(Body b) {
      sw.bafToJLocals = null;
      return calculateStackHeights(b, false);
   }

   public static Map<Unit, Stack<Type>> calculateStackHeights(Body b, boolean jimpleLocals) {
      if (!(b instanceof BafBody)) {
         throw new RuntimeException("Expecting Baf Body");
      } else {
         Map<Unit, Stack<Type>> results = new HashMap();
         bug = new BriefUnitGraph(b);
         List<Unit> heads = bug.getHeads();

         for(int i = 0; i < heads.size(); ++i) {
            Unit h = (Unit)heads.get(i);
            RefType handlerExc = isHandlerUnit(b.getTraps(), h);
            Stack<Type> stack = (Stack)results.get(h);
            if (stack != null) {
               if (stack.size() != (handlerExc != null ? 1 : 0)) {
                  throw new RuntimeException("Problem with stack height - head expects ZERO or one if handler");
               }
            } else {
               List<Unit> worklist = new ArrayList();
               stack = new Stack();
               if (handlerExc != null) {
                  stack.push(handlerExc);
               }

               results.put(h, stack);
               worklist.add(h);

               while(!worklist.isEmpty()) {
                  Inst inst = (Inst)worklist.remove(0);
                  inst.apply(sw);
                  stack = updateStack(sw, (Stack)results.get(inst));
                  Iterator lit = bug.getSuccsOf(inst).iterator();

                  while(lit.hasNext()) {
                     Unit next = (Unit)lit.next();
                     Stack<Type> nxtStck = (Stack)results.get(next);
                     if (nxtStck != null) {
                        if (nxtStck.size() != stack.size()) {
                           printStack(b.getUnits(), results, false);
                           throw new RuntimeException("Problem with stack height at: " + next + "\n\rHas Stack " + nxtStck + " but is expecting " + stack);
                        }
                     } else {
                        results.put(next, stack);
                        worklist.add(next);
                     }
                  }
               }
            }
         }

         return results;
      }
   }

   public static Stack<Type> updateStack(Unit u, Stack<Type> st) {
      u.apply(sw);
      return updateStack(sw, st);
   }

   public static Stack<Type> updateStack(StackTypeHeightCalculator.StackEffectSwitch sw, Stack<Type> st) {
      Stack<Type> clone = (Stack)st.clone();
      int var5;
      int i;
      if (sw.remove_types != null) {
         if (sw.remove_types.length > clone.size()) {
            String exc = "Expecting values on stack: ";
            Type[] var4 = sw.remove_types;
            var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Type element = var4[var6];
               String type = element.toString();
               if (type.trim().length() == 0) {
                  type = element instanceof RefLikeType ? "L" : "U";
               }

               exc = exc + type + "  ";
            }

            exc = exc + "\n\tbut only found: ";

            for(i = 0; i < clone.size(); ++i) {
               String type = ((Type)clone.get(i)).toString();
               if (type.trim().length() == 0) {
                  type = clone.get(i) instanceof RefLikeType ? "L" : "U";
               }

               exc = exc + type + "  ";
            }

            if (sw.shouldThrow) {
               throw new RuntimeException(exc);
            }

            logger.debug("" + exc);
         }

         for(int i = sw.remove_types.length - 1; i >= 0; --i) {
            try {
               Type t = (Type)clone.pop();
               if (!checkTypes(t, sw.remove_types[i])) {
               }
            } catch (Exception var9) {
               return null;
            }
         }
      }

      if (sw.add_types != null) {
         Type[] var12 = sw.add_types;
         i = var12.length;

         for(var5 = 0; var5 < i; ++var5) {
            Type element = var12[var5];
            clone.push(element);
         }
      }

      return clone;
   }

   private static boolean checkTypes(Type t1, Type t2) {
      if (t1 == t2) {
         return true;
      } else if (t1 instanceof RefLikeType && t2 instanceof RefLikeType) {
         return true;
      } else if (t1 instanceof IntegerType && t2 instanceof IntegerType) {
         return true;
      } else if (t1 instanceof LongType && t2 instanceof LongType) {
         return true;
      } else if (t1 instanceof DoubleType && t2 instanceof DoubleType) {
         return true;
      } else {
         return t1 instanceof FloatType && t2 instanceof FloatType;
      }
   }

   public static void printStack(PatchingChain<Unit> units, Map<Unit, Stack<Type>> stacks, boolean before) {
      int count = 0;
      sw.shouldThrow = false;
      Map<Unit, Integer> indexes = new HashMap();
      Iterator it = units.snapshotIterator();

      while(it.hasNext()) {
         indexes.put(it.next(), new Integer(count++));
      }

      String s;
      for(it = units.snapshotIterator(); it.hasNext(); System.out.println(s + "]")) {
         s = "";
         Unit unit = (Unit)it.next();
         int i;
         if (unit instanceof TargetArgInst) {
            Object t = ((TargetArgInst)unit).getTarget();
            s = ((Integer)indexes.get(t)).toString();
         } else if (unit instanceof TableSwitchInst) {
            TableSwitchInst tswi = (TableSwitchInst)unit;
            s = s + "\r\tdefault: " + tswi.getDefaultTarget() + "  " + indexes.get(tswi.getDefaultTarget());
            i = 0;

            for(int x = tswi.getLowIndex(); x <= tswi.getHighIndex(); ++x) {
               s = s + "\r\t " + x + ": " + tswi.getTarget(i) + "  " + indexes.get(tswi.getTarget(i++));
            }
         }

         try {
            s = indexes.get(unit) + " " + unit + "  " + s + "   [";
         } catch (Exception var11) {
            logger.debug("Error in StackTypeHeightCalculator trying to find index of unit");
         }

         Stack<Type> stack = (Stack)stacks.get(unit);
         if (stack != null) {
            if (!before) {
               unit.apply(sw);
               stack = updateStack(sw, stack);
               if (stack == null) {
                  Debugger.printUnits(units, " StackTypeHeightCalc failed");
                  sw.shouldThrow = true;
                  return;
               }
            }

            for(i = 0; i < stack.size(); ++i) {
               s = s + printType((Type)stack.get(i));
            }
         } else {
            s = s + "***missing***";
         }
      }

      sw.shouldThrow = true;
   }

   private static String printType(Type t) {
      if (t instanceof IntegerType) {
         return "I";
      } else if (t instanceof FloatType) {
         return "F";
      } else if (t instanceof DoubleType) {
         return "D";
      } else if (t instanceof LongType) {
         return "J";
      } else {
         return t instanceof RefLikeType ? "L" + t.toString() : "U(" + t.getClass().toString() + ")";
      }
   }

   private static RefType isHandlerUnit(Chain<Trap> traps, Unit h) {
      Iterator it = traps.iterator();

      Trap t;
      do {
         if (!it.hasNext()) {
            return null;
         }

         t = (Trap)it.next();
      } while(t.getHandlerUnit() != h);

      return t.getException().getType();
   }

   public static Stack<Type> getAfterStack(Body b, Unit u) {
      Stack<Type> stack = (Stack)calculateStackHeights(b).get(u);
      u.apply(sw);
      return updateStack(sw, stack);
   }

   public static Stack<Type> getAfterStack(Stack<Type> beforeStack, Unit u) {
      u.apply(sw);
      return updateStack(sw, beforeStack);
   }

   protected class StackEffectSwitch implements InstSwitch {
      public boolean shouldThrow = true;
      public Map<Local, Local> bafToJLocals = null;
      public Type[] remove_types = null;
      public Type[] add_types = null;

      public void caseReturnInst(ReturnInst i) {
         this.remove_types = new Type[]{i.getOpType()};
         this.add_types = null;
      }

      public void caseReturnVoidInst(ReturnVoidInst i) {
         this.remove_types = null;
         this.add_types = null;
      }

      public void caseNopInst(NopInst i) {
         this.remove_types = null;
         this.add_types = null;
      }

      public void caseGotoInst(GotoInst i) {
         this.remove_types = null;
         this.add_types = null;
      }

      public void caseJSRInst(JSRInst i) {
         this.remove_types = null;
         this.add_types = new Type[]{StmtAddressType.v()};
      }

      public void casePushInst(PushInst i) {
         this.remove_types = null;
         this.add_types = new Type[]{i.getConstant().getType()};
      }

      public void casePopInst(PopInst i) {
         this.remove_types = new Type[]{((BPopInst)i).getType()};
         this.add_types = null;
      }

      public void caseIdentityInst(IdentityInst i) {
         this.remove_types = null;
         this.add_types = null;
      }

      public void caseStoreInst(StoreInst i) {
         this.remove_types = new Type[]{((AbstractOpTypeInst)i).getOpType()};
         this.add_types = null;
      }

      public void caseLoadInst(LoadInst i) {
         this.remove_types = null;
         this.add_types = null;
         if (this.bafToJLocals != null) {
            Local jl = (Local)this.bafToJLocals.get(i.getLocal());
            if (jl != null) {
               this.add_types = new Type[]{jl.getType()};
            }
         }

         if (this.add_types == null) {
            this.add_types = new Type[]{i.getOpType()};
         }

      }

      public void caseArrayWriteInst(ArrayWriteInst i) {
         this.remove_types = new Type[]{RefType.v(), IntType.v(), i.getOpType()};
         this.add_types = null;
      }

      public void caseArrayReadInst(ArrayReadInst i) {
         this.remove_types = new Type[]{RefType.v(), IntType.v()};
         this.add_types = new Type[]{i.getOpType()};
      }

      public void caseIfNullInst(IfNullInst i) {
         this.remove_types = new Type[]{RefType.v("java.lang.Object")};
         this.add_types = null;
      }

      public void caseIfNonNullInst(IfNonNullInst i) {
         this.remove_types = new Type[]{RefType.v("java.lang.Object")};
         this.add_types = null;
      }

      public void caseIfEqInst(IfEqInst i) {
         this.remove_types = new Type[]{IntType.v()};
         this.add_types = null;
      }

      public void caseIfNeInst(IfNeInst i) {
         this.remove_types = new Type[]{IntType.v()};
         this.add_types = null;
      }

      public void caseIfGtInst(IfGtInst i) {
         this.remove_types = new Type[]{IntType.v()};
         this.add_types = null;
      }

      public void caseIfGeInst(IfGeInst i) {
         this.remove_types = new Type[]{IntType.v()};
         this.add_types = null;
      }

      public void caseIfLtInst(IfLtInst i) {
         this.remove_types = new Type[]{IntType.v()};
         this.add_types = null;
      }

      public void caseIfLeInst(IfLeInst i) {
         this.remove_types = new Type[]{IntType.v()};
         this.add_types = null;
      }

      public void caseIfCmpEqInst(IfCmpEqInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = null;
      }

      public void caseIfCmpNeInst(IfCmpNeInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = null;
      }

      public void caseIfCmpGtInst(IfCmpGtInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = null;
      }

      public void caseIfCmpGeInst(IfCmpGeInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = null;
      }

      public void caseIfCmpLtInst(IfCmpLtInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = null;
      }

      public void caseIfCmpLeInst(IfCmpLeInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = null;
      }

      public void caseStaticGetInst(StaticGetInst i) {
         this.remove_types = null;
         this.add_types = new Type[]{i.getField().getType()};
      }

      public void caseStaticPutInst(StaticPutInst i) {
         this.remove_types = new Type[]{i.getField().getType()};
         this.add_types = null;
      }

      public void caseFieldGetInst(FieldGetInst i) {
         this.remove_types = new Type[]{i.getField().getDeclaringClass().getType()};
         this.add_types = new Type[]{i.getField().getType()};
      }

      public void caseFieldPutInst(FieldPutInst i) {
         this.remove_types = new Type[]{i.getField().getDeclaringClass().getType(), i.getField().getType()};
         this.add_types = null;
      }

      public void caseInstanceCastInst(InstanceCastInst i) {
         this.remove_types = new Type[]{RefType.v("java.lang.Object")};
         this.add_types = new Type[]{i.getCastType()};
      }

      public void caseInstanceOfInst(InstanceOfInst i) {
         this.remove_types = new Type[]{RefType.v("java.lang.Object")};
         this.add_types = new Type[]{IntType.v()};
      }

      public void casePrimitiveCastInst(PrimitiveCastInst i) {
         this.remove_types = new Type[]{i.getFromType()};
         this.add_types = new Type[]{i.getToType()};
      }

      public void caseDynamicInvokeInst(DynamicInvokeInst i) {
         SootMethod m = i.getMethod();
         Object[] args = m.getParameterTypes().toArray();
         this.remove_types = new Type[args.length];

         for(int ii = 0; ii < args.length; ++ii) {
            this.remove_types[ii] = (Type)args[ii];
         }

         if (m.getReturnType() instanceof VoidType) {
            this.add_types = null;
         } else {
            this.add_types = new Type[]{m.getReturnType()};
         }

      }

      public void caseStaticInvokeInst(StaticInvokeInst i) {
         SootMethod m = i.getMethod();
         Object[] args = m.getParameterTypes().toArray();
         this.remove_types = new Type[args.length];

         for(int ii = 0; ii < args.length; ++ii) {
            this.remove_types[ii] = (Type)args[ii];
         }

         if (m.getReturnType() instanceof VoidType) {
            this.add_types = null;
         } else {
            this.add_types = new Type[]{m.getReturnType()};
         }

      }

      private void instanceinvoke(MethodArgInst i) {
         SootMethod m = i.getMethod();
         int length = m.getParameterCount();
         this.remove_types = new Type[length + 1];
         this.remove_types[0] = RefType.v();
         System.arraycopy(m.getParameterTypes().toArray(), 0, this.remove_types, 1, length);
         if (m.getReturnType() instanceof VoidType) {
            this.add_types = null;
         } else {
            this.add_types = new Type[]{m.getReturnType()};
         }

      }

      public void caseVirtualInvokeInst(VirtualInvokeInst i) {
         this.instanceinvoke(i);
      }

      public void caseInterfaceInvokeInst(InterfaceInvokeInst i) {
         this.instanceinvoke(i);
      }

      public void caseSpecialInvokeInst(SpecialInvokeInst i) {
         this.instanceinvoke(i);
      }

      public void caseThrowInst(ThrowInst i) {
         this.remove_types = new Type[]{RefType.v("java.lang.Throwable")};
         this.add_types = null;
      }

      public void caseAddInst(AddInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = new Type[]{i.getOpType()};
      }

      private void bitOps(OpTypeArgInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = new Type[]{i.getOpType()};
      }

      public void caseAndInst(AndInst i) {
         this.bitOps(i);
      }

      public void caseOrInst(OrInst i) {
         this.bitOps(i);
      }

      public void caseXorInst(XorInst i) {
         this.bitOps(i);
      }

      public void caseArrayLengthInst(ArrayLengthInst i) {
         this.remove_types = new Type[]{RefType.v()};
         this.add_types = new Type[]{IntType.v()};
      }

      public void caseCmpInst(CmpInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = new Type[]{IntType.v()};
      }

      public void caseCmpgInst(CmpgInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = new Type[]{IntType.v()};
      }

      public void caseCmplInst(CmplInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = new Type[]{IntType.v()};
      }

      public void caseDivInst(DivInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = new Type[]{i.getOpType()};
      }

      public void caseIncInst(IncInst i) {
         this.remove_types = null;
         this.add_types = null;
      }

      public void caseMulInst(MulInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = new Type[]{i.getOpType()};
      }

      public void caseRemInst(RemInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = new Type[]{i.getOpType()};
      }

      public void caseSubInst(SubInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = new Type[]{i.getOpType()};
      }

      public void caseShlInst(ShlInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = new Type[]{i.getOpType()};
      }

      public void caseShrInst(ShrInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = new Type[]{i.getOpType()};
      }

      public void caseUshrInst(UshrInst i) {
         this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
         this.add_types = new Type[]{i.getOpType()};
      }

      public void caseNewInst(NewInst i) {
         this.remove_types = null;
         this.add_types = new Type[]{i.getBaseType()};
      }

      public void caseNegInst(NegInst i) {
         this.remove_types = null;
         this.add_types = null;
      }

      public void caseSwapInst(SwapInst i) {
         this.remove_types = new Type[]{i.getFromType(), i.getToType()};
         this.add_types = new Type[]{i.getToType(), i.getFromType()};
      }

      public void caseDup1Inst(Dup1Inst i) {
         this.remove_types = new Type[]{i.getOp1Type()};
         this.add_types = new Type[]{i.getOp1Type(), i.getOp1Type()};
      }

      public void caseDup2Inst(Dup2Inst i) {
         if (!(i.getOp1Type() instanceof DoubleType) && !(i.getOp1Type() instanceof LongType)) {
            this.add_types = new Type[]{i.getOp2Type(), i.getOp1Type()};
            this.remove_types = null;
         } else {
            this.add_types = new Type[]{i.getOp1Type()};
            this.remove_types = null;
         }

      }

      public void caseDup1_x1Inst(Dup1_x1Inst i) {
         this.remove_types = new Type[]{i.getUnder1Type(), i.getOp1Type()};
         this.add_types = new Type[]{i.getOp1Type(), i.getUnder1Type(), i.getOp1Type()};
      }

      public void caseDup1_x2Inst(Dup1_x2Inst i) {
         Type u1 = i.getUnder1Type();
         if (!(u1 instanceof DoubleType) && !(u1 instanceof LongType)) {
            this.remove_types = new Type[]{i.getUnder2Type(), u1, i.getOp1Type()};
            this.add_types = new Type[]{i.getOp1Type(), i.getUnder2Type(), u1, i.getOp1Type()};
         } else {
            this.remove_types = new Type[]{u1, i.getOp1Type()};
            this.add_types = new Type[]{i.getOp1Type(), u1, i.getOp1Type()};
         }

      }

      public void caseDup2_x1Inst(Dup2_x1Inst i) {
         Type ot = i.getOp1Type();
         if (!(ot instanceof DoubleType) && !(ot instanceof LongType)) {
            this.remove_types = new Type[]{i.getUnder1Type(), i.getOp2Type(), ot};
            this.add_types = new Type[]{i.getOp2Type(), ot, i.getUnder1Type(), i.getOp2Type(), ot};
         } else {
            this.remove_types = new Type[]{i.getUnder1Type(), ot};
            this.add_types = new Type[]{ot, i.getUnder1Type(), ot};
         }

      }

      public void caseDup2_x2Inst(Dup2_x2Inst i) {
         Type u1 = i.getUnder1Type();
         Type o1 = i.getOp1Type();
         if (!(u1 instanceof DoubleType) && !(u1 instanceof LongType)) {
            if (!(o1 instanceof DoubleType) && !(o1 instanceof LongType)) {
               this.remove_types = new Type[]{i.getUnder2Type(), u1, i.getOp2Type(), o1};
               this.add_types = new Type[]{i.getOp2Type(), o1, i.getUnder2Type(), u1, i.getOp2Type(), o1};
            } else {
               this.remove_types = new Type[]{i.getUnder2Type(), u1, o1};
               this.add_types = new Type[]{o1, i.getUnder2Type(), u1, o1};
            }
         } else if (!(o1 instanceof DoubleType) && !(o1 instanceof LongType)) {
            this.remove_types = new Type[]{u1, i.getOp2Type(), o1};
            this.add_types = new Type[]{i.getOp2Type(), o1, u1, i.getOp2Type(), o1};
         } else {
            this.remove_types = new Type[]{u1, o1};
            this.add_types = new Type[]{o1, u1, o1};
         }

      }

      public void caseNewArrayInst(NewArrayInst i) {
         this.remove_types = new Type[]{IntType.v()};
         this.add_types = new Type[]{RefType.v()};
      }

      public void caseNewMultiArrayInst(NewMultiArrayInst i) {
         this.remove_types = new Type[i.getDimensionCount()];

         for(int ii = 0; ii < this.remove_types.length; ++ii) {
            this.remove_types[ii] = IntType.v();
         }

         this.add_types = new Type[]{RefType.v()};
      }

      public void caseLookupSwitchInst(LookupSwitchInst i) {
         this.remove_types = new Type[]{IntType.v()};
         this.add_types = null;
      }

      public void caseTableSwitchInst(TableSwitchInst i) {
         this.remove_types = new Type[]{IntType.v()};
         this.add_types = null;
      }

      public void caseEnterMonitorInst(EnterMonitorInst i) {
         this.remove_types = new Type[]{RefType.v("java.lang.Object")};
         this.add_types = null;
      }

      public void caseExitMonitorInst(ExitMonitorInst i) {
         this.remove_types = new Type[]{RefType.v("java.lang.Object")};
         this.add_types = null;
      }
   }
}
