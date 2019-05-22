package polyglot.ext.jl.types;

import polyglot.main.Options;
import polyglot.types.PrimitiveType;
import polyglot.types.Resolver;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;

public class PrimitiveType_c extends Type_c implements PrimitiveType {
   protected PrimitiveType.Kind kind;

   protected PrimitiveType_c() {
   }

   public PrimitiveType_c(TypeSystem ts, PrimitiveType.Kind kind) {
      super(ts);
      this.kind = kind;
   }

   public PrimitiveType.Kind kind() {
      return this.kind;
   }

   public String toString() {
      return this.kind.toString();
   }

   public String translate(Resolver c) {
      String s = this.kind.toString();
      return Options.global.cppBackend() && s.equals("boolean") ? "bool" : s;
   }

   public boolean isCanonical() {
      return true;
   }

   public boolean isPrimitive() {
      return true;
   }

   public PrimitiveType toPrimitive() {
      return this;
   }

   public boolean isVoid() {
      return this.kind == PrimitiveType.VOID;
   }

   public boolean isBoolean() {
      return this.kind == PrimitiveType.BOOLEAN;
   }

   public boolean isChar() {
      return this.kind == PrimitiveType.CHAR;
   }

   public boolean isByte() {
      return this.kind == PrimitiveType.BYTE;
   }

   public boolean isShort() {
      return this.kind == PrimitiveType.SHORT;
   }

   public boolean isInt() {
      return this.kind == PrimitiveType.INT;
   }

   public boolean isLong() {
      return this.kind == PrimitiveType.LONG;
   }

   public boolean isFloat() {
      return this.kind == PrimitiveType.FLOAT;
   }

   public boolean isDouble() {
      return this.kind == PrimitiveType.DOUBLE;
   }

   public boolean isIntOrLess() {
      return this.kind == PrimitiveType.CHAR || this.kind == PrimitiveType.BYTE || this.kind == PrimitiveType.SHORT || this.kind == PrimitiveType.INT;
   }

   public boolean isLongOrLess() {
      return this.isIntOrLess() || this.kind == PrimitiveType.LONG;
   }

   public boolean isNumeric() {
      return this.isLongOrLess() || this.kind == PrimitiveType.FLOAT || this.kind == PrimitiveType.DOUBLE;
   }

   public int hashCode() {
      return this.kind.hashCode();
   }

   public boolean equalsImpl(TypeObject t) {
      if (t instanceof PrimitiveType) {
         PrimitiveType p = (PrimitiveType)t;
         return this.kind() == p.kind();
      } else {
         return false;
      }
   }

   public String wrapperTypeString(TypeSystem ts) {
      return ts.wrapperTypeString(this);
   }

   public String name() {
      return this.toString();
   }

   public String fullName() {
      return this.name();
   }

   public boolean descendsFromImpl(Type ancestor) {
      return false;
   }

   public boolean isImplicitCastValidImpl(Type toType) {
      if (!toType.isPrimitive()) {
         return false;
      } else {
         PrimitiveType t = toType.toPrimitive();
         if (t.isVoid()) {
            return false;
         } else if (this.isVoid()) {
            return false;
         } else if (this.ts.equals(t, this)) {
            return true;
         } else if (t.isBoolean()) {
            return this.isBoolean();
         } else if (this.isBoolean()) {
            return false;
         } else if (this.isNumeric() && t.isNumeric()) {
            if (t.isDouble()) {
               return true;
            } else if (this.isDouble()) {
               return false;
            } else if (t.isFloat()) {
               return true;
            } else if (this.isFloat()) {
               return false;
            } else if (t.isLong()) {
               return true;
            } else if (this.isLong()) {
               return false;
            } else if (t.isInt()) {
               return true;
            } else if (this.isInt()) {
               return false;
            } else if (!t.isShort()) {
               if (this.isShort()) {
                  return false;
               } else if (t.isChar()) {
                  return this.isChar();
               } else if (this.isChar()) {
                  return false;
               } else if (t.isByte()) {
                  return this.isByte();
               } else {
                  return this.isByte() ? false : false;
               }
            } else {
               return this.isShort() || this.isByte();
            }
         } else {
            return false;
         }
      }
   }

   public boolean isCastValidImpl(Type toType) {
      if (!this.isVoid() && !toType.isVoid()) {
         if (this.ts.equals(this, toType)) {
            return true;
         } else {
            return this.isNumeric() && toType.isNumeric();
         }
      } else {
         return false;
      }
   }

   public boolean numericConversionValidImpl(long value) {
      return this.numericConversionValidImpl(new Long(value));
   }

   public boolean numericConversionValidImpl(Object value) {
      if (value == null) {
         return false;
      } else if (!(value instanceof Float) && !(value instanceof Double)) {
         long var2;
         if (value instanceof Number) {
            var2 = ((Number)value).longValue();
         } else {
            if (!(value instanceof Character)) {
               return false;
            }

            var2 = (long)(Character)value;
         }

         if (this.isLong()) {
            return true;
         } else if (this.isInt()) {
            return -2147483648L <= var2 && var2 <= 2147483647L;
         } else if (this.isChar()) {
            return 0L <= var2 && var2 <= 65535L;
         } else if (this.isShort()) {
            return -32768L <= var2 && var2 <= 32767L;
         } else if (!this.isByte()) {
            return false;
         } else {
            return -128L <= var2 && var2 <= 127L;
         }
      } else {
         return false;
      }
   }
}
