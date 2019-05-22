package soot.sootify;

import soot.AnySubType;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.ErroneousType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.NullType;
import soot.RefType;
import soot.ShortType;
import soot.StmtAddressType;
import soot.Type;
import soot.TypeSwitch;
import soot.UnknownType;
import soot.VoidType;

public class TypeTemplatePrinter extends TypeSwitch {
   private String varName;
   private final TemplatePrinter p;

   public void printAssign(String v, Type t) {
      String oldName = this.varName;
      this.varName = v;
      t.apply(this);
      this.varName = oldName;
   }

   public TypeTemplatePrinter(TemplatePrinter p) {
      this.p = p;
   }

   public void setVariableName(String name) {
      this.varName = name;
   }

   private void emit(String rhs) {
      this.p.println("Type " + this.varName + " = " + rhs + ";");
   }

   private void emitSpecial(String type, String rhs) {
      this.p.println(type + " " + this.varName + " = " + rhs + ";");
   }

   public void caseAnySubType(AnySubType t) {
      throw new IllegalArgumentException("cannot print this type");
   }

   public void caseArrayType(ArrayType t) {
      this.printAssign("baseType", t.getElementType());
      this.p.println("int numDimensions=" + t.numDimensions + ";");
      this.emit("ArrayType.v(baseType, numDimensions)");
   }

   public void caseBooleanType(BooleanType t) {
      this.emit("BooleanType.v()");
   }

   public void caseByteType(ByteType t) {
      this.emit("ByteType.v()");
   }

   public void caseCharType(CharType t) {
      this.emit("CharType.v()");
   }

   public void defaultCase(Type t) {
      throw new IllegalArgumentException("cannot print this type");
   }

   public void caseDoubleType(DoubleType t) {
      this.emit("DoubleType.v()");
   }

   public void caseErroneousType(ErroneousType t) {
      throw new IllegalArgumentException("cannot print this type");
   }

   public void caseFloatType(FloatType t) {
      this.emit("FloatType.v()");
   }

   public void caseIntType(IntType t) {
      this.emit("IntType.v()");
   }

   public void caseLongType(LongType t) {
      this.emit("LongType.v()");
   }

   public void caseNullType(NullType t) {
      this.emit("NullType.v()");
   }

   public void caseRefType(RefType t) {
      this.emitSpecial("RefType", "RefType.v(\"" + t.getClassName() + "\")");
   }

   public void caseShortType(ShortType t) {
      this.emit("ShortType.v()");
   }

   public void caseStmtAddressType(StmtAddressType t) {
      throw new IllegalArgumentException("cannot print this type");
   }

   public void caseUnknownType(UnknownType t) {
      throw new IllegalArgumentException("cannot print this type");
   }

   public void caseVoidType(VoidType t) {
      this.emit("VoidType.v()");
   }
}
