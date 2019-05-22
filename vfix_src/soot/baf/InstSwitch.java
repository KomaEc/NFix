package soot.baf;

import soot.util.Switch;

public interface InstSwitch extends Switch {
   void caseReturnVoidInst(ReturnVoidInst var1);

   void caseReturnInst(ReturnInst var1);

   void caseNopInst(NopInst var1);

   void caseGotoInst(GotoInst var1);

   void caseJSRInst(JSRInst var1);

   void casePushInst(PushInst var1);

   void casePopInst(PopInst var1);

   void caseIdentityInst(IdentityInst var1);

   void caseStoreInst(StoreInst var1);

   void caseLoadInst(LoadInst var1);

   void caseArrayWriteInst(ArrayWriteInst var1);

   void caseArrayReadInst(ArrayReadInst var1);

   void caseIfNullInst(IfNullInst var1);

   void caseIfNonNullInst(IfNonNullInst var1);

   void caseIfEqInst(IfEqInst var1);

   void caseIfNeInst(IfNeInst var1);

   void caseIfGtInst(IfGtInst var1);

   void caseIfGeInst(IfGeInst var1);

   void caseIfLtInst(IfLtInst var1);

   void caseIfLeInst(IfLeInst var1);

   void caseIfCmpEqInst(IfCmpEqInst var1);

   void caseIfCmpNeInst(IfCmpNeInst var1);

   void caseIfCmpGtInst(IfCmpGtInst var1);

   void caseIfCmpGeInst(IfCmpGeInst var1);

   void caseIfCmpLtInst(IfCmpLtInst var1);

   void caseIfCmpLeInst(IfCmpLeInst var1);

   void caseStaticGetInst(StaticGetInst var1);

   void caseStaticPutInst(StaticPutInst var1);

   void caseFieldGetInst(FieldGetInst var1);

   void caseFieldPutInst(FieldPutInst var1);

   void caseInstanceCastInst(InstanceCastInst var1);

   void caseInstanceOfInst(InstanceOfInst var1);

   void casePrimitiveCastInst(PrimitiveCastInst var1);

   void caseDynamicInvokeInst(DynamicInvokeInst var1);

   void caseStaticInvokeInst(StaticInvokeInst var1);

   void caseVirtualInvokeInst(VirtualInvokeInst var1);

   void caseInterfaceInvokeInst(InterfaceInvokeInst var1);

   void caseSpecialInvokeInst(SpecialInvokeInst var1);

   void caseThrowInst(ThrowInst var1);

   void caseAddInst(AddInst var1);

   void caseAndInst(AndInst var1);

   void caseOrInst(OrInst var1);

   void caseXorInst(XorInst var1);

   void caseArrayLengthInst(ArrayLengthInst var1);

   void caseCmpInst(CmpInst var1);

   void caseCmpgInst(CmpgInst var1);

   void caseCmplInst(CmplInst var1);

   void caseDivInst(DivInst var1);

   void caseIncInst(IncInst var1);

   void caseMulInst(MulInst var1);

   void caseRemInst(RemInst var1);

   void caseSubInst(SubInst var1);

   void caseShlInst(ShlInst var1);

   void caseShrInst(ShrInst var1);

   void caseUshrInst(UshrInst var1);

   void caseNewInst(NewInst var1);

   void caseNegInst(NegInst var1);

   void caseSwapInst(SwapInst var1);

   void caseDup1Inst(Dup1Inst var1);

   void caseDup2Inst(Dup2Inst var1);

   void caseDup1_x1Inst(Dup1_x1Inst var1);

   void caseDup1_x2Inst(Dup1_x2Inst var1);

   void caseDup2_x1Inst(Dup2_x1Inst var1);

   void caseDup2_x2Inst(Dup2_x2Inst var1);

   void caseNewArrayInst(NewArrayInst var1);

   void caseNewMultiArrayInst(NewMultiArrayInst var1);

   void caseLookupSwitchInst(LookupSwitchInst var1);

   void caseTableSwitchInst(TableSwitchInst var1);

   void caseEnterMonitorInst(EnterMonitorInst var1);

   void caseExitMonitorInst(ExitMonitorInst var1);
}
