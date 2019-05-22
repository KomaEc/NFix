package soot;

import soot.util.Switch;

interface ITypeSwitch extends Switch {
   void caseArrayType(ArrayType var1);

   void caseBooleanType(BooleanType var1);

   void caseByteType(ByteType var1);

   void caseCharType(CharType var1);

   void caseDoubleType(DoubleType var1);

   void caseFloatType(FloatType var1);

   void caseIntType(IntType var1);

   void caseLongType(LongType var1);

   void caseRefType(RefType var1);

   void caseShortType(ShortType var1);

   void caseStmtAddressType(StmtAddressType var1);

   void caseUnknownType(UnknownType var1);

   void caseVoidType(VoidType var1);

   void caseAnySubType(AnySubType var1);

   void caseNullType(NullType var1);

   void caseErroneousType(ErroneousType var1);

   void caseDefault(Type var1);
}
