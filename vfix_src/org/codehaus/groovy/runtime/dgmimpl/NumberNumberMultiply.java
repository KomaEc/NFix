package org.codehaus.groovy.runtime.dgmimpl;

import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.NumberMath;

public final class NumberNumberMultiply extends NumberNumberMetaMethod {
   public String getName() {
      return "multiply";
   }

   public Object invoke(Object object, Object[] arguments) {
      return NumberMath.multiply((Number)object, (Number)arguments[0]);
   }

   public static Number multiply(Number left, Number right) {
      return NumberMath.multiply(left, right);
   }

   public CallSite createPojoCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
      if (receiver instanceof Integer) {
         if (args[0] instanceof Integer) {
            return new NumberNumberMultiply.IntegerInteger(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Long) {
            return new NumberNumberMultiply.IntegerLong(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Float) {
            return new NumberNumberMultiply.IntegerFloat(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Double) {
            return new NumberNumberMultiply.IntegerDouble(site, metaClass, metaMethod, params, receiver, args);
         }
      }

      if (receiver instanceof Long) {
         if (args[0] instanceof Integer) {
            return new NumberNumberMultiply.LongInteger(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Long) {
            return new NumberNumberMultiply.LongLong(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Float) {
            return new NumberNumberMultiply.LongFloat(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Double) {
            return new NumberNumberMultiply.LongDouble(site, metaClass, metaMethod, params, receiver, args);
         }
      }

      if (receiver instanceof Float) {
         if (args[0] instanceof Integer) {
            return new NumberNumberMultiply.FloatInteger(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Long) {
            return new NumberNumberMultiply.FloatLong(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Float) {
            return new NumberNumberMultiply.FloatFloat(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Double) {
            return new NumberNumberMultiply.FloatDouble(site, metaClass, metaMethod, params, receiver, args);
         }
      }

      if (receiver instanceof Double) {
         if (args[0] instanceof Integer) {
            return new NumberNumberMultiply.DoubleInteger(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Long) {
            return new NumberNumberMultiply.DoubleLong(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Float) {
            return new NumberNumberMultiply.DoubleFloat(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Double) {
            return new NumberNumberMultiply.DoubleDouble(site, metaClass, metaMethod, params, receiver, args);
         }
      }

      return new NumberNumberMultiply.NumberNumber(site, metaClass, metaMethod, params, receiver, args);
   }

   private static class NumberNumber extends NumberNumberMetaMethod.NumberNumberCallSite {
      public NumberNumber(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object invoke(Object receiver, Object[] args) {
         return this.math.multiplyImpl((Number)receiver, (Number)args[0]);
      }

      public final Object invoke(Object receiver, Object arg) {
         return this.math.multiplyImpl((Number)receiver, (Number)arg);
      }
   }

   private static class IntegerInteger extends NumberNumberMetaMethod.NumberNumberCallSite {
      public IntegerInteger(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Integer((Integer)receiver * (Integer)arg);
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class IntegerLong extends NumberNumberMetaMethod.NumberNumberCallSite {
      public IntegerLong(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Long(((Integer)receiver).longValue() * (Long)arg);
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class IntegerFloat extends NumberNumberMetaMethod.NumberNumberCallSite {
      public IntegerFloat(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double(((Integer)receiver).doubleValue() * ((Float)arg).doubleValue());
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class IntegerDouble extends NumberNumberMetaMethod.NumberNumberCallSite {
      public IntegerDouble(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double(((Integer)receiver).doubleValue() * (Double)arg);
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class LongInteger extends NumberNumberMetaMethod.NumberNumberCallSite {
      public LongInteger(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Long((Long)receiver * ((Integer)arg).longValue());
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class LongLong extends NumberNumberMetaMethod.NumberNumberCallSite {
      public LongLong(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Long((Long)receiver * (Long)arg);
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class LongFloat extends NumberNumberMetaMethod.NumberNumberCallSite {
      public LongFloat(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double(((Long)receiver).doubleValue() * ((Float)arg).doubleValue());
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class LongDouble extends NumberNumberMetaMethod.NumberNumberCallSite {
      public LongDouble(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double(((Long)receiver).doubleValue() * (Double)arg);
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class FloatInteger extends NumberNumberMetaMethod.NumberNumberCallSite {
      public FloatInteger(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double(((Float)receiver).doubleValue() * ((Integer)arg).doubleValue());
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class FloatLong extends NumberNumberMetaMethod.NumberNumberCallSite {
      public FloatLong(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double(((Float)receiver).doubleValue() * ((Long)arg).doubleValue());
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class FloatFloat extends NumberNumberMetaMethod.NumberNumberCallSite {
      public FloatFloat(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double(((Float)receiver).doubleValue() * ((Float)arg).doubleValue());
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class FloatDouble extends NumberNumberMetaMethod.NumberNumberCallSite {
      public FloatDouble(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double(((Float)receiver).doubleValue() * (Double)arg);
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class DoubleInteger extends NumberNumberMetaMethod.NumberNumberCallSite {
      public DoubleInteger(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double((Double)receiver * (double)(Integer)arg);
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class DoubleLong extends NumberNumberMetaMethod.NumberNumberCallSite {
      public DoubleLong(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double((Double)receiver * ((Long)arg).doubleValue());
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class DoubleFloat extends NumberNumberMetaMethod.NumberNumberCallSite {
      public DoubleFloat(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double((Double)receiver * ((Float)arg).doubleValue());
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }

   private static class DoubleDouble extends NumberNumberMetaMethod.NumberNumberCallSite {
      public DoubleDouble(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double((Double)receiver * (Double)arg);
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }
}
