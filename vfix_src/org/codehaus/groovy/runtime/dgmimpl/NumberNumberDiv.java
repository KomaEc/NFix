package org.codehaus.groovy.runtime.dgmimpl;

import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.NumberMath;

public final class NumberNumberDiv extends NumberNumberMetaMethod {
   public String getName() {
      return "div";
   }

   public Object invoke(Object object, Object[] arguments) {
      return NumberMath.divide((Number)object, (Number)arguments[0]);
   }

   public static Number div(Number left, Number right) {
      return NumberMath.divide(left, right);
   }

   public CallSite createPojoCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
      if (receiver instanceof Integer) {
         if (args[0] instanceof Float) {
            return new NumberNumberDiv.IntegerFloat(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Double) {
            return new NumberNumberDiv.IntegerDouble(site, metaClass, metaMethod, params, receiver, args);
         }
      }

      if (receiver instanceof Long) {
         if (args[0] instanceof Float) {
            return new NumberNumberDiv.LongFloat(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Double) {
            return new NumberNumberDiv.LongDouble(site, metaClass, metaMethod, params, receiver, args);
         }
      }

      if (receiver instanceof Float) {
         if (args[0] instanceof Integer) {
            return new NumberNumberDiv.FloatInteger(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Long) {
            return new NumberNumberDiv.FloatLong(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Float) {
            return new NumberNumberDiv.FloatFloat(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Double) {
            return new NumberNumberDiv.FloatDouble(site, metaClass, metaMethod, params, receiver, args);
         }
      }

      if (receiver instanceof Double) {
         if (args[0] instanceof Integer) {
            return new NumberNumberDiv.DoubleInteger(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Long) {
            return new NumberNumberDiv.DoubleLong(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Float) {
            return new NumberNumberDiv.DoubleFloat(site, metaClass, metaMethod, params, receiver, args);
         }

         if (args[0] instanceof Double) {
            return new NumberNumberDiv.DoubleDouble(site, metaClass, metaMethod, params, receiver, args);
         }
      }

      return new NumberNumberDiv.NumberNumber(site, metaClass, metaMethod, params, receiver, args);
   }

   private static class NumberNumber extends NumberNumberMetaMethod.NumberNumberCallSite {
      public NumberNumber(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object invoke(Object receiver, Object[] args) {
         return this.math.divideImpl((Number)receiver, (Number)args[0]);
      }

      public final Object invoke(Object receiver, Object arg) {
         return this.math.divideImpl((Number)receiver, (Number)arg);
      }
   }

   private static class DoubleDouble extends NumberNumberMetaMethod.NumberNumberCallSite {
      public DoubleDouble(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double((Double)receiver / (Double)arg);
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
               return new Double((Double)receiver / ((Float)arg).doubleValue());
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
               return new Double((Double)receiver / ((Long)arg).doubleValue());
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
               return new Double((Double)receiver / ((Integer)arg).doubleValue());
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }

      public final Object invoke(Object receiver, Object[] args) {
         return new Double((Double)receiver / ((Integer)args[0]).doubleValue());
      }

      public final Object invoke(Object receiver, Object arg) {
         return new Double((Double)receiver / ((Integer)arg).doubleValue());
      }
   }

   private static class FloatDouble extends NumberNumberMetaMethod.NumberNumberCallSite {
      public FloatDouble(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double(((Float)receiver).doubleValue() / (Double)arg);
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
               return new Double(((Float)receiver).doubleValue() / ((Float)arg).doubleValue());
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }

      public final Object invoke(Object receiver, Object[] args) {
         return new Double(((Float)receiver).doubleValue() / ((Float)args[0]).doubleValue());
      }

      public final Object invoke(Object receiver, Object arg) {
         return new Double(((Float)receiver).doubleValue() / ((Float)arg).doubleValue());
      }
   }

   private static class FloatLong extends NumberNumberMetaMethod.NumberNumberCallSite {
      public FloatLong(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
         super(site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]);
      }

      public final Object call(Object receiver, Object arg) throws Throwable {
         try {
            if (this.checkPojoMetaClass()) {
               return new Double(((Float)receiver).doubleValue() / ((Long)arg).doubleValue());
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
               return new Double(((Float)receiver).doubleValue() / ((Integer)arg).doubleValue());
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
               return new Double(((Long)receiver).doubleValue() / (Double)arg);
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
               return new Double(((Long)receiver).doubleValue() / ((Float)arg).doubleValue());
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
               return new Double((double)(Integer)receiver / (Double)arg);
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
               return new Double(((Integer)receiver).doubleValue() / ((Float)arg).doubleValue());
            }
         } catch (ClassCastException var4) {
         }

         return super.call(receiver, arg);
      }
   }
}
