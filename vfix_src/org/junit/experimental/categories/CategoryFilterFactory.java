package org.junit.experimental.categories;

import java.util.ArrayList;
import java.util.List;
import org.junit.internal.Classes;
import org.junit.runner.FilterFactory;
import org.junit.runner.FilterFactoryParams;
import org.junit.runner.manipulation.Filter;

abstract class CategoryFilterFactory implements FilterFactory {
   public Filter createFilter(FilterFactoryParams params) throws FilterFactory.FilterNotCreatedException {
      try {
         return this.createFilter(this.parseCategories(params.getArgs()));
      } catch (ClassNotFoundException var3) {
         throw new FilterFactory.FilterNotCreatedException(var3);
      }
   }

   protected abstract Filter createFilter(List<Class<?>> var1);

   private List<Class<?>> parseCategories(String categories) throws ClassNotFoundException {
      List<Class<?>> categoryClasses = new ArrayList();
      String[] arr$ = categories.split(",");
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String category = arr$[i$];
         Class<?> categoryClass = Classes.getClass(category);
         categoryClasses.add(categoryClass);
      }

      return categoryClasses;
   }
}
