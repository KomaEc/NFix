package org.testng;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IFactoryAnnotation;

public interface IAnnotationTransformer2 extends IAnnotationTransformer {
   void transform(IConfigurationAnnotation var1, Class var2, Constructor var3, Method var4);

   void transform(IDataProviderAnnotation var1, Method var2);

   void transform(IFactoryAnnotation var1, Method var2);
}
