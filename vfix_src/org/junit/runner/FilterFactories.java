package org.junit.runner;

import org.junit.internal.Classes;
import org.junit.runner.manipulation.Filter;

class FilterFactories {
   public static Filter createFilterFromFilterSpec(Request request, String filterSpec) throws FilterFactory.FilterNotCreatedException {
      Description topLevelDescription = request.getRunner().getDescription();
      String[] tuple;
      if (filterSpec.contains("=")) {
         tuple = filterSpec.split("=", 2);
      } else {
         tuple = new String[]{filterSpec, ""};
      }

      return createFilter(tuple[0], new FilterFactoryParams(topLevelDescription, tuple[1]));
   }

   public static Filter createFilter(String filterFactoryFqcn, FilterFactoryParams params) throws FilterFactory.FilterNotCreatedException {
      FilterFactory filterFactory = createFilterFactory(filterFactoryFqcn);
      return filterFactory.createFilter(params);
   }

   public static Filter createFilter(Class<? extends FilterFactory> filterFactoryClass, FilterFactoryParams params) throws FilterFactory.FilterNotCreatedException {
      FilterFactory filterFactory = createFilterFactory(filterFactoryClass);
      return filterFactory.createFilter(params);
   }

   static FilterFactory createFilterFactory(String filterFactoryFqcn) throws FilterFactory.FilterNotCreatedException {
      Class filterFactoryClass;
      try {
         filterFactoryClass = Classes.getClass(filterFactoryFqcn).asSubclass(FilterFactory.class);
      } catch (Exception var3) {
         throw new FilterFactory.FilterNotCreatedException(var3);
      }

      return createFilterFactory(filterFactoryClass);
   }

   static FilterFactory createFilterFactory(Class<? extends FilterFactory> filterFactoryClass) throws FilterFactory.FilterNotCreatedException {
      try {
         return (FilterFactory)filterFactoryClass.getConstructor().newInstance();
      } catch (Exception var2) {
         throw new FilterFactory.FilterNotCreatedException(var2);
      }
   }
}
