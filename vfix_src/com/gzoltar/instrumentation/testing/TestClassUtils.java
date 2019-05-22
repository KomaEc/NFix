package com.gzoltar.instrumentation.testing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public class TestClassUtils {
   public static boolean isTest(Class<?> var0) {
      return isJUnitTest(var0) || isTestNGTest(var0);
   }

   public static boolean isJUnitTest(Class<?> var0) {
      if (Modifier.isAbstract(var0.getModifiers()) || Modifier.isPrivate(var0.getModifiers()) || var0.isAnonymousClass() || var0.isMemberClass() && !Modifier.isStatic(var0.getModifiers())) {
         return false;
      } else {
         Iterator var1;
         boolean var10001;
         try {
            var1 = (new TestClass(var0)).getAnnotatedMethods(Test.class).iterator();
         } catch (IllegalArgumentException var5) {
            var10001 = false;
            return false;
         }

         while(true) {
            try {
               if (!var1.hasNext()) {
                  break;
               }

               FrameworkMethod var2 = (FrameworkMethod)var1.next();
               ArrayList var3 = new ArrayList();
               var2.validatePublicVoidNoArg(false, var3);
               if (var3.isEmpty()) {
                  return true;
               }
            } catch (IllegalArgumentException var4) {
               var10001 = false;
               return false;
            }
         }

         Class var6 = var0;

         while((var6 = var6.getSuperclass()) != null && !var6.getName().equals(Object.class.getCanonicalName())) {
            if (var6.getName().equals(TestCase.class.getCanonicalName())) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean isTestNGTest(Class<?> var0) {
      if (Modifier.isAbstract(var0.getModifiers())) {
         return false;
      } else {
         Method[] var7;
         int var1 = (var7 = var0.getMethods()).length;

         for(int var2 = 0; var2 < var1; ++var2) {
            Annotation[] var3;
            int var4 = (var3 = var7[var2].getAnnotations()).length;

            for(int var5 = 0; var5 < var4; ++var5) {
               if (var3[var5].annotationType().equals(org.testng.annotations.Test.class)) {
                  return true;
               }
            }
         }

         return false;
      }
   }
}
