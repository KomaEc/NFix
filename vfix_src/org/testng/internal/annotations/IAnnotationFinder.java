package org.testng.internal.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.ITestNGMethod;
import org.testng.annotations.IAnnotation;

public interface IAnnotationFinder {
   <A extends IAnnotation> A findAnnotation(Class<?> var1, Class<A> var2);

   <A extends IAnnotation> A findAnnotation(Method var1, Class<A> var2);

   <A extends IAnnotation> A findAnnotation(ITestNGMethod var1, Class<A> var2);

   <A extends IAnnotation> A findAnnotation(Constructor<?> var1, Class<A> var2);

   boolean hasTestInstance(Method var1, int var2);

   String[] findOptionalValues(Method var1);

   String[] findOptionalValues(Constructor var1);
}
