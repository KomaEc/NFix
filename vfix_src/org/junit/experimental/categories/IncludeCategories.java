package org.junit.experimental.categories;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.runner.manipulation.Filter;

public final class IncludeCategories extends CategoryFilterFactory {
   protected Filter createFilter(List<Class<?>> categories) {
      return new IncludeCategories.IncludesAny(categories);
   }

   private static class IncludesAny extends Categories.CategoryFilter {
      public IncludesAny(List<Class<?>> categories) {
         this((Set)(new HashSet(categories)));
      }

      public IncludesAny(Set<Class<?>> categories) {
         super(true, categories, true, (Set)null);
      }

      public String describe() {
         return "includes " + super.describe();
      }
   }
}
