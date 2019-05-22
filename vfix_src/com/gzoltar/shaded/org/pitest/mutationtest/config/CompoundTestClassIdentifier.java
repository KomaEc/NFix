package com.gzoltar.shaded.org.pitest.mutationtest.config;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.testapi.TestClassIdentifier;

public class CompoundTestClassIdentifier implements TestClassIdentifier {
   private final Iterable<TestClassIdentifier> children;

   public CompoundTestClassIdentifier(Iterable<TestClassIdentifier> children) {
      this.children = children;
   }

   public boolean isATestClass(ClassInfo a) {
      return FCollection.contains(this.children, isATest(a));
   }

   public boolean isIncluded(ClassInfo a) {
      return FCollection.contains(this.children, this.isIncludedClass(a));
   }

   private F<TestClassIdentifier, Boolean> isIncludedClass(final ClassInfo classInfo) {
      return new F<TestClassIdentifier, Boolean>() {
         public Boolean apply(TestClassIdentifier a) {
            return a.isIncluded(classInfo);
         }
      };
   }

   private static F<TestClassIdentifier, Boolean> isATest(final ClassInfo classInfo) {
      return new F<TestClassIdentifier, Boolean>() {
         public Boolean apply(TestClassIdentifier a) {
            return a.isATestClass(classInfo);
         }
      };
   }
}
