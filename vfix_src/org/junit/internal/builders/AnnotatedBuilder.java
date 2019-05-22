package org.junit.internal.builders;

import java.lang.reflect.Modifier;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class AnnotatedBuilder extends RunnerBuilder {
   private static final String CONSTRUCTOR_ERROR_FORMAT = "Custom runner class %s should have a public constructor with signature %s(Class testClass)";
   private final RunnerBuilder suiteBuilder;

   public AnnotatedBuilder(RunnerBuilder suiteBuilder) {
      this.suiteBuilder = suiteBuilder;
   }

   public Runner runnerForClass(Class<?> testClass) throws Exception {
      for(Class currentTestClass = testClass; currentTestClass != null; currentTestClass = this.getEnclosingClassForNonStaticMemberClass(currentTestClass)) {
         RunWith annotation = (RunWith)currentTestClass.getAnnotation(RunWith.class);
         if (annotation != null) {
            return this.buildRunner(annotation.value(), testClass);
         }
      }

      return null;
   }

   private Class<?> getEnclosingClassForNonStaticMemberClass(Class<?> currentTestClass) {
      return currentTestClass.isMemberClass() && !Modifier.isStatic(currentTestClass.getModifiers()) ? currentTestClass.getEnclosingClass() : null;
   }

   public Runner buildRunner(Class<? extends Runner> runnerClass, Class<?> testClass) throws Exception {
      try {
         return (Runner)runnerClass.getConstructor(Class.class).newInstance(testClass);
      } catch (NoSuchMethodException var7) {
         try {
            return (Runner)runnerClass.getConstructor(Class.class, RunnerBuilder.class).newInstance(testClass, this.suiteBuilder);
         } catch (NoSuchMethodException var6) {
            String simpleName = runnerClass.getSimpleName();
            throw new InitializationError(String.format("Custom runner class %s should have a public constructor with signature %s(Class testClass)", simpleName, simpleName));
         }
      }
   }
}
