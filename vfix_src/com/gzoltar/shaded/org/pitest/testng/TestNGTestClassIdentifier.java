package com.gzoltar.shaded.org.pitest.testng;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.testapi.BaseTestClassIdentifier;

public class TestNGTestClassIdentifier extends BaseTestClassIdentifier {
   private static final ClassName ANNOTATION_NAME = new ClassName("org.testng.annotations.Test");

   public boolean isATestClass(ClassInfo a) {
      return a.hasAnnotation(ANNOTATION_NAME) || this.isATestClass(a.getSuperClass());
   }

   private boolean isATestClass(Option<ClassInfo> clazz) {
      return clazz.hasSome() && this.isATestClass((ClassInfo)clazz.value());
   }
}
