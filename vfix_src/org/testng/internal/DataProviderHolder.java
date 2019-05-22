package org.testng.internal;

import java.lang.reflect.Method;
import org.testng.annotations.IDataProviderAnnotation;

public class DataProviderHolder {
   Object instance;
   Method method;
   IDataProviderAnnotation annotation;

   public DataProviderHolder(IDataProviderAnnotation annotation, Method method, Object instance) {
      this.annotation = annotation;
      this.method = method;
      this.instance = instance;
   }
}
