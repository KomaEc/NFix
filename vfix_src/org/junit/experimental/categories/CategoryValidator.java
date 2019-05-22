package org.junit.experimental.categories;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runners.model.FrameworkMethod;
import org.junit.validator.AnnotationValidator;

public final class CategoryValidator extends AnnotationValidator {
   private static final Set<Class<? extends Annotation>> INCOMPATIBLE_ANNOTATIONS = Collections.unmodifiableSet(new HashSet(Arrays.asList(BeforeClass.class, AfterClass.class, Before.class, After.class)));

   public List<Exception> validateAnnotatedMethod(FrameworkMethod method) {
      List<Exception> errors = new ArrayList();
      Annotation[] annotations = method.getAnnotations();
      Annotation[] arr$ = annotations;
      int len$ = annotations.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Annotation annotation = arr$[i$];
         Iterator i$ = INCOMPATIBLE_ANNOTATIONS.iterator();

         while(i$.hasNext()) {
            Class<?> clazz = (Class)i$.next();
            if (annotation.annotationType().isAssignableFrom(clazz)) {
               this.addErrorMessage(errors, clazz);
            }
         }
      }

      return Collections.unmodifiableList(errors);
   }

   private void addErrorMessage(List<Exception> errors, Class<?> clazz) {
      String message = String.format("@%s can not be combined with @Category", clazz.getSimpleName());
      errors.add(new Exception(message));
   }
}
