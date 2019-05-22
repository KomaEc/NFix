package soot.jimple;

import soot.util.Switch;

public interface ExprSwitch extends Switch {
   void caseAddExpr(AddExpr var1);

   void caseAndExpr(AndExpr var1);

   void caseCmpExpr(CmpExpr var1);

   void caseCmpgExpr(CmpgExpr var1);

   void caseCmplExpr(CmplExpr var1);

   void caseDivExpr(DivExpr var1);

   void caseEqExpr(EqExpr var1);

   void caseNeExpr(NeExpr var1);

   void caseGeExpr(GeExpr var1);

   void caseGtExpr(GtExpr var1);

   void caseLeExpr(LeExpr var1);

   void caseLtExpr(LtExpr var1);

   void caseMulExpr(MulExpr var1);

   void caseOrExpr(OrExpr var1);

   void caseRemExpr(RemExpr var1);

   void caseShlExpr(ShlExpr var1);

   void caseShrExpr(ShrExpr var1);

   void caseUshrExpr(UshrExpr var1);

   void caseSubExpr(SubExpr var1);

   void caseXorExpr(XorExpr var1);

   void caseInterfaceInvokeExpr(InterfaceInvokeExpr var1);

   void caseSpecialInvokeExpr(SpecialInvokeExpr var1);

   void caseStaticInvokeExpr(StaticInvokeExpr var1);

   void caseVirtualInvokeExpr(VirtualInvokeExpr var1);

   void caseDynamicInvokeExpr(DynamicInvokeExpr var1);

   void caseCastExpr(CastExpr var1);

   void caseInstanceOfExpr(InstanceOfExpr var1);

   void caseNewArrayExpr(NewArrayExpr var1);

   void caseNewMultiArrayExpr(NewMultiArrayExpr var1);

   void caseNewExpr(NewExpr var1);

   void caseLengthExpr(LengthExpr var1);

   void caseNegExpr(NegExpr var1);

   void defaultCase(Object var1);
}
