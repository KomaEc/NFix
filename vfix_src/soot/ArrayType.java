package soot;

import soot.util.Switch;

public class ArrayType extends RefLikeType {
   public final Type baseType;
   public final int numDimensions;

   private ArrayType(Type baseType, int numDimensions) {
      if (!(baseType instanceof PrimType) && !(baseType instanceof RefType) && !(baseType instanceof NullType)) {
         throw new RuntimeException("oops,  base type must be PrimType or RefType but not '" + baseType + "'");
      } else if (numDimensions < 1) {
         throw new RuntimeException("attempt to create array with " + numDimensions + " dimensions");
      } else {
         this.baseType = baseType;
         this.numDimensions = numDimensions;
      }
   }

   public static ArrayType v(Type baseType, int numDimensions) {
      if (numDimensions < 0) {
         throw new RuntimeException("Invalid number of array dimensions: " + numDimensions);
      } else {
         int orgDimensions = numDimensions;

         Object elementType;
         for(elementType = baseType; numDimensions > 0; --numDimensions) {
            ArrayType ret = ((Type)elementType).getArrayType();
            if (ret == null) {
               ret = new ArrayType(baseType, orgDimensions - numDimensions + 1);
               ((Type)elementType).setArrayType(ret);
            }

            elementType = ret;
         }

         return (ArrayType)elementType;
      }
   }

   public boolean equals(Object t) {
      return t == this;
   }

   public void toString(UnitPrinter up) {
      up.type(this.baseType);

      for(int i = 0; i < this.numDimensions; ++i) {
         up.literal("[]");
      }

   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append(this.baseType.toString());

      for(int i = 0; i < this.numDimensions; ++i) {
         buffer.append("[]");
      }

      return buffer.toString();
   }

   public String toQuotedString() {
      StringBuilder buffer = new StringBuilder();
      buffer.append(this.baseType.toQuotedString());

      for(int i = 0; i < this.numDimensions; ++i) {
         buffer.append("[]");
      }

      return buffer.toString();
   }

   public int hashCode() {
      return this.baseType.hashCode() + 1127088961 * this.numDimensions;
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseArrayType(this);
   }

   public Type getArrayElementType() {
      return this.getElementType();
   }

   public Type getElementType() {
      return (Type)(this.numDimensions > 1 ? v(this.baseType, this.numDimensions - 1) : this.baseType);
   }

   public ArrayType makeArrayType() {
      return v(this.baseType, this.numDimensions + 1);
   }

   public boolean isAllowedInFinalCode() {
      return true;
   }
}
