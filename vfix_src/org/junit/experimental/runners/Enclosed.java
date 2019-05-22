package org.junit.experimental.runners;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.junit.runners.Suite;
import org.junit.runners.model.RunnerBuilder;

public class Enclosed extends Suite {
   public Enclosed(Class<?> klass, RunnerBuilder builder) throws Throwable {
      super(builder, klass, filterAbstractClasses(klass.getClasses()));
   }

   private static Class<?>[] filterAbstractClasses(Class<?>[] classes) {
      List<Class<?>> filteredList = new ArrayList(classes.length);
      Class[] arr$ = classes;
      int len$ = classes.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class<?> clazz = arr$[i$];
         if (!Modifier.isAbstract(clazz.getModifiers())) {
            filteredList.add(clazz);
         }
      }

      return (Class[])filteredList.toArray(new Class[filteredList.size()]);
   }
}
