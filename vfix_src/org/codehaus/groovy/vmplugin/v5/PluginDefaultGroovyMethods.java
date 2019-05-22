package org.codehaus.groovy.vmplugin.v5;

import groovy.lang.EmptyRange;
import groovy.lang.IntRange;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.codehaus.groovy.runtime.DefaultGroovyMethodsSupport;
import org.codehaus.groovy.runtime.InvokerHelper;

public class PluginDefaultGroovyMethods extends DefaultGroovyMethodsSupport {
   private static final Object[] NO_ARGS = new Object[0];

   public static Object next(Enum self) {
      Method[] methods = self.getClass().getMethods();

      for(int i = 0; i < methods.length; ++i) {
         Method method = methods[i];
         if (method.getName().equals("next") && method.getParameterTypes().length == 0) {
            return InvokerHelper.invokeMethod(self, "next", NO_ARGS);
         }
      }

      Object[] values = (Object[])((Object[])InvokerHelper.invokeStaticMethod((Class)self.getClass(), "values", NO_ARGS));
      int index = Arrays.asList(values).indexOf(self);
      return values[index < values.length - 1 ? index + 1 : 0];
   }

   public static Object previous(Enum self) {
      Method[] methods = self.getClass().getMethods();

      for(int i = 0; i < methods.length; ++i) {
         Method method = methods[i];
         if (method.getName().equals("previous") && method.getParameterTypes().length == 0) {
            return InvokerHelper.invokeMethod(self, "previous", NO_ARGS);
         }
      }

      Object[] values = (Object[])((Object[])InvokerHelper.invokeStaticMethod((Class)self.getClass(), "values", NO_ARGS));
      int index = Arrays.asList(values).indexOf(self);
      return values[index > 0 ? index - 1 : values.length - 1];
   }

   public static int size(StringBuilder builder) {
      return builder.length();
   }

   public static StringBuilder leftShift(StringBuilder self, Object value) {
      return value instanceof CharSequence ? self.append((CharSequence)value) : self.append(value);
   }

   public static void putAt(StringBuilder self, IntRange range, Object value) {
      DefaultGroovyMethodsSupport.RangeInfo info = subListBorders(self.length(), range);
      self.replace(info.from, info.to, value.toString());
   }

   public static void putAt(StringBuilder self, EmptyRange range, Object value) {
      DefaultGroovyMethodsSupport.RangeInfo info = subListBorders(self.length(), range);
      self.replace(info.from, info.to, value.toString());
   }

   public static String plus(StringBuilder self, String value) {
      return self + value;
   }
}
