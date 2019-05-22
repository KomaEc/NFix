package org.junit.runners.parameterized;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Parameterized;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class BlockJUnit4ClassRunnerWithParameters extends BlockJUnit4ClassRunner {
   private final Object[] parameters;
   private final String name;

   public BlockJUnit4ClassRunnerWithParameters(TestWithParameters test) throws InitializationError {
      super(test.getTestClass().getJavaClass());
      this.parameters = test.getParameters().toArray(new Object[test.getParameters().size()]);
      this.name = test.getName();
   }

   public Object createTest() throws Exception {
      return this.fieldsAreAnnotated() ? this.createTestUsingFieldInjection() : this.createTestUsingConstructorInjection();
   }

   private Object createTestUsingConstructorInjection() throws Exception {
      return this.getTestClass().getOnlyConstructor().newInstance(this.parameters);
   }

   private Object createTestUsingFieldInjection() throws Exception {
      List<FrameworkField> annotatedFieldsByParameter = this.getAnnotatedFieldsByParameter();
      if (annotatedFieldsByParameter.size() != this.parameters.length) {
         throw new Exception("Wrong number of parameters and @Parameter fields. @Parameter fields counted: " + annotatedFieldsByParameter.size() + ", available parameters: " + this.parameters.length + ".");
      } else {
         Object testClassInstance = this.getTestClass().getJavaClass().newInstance();
         Iterator i$ = annotatedFieldsByParameter.iterator();

         while(i$.hasNext()) {
            FrameworkField each = (FrameworkField)i$.next();
            Field field = each.getField();
            Parameterized.Parameter annotation = (Parameterized.Parameter)field.getAnnotation(Parameterized.Parameter.class);
            int index = annotation.value();

            try {
               field.set(testClassInstance, this.parameters[index]);
            } catch (IllegalArgumentException var9) {
               throw new Exception(this.getTestClass().getName() + ": Trying to set " + field.getName() + " with the value " + this.parameters[index] + " that is not the right type (" + this.parameters[index].getClass().getSimpleName() + " instead of " + field.getType().getSimpleName() + ").", var9);
            }
         }

         return testClassInstance;
      }
   }

   protected String getName() {
      return this.name;
   }

   protected String testName(FrameworkMethod method) {
      return method.getName() + this.getName();
   }

   protected void validateConstructor(List<Throwable> errors) {
      this.validateOnlyOneConstructor(errors);
      if (this.fieldsAreAnnotated()) {
         this.validateZeroArgConstructor(errors);
      }

   }

   protected void validateFields(List<Throwable> errors) {
      super.validateFields(errors);
      if (this.fieldsAreAnnotated()) {
         List<FrameworkField> annotatedFieldsByParameter = this.getAnnotatedFieldsByParameter();
         int[] usedIndices = new int[annotatedFieldsByParameter.size()];
         Iterator i$ = annotatedFieldsByParameter.iterator();

         while(true) {
            while(i$.hasNext()) {
               FrameworkField each = (FrameworkField)i$.next();
               int index = ((Parameterized.Parameter)each.getField().getAnnotation(Parameterized.Parameter.class)).value();
               if (index >= 0 && index <= annotatedFieldsByParameter.size() - 1) {
                  int var10002 = usedIndices[index]++;
               } else {
                  errors.add(new Exception("Invalid @Parameter value: " + index + ". @Parameter fields counted: " + annotatedFieldsByParameter.size() + ". Please use an index between 0 and " + (annotatedFieldsByParameter.size() - 1) + "."));
               }
            }

            for(int index = 0; index < usedIndices.length; ++index) {
               int numberOfUse = usedIndices[index];
               if (numberOfUse == 0) {
                  errors.add(new Exception("@Parameter(" + index + ") is never used."));
               } else if (numberOfUse > 1) {
                  errors.add(new Exception("@Parameter(" + index + ") is used more than once (" + numberOfUse + ")."));
               }
            }
            break;
         }
      }

   }

   protected Statement classBlock(RunNotifier notifier) {
      return this.childrenInvoker(notifier);
   }

   protected Annotation[] getRunnerAnnotations() {
      return new Annotation[0];
   }

   private List<FrameworkField> getAnnotatedFieldsByParameter() {
      return this.getTestClass().getAnnotatedFields(Parameterized.Parameter.class);
   }

   private boolean fieldsAreAnnotated() {
      return !this.getAnnotatedFieldsByParameter().isEmpty();
   }
}
