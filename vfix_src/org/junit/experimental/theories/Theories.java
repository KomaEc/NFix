package org.junit.experimental.theories;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.experimental.theories.internal.Assignments;
import org.junit.experimental.theories.internal.ParameterizedAssertionError;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

public class Theories extends BlockJUnit4ClassRunner {
   public Theories(Class<?> klass) throws InitializationError {
      super(klass);
   }

   protected void collectInitializationErrors(List<Throwable> errors) {
      super.collectInitializationErrors(errors);
      this.validateDataPointFields(errors);
      this.validateDataPointMethods(errors);
   }

   private void validateDataPointFields(List<Throwable> errors) {
      Field[] fields = this.getTestClass().getJavaClass().getDeclaredFields();
      Field[] arr$ = fields;
      int len$ = fields.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Field field = arr$[i$];
         if (field.getAnnotation(DataPoint.class) != null || field.getAnnotation(DataPoints.class) != null) {
            if (!Modifier.isStatic(field.getModifiers())) {
               errors.add(new Error("DataPoint field " + field.getName() + " must be static"));
            }

            if (!Modifier.isPublic(field.getModifiers())) {
               errors.add(new Error("DataPoint field " + field.getName() + " must be public"));
            }
         }
      }

   }

   private void validateDataPointMethods(List<Throwable> errors) {
      Method[] methods = this.getTestClass().getJavaClass().getDeclaredMethods();
      Method[] arr$ = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Method method = arr$[i$];
         if (method.getAnnotation(DataPoint.class) != null || method.getAnnotation(DataPoints.class) != null) {
            if (!Modifier.isStatic(method.getModifiers())) {
               errors.add(new Error("DataPoint method " + method.getName() + " must be static"));
            }

            if (!Modifier.isPublic(method.getModifiers())) {
               errors.add(new Error("DataPoint method " + method.getName() + " must be public"));
            }
         }
      }

   }

   protected void validateConstructor(List<Throwable> errors) {
      this.validateOnlyOneConstructor(errors);
   }

   protected void validateTestMethods(List<Throwable> errors) {
      Iterator i$ = this.computeTestMethods().iterator();

      while(i$.hasNext()) {
         FrameworkMethod each = (FrameworkMethod)i$.next();
         if (each.getAnnotation(Theory.class) != null) {
            each.validatePublicVoid(false, errors);
            each.validateNoTypeParametersOnArgs(errors);
         } else {
            each.validatePublicVoidNoArg(false, errors);
         }

         Iterator i$ = ParameterSignature.signatures(each.getMethod()).iterator();

         while(i$.hasNext()) {
            ParameterSignature signature = (ParameterSignature)i$.next();
            ParametersSuppliedBy annotation = (ParametersSuppliedBy)signature.findDeepAnnotation(ParametersSuppliedBy.class);
            if (annotation != null) {
               this.validateParameterSupplier(annotation.value(), errors);
            }
         }
      }

   }

   private void validateParameterSupplier(Class<? extends ParameterSupplier> supplierClass, List<Throwable> errors) {
      Constructor<?>[] constructors = supplierClass.getConstructors();
      if (constructors.length != 1) {
         errors.add(new Error("ParameterSupplier " + supplierClass.getName() + " must have only one constructor (either empty or taking only a TestClass)"));
      } else {
         Class<?>[] paramTypes = constructors[0].getParameterTypes();
         if (paramTypes.length != 0 && !paramTypes[0].equals(TestClass.class)) {
            errors.add(new Error("ParameterSupplier " + supplierClass.getName() + " constructor must take either nothing or a single TestClass instance"));
         }
      }

   }

   protected List<FrameworkMethod> computeTestMethods() {
      List<FrameworkMethod> testMethods = new ArrayList(super.computeTestMethods());
      List<FrameworkMethod> theoryMethods = this.getTestClass().getAnnotatedMethods(Theory.class);
      testMethods.removeAll(theoryMethods);
      testMethods.addAll(theoryMethods);
      return testMethods;
   }

   public Statement methodBlock(FrameworkMethod method) {
      return new Theories.TheoryAnchor(method, this.getTestClass());
   }

   public static class TheoryAnchor extends Statement {
      private int successes = 0;
      private final FrameworkMethod testMethod;
      private final TestClass testClass;
      private List<AssumptionViolatedException> fInvalidParameters = new ArrayList();

      public TheoryAnchor(FrameworkMethod testMethod, TestClass testClass) {
         this.testMethod = testMethod;
         this.testClass = testClass;
      }

      private TestClass getTestClass() {
         return this.testClass;
      }

      public void evaluate() throws Throwable {
         this.runWithAssignment(Assignments.allUnassigned(this.testMethod.getMethod(), this.getTestClass()));
         boolean hasTheoryAnnotation = this.testMethod.getAnnotation(Theory.class) != null;
         if (this.successes == 0 && hasTheoryAnnotation) {
            Assert.fail("Never found parameters that satisfied method assumptions.  Violated assumptions: " + this.fInvalidParameters);
         }

      }

      protected void runWithAssignment(Assignments parameterAssignment) throws Throwable {
         if (!parameterAssignment.isComplete()) {
            this.runWithIncompleteAssignment(parameterAssignment);
         } else {
            this.runWithCompleteAssignment(parameterAssignment);
         }

      }

      protected void runWithIncompleteAssignment(Assignments incomplete) throws Throwable {
         Iterator i$ = incomplete.potentialsForNextUnassigned().iterator();

         while(i$.hasNext()) {
            PotentialAssignment source = (PotentialAssignment)i$.next();
            this.runWithAssignment(incomplete.assignNext(source));
         }

      }

      protected void runWithCompleteAssignment(final Assignments complete) throws Throwable {
         (new BlockJUnit4ClassRunner(this.getTestClass().getJavaClass()) {
            protected void collectInitializationErrors(List<Throwable> errors) {
            }

            public Statement methodBlock(FrameworkMethod method) {
               final Statement statement = super.methodBlock(method);
               return new Statement() {
                  public void evaluate() throws Throwable {
                     try {
                        statement.evaluate();
                        TheoryAnchor.this.handleDataPointSuccess();
                     } catch (AssumptionViolatedException var2) {
                        TheoryAnchor.this.handleAssumptionViolation(var2);
                     } catch (Throwable var3) {
                        TheoryAnchor.this.reportParameterizedError(var3, complete.getArgumentStrings(TheoryAnchor.this.nullsOk()));
                     }

                  }
               };
            }

            protected Statement methodInvoker(FrameworkMethod method, Object test) {
               return TheoryAnchor.this.methodCompletesWithParameters(method, complete, test);
            }

            public Object createTest() throws Exception {
               Object[] params = complete.getConstructorArguments();
               if (!TheoryAnchor.this.nullsOk()) {
                  Assume.assumeNotNull(params);
               }

               return this.getTestClass().getOnlyConstructor().newInstance(params);
            }
         }).methodBlock(this.testMethod).evaluate();
      }

      private Statement methodCompletesWithParameters(final FrameworkMethod method, final Assignments complete, final Object freshInstance) {
         return new Statement() {
            public void evaluate() throws Throwable {
               Object[] values = complete.getMethodArguments();
               if (!TheoryAnchor.this.nullsOk()) {
                  Assume.assumeNotNull(values);
               }

               method.invokeExplosively(freshInstance, values);
            }
         };
      }

      protected void handleAssumptionViolation(AssumptionViolatedException e) {
         this.fInvalidParameters.add(e);
      }

      protected void reportParameterizedError(Throwable e, Object... params) throws Throwable {
         if (params.length == 0) {
            throw e;
         } else {
            throw new ParameterizedAssertionError(e, this.testMethod.getName(), params);
         }
      }

      private boolean nullsOk() {
         Theory annotation = (Theory)this.testMethod.getMethod().getAnnotation(Theory.class);
         return annotation == null ? false : annotation.nullsAccepted();
      }

      protected void handleDataPointSuccess() {
         ++this.successes;
      }
   }
}
