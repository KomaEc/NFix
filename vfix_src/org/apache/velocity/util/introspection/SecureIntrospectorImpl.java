package org.apache.velocity.util.introspection;

import java.lang.reflect.Method;
import org.apache.velocity.runtime.log.Log;

public class SecureIntrospectorImpl extends Introspector implements SecureIntrospectorControl {
   private String[] badClasses;
   private String[] badPackages;
   // $FF: synthetic field
   static Class class$java$lang$Number;
   // $FF: synthetic field
   static Class class$java$lang$Boolean;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$lang$Class;

   public SecureIntrospectorImpl(String[] badClasses, String[] badPackages, Log log) {
      super(log);
      this.badClasses = badClasses;
      this.badPackages = badPackages;
   }

   public Method getMethod(Class clazz, String methodName, Object[] params) throws IllegalArgumentException {
      if (!this.checkObjectExecutePermission(clazz, methodName)) {
         this.log.warn("Cannot retrieve method " + methodName + " from object of class " + clazz.getName() + " due to security restrictions.");
         return null;
      } else {
         return super.getMethod(clazz, methodName, params);
      }
   }

   public boolean checkObjectExecutePermission(Class clazz, String methodName) {
      if (methodName != null && (methodName.equals("wait") || methodName.equals("notify"))) {
         return false;
      } else if ((class$java$lang$Number == null ? (class$java$lang$Number = class$("java.lang.Number")) : class$java$lang$Number).isAssignableFrom(clazz)) {
         return true;
      } else if ((class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean).isAssignableFrom(clazz)) {
         return true;
      } else if ((class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String).isAssignableFrom(clazz)) {
         return true;
      } else if ((class$java$lang$Class == null ? (class$java$lang$Class = class$("java.lang.Class")) : class$java$lang$Class).isAssignableFrom(clazz) && methodName != null && methodName.equals("getName")) {
         return true;
      } else {
         String className = clazz.getName();
         if (className.startsWith("[L") && className.endsWith(";")) {
            className = className.substring(2, className.length() - 1);
         }

         int dotPos = className.lastIndexOf(46);
         String packageName = dotPos == -1 ? "" : className.substring(0, dotPos);
         int sz = this.badPackages.length;

         int i;
         for(i = 0; i < sz; ++i) {
            if (packageName.equals(this.badPackages[i])) {
               return false;
            }
         }

         sz = this.badClasses.length;

         for(i = 0; i < sz; ++i) {
            if (className.equals(this.badClasses[i])) {
               return false;
            }
         }

         return true;
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
