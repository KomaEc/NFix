package com.gzoltar.shaded.org.pitest.classinfo;

import com.gzoltar.shaded.org.pitest.functional.Option;

public class TestToClassMapper {
   private static final int TEST_LENGTH = "Test".length();
   private final Repository repository;

   public TestToClassMapper(Repository repository) {
      this.repository = repository;
   }

   public Option<ClassName> findTestee(String className) {
      ClassName name = new ClassName(className);
      if (name.asJavaName().endsWith("Test") && this.tryName(name.withoutSuffixChars(TEST_LENGTH))) {
         return Option.some(name.withoutSuffixChars(TEST_LENGTH));
      } else {
         return (Option)(name.getNameWithoutPackage().asJavaName().startsWith("Test") && this.tryName(name.withoutPrefixChars(TEST_LENGTH)) ? Option.some(name.withoutPrefixChars(TEST_LENGTH)) : Option.none());
      }
   }

   private boolean tryName(ClassName name) {
      return this.repository.hasClass(name);
   }
}
