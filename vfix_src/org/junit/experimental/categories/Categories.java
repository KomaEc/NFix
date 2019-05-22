package org.junit.experimental.categories;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class Categories extends Suite {
   public Categories(Class<?> klass, RunnerBuilder builder) throws InitializationError {
      super(klass, builder);

      try {
         Set<Class<?>> included = getIncludedCategory(klass);
         Set<Class<?>> excluded = getExcludedCategory(klass);
         boolean isAnyIncluded = isAnyIncluded(klass);
         boolean isAnyExcluded = isAnyExcluded(klass);
         this.filter(Categories.CategoryFilter.categoryFilter(isAnyIncluded, included, isAnyExcluded, excluded));
      } catch (NoTestsRemainException var7) {
         throw new InitializationError(var7);
      }

      assertNoCategorizedDescendentsOfUncategorizeableParents(this.getDescription());
   }

   private static Set<Class<?>> getIncludedCategory(Class<?> klass) {
      Categories.IncludeCategory annotation = (Categories.IncludeCategory)klass.getAnnotation(Categories.IncludeCategory.class);
      return createSet(annotation == null ? null : annotation.value());
   }

   private static boolean isAnyIncluded(Class<?> klass) {
      Categories.IncludeCategory annotation = (Categories.IncludeCategory)klass.getAnnotation(Categories.IncludeCategory.class);
      return annotation == null || annotation.matchAny();
   }

   private static Set<Class<?>> getExcludedCategory(Class<?> klass) {
      Categories.ExcludeCategory annotation = (Categories.ExcludeCategory)klass.getAnnotation(Categories.ExcludeCategory.class);
      return createSet(annotation == null ? null : annotation.value());
   }

   private static boolean isAnyExcluded(Class<?> klass) {
      Categories.ExcludeCategory annotation = (Categories.ExcludeCategory)klass.getAnnotation(Categories.ExcludeCategory.class);
      return annotation == null || annotation.matchAny();
   }

   private static void assertNoCategorizedDescendentsOfUncategorizeableParents(Description description) throws InitializationError {
      if (!canHaveCategorizedChildren(description)) {
         assertNoDescendantsHaveCategoryAnnotations(description);
      }

      Iterator i$ = description.getChildren().iterator();

      while(i$.hasNext()) {
         Description each = (Description)i$.next();
         assertNoCategorizedDescendentsOfUncategorizeableParents(each);
      }

   }

   private static void assertNoDescendantsHaveCategoryAnnotations(Description description) throws InitializationError {
      Iterator i$ = description.getChildren().iterator();

      while(i$.hasNext()) {
         Description each = (Description)i$.next();
         if (each.getAnnotation(Category.class) != null) {
            throw new InitializationError("Category annotations on Parameterized classes are not supported on individual methods.");
         }

         assertNoDescendantsHaveCategoryAnnotations(each);
      }

   }

   private static boolean canHaveCategorizedChildren(Description description) {
      Iterator i$ = description.getChildren().iterator();

      Description each;
      do {
         if (!i$.hasNext()) {
            return true;
         }

         each = (Description)i$.next();
      } while(each.getTestClass() != null);

      return false;
   }

   private static boolean hasAssignableTo(Set<Class<?>> assigns, Class<?> to) {
      Iterator i$ = assigns.iterator();

      Class from;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         from = (Class)i$.next();
      } while(!to.isAssignableFrom(from));

      return true;
   }

   private static Set<Class<?>> createSet(Class<?>... t) {
      Set<Class<?>> set = new HashSet();
      if (t != null) {
         Collections.addAll(set, t);
      }

      return set;
   }

   public static class CategoryFilter extends Filter {
      private final Set<Class<?>> included;
      private final Set<Class<?>> excluded;
      private final boolean includedAny;
      private final boolean excludedAny;

      public static Categories.CategoryFilter include(boolean matchAny, Class<?>... categories) {
         if (hasNull(categories)) {
            throw new NullPointerException("has null category");
         } else {
            return categoryFilter(matchAny, Categories.createSet(categories), true, (Set)null);
         }
      }

      public static Categories.CategoryFilter include(Class<?> category) {
         return include(true, category);
      }

      public static Categories.CategoryFilter include(Class<?>... categories) {
         return include(true, categories);
      }

      public static Categories.CategoryFilter exclude(boolean matchAny, Class<?>... categories) {
         if (hasNull(categories)) {
            throw new NullPointerException("has null category");
         } else {
            return categoryFilter(true, (Set)null, matchAny, Categories.createSet(categories));
         }
      }

      public static Categories.CategoryFilter exclude(Class<?> category) {
         return exclude(true, category);
      }

      public static Categories.CategoryFilter exclude(Class<?>... categories) {
         return exclude(true, categories);
      }

      public static Categories.CategoryFilter categoryFilter(boolean matchAnyInclusions, Set<Class<?>> inclusions, boolean matchAnyExclusions, Set<Class<?>> exclusions) {
         return new Categories.CategoryFilter(matchAnyInclusions, inclusions, matchAnyExclusions, exclusions);
      }

      protected CategoryFilter(boolean matchAnyIncludes, Set<Class<?>> includes, boolean matchAnyExcludes, Set<Class<?>> excludes) {
         this.includedAny = matchAnyIncludes;
         this.excludedAny = matchAnyExcludes;
         this.included = copyAndRefine(includes);
         this.excluded = copyAndRefine(excludes);
      }

      public String describe() {
         return this.toString();
      }

      public String toString() {
         StringBuilder description = (new StringBuilder("categories ")).append(this.included.isEmpty() ? "[all]" : this.included);
         if (!this.excluded.isEmpty()) {
            description.append(" - ").append(this.excluded);
         }

         return description.toString();
      }

      public boolean shouldRun(Description description) {
         if (this.hasCorrectCategoryAnnotation(description)) {
            return true;
         } else {
            Iterator i$ = description.getChildren().iterator();

            Description each;
            do {
               if (!i$.hasNext()) {
                  return false;
               }

               each = (Description)i$.next();
            } while(!this.shouldRun(each));

            return true;
         }
      }

      private boolean hasCorrectCategoryAnnotation(Description description) {
         Set<Class<?>> childCategories = categories(description);
         if (childCategories.isEmpty()) {
            return this.included.isEmpty();
         } else {
            if (!this.excluded.isEmpty()) {
               if (this.excludedAny) {
                  if (this.matchesAnyParentCategories(childCategories, this.excluded)) {
                     return false;
                  }
               } else if (this.matchesAllParentCategories(childCategories, this.excluded)) {
                  return false;
               }
            }

            if (this.included.isEmpty()) {
               return true;
            } else {
               return this.includedAny ? this.matchesAnyParentCategories(childCategories, this.included) : this.matchesAllParentCategories(childCategories, this.included);
            }
         }
      }

      private boolean matchesAnyParentCategories(Set<Class<?>> childCategories, Set<Class<?>> parentCategories) {
         Iterator i$ = parentCategories.iterator();

         Class parentCategory;
         do {
            if (!i$.hasNext()) {
               return false;
            }

            parentCategory = (Class)i$.next();
         } while(!Categories.hasAssignableTo(childCategories, parentCategory));

         return true;
      }

      private boolean matchesAllParentCategories(Set<Class<?>> childCategories, Set<Class<?>> parentCategories) {
         Iterator i$ = parentCategories.iterator();

         Class parentCategory;
         do {
            if (!i$.hasNext()) {
               return true;
            }

            parentCategory = (Class)i$.next();
         } while(Categories.hasAssignableTo(childCategories, parentCategory));

         return false;
      }

      private static Set<Class<?>> categories(Description description) {
         Set<Class<?>> categories = new HashSet();
         Collections.addAll(categories, directCategories(description));
         Collections.addAll(categories, directCategories(parentDescription(description)));
         return categories;
      }

      private static Description parentDescription(Description description) {
         Class<?> testClass = description.getTestClass();
         return testClass == null ? null : Description.createSuiteDescription(testClass);
      }

      private static Class<?>[] directCategories(Description description) {
         if (description == null) {
            return new Class[0];
         } else {
            Category annotation = (Category)description.getAnnotation(Category.class);
            return annotation == null ? new Class[0] : annotation.value();
         }
      }

      private static Set<Class<?>> copyAndRefine(Set<Class<?>> classes) {
         HashSet<Class<?>> c = new HashSet();
         if (classes != null) {
            c.addAll(classes);
         }

         c.remove((Object)null);
         return c;
      }

      private static boolean hasNull(Class<?>... classes) {
         if (classes == null) {
            return false;
         } else {
            Class[] arr$ = classes;
            int len$ = classes.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Class<?> clazz = arr$[i$];
               if (clazz == null) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   @Retention(RetentionPolicy.RUNTIME)
   public @interface ExcludeCategory {
      Class<?>[] value() default {};

      boolean matchAny() default true;
   }

   @Retention(RetentionPolicy.RUNTIME)
   public @interface IncludeCategory {
      Class<?>[] value() default {};

      boolean matchAny() default true;
   }
}
