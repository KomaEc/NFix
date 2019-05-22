package org.testng.internal;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.testng.ITestNGMethod;
import org.testng.annotations.IAnnotation;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.ITestAnnotation;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.internal.annotations.AnnotationHelper;
import org.testng.internal.annotations.ConfigurationAnnotation;
import org.testng.internal.annotations.IAfterClass;
import org.testng.internal.annotations.IAfterGroups;
import org.testng.internal.annotations.IAfterMethod;
import org.testng.internal.annotations.IAfterSuite;
import org.testng.internal.annotations.IAfterTest;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.internal.annotations.IBeforeClass;
import org.testng.internal.annotations.IBeforeGroups;
import org.testng.internal.annotations.IBeforeMethod;
import org.testng.internal.annotations.IBeforeSuite;
import org.testng.internal.annotations.IBeforeTest;

public class ConfigurationMethod extends BaseTestMethod {
   private static final long serialVersionUID = -6537771498553619645L;
   private final boolean m_isBeforeSuiteConfiguration;
   private final boolean m_isAfterSuiteConfiguration;
   private final boolean m_isBeforeTestConfiguration;
   private final boolean m_isAfterTestConfiguration;
   private final boolean m_isBeforeClassConfiguration;
   private final boolean m_isAfterClassConfiguration;
   private final boolean m_isBeforeMethodConfiguration;
   private final boolean m_isAfterMethodConfiguration;
   private boolean m_inheritGroupsFromTestClass;

   private ConfigurationMethod(ConstructorOrMethod com, IAnnotationFinder annotationFinder, boolean isBeforeSuite, boolean isAfterSuite, boolean isBeforeTest, boolean isAfterTest, boolean isBeforeClass, boolean isAfterClass, boolean isBeforeMethod, boolean isAfterMethod, String[] beforeGroups, String[] afterGroups, boolean initialize, Object instance) {
      super(com.getName(), com, annotationFinder, instance);
      this.m_inheritGroupsFromTestClass = false;
      if (initialize) {
         this.init();
      }

      this.m_isBeforeSuiteConfiguration = isBeforeSuite;
      this.m_isAfterSuiteConfiguration = isAfterSuite;
      this.m_isBeforeTestConfiguration = isBeforeTest;
      this.m_isAfterTestConfiguration = isAfterTest;
      this.m_isBeforeClassConfiguration = isBeforeClass;
      this.m_isAfterClassConfiguration = isAfterClass;
      this.m_isBeforeMethodConfiguration = isBeforeMethod;
      this.m_isAfterMethodConfiguration = isAfterMethod;
      this.m_beforeGroups = beforeGroups;
      this.m_afterGroups = afterGroups;
   }

   /** @deprecated */
   @Deprecated
   public ConfigurationMethod(Method method, IAnnotationFinder annotationFinder, boolean isBeforeSuite, boolean isAfterSuite, boolean isBeforeTest, boolean isAfterTest, boolean isBeforeClass, boolean isAfterClass, boolean isBeforeMethod, boolean isAfterMethod, String[] beforeGroups, String[] afterGroups, Object instance) {
      this(new ConstructorOrMethod(method), annotationFinder, isBeforeSuite, isAfterSuite, isBeforeTest, isAfterTest, isBeforeClass, isAfterClass, isBeforeMethod, isAfterMethod, beforeGroups, afterGroups, instance);
   }

   public ConfigurationMethod(ConstructorOrMethod com, IAnnotationFinder annotationFinder, boolean isBeforeSuite, boolean isAfterSuite, boolean isBeforeTest, boolean isAfterTest, boolean isBeforeClass, boolean isAfterClass, boolean isBeforeMethod, boolean isAfterMethod, String[] beforeGroups, String[] afterGroups, Object instance) {
      this(com, annotationFinder, isBeforeSuite, isAfterSuite, isBeforeTest, isAfterTest, isBeforeClass, isAfterClass, isBeforeMethod, isAfterMethod, beforeGroups, afterGroups, true, instance);
   }

   private static ITestNGMethod[] createMethods(ITestNGMethod[] methods, IAnnotationFinder finder, boolean isBeforeSuite, boolean isAfterSuite, boolean isBeforeTest, boolean isAfterTest, boolean isBeforeClass, boolean isAfterClass, boolean isBeforeMethod, boolean isAfterMethod, String[] beforeGroups, String[] afterGroups, Object instance) {
      List<ITestNGMethod> result = Lists.newArrayList();
      ITestNGMethod[] arr$ = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod method = arr$[i$];
         result.add(new ConfigurationMethod(method.getConstructorOrMethod(), finder, isBeforeSuite, isAfterSuite, isBeforeTest, isAfterTest, isBeforeClass, isAfterClass, isBeforeMethod, isAfterMethod, new String[0], new String[0], instance));
      }

      return (ITestNGMethod[])result.toArray(new ITestNGMethod[result.size()]);
   }

   public static ITestNGMethod[] createSuiteConfigurationMethods(ITestNGMethod[] methods, IAnnotationFinder annotationFinder, boolean isBefore, Object instance) {
      return createMethods(methods, annotationFinder, isBefore, !isBefore, false, false, false, false, false, false, new String[0], new String[0], instance);
   }

   public static ITestNGMethod[] createTestConfigurationMethods(ITestNGMethod[] methods, IAnnotationFinder annotationFinder, boolean isBefore, Object instance) {
      return createMethods(methods, annotationFinder, false, false, isBefore, !isBefore, false, false, false, false, new String[0], new String[0], instance);
   }

   public static ITestNGMethod[] createClassConfigurationMethods(ITestNGMethod[] methods, IAnnotationFinder annotationFinder, boolean isBefore, Object instance) {
      return createMethods(methods, annotationFinder, false, false, false, false, isBefore, !isBefore, false, false, new String[0], new String[0], instance);
   }

   public static ITestNGMethod[] createBeforeConfigurationMethods(ITestNGMethod[] methods, IAnnotationFinder annotationFinder, boolean isBefore, Object instance) {
      ITestNGMethod[] result = new ITestNGMethod[methods.length];

      for(int i = 0; i < methods.length; ++i) {
         result[i] = new ConfigurationMethod(methods[i].getConstructorOrMethod(), annotationFinder, false, false, false, false, false, false, false, false, isBefore ? methods[i].getBeforeGroups() : new String[0], new String[0], instance);
      }

      return result;
   }

   public static ITestNGMethod[] createAfterConfigurationMethods(ITestNGMethod[] methods, IAnnotationFinder annotationFinder, boolean isBefore, Object instance) {
      ITestNGMethod[] result = new ITestNGMethod[methods.length];

      for(int i = 0; i < methods.length; ++i) {
         result[i] = new ConfigurationMethod(methods[i].getConstructorOrMethod(), annotationFinder, false, false, false, false, false, false, false, false, new String[0], isBefore ? new String[0] : methods[i].getAfterGroups(), instance);
      }

      return result;
   }

   public static ITestNGMethod[] createTestMethodConfigurationMethods(ITestNGMethod[] methods, IAnnotationFinder annotationFinder, boolean isBefore, Object instance) {
      return createMethods(methods, annotationFinder, false, false, false, false, false, false, isBefore, !isBefore, new String[0], new String[0], instance);
   }

   public boolean isAfterClassConfiguration() {
      return this.m_isAfterClassConfiguration;
   }

   public boolean isAfterMethodConfiguration() {
      return this.m_isAfterMethodConfiguration;
   }

   public boolean isBeforeClassConfiguration() {
      return this.m_isBeforeClassConfiguration;
   }

   public boolean isBeforeMethodConfiguration() {
      return this.m_isBeforeMethodConfiguration;
   }

   public boolean isAfterSuiteConfiguration() {
      return this.m_isAfterSuiteConfiguration;
   }

   public boolean isBeforeSuiteConfiguration() {
      return this.m_isBeforeSuiteConfiguration;
   }

   public boolean isBeforeTestConfiguration() {
      return this.m_isBeforeTestConfiguration;
   }

   public boolean isAfterTestConfiguration() {
      return this.m_isAfterTestConfiguration;
   }

   public boolean isBeforeGroupsConfiguration() {
      return this.m_beforeGroups != null && this.m_beforeGroups.length > 0;
   }

   public boolean isAfterGroupsConfiguration() {
      return this.m_afterGroups != null && this.m_afterGroups.length > 0;
   }

   private boolean inheritGroupsFromTestClass() {
      return this.m_inheritGroupsFromTestClass;
   }

   private void init() {
      IAnnotation a = AnnotationHelper.findConfiguration(this.m_annotationFinder, this.m_method.getMethod());
      IConfigurationAnnotation annotation = (IConfigurationAnnotation)a;
      if (a != null) {
         this.m_inheritGroupsFromTestClass = annotation.getInheritGroups();
         this.setEnabled(annotation.getEnabled());
         this.setDescription(annotation.getDescription());
      }

      if (annotation != null && annotation.isFakeConfiguration()) {
         if (annotation.getBeforeSuite()) {
            this.initGroups(IBeforeSuite.class);
         }

         if (annotation.getAfterSuite()) {
            this.initGroups(IAfterSuite.class);
         }

         if (annotation.getBeforeTest()) {
            this.initGroups(IBeforeTest.class);
         }

         if (annotation.getAfterTest()) {
            this.initGroups(IAfterTest.class);
         }

         if (annotation.getBeforeGroups().length != 0) {
            this.initGroups(IBeforeGroups.class);
         }

         if (annotation.getAfterGroups().length != 0) {
            this.initGroups(IAfterGroups.class);
         }

         if (annotation.getBeforeTestClass()) {
            this.initGroups(IBeforeClass.class);
         }

         if (annotation.getAfterTestClass()) {
            this.initGroups(IAfterClass.class);
         }

         if (annotation.getBeforeTestMethod()) {
            this.initGroups(IBeforeMethod.class);
         }

         if (annotation.getAfterTestMethod()) {
            this.initGroups(IAfterMethod.class);
         }
      } else {
         this.initGroups(IConfigurationAnnotation.class);
      }

      if (this.inheritGroupsFromTestClass()) {
         ITestAnnotation classAnnotation = (ITestAnnotation)this.m_annotationFinder.findAnnotation(this.m_methodClass, ITestAnnotation.class);
         if (classAnnotation != null) {
            String[] groups = classAnnotation.getGroups();
            Map<String, String> newGroups = Maps.newHashMap();
            String[] arr$ = this.getGroups();
            int len$ = arr$.length;

            int i$;
            String g;
            for(i$ = 0; i$ < len$; ++i$) {
               g = arr$[i$];
               newGroups.put(g, g);
            }

            if (groups != null) {
               arr$ = groups;
               len$ = groups.length;

               for(i$ = 0; i$ < len$; ++i$) {
                  g = arr$[i$];
                  newGroups.put(g, g);
               }

               this.setGroups((String[])newGroups.values().toArray(new String[newGroups.size()]));
            }
         }
      }

      if (annotation != null) {
         this.setTimeOut(annotation.getTimeOut());
      }

   }

   private static void ppp(String s) {
      System.out.println("[ConfigurationMethod] " + s);
   }

   public ConfigurationMethod clone() {
      ConfigurationMethod clone = new ConfigurationMethod(this.getConstructorOrMethod(), this.getAnnotationFinder(), this.isBeforeSuiteConfiguration(), this.isAfterSuiteConfiguration(), this.isBeforeTestConfiguration(), this.isAfterTestConfiguration(), this.isBeforeClassConfiguration(), this.isAfterClassConfiguration(), this.isBeforeMethodConfiguration(), this.isAfterMethodConfiguration(), this.getBeforeGroups(), this.getAfterGroups(), false, this.getInstance());
      clone.m_testClass = this.getTestClass();
      clone.setDate(this.getDate());
      clone.setGroups(this.getGroups());
      clone.setGroupsDependedUpon(this.getGroupsDependedUpon(), Collections.emptyList());
      clone.setMethodsDependedUpon(this.getMethodsDependedUpon());
      clone.setAlwaysRun(this.isAlwaysRun());
      clone.setMissingGroup(this.getMissingGroup());
      clone.setDescription(this.getDescription());
      clone.setEnabled(this.getEnabled());
      clone.setParameterInvocationCount(this.getParameterInvocationCount());
      clone.m_inheritGroupsFromTestClass = this.inheritGroupsFromTestClass();
      return clone;
   }

   public boolean isFirstTimeOnly() {
      boolean result = false;
      IAnnotation before = this.m_annotationFinder.findAnnotation(this.getMethod(), IBeforeMethod.class);
      if (before != null) {
         result = ((ConfigurationAnnotation)before).isFirstTimeOnly();
      }

      return result;
   }

   public boolean isLastTimeOnly() {
      boolean result = false;
      IAnnotation before = this.m_annotationFinder.findAnnotation(this.getMethod(), IAfterMethod.class);
      if (before != null) {
         result = ((ConfigurationAnnotation)before).isLastTimeOnly();
      }

      return result;
   }
}
