package com.gzoltar.shaded.org.pitest.junit;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.testapi.TestClassIdentifier;
import com.gzoltar.shaded.org.pitest.testapi.TestGroupConfig;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.experimental.categories.Category;

public class JUnitTestClassIdentifier implements TestClassIdentifier {
   private final TestGroupConfig config;

   public JUnitTestClassIdentifier(TestGroupConfig config) {
      this.config = config;
   }

   public boolean isATestClass(ClassInfo a) {
      return TestInfo.isWithinATestClass(a);
   }

   public boolean isIncluded(ClassInfo a) {
      return this.isIncludedCategory(a) && !this.isExcludedCategory(a);
   }

   private boolean isIncludedCategory(ClassInfo a) {
      List<String> included = this.config.getIncludedGroups();
      return included.isEmpty() || !Collections.disjoint(included, Arrays.asList(this.getCategories(a)));
   }

   private boolean isExcludedCategory(ClassInfo a) {
      List<String> excluded = this.config.getExcludedGroups();
      return !excluded.isEmpty() && !Collections.disjoint(excluded, Arrays.asList(this.getCategories(a)));
   }

   private String[] getCategories(ClassInfo a) {
      Object[] categoryArray = (Object[])((Object[])a.getClassAnnotationValue(ClassName.fromClass(Category.class)));
      return categoryArray == null ? new String[0] : this.copyArray(categoryArray);
   }

   private String[] copyArray(Object[] original) {
      String[] copy = new String[original.length];
      System.arraycopy(original, 0, copy, 0, original.length);
      return copy;
   }
}
