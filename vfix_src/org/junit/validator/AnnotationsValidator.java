package org.junit.validator;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.junit.runners.model.Annotatable;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public final class AnnotationsValidator implements TestClassValidator {
   private static final List<AnnotationsValidator.AnnotatableValidator<?>> VALIDATORS = Arrays.asList(new AnnotationsValidator.ClassValidator(), new AnnotationsValidator.MethodValidator(), new AnnotationsValidator.FieldValidator());

   public List<Exception> validateTestClass(TestClass testClass) {
      List<Exception> validationErrors = new ArrayList();
      Iterator i$ = VALIDATORS.iterator();

      while(i$.hasNext()) {
         AnnotationsValidator.AnnotatableValidator<?> validator = (AnnotationsValidator.AnnotatableValidator)i$.next();
         List<Exception> additionalErrors = validator.validateTestClass(testClass);
         validationErrors.addAll(additionalErrors);
      }

      return validationErrors;
   }

   private static class FieldValidator extends AnnotationsValidator.AnnotatableValidator<FrameworkField> {
      private FieldValidator() {
         super(null);
      }

      Iterable<FrameworkField> getAnnotatablesForTestClass(TestClass testClass) {
         return testClass.getAnnotatedFields();
      }

      List<Exception> validateAnnotatable(AnnotationValidator validator, FrameworkField field) {
         return validator.validateAnnotatedField(field);
      }

      // $FF: synthetic method
      FieldValidator(Object x0) {
         this();
      }
   }

   private static class MethodValidator extends AnnotationsValidator.AnnotatableValidator<FrameworkMethod> {
      private MethodValidator() {
         super(null);
      }

      Iterable<FrameworkMethod> getAnnotatablesForTestClass(TestClass testClass) {
         return testClass.getAnnotatedMethods();
      }

      List<Exception> validateAnnotatable(AnnotationValidator validator, FrameworkMethod method) {
         return validator.validateAnnotatedMethod(method);
      }

      // $FF: synthetic method
      MethodValidator(Object x0) {
         this();
      }
   }

   private static class ClassValidator extends AnnotationsValidator.AnnotatableValidator<TestClass> {
      private ClassValidator() {
         super(null);
      }

      Iterable<TestClass> getAnnotatablesForTestClass(TestClass testClass) {
         return Collections.singletonList(testClass);
      }

      List<Exception> validateAnnotatable(AnnotationValidator validator, TestClass testClass) {
         return validator.validateAnnotatedClass(testClass);
      }

      // $FF: synthetic method
      ClassValidator(Object x0) {
         this();
      }
   }

   private abstract static class AnnotatableValidator<T extends Annotatable> {
      private static final AnnotationValidatorFactory ANNOTATION_VALIDATOR_FACTORY = new AnnotationValidatorFactory();

      private AnnotatableValidator() {
      }

      abstract Iterable<T> getAnnotatablesForTestClass(TestClass var1);

      abstract List<Exception> validateAnnotatable(AnnotationValidator var1, T var2);

      public List<Exception> validateTestClass(TestClass testClass) {
         List<Exception> validationErrors = new ArrayList();
         Iterator i$ = this.getAnnotatablesForTestClass(testClass).iterator();

         while(i$.hasNext()) {
            T annotatable = (Annotatable)i$.next();
            List<Exception> additionalErrors = this.validateAnnotatable(annotatable);
            validationErrors.addAll(additionalErrors);
         }

         return validationErrors;
      }

      private List<Exception> validateAnnotatable(T annotatable) {
         List<Exception> validationErrors = new ArrayList();
         Annotation[] arr$ = annotatable.getAnnotations();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Annotation annotation = arr$[i$];
            Class<? extends Annotation> annotationType = annotation.annotationType();
            ValidateWith validateWith = (ValidateWith)annotationType.getAnnotation(ValidateWith.class);
            if (validateWith != null) {
               AnnotationValidator annotationValidator = ANNOTATION_VALIDATOR_FACTORY.createAnnotationValidator(validateWith);
               List<Exception> errors = this.validateAnnotatable(annotationValidator, annotatable);
               validationErrors.addAll(errors);
            }
         }

         return validationErrors;
      }

      // $FF: synthetic method
      AnnotatableValidator(Object x0) {
         this();
      }
   }
}
