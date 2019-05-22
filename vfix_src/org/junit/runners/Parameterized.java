package org.junit.runners;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.junit.runner.Runner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParametersFactory;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

public class Parameterized extends Suite {
   private static final ParametersRunnerFactory DEFAULT_FACTORY = new BlockJUnit4ClassRunnerWithParametersFactory();
   private static final List<Runner> NO_RUNNERS = Collections.emptyList();
   private final List<Runner> runners;

   public Parameterized(Class<?> klass) throws Throwable {
      super(klass, NO_RUNNERS);
      ParametersRunnerFactory runnerFactory = this.getParametersRunnerFactory(klass);
      Parameterized.Parameters parameters = (Parameterized.Parameters)this.getParametersMethod().getAnnotation(Parameterized.Parameters.class);
      this.runners = Collections.unmodifiableList(this.createRunnersForParameters(this.allParameters(), parameters.name(), runnerFactory));
   }

   private ParametersRunnerFactory getParametersRunnerFactory(Class<?> klass) throws InstantiationException, IllegalAccessException {
      Parameterized.UseParametersRunnerFactory annotation = (Parameterized.UseParametersRunnerFactory)klass.getAnnotation(Parameterized.UseParametersRunnerFactory.class);
      if (annotation == null) {
         return DEFAULT_FACTORY;
      } else {
         Class<? extends ParametersRunnerFactory> factoryClass = annotation.value();
         return (ParametersRunnerFactory)factoryClass.newInstance();
      }
   }

   protected List<Runner> getChildren() {
      return this.runners;
   }

   private TestWithParameters createTestWithNotNormalizedParameters(String pattern, int index, Object parametersOrSingleParameter) {
      Object[] parameters = parametersOrSingleParameter instanceof Object[] ? (Object[])((Object[])parametersOrSingleParameter) : new Object[]{parametersOrSingleParameter};
      return createTestWithParameters(this.getTestClass(), pattern, index, parameters);
   }

   private Iterable<Object> allParameters() throws Throwable {
      Object parameters = this.getParametersMethod().invokeExplosively((Object)null);
      if (parameters instanceof Iterable) {
         return (Iterable)parameters;
      } else if (parameters instanceof Object[]) {
         return Arrays.asList((Object[])((Object[])parameters));
      } else {
         throw this.parametersMethodReturnedWrongType();
      }
   }

   private FrameworkMethod getParametersMethod() throws Exception {
      List<FrameworkMethod> methods = this.getTestClass().getAnnotatedMethods(Parameterized.Parameters.class);
      Iterator i$ = methods.iterator();

      FrameworkMethod each;
      do {
         if (!i$.hasNext()) {
            throw new Exception("No public static parameters method on class " + this.getTestClass().getName());
         }

         each = (FrameworkMethod)i$.next();
      } while(!each.isStatic() || !each.isPublic());

      return each;
   }

   private List<Runner> createRunnersForParameters(Iterable<Object> allParameters, String namePattern, ParametersRunnerFactory runnerFactory) throws InitializationError, Exception {
      try {
         List<TestWithParameters> tests = this.createTestsForParameters(allParameters, namePattern);
         List<Runner> runners = new ArrayList();
         Iterator i$ = tests.iterator();

         while(i$.hasNext()) {
            TestWithParameters test = (TestWithParameters)i$.next();
            runners.add(runnerFactory.createRunnerForTestWithParameters(test));
         }

         return runners;
      } catch (ClassCastException var8) {
         throw this.parametersMethodReturnedWrongType();
      }
   }

   private List<TestWithParameters> createTestsForParameters(Iterable<Object> allParameters, String namePattern) throws Exception {
      int i = 0;
      List<TestWithParameters> children = new ArrayList();
      Iterator i$ = allParameters.iterator();

      while(i$.hasNext()) {
         Object parametersOfSingleTest = i$.next();
         children.add(this.createTestWithNotNormalizedParameters(namePattern, i++, parametersOfSingleTest));
      }

      return children;
   }

   private Exception parametersMethodReturnedWrongType() throws Exception {
      String className = this.getTestClass().getName();
      String methodName = this.getParametersMethod().getName();
      String message = MessageFormat.format("{0}.{1}() must return an Iterable of arrays.", className, methodName);
      return new Exception(message);
   }

   private static TestWithParameters createTestWithParameters(TestClass testClass, String pattern, int index, Object[] parameters) {
      String finalPattern = pattern.replaceAll("\\{index\\}", Integer.toString(index));
      String name = MessageFormat.format(finalPattern, parameters);
      return new TestWithParameters("[" + name + "]", testClass, Arrays.asList(parameters));
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Inherited
   @Target({ElementType.TYPE})
   public @interface UseParametersRunnerFactory {
      Class<? extends ParametersRunnerFactory> value() default BlockJUnit4ClassRunnerWithParametersFactory.class;
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.FIELD})
   public @interface Parameter {
      int value() default 0;
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.METHOD})
   public @interface Parameters {
      String name() default "{index}";
   }
}
