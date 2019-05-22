package org.testng.internal.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.TestNGException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Configuration;
import org.testng.annotations.DataProvider;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Factory;
import org.testng.annotations.IAnnotation;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IExpectedExceptionsAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.IObjectFactoryAnnotation;
import org.testng.annotations.IParametersAnnotation;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.collections.Lists;
import org.testng.internal.Utils;

public class JDK15TagFactory {
   private static final JDK15TagFactory.Default<Class<?>> DEFAULT_CLASS = new JDK15TagFactory.Default<Class<?>>() {
      public boolean isDefault(Class<?> c) {
         return c == Object.class;
      }
   };
   private static final JDK15TagFactory.Default<String> DEFAULT_STRING = new JDK15TagFactory.Default<String>() {
      public boolean isDefault(String s) {
         return Utils.isStringEmpty(s);
      }
   };

   public <A extends IAnnotation> A createTag(Class<?> cls, Annotation a, Class<A> annotationClass, org.testng.IAnnotationTransformer transformer) {
      IAnnotation result = null;
      if (a != null) {
         if (annotationClass == IConfigurationAnnotation.class) {
            result = this.createConfigurationTag(cls, a);
         } else if (annotationClass == IDataProviderAnnotation.class) {
            result = this.createDataProviderTag(a);
         } else if (annotationClass == IExpectedExceptionsAnnotation.class) {
            result = this.createExpectedExceptionsTag(a);
         } else if (annotationClass == IFactoryAnnotation.class) {
            result = this.createFactoryTag(cls, a);
         } else if (annotationClass == IParametersAnnotation.class) {
            result = this.createParametersTag(a);
         } else if (annotationClass == IObjectFactoryAnnotation.class) {
            result = this.createObjectFactoryTag(a);
         } else if (annotationClass == ITestAnnotation.class) {
            result = this.createTestTag(cls, a, transformer);
         } else if (annotationClass == IListeners.class) {
            result = this.createListenersTag(cls, a, transformer);
         } else {
            if (annotationClass != IBeforeSuite.class && annotationClass != IAfterSuite.class && annotationClass != IBeforeTest.class && annotationClass != IAfterTest.class && annotationClass != IBeforeGroups.class && annotationClass != IAfterGroups.class && annotationClass != IBeforeClass.class && annotationClass != IAfterClass.class && annotationClass != IBeforeMethod.class && annotationClass != IAfterMethod.class) {
               throw new TestNGException("Unknown annotation requested:" + annotationClass);
            }

            result = this.maybeCreateNewConfigurationTag(cls, a, annotationClass);
         }
      }

      return (IAnnotation)result;
   }

   private IAnnotation maybeCreateNewConfigurationTag(Class<?> cls, Annotation a, Class<?> annotationClass) {
      IAnnotation result = null;
      if (annotationClass == IBeforeSuite.class) {
         BeforeSuite bs = (BeforeSuite)a;
         result = this.createConfigurationTag(cls, a, true, false, false, false, new String[0], new String[0], false, false, false, false, bs.alwaysRun(), bs.dependsOnGroups(), bs.dependsOnMethods(), bs.description(), bs.enabled(), bs.groups(), bs.inheritGroups(), (String[])null, false, false, bs.timeOut());
      } else if (annotationClass == IAfterSuite.class) {
         AfterSuite bs = (AfterSuite)a;
         result = this.createConfigurationTag(cls, a, false, true, false, false, new String[0], new String[0], false, false, false, false, bs.alwaysRun(), bs.dependsOnGroups(), bs.dependsOnMethods(), bs.description(), bs.enabled(), bs.groups(), bs.inheritGroups(), (String[])null, false, false, bs.timeOut());
      } else if (annotationClass == IBeforeTest.class) {
         BeforeTest bs = (BeforeTest)a;
         result = this.createConfigurationTag(cls, a, false, false, true, false, new String[0], new String[0], false, false, false, false, bs.alwaysRun(), bs.dependsOnGroups(), bs.dependsOnMethods(), bs.description(), bs.enabled(), bs.groups(), bs.inheritGroups(), (String[])null, false, false, bs.timeOut());
      } else if (annotationClass == IAfterTest.class) {
         AfterTest bs = (AfterTest)a;
         result = this.createConfigurationTag(cls, a, false, false, false, true, new String[0], new String[0], false, false, false, false, bs.alwaysRun(), bs.dependsOnGroups(), bs.dependsOnMethods(), bs.description(), bs.enabled(), bs.groups(), bs.inheritGroups(), (String[])null, false, false, bs.timeOut());
      } else {
         String[] groups;
         if (annotationClass == IBeforeGroups.class) {
            BeforeGroups bs = (BeforeGroups)a;
            groups = bs.value().length > 0 ? bs.value() : bs.groups();
            result = this.createConfigurationTag(cls, a, false, false, false, false, groups, new String[0], false, false, false, false, bs.alwaysRun(), bs.dependsOnGroups(), bs.dependsOnMethods(), bs.description(), bs.enabled(), bs.groups(), bs.inheritGroups(), (String[])null, false, false, bs.timeOut());
         } else if (annotationClass == IAfterGroups.class) {
            AfterGroups bs = (AfterGroups)a;
            groups = bs.value().length > 0 ? bs.value() : bs.groups();
            result = this.createConfigurationTag(cls, a, false, false, false, false, new String[0], groups, false, false, false, false, bs.alwaysRun(), bs.dependsOnGroups(), bs.dependsOnMethods(), bs.description(), bs.enabled(), bs.groups(), bs.inheritGroups(), (String[])null, false, false, bs.timeOut());
         } else if (annotationClass == IBeforeClass.class) {
            BeforeClass bs = (BeforeClass)a;
            result = this.createConfigurationTag(cls, a, false, false, false, false, new String[0], new String[0], true, false, false, false, bs.alwaysRun(), bs.dependsOnGroups(), bs.dependsOnMethods(), bs.description(), bs.enabled(), bs.groups(), bs.inheritGroups(), (String[])null, false, false, bs.timeOut());
         } else if (annotationClass == IAfterClass.class) {
            AfterClass bs = (AfterClass)a;
            result = this.createConfigurationTag(cls, a, false, false, false, false, new String[0], new String[0], false, true, false, false, bs.alwaysRun(), bs.dependsOnGroups(), bs.dependsOnMethods(), bs.description(), bs.enabled(), bs.groups(), bs.inheritGroups(), (String[])null, false, false, bs.timeOut());
         } else if (annotationClass == IBeforeMethod.class) {
            BeforeMethod bs = (BeforeMethod)a;
            result = this.createConfigurationTag(cls, a, false, false, false, false, new String[0], new String[0], false, false, true, false, bs.alwaysRun(), bs.dependsOnGroups(), bs.dependsOnMethods(), bs.description(), bs.enabled(), bs.groups(), bs.inheritGroups(), (String[])null, bs.firstTimeOnly(), false, bs.timeOut());
         } else if (annotationClass == IAfterMethod.class) {
            AfterMethod bs = (AfterMethod)a;
            result = this.createConfigurationTag(cls, a, false, false, false, false, new String[0], new String[0], false, false, false, true, bs.alwaysRun(), bs.dependsOnGroups(), bs.dependsOnMethods(), bs.description(), bs.enabled(), bs.groups(), bs.inheritGroups(), (String[])null, false, bs.lastTimeOnly(), bs.timeOut());
         }
      }

      return result;
   }

   private ConfigurationAnnotation createConfigurationTag(Class<?> cls, Annotation a) {
      ConfigurationAnnotation result = new ConfigurationAnnotation();
      Configuration c = (Configuration)a;
      result.setBeforeTestClass(c.beforeTestClass());
      result.setAfterTestClass(c.afterTestClass());
      result.setBeforeTestMethod(c.beforeTestMethod());
      result.setAfterTestMethod(c.afterTestMethod());
      result.setBeforeTest(c.beforeTest());
      result.setAfterTest(c.afterTest());
      result.setBeforeSuite(c.beforeSuite());
      result.setAfterSuite(c.afterSuite());
      result.setBeforeGroups(c.beforeGroups());
      result.setAfterGroups(c.afterGroups());
      result.setParameters(c.parameters());
      result.setEnabled(c.enabled());
      result.setGroups(this.join(c.groups(), this.findInheritedStringArray(cls, Test.class, "groups")));
      result.setDependsOnGroups(c.dependsOnGroups());
      result.setDependsOnMethods(c.dependsOnMethods());
      result.setAlwaysRun(c.alwaysRun());
      result.setInheritGroups(c.inheritGroups());
      result.setDescription(c.description());
      return result;
   }

   private IAnnotation createConfigurationTag(Class<?> cls, Annotation a, boolean beforeSuite, boolean afterSuite, boolean beforeTest, boolean afterTest, String[] beforeGroups, String[] afterGroups, boolean beforeClass, boolean afterClass, boolean beforeMethod, boolean afterMethod, boolean alwaysRun, String[] dependsOnGroups, String[] dependsOnMethods, String description, boolean enabled, String[] groups, boolean inheritGroups, String[] parameters, boolean firstTimeOnly, boolean lastTimeOnly, long timeOut) {
      ConfigurationAnnotation result = new ConfigurationAnnotation();
      result.setFakeConfiguration(true);
      result.setBeforeSuite(beforeSuite);
      result.setAfterSuite(afterSuite);
      result.setBeforeTest(beforeTest);
      result.setAfterTest(afterTest);
      result.setBeforeTestClass(beforeClass);
      result.setAfterTestClass(afterClass);
      result.setBeforeGroups(beforeGroups);
      result.setAfterGroups(afterGroups);
      result.setBeforeTestMethod(beforeMethod);
      result.setAfterTestMethod(afterMethod);
      result.setAlwaysRun(alwaysRun);
      result.setDependsOnGroups(dependsOnGroups);
      result.setDependsOnMethods(dependsOnMethods);
      result.setDescription(description);
      result.setEnabled(enabled);
      result.setGroups(groups);
      result.setInheritGroups(inheritGroups);
      result.setParameters(parameters);
      result.setFirstTimeOnly(firstTimeOnly);
      result.setLastTimeOnly(lastTimeOnly);
      result.setTimeOut(timeOut);
      return result;
   }

   private IAnnotation createDataProviderTag(Annotation a) {
      DataProviderAnnotation result = new DataProviderAnnotation();
      DataProvider c = (DataProvider)a;
      result.setName(c.name());
      result.setParallel(c.parallel());
      return result;
   }

   private IAnnotation createExpectedExceptionsTag(Annotation a) {
      ExpectedExceptionsAnnotation result = new ExpectedExceptionsAnnotation();
      ExpectedExceptions c = (ExpectedExceptions)a;
      result.setValue(c.value());
      return result;
   }

   private IAnnotation createFactoryTag(Class<?> cls, Annotation a) {
      FactoryAnnotation result = new FactoryAnnotation();
      Factory c = (Factory)a;
      result.setParameters(c.parameters());
      result.setDataProvider(c.dataProvider());
      result.setDataProviderClass((Class)this.findInherited(c.dataProviderClass(), cls, Factory.class, "dataProviderClass", DEFAULT_CLASS));
      result.setEnabled(c.enabled());
      return result;
   }

   private IAnnotation createObjectFactoryTag(Annotation a) {
      return new ObjectFactoryAnnotation();
   }

   private IAnnotation createParametersTag(Annotation a) {
      ParametersAnnotation result = new ParametersAnnotation();
      Parameters c = (Parameters)a;
      result.setValue(c.value());
      return result;
   }

   private IAnnotation createListenersTag(Class<?> cls, Annotation a, org.testng.IAnnotationTransformer transformer) {
      ListenersAnnotation result = new ListenersAnnotation();
      Listeners l = (Listeners)a;
      result.setValue(l.value());
      return result;
   }

   private IAnnotation createTestTag(Class<?> cls, Annotation a, org.testng.IAnnotationTransformer transformer) {
      TestAnnotation result = new TestAnnotation();
      Test test = (Test)a;
      result.setEnabled(test.enabled());
      result.setGroups(this.join(test.groups(), this.findInheritedStringArray(cls, Test.class, "groups")));
      result.setParameters(test.parameters());
      result.setDependsOnGroups(this.join(test.dependsOnGroups(), this.findInheritedStringArray(cls, Test.class, "dependsOnGroups")));
      result.setDependsOnMethods(this.join(test.dependsOnMethods(), this.findInheritedStringArray(cls, Test.class, "dependsOnMethods")));
      result.setTimeOut(test.timeOut());
      result.setInvocationTimeOut(test.invocationTimeOut());
      result.setInvocationCount(test.invocationCount());
      result.setThreadPoolSize(test.threadPoolSize());
      result.setSuccessPercentage(test.successPercentage());
      result.setDataProvider(test.dataProvider());
      result.setDataProviderClass((Class)this.findInherited(test.dataProviderClass(), cls, Test.class, "dataProviderClass", DEFAULT_CLASS));
      result.setAlwaysRun(test.alwaysRun());
      result.setDescription((String)this.findInherited(test.description(), cls, Test.class, "description", DEFAULT_STRING));
      result.setExpectedExceptions(test.expectedExceptions());
      result.setExpectedExceptionsMessageRegExp(test.expectedExceptionsMessageRegExp());
      result.setSuiteName(test.suiteName());
      result.setTestName(test.testName());
      result.setSequential(test.sequential());
      result.setSingleThreaded(test.singleThreaded());
      result.setRetryAnalyzer(test.retryAnalyzer());
      result.setSkipFailedInvocations(test.skipFailedInvocations());
      result.setIgnoreMissingDependencies(test.ignoreMissingDependencies());
      result.setPriority(test.priority());
      return result;
   }

   private String[] join(String[] strings, String[] strings2) {
      List<String> result = Lists.newArrayList((Object[])strings);
      Set<String> seen = new HashSet(Lists.newArrayList((Object[])strings));
      String[] arr$ = strings2;
      int len$ = strings2.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String s = arr$[i$];
         if (!seen.contains(s)) {
            result.add(s);
         }
      }

      return (String[])result.toArray(new String[result.size()]);
   }

   private <T> T findInherited(T methodValue, Class<?> cls, Class<? extends Annotation> annotationClass, String methodName, JDK15TagFactory.Default<T> def) {
      if (!def.isDefault(methodValue)) {
         return methodValue;
      } else {
         for(; cls != null && cls != Object.class; cls = cls.getSuperclass()) {
            Annotation annotation = cls.getAnnotation(annotationClass);
            if (annotation != null) {
               T result = this.invokeMethod(annotation, methodName);
               if (!def.isDefault(result)) {
                  return result;
               }
            }
         }

         return null;
      }
   }

   private String[] findInheritedStringArray(Class<?> cls, Class<? extends Annotation> annotationClass, String methodName) {
      if (null == cls) {
         return new String[0];
      } else {
         List result;
         for(result = Lists.newArrayList(); cls != null && cls != Object.class; cls = cls.getSuperclass()) {
            Annotation annotation = cls.getAnnotation(annotationClass);
            if (annotation != null) {
               String[] g = (String[])((String[])this.invokeMethod(annotation, methodName));
               String[] arr$ = g;
               int len$ = g.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  String s = arr$[i$];
                  result.add(s);
               }
            }
         }

         return (String[])result.toArray(new String[result.size()]);
      }
   }

   private Object invokeMethod(Annotation test, String methodName) {
      Object result = null;

      try {
         Method m = test.getClass().getMethod(methodName);
         result = m.invoke(test);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return result;
   }

   private void ppp(String string) {
      System.out.println("[JDK15TagFactory] " + string);
   }

   interface Default<T> {
      boolean isDefault(T var1);
   }
}
