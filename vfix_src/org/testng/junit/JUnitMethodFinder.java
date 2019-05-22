package org.testng.junit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.ITestMethodFinder;
import org.testng.ITestNGMethod;
import org.testng.collections.Lists;
import org.testng.internal.TestNGMethod;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlTest;

public class JUnitMethodFinder implements ITestMethodFinder {
   private String m_testName = null;
   private IAnnotationFinder m_annotationFinder = null;

   public JUnitMethodFinder(String testName, IAnnotationFinder finder) {
      this.m_testName = testName;
      this.m_annotationFinder = finder;
   }

   private Constructor findConstructor(Class cls, Class[] parameters) {
      Constructor result = null;

      try {
         result = cls.getConstructor(parameters);
      } catch (NoSuchMethodException | SecurityException var5) {
      }

      return result;
   }

   public ITestNGMethod[] getTestMethods(Class cls, XmlTest xmlTest) {
      ITestNGMethod[] result = this.privateFindTestMethods(new INameFilter() {
         public boolean accept(Method method) {
            return method.getName().startsWith("test") && method.getParameterTypes().length == 0;
         }
      }, cls);
      return result;
   }

   private ITestNGMethod[] privateFindTestMethods(INameFilter filter, Class cls) {
      List<ITestNGMethod> vResult = Lists.newArrayList();
      Set<String> acceptedMethodNames = new HashSet();

      for(Class current = cls; current != Object.class; current = current.getSuperclass()) {
         Method[] allMethods = current.getDeclaredMethods();
         Method[] arr$ = allMethods;
         int len$ = allMethods.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Method allMethod = arr$[i$];
            ITestNGMethod m = new TestNGMethod(allMethod, this.m_annotationFinder, (XmlTest)null, (Object)null);
            Method method = m.getMethod();
            String methodName = method.getName();
            if (filter.accept(method) && !acceptedMethodNames.contains(methodName)) {
               vResult.add(m);
               acceptedMethodNames.add(methodName);
            }
         }
      }

      return (ITestNGMethod[])vResult.toArray(new ITestNGMethod[vResult.size()]);
   }

   private static void ppp(String s) {
      System.out.println("[JUnitMethodFinder] " + s);
   }

   private Object instantiate(Class cls) {
      Object result = null;
      Constructor ctor = this.findConstructor(cls, new Class[]{String.class});

      try {
         if (null != ctor) {
            result = ctor.newInstance(this.m_testName);
         } else {
            ctor = cls.getConstructor();
            result = ctor.newInstance();
         }
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | SecurityException | IllegalArgumentException var5) {
         var5.printStackTrace();
      } catch (InstantiationException var6) {
         System.err.println("Couldn't find a constructor with a String parameter on your JUnit test class.");
         var6.printStackTrace();
      }

      return result;
   }

   public ITestNGMethod[] getBeforeTestMethods(Class cls) {
      ITestNGMethod[] result = this.privateFindTestMethods(new INameFilter() {
         public boolean accept(Method method) {
            return "setUp".equals(method.getName());
         }
      }, cls);
      return result;
   }

   public ITestNGMethod[] getAfterTestMethods(Class cls) {
      ITestNGMethod[] result = this.privateFindTestMethods(new INameFilter() {
         public boolean accept(Method method) {
            return "tearDown".equals(method.getName());
         }
      }, cls);
      return result;
   }

   public ITestNGMethod[] getAfterClassMethods(Class cls) {
      return new ITestNGMethod[0];
   }

   public ITestNGMethod[] getBeforeClassMethods(Class cls) {
      return new ITestNGMethod[0];
   }

   public ITestNGMethod[] getBeforeSuiteMethods(Class cls) {
      return new ITestNGMethod[0];
   }

   public ITestNGMethod[] getAfterSuiteMethods(Class cls) {
      return new ITestNGMethod[0];
   }

   public ITestNGMethod[] getBeforeTestConfigurationMethods(Class testClass) {
      return new ITestNGMethod[0];
   }

   public ITestNGMethod[] getAfterTestConfigurationMethods(Class testClass) {
      return new ITestNGMethod[0];
   }

   public ITestNGMethod[] getBeforeGroupsConfigurationMethods(Class testClass) {
      return new ITestNGMethod[0];
   }

   public ITestNGMethod[] getAfterGroupsConfigurationMethods(Class testClass) {
      return new ITestNGMethod[0];
   }
}
