package org.testng.internal.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import org.testng.ITestNGMethod;
import org.testng.annotations.IAnnotation;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IExpectedExceptionsAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.IParametersAnnotation;
import org.testng.annotations.ITestAnnotation;
import org.testng.collections.Maps;
import org.testng.internal.TestNGMethod;
import org.testng.internal.Utils;
import org.testng.xml.XmlTest;

public class AnnotationHelper {
   private static Class[] ALL_ANNOTATIONS = new Class[]{ITestAnnotation.class, IConfigurationAnnotation.class, IBeforeClass.class, IAfterClass.class, IBeforeMethod.class, IAfterMethod.class, IDataProviderAnnotation.class, IExpectedExceptionsAnnotation.class, IFactoryAnnotation.class, IParametersAnnotation.class, IBeforeSuite.class, IAfterSuite.class, IBeforeTest.class, IAfterTest.class, IBeforeGroups.class, IAfterGroups.class};
   public static Class[] CONFIGURATION_CLASSES = new Class[]{IConfigurationAnnotation.class, IBeforeSuite.class, IAfterSuite.class, IBeforeTest.class, IAfterTest.class, IBeforeGroups.class, IAfterGroups.class, IBeforeClass.class, IAfterClass.class, IBeforeMethod.class, IAfterMethod.class};

   public static ITestAnnotation findTest(IAnnotationFinder finder, Class<?> cls) {
      return (ITestAnnotation)finder.findAnnotation(cls, ITestAnnotation.class);
   }

   public static ITestAnnotation findTest(IAnnotationFinder finder, Method m) {
      return (ITestAnnotation)finder.findAnnotation(m, ITestAnnotation.class);
   }

   public static ITestAnnotation findTest(IAnnotationFinder finder, ITestNGMethod m) {
      return (ITestAnnotation)finder.findAnnotation(m, ITestAnnotation.class);
   }

   public static IFactoryAnnotation findFactory(IAnnotationFinder finder, Method m) {
      return (IFactoryAnnotation)finder.findAnnotation(m, IFactoryAnnotation.class);
   }

   public static IFactoryAnnotation findFactory(IAnnotationFinder finder, Constructor c) {
      return (IFactoryAnnotation)finder.findAnnotation(c, IFactoryAnnotation.class);
   }

   public static ITestAnnotation findTest(IAnnotationFinder finder, Constructor ctor) {
      return (ITestAnnotation)finder.findAnnotation(ctor, ITestAnnotation.class);
   }

   public static IConfigurationAnnotation findConfiguration(IAnnotationFinder finder, Constructor ctor) {
      IConfigurationAnnotation result = (IConfigurationAnnotation)finder.findAnnotation(ctor, IConfigurationAnnotation.class);
      if (result == null) {
         IConfigurationAnnotation bs = (IConfigurationAnnotation)finder.findAnnotation(ctor, IBeforeSuite.class);
         IConfigurationAnnotation as = (IConfigurationAnnotation)finder.findAnnotation(ctor, IAfterSuite.class);
         IConfigurationAnnotation bt = (IConfigurationAnnotation)finder.findAnnotation(ctor, IBeforeTest.class);
         IConfigurationAnnotation at = (IConfigurationAnnotation)finder.findAnnotation(ctor, IAfterTest.class);
         IConfigurationAnnotation bg = (IConfigurationAnnotation)finder.findAnnotation(ctor, IBeforeGroups.class);
         IConfigurationAnnotation ag = (IConfigurationAnnotation)finder.findAnnotation(ctor, IAfterGroups.class);
         IConfigurationAnnotation bc = (IConfigurationAnnotation)finder.findAnnotation(ctor, IBeforeClass.class);
         IConfigurationAnnotation ac = (IConfigurationAnnotation)finder.findAnnotation(ctor, IAfterClass.class);
         IConfigurationAnnotation bm = (IConfigurationAnnotation)finder.findAnnotation(ctor, IBeforeMethod.class);
         IConfigurationAnnotation am = (IConfigurationAnnotation)finder.findAnnotation(ctor, IAfterMethod.class);
         if (bs != null || as != null || bt != null || at != null || bg != null || ag != null || bc != null || ac != null || bm != null || am != null) {
            result = createConfiguration(bs, as, bt, at, bg, ag, bc, ac, bm, am);
         }
      }

      return result;
   }

   public static IConfigurationAnnotation findConfiguration(IAnnotationFinder finder, Method m) {
      IConfigurationAnnotation result = (IConfigurationAnnotation)finder.findAnnotation(m, IConfigurationAnnotation.class);
      if (result == null) {
         IConfigurationAnnotation bs = (IConfigurationAnnotation)finder.findAnnotation(m, IBeforeSuite.class);
         IConfigurationAnnotation as = (IConfigurationAnnotation)finder.findAnnotation(m, IAfterSuite.class);
         IConfigurationAnnotation bt = (IConfigurationAnnotation)finder.findAnnotation(m, IBeforeTest.class);
         IConfigurationAnnotation at = (IConfigurationAnnotation)finder.findAnnotation(m, IAfterTest.class);
         IConfigurationAnnotation bg = (IConfigurationAnnotation)finder.findAnnotation(m, IBeforeGroups.class);
         IConfigurationAnnotation ag = (IConfigurationAnnotation)finder.findAnnotation(m, IAfterGroups.class);
         IConfigurationAnnotation bc = (IConfigurationAnnotation)finder.findAnnotation(m, IBeforeClass.class);
         IConfigurationAnnotation ac = (IConfigurationAnnotation)finder.findAnnotation(m, IAfterClass.class);
         IConfigurationAnnotation bm = (IConfigurationAnnotation)finder.findAnnotation(m, IBeforeMethod.class);
         IConfigurationAnnotation am = (IConfigurationAnnotation)finder.findAnnotation(m, IAfterMethod.class);
         if (bs != null || as != null || bt != null || at != null || bg != null || ag != null || bc != null || ac != null || bm != null || am != null) {
            result = createConfiguration(bs, as, bt, at, bg, ag, bc, ac, bm, am);
         }
      }

      return result;
   }

   private static IConfigurationAnnotation createConfiguration(IConfigurationAnnotation bs, IConfigurationAnnotation as, IConfigurationAnnotation bt, IConfigurationAnnotation at, IConfigurationAnnotation bg, IConfigurationAnnotation ag, IConfigurationAnnotation bc, IConfigurationAnnotation ac, IConfigurationAnnotation bm, IConfigurationAnnotation am) {
      ConfigurationAnnotation result = new ConfigurationAnnotation();
      if (bs != null) {
         result.setBeforeSuite(true);
         finishInitialize(result, bs);
      }

      if (as != null) {
         result.setAfterSuite(true);
         finishInitialize(result, as);
      }

      if (bt != null) {
         result.setBeforeTest(true);
         finishInitialize(result, bt);
      }

      if (at != null) {
         result.setAfterTest(true);
         finishInitialize(result, at);
      }

      if (bg != null) {
         result.setBeforeGroups(bg.getBeforeGroups());
         finishInitialize(result, bg);
      }

      if (ag != null) {
         result.setAfterGroups(ag.getAfterGroups());
         finishInitialize(result, ag);
      }

      if (bc != null) {
         result.setBeforeTestClass(true);
         finishInitialize(result, bc);
      }

      if (ac != null) {
         result.setAfterTestClass(true);
         finishInitialize(result, ac);
      }

      if (bm != null) {
         result.setBeforeTestMethod(true);
         finishInitialize(result, bm);
      }

      if (am != null) {
         result.setAfterTestMethod(true);
         finishInitialize(result, am);
      }

      return result;
   }

   private static void finishInitialize(ConfigurationAnnotation result, IConfigurationAnnotation bs) {
      result.setFakeConfiguration(true);
      result.setAlwaysRun(bs.getAlwaysRun());
      result.setDependsOnGroups(bs.getDependsOnGroups());
      result.setDependsOnMethods(bs.getDependsOnMethods());
      result.setDescription(bs.getDescription());
      result.setEnabled(bs.getEnabled());
      result.setGroups(bs.getGroups());
      result.setInheritGroups(bs.getInheritGroups());
      result.setParameters(bs.getParameters());
      result.setTimeOut(bs.getTimeOut());
   }

   public static Class[] getAllAnnotations() {
      return ALL_ANNOTATIONS;
   }

   public static ITestNGMethod[] findMethodsWithAnnotation(Class<?> rootClass, Class<? extends IAnnotation> annotationClass, IAnnotationFinder annotationFinder, XmlTest xmlTest) {
      Map vResult = Maps.newHashMap();

      try {
         vResult = Maps.newHashMap();

         for(Class cls = rootClass; null != cls; cls = cls.getSuperclass()) {
            boolean hasClassAnnotation = isAnnotationPresent(annotationFinder, cls, annotationClass);
            Method[] methods = cls.getDeclaredMethods();
            Method[] arr$ = methods;
            int len$ = methods.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Method m = arr$[i$];
               boolean hasMethodAnnotation = isAnnotationPresent(annotationFinder, m, annotationClass);
               boolean hasTestNGAnnotation = isAnnotationPresent(annotationFinder, m, IFactoryAnnotation.class) || isAnnotationPresent(annotationFinder, m, ITestAnnotation.class) || isAnnotationPresent(annotationFinder, m, CONFIGURATION_CLASSES);
               boolean isPublic = Modifier.isPublic(m.getModifiers());
               boolean isSynthetic = m.isSynthetic();
               if (isPublic && hasClassAnnotation && !isSynthetic && !hasTestNGAnnotation || hasMethodAnnotation) {
                  if (isAnnotationPresent(annotationFinder, m, IConfigurationAnnotation.class) && isAnnotationPresent(annotationFinder, cls, ITestAnnotation.class)) {
                     Utils.log("", 3, "Method " + m + " has a configuration annotation" + " and a class-level @Test. This method will only be kept as a" + " configuration method.");
                  } else if (m.getReturnType() != Void.TYPE && !xmlTest.getAllowReturnValues()) {
                     Utils.log("", 2, "Method " + m + " has a @Test annotation" + " but also a return value:" + " ignoring it. Use <suite allow-return-values=\"true\"> to fix this");
                  } else {
                     String key = createMethodKey(m);
                     if (null == vResult.get(key)) {
                        ITestNGMethod tm = new TestNGMethod(m, annotationFinder, xmlTest, (Object)null);
                        vResult.put(key, tm);
                     }
                  }
               }
            }
         }
      } catch (SecurityException var18) {
         var18.printStackTrace();
      }

      ITestNGMethod[] result = (ITestNGMethod[])vResult.values().toArray(new ITestNGMethod[vResult.size()]);
      return result;
   }

   public static Annotation findAnnotationSuperClasses(Class<?> annotationClass, Class c) {
      while(c != null) {
         Annotation result = c.getAnnotation(annotationClass);
         if (result != null) {
            return result;
         }

         c = c.getSuperclass();
      }

      return null;
   }

   private static boolean isAnnotationPresent(IAnnotationFinder annotationFinder, Method m, Class[] annotationClasses) {
      Class[] arr$ = annotationClasses;
      int len$ = annotationClasses.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class a = arr$[i$];
         if (annotationFinder.findAnnotation(m, a) != null) {
            return true;
         }
      }

      return false;
   }

   private static boolean isAnnotationPresent(IAnnotationFinder annotationFinder, Method m, Class<? extends IAnnotation> annotationClass) {
      return annotationFinder.findAnnotation(m, annotationClass) != null;
   }

   private static boolean isAnnotationPresent(IAnnotationFinder annotationFinder, Class<?> cls, Class<? extends IAnnotation> annotationClass) {
      return annotationFinder.findAnnotation(cls, annotationClass) != null;
   }

   private static String createMethodKey(Method m) {
      StringBuffer result = new StringBuffer(m.getName());
      Class[] arr$ = m.getParameterTypes();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class paramClass = arr$[i$];
         result.append(' ').append(paramClass.toString());
      }

      return result.toString();
   }
}
