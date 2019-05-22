package soot.dava.toolkits.base.AST.structuredAnalysis;

import soot.BooleanType;
import soot.Value;
import soot.dava.internal.javaRep.DIntConstant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;

public class CPHelper {
   public static Object wrapperClassCloner(Object value) {
      if (value instanceof Double) {
         return new Double((Double)value);
      } else if (value instanceof Float) {
         return new Float((Float)value);
      } else if (value instanceof Long) {
         return new Long((Long)value);
      } else if (value instanceof Boolean) {
         return new Boolean((Boolean)value);
      } else {
         return value instanceof Integer ? new Integer((Integer)value) : null;
      }
   }

   public static Object isAConstantValue(Value toCheck) {
      Object value = null;
      if (toCheck instanceof LongConstant) {
         value = new Long(((LongConstant)toCheck).value);
      } else if (toCheck instanceof DoubleConstant) {
         value = new Double(((DoubleConstant)toCheck).value);
      } else if (toCheck instanceof FloatConstant) {
         value = new Float(((FloatConstant)toCheck).value);
      } else if (toCheck instanceof IntConstant) {
         int val = ((IntConstant)toCheck).value;
         value = new Integer(val);
      }

      return value;
   }

   public static Value createConstant(Object toConvert) {
      if (toConvert instanceof Long) {
         return LongConstant.v((Long)toConvert);
      } else if (toConvert instanceof Double) {
         return DoubleConstant.v((Double)toConvert);
      } else if (toConvert instanceof Boolean) {
         boolean val = (Boolean)toConvert;
         return val ? DIntConstant.v(1, BooleanType.v()) : DIntConstant.v(0, BooleanType.v());
      } else if (toConvert instanceof Float) {
         return FloatConstant.v((Float)toConvert);
      } else {
         return toConvert instanceof Integer ? IntConstant.v((Integer)toConvert) : null;
      }
   }
}
