package org.testng.internal.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.annotations.ITestAnnotation;

public class DefaultAnnotationTransformer implements org.testng.IAnnotationTransformer {
   public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
   }
}
