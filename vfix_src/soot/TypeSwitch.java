package soot;

public class TypeSwitch implements ITypeSwitch {
   Object result;

   public void caseArrayType(ArrayType t) {
      this.defaultCase(t);
   }

   public void caseBooleanType(BooleanType t) {
      this.defaultCase(t);
   }

   public void caseByteType(ByteType t) {
      this.defaultCase(t);
   }

   public void caseCharType(CharType t) {
      this.defaultCase(t);
   }

   public void caseDoubleType(DoubleType t) {
      this.defaultCase(t);
   }

   public void caseFloatType(FloatType t) {
      this.defaultCase(t);
   }

   public void caseIntType(IntType t) {
      this.defaultCase(t);
   }

   public void caseLongType(LongType t) {
      this.defaultCase(t);
   }

   public void caseRefType(RefType t) {
      this.defaultCase(t);
   }

   public void caseShortType(ShortType t) {
      this.defaultCase(t);
   }

   public void caseStmtAddressType(StmtAddressType t) {
      this.defaultCase(t);
   }

   public void caseUnknownType(UnknownType t) {
      this.defaultCase(t);
   }

   public void caseVoidType(VoidType t) {
      this.defaultCase(t);
   }

   public void caseAnySubType(AnySubType t) {
      this.defaultCase(t);
   }

   public void caseNullType(NullType t) {
      this.defaultCase(t);
   }

   public void caseErroneousType(ErroneousType t) {
      this.defaultCase(t);
   }

   public void defaultCase(Type t) {
   }

   /** @deprecated */
   @Deprecated
   public void caseDefault(Type t) {
   }

   public void setResult(Object result) {
      this.result = result;
   }

   public Object getResult() {
      return this.result;
   }
}
