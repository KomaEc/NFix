package soot.jimple;

import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.PrimType;
import soot.RefType;
import soot.ShortType;
import soot.Type;
import soot.util.StringTools;
import soot.util.Switch;

public class ClassConstant extends Constant {
   public final String value;

   private ClassConstant(String s) {
      this.value = s;
   }

   public static ClassConstant v(String value) {
      if (value.contains(".")) {
         throw new RuntimeException("ClassConstants must use class names separated by '/', not '.'!");
      } else {
         return new ClassConstant(value);
      }
   }

   public static ClassConstant fromType(Type tp) {
      return v(sootTypeToString(tp));
   }

   private static String sootTypeToString(Type tp) {
      if (tp instanceof RefType) {
         return "L" + ((RefType)tp).getClassName().replaceAll("\\.", "/") + ";";
      } else if (tp instanceof ArrayType) {
         ArrayType at = (ArrayType)tp;
         return "[" + sootTypeToString(at.getElementType());
      } else if (tp instanceof PrimType) {
         if (tp instanceof IntType) {
            return "I";
         } else if (tp instanceof ByteType) {
            return "B";
         } else if (tp instanceof CharType) {
            return "C";
         } else if (tp instanceof DoubleType) {
            return "D";
         } else if (tp instanceof FloatType) {
            return "F";
         } else if (tp instanceof LongType) {
            return "L";
         } else if (tp instanceof ShortType) {
            return "S";
         } else if (tp instanceof BooleanType) {
            return "Z";
         } else {
            throw new RuntimeException("Unsupported primitive type");
         }
      } else {
         throw new RuntimeException("Unsupported type" + tp);
      }
   }

   public boolean isRefType() {
      return this.value.startsWith("L") && this.value.endsWith(";");
   }

   public Type toSootType() {
      int numDimensions = 0;

      String tmp;
      for(tmp = this.value; tmp.startsWith("["); tmp = tmp.substring(1)) {
         ++numDimensions;
      }

      Type baseType = null;
      if (tmp.startsWith("L")) {
         tmp = tmp.substring(1);
         if (tmp.endsWith(";")) {
            tmp = tmp.substring(0, tmp.length() - 1);
         }

         tmp = tmp.replace("/", ".");
         baseType = RefType.v(tmp);
      } else if (tmp.equals("I")) {
         baseType = IntType.v();
      } else if (tmp.equals("B")) {
         baseType = ByteType.v();
      } else if (tmp.equals("C")) {
         baseType = CharType.v();
      } else if (tmp.equals("D")) {
         baseType = DoubleType.v();
      } else if (tmp.equals("F")) {
         baseType = FloatType.v();
      } else if (tmp.equals("L")) {
         baseType = LongType.v();
      } else if (tmp.equals("S")) {
         baseType = ShortType.v();
      } else {
         if (!tmp.equals("Z")) {
            throw new RuntimeException("Unsupported class constant: " + this.value);
         }

         baseType = BooleanType.v();
      }

      return (Type)(numDimensions > 0 ? ArrayType.v((Type)baseType, numDimensions) : baseType);
   }

   public boolean equals(Object c) {
      return c instanceof ClassConstant && ((ClassConstant)c).value.equals(this.value);
   }

   public int hashCode() {
      return this.value.hashCode();
   }

   public String toString() {
      return "class " + StringTools.getQuotedStringOf(this.value);
   }

   public String getValue() {
      return this.value;
   }

   public Type getType() {
      return RefType.v("java.lang.Class");
   }

   public void apply(Switch sw) {
      ((ConstantSwitch)sw).caseClassConstant(this);
   }
}
