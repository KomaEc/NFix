package org.testng;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.annotations.ITestAnnotation;

public interface IAnnotationTransformer extends ITestNGListener {
   void transform(ITestAnnotation var1, Class var2, Constructor var3, Method var4);
}
