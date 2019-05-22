package com.gzoltar.shaded.org.pitest.junit;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.junit.adapter.AdaptedJUnitTestUnit;
import com.gzoltar.shaded.org.pitest.reflection.IsAnnotatedWith;
import com.gzoltar.shaded.org.pitest.reflection.Reflection;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import com.gzoltar.shaded.org.pitest.testapi.TestUnitFinder;
import com.gzoltar.shaded.org.pitest.util.IsolationUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runners.Parameterized;

public class JUnitCustomRunnerTestUnitFinder implements TestUnitFinder {
   private static final Option<Class> CLASS_RULE = findClassRuleClass();

   public List<TestUnit> findTestUnits(Class<?> clazz) {
      Runner runner = AdaptedJUnitTestUnit.createRunner(clazz);
      if (this.isNotARunnableTest(runner, clazz.getName())) {
         return Collections.emptyList();
      } else {
         return Filterable.class.isAssignableFrom(runner.getClass()) && !this.shouldTreatAsOneUnit(clazz, runner) ? this.splitIntoFilteredUnits(runner.getDescription()) : Collections.singletonList(new AdaptedJUnitTestUnit(clazz, Option.none()));
      }
   }

   private boolean isNotARunnableTest(Runner runner, String className) {
      return runner == null || runner.getClass().isAssignableFrom(ErrorReportingRunner.class) || this.isParameterizedTest(runner) || this.isAJUnitThreeErrorOrWarning(runner) || this.isJUnitThreeSuiteMethodNotForOwnClass(runner, className);
   }

   private boolean isAJUnitThreeErrorOrWarning(Runner runner) {
      return !runner.getDescription().getChildren().isEmpty() && ((Description)runner.getDescription().getChildren().get(0)).getClassName().startsWith("junit.framework.TestSuite");
   }

   private boolean shouldTreatAsOneUnit(Class<?> clazz, Runner runner) {
      Set<Method> methods = Reflection.allMethods(clazz);
      return this.runnerCannotBeSplit(runner) || this.hasAnnotation(methods, BeforeClass.class) || this.hasAnnotation(methods, AfterClass.class) || this.hasClassRuleAnnotations(clazz, methods);
   }

   private boolean hasClassRuleAnnotations(Class<?> clazz, Set<Method> methods) {
      if (CLASS_RULE.hasNone()) {
         return false;
      } else {
         return this.hasAnnotation(methods, (Class)CLASS_RULE.value()) || this.hasAnnotation(Reflection.publicFields(clazz), (Class)CLASS_RULE.value());
      }
   }

   private boolean hasAnnotation(Set<? extends AccessibleObject> methods, Class<? extends Annotation> annotation) {
      return FCollection.contains(methods, IsAnnotatedWith.instance(annotation));
   }

   private boolean isParameterizedTest(Runner runner) {
      return Parameterized.class.isAssignableFrom(runner.getClass());
   }

   private boolean runnerCannotBeSplit(Runner runner) {
      String runnerName = runner.getClass().getName();
      return runnerName.equals("junitparams.JUnitParamsRunner") || runnerName.startsWith("org.spockframework.runtime.Sputnik") || runnerName.startsWith("com.insightfullogic.lambdabehave") || runnerName.startsWith("com.google.gwtmockito.GwtMockitoTestRunner");
   }

   private boolean isJUnitThreeSuiteMethodNotForOwnClass(Runner runner, String className) {
      return runner.getClass().getName().equals("org.junit.internal.runners.SuiteMethod") && !runner.getDescription().getClassName().equals(className);
   }

   private List<TestUnit> splitIntoFilteredUnits(Description description) {
      return FCollection.filter(description.getChildren(), this.isTest()).map(this.descriptionToTestUnit());
   }

   private F<Description, TestUnit> descriptionToTestUnit() {
      return new F<Description, TestUnit>() {
         public TestUnit apply(Description a) {
            return JUnitCustomRunnerTestUnitFinder.this.descriptionToTest(a);
         }
      };
   }

   private F<Description, Boolean> isTest() {
      return new F<Description, Boolean>() {
         public Boolean apply(Description a) {
            return a.isTest();
         }
      };
   }

   private TestUnit descriptionToTest(Description description) {
      Class<?> clazz = description.getTestClass();
      if (clazz == null) {
         clazz = IsolationUtils.convertForClassLoader(IsolationUtils.getContextClassLoader(), description.getClassName());
      }

      return new AdaptedJUnitTestUnit(clazz, Option.some(this.createFilterFor(description)));
   }

   private Filter createFilterFor(Description description) {
      return new DescriptionFilter(description.toString());
   }

   private static Option<Class> findClassRuleClass() {
      try {
         return Option.some(Class.forName("org.junit.ClassRule"));
      } catch (ClassNotFoundException var1) {
         return Option.none();
      }
   }
}
