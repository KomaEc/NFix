package soot;

import java.io.Serializable;
import soot.util.Numberable;
import soot.util.Switch;
import soot.util.Switchable;

public abstract class Type implements Switchable, Serializable, Numberable {
   protected ArrayType arrayType;
   private int number = 0;

   public Type() {
      Scene.v().getTypeNumberer().add((Numberable)this);
   }

   public abstract String toString();

   public String toQuotedString() {
      return this.toString();
   }

   /** @deprecated */
   @Deprecated
   public String getEscapedName() {
      return this.toQuotedString();
   }

   public static Type toMachineType(Type t) {
      return (Type)(!t.equals(ShortType.v()) && !t.equals(ByteType.v()) && !t.equals(BooleanType.v()) && !t.equals(CharType.v()) ? t : IntType.v());
   }

   public Type merge(Type other, Scene cm) {
      throw new RuntimeException("illegal type merge: " + this + " and " + other);
   }

   public void apply(Switch sw) {
   }

   public void setArrayType(ArrayType at) {
      this.arrayType = at;
   }

   public ArrayType getArrayType() {
      return this.arrayType;
   }

   public ArrayType makeArrayType() {
      return ArrayType.v(this, 1);
   }

   public boolean isAllowedInFinalCode() {
      return false;
   }

   public final int getNumber() {
      return this.number;
   }

   public final void setNumber(int number) {
      this.number = number;
   }
}
