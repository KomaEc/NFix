package soot.jimple;

import soot.Local;

public abstract class AbstractJimpleValueSwitch extends AbstractConstantSwitch implements JimpleValueSwitch {
   public void caseArrayRef(ArrayRef v) {
      this.defaultCase(v);
   }

   public void caseAddExpr(AddExpr v) {
      this.defaultCase(v);
   }

   public void caseAndExpr(AndExpr v) {
      this.defaultCase(v);
   }

   public void caseCmpExpr(CmpExpr v) {
      this.defaultCase(v);
   }

   public void caseCmpgExpr(CmpgExpr v) {
      this.defaultCase(v);
   }

   public void caseCmplExpr(CmplExpr v) {
      this.defaultCase(v);
   }

   public void caseDivExpr(DivExpr v) {
      this.defaultCase(v);
   }

   public void caseEqExpr(EqExpr v) {
      this.defaultCase(v);
   }

   public void caseGeExpr(GeExpr v) {
      this.defaultCase(v);
   }

   public void caseGtExpr(GtExpr v) {
      this.defaultCase(v);
   }

   public void caseLeExpr(LeExpr v) {
      this.defaultCase(v);
   }

   public void caseLtExpr(LtExpr v) {
      this.defaultCase(v);
   }

   public void caseMulExpr(MulExpr v) {
      this.defaultCase(v);
   }

   public void caseNeExpr(NeExpr v) {
      this.defaultCase(v);
   }

   public void caseOrExpr(OrExpr v) {
      this.defaultCase(v);
   }

   public void caseRemExpr(RemExpr v) {
      this.defaultCase(v);
   }

   public void caseShlExpr(ShlExpr v) {
      this.defaultCase(v);
   }

   public void caseShrExpr(ShrExpr v) {
      this.defaultCase(v);
   }

   public void caseSubExpr(SubExpr v) {
      this.defaultCase(v);
   }

   public void caseUshrExpr(UshrExpr v) {
      this.defaultCase(v);
   }

   public void caseXorExpr(XorExpr v) {
      this.defaultCase(v);
   }

   public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
      this.defaultCase(v);
   }

   public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
      this.defaultCase(v);
   }

   public void caseStaticInvokeExpr(StaticInvokeExpr v) {
      this.defaultCase(v);
   }

   public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
      this.defaultCase(v);
   }

   public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
      this.defaultCase(v);
   }

   public void caseCastExpr(CastExpr v) {
      this.defaultCase(v);
   }

   public void caseInstanceOfExpr(InstanceOfExpr v) {
      this.defaultCase(v);
   }

   public void caseNewArrayExpr(NewArrayExpr v) {
      this.defaultCase(v);
   }

   public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
      this.defaultCase(v);
   }

   public void caseNewExpr(NewExpr v) {
      this.defaultCase(v);
   }

   public void caseLengthExpr(LengthExpr v) {
      this.defaultCase(v);
   }

   public void caseNegExpr(NegExpr v) {
      this.defaultCase(v);
   }

   public void caseInstanceFieldRef(InstanceFieldRef v) {
      this.defaultCase(v);
   }

   public void caseLocal(Local v) {
      this.defaultCase(v);
   }

   public void caseParameterRef(ParameterRef v) {
      this.defaultCase(v);
   }

   public void caseCaughtExceptionRef(CaughtExceptionRef v) {
      this.defaultCase(v);
   }

   public void caseThisRef(ThisRef v) {
      this.defaultCase(v);
   }

   public void caseStaticFieldRef(StaticFieldRef v) {
      this.defaultCase(v);
   }
}
