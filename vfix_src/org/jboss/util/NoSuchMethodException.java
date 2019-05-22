package org.jboss.util;

import java.lang.reflect.Method;

public class NoSuchMethodException extends java.lang.NoSuchMethodException {
   private static final long serialVersionUID = -3044955578250977290L;

   public NoSuchMethodException(String msg) {
      super(msg);
   }

   public NoSuchMethodException(Method method) {
      super(format(method));
   }

   public NoSuchMethodException(String msg, Method method) {
      super(msg + format(method));
   }

   public NoSuchMethodException() {
   }

   public static String format(Method method) {
      StringBuffer buffer = new StringBuffer();
      buffer.append(method.getName()).append("(");
      Class<?>[] paramTypes = method.getParameterTypes();

      for(int count = 0; count < paramTypes.length; ++count) {
         if (count > 0) {
            buffer.append(",");
         }

         buffer.append(paramTypes[count].getName().substring(paramTypes[count].getName().lastIndexOf(".") + 1));
      }

      buffer.append(")");
      return buffer.toString();
   }
}
