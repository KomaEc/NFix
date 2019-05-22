package com.gzoltar.shaded.org.pitest.classinfo;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.Option;

public class NameToClassInfo implements F<ClassName, Option<ClassInfo>> {
   private final ClassInfoSource repository;

   public NameToClassInfo(ClassInfoSource repository) {
      this.repository = repository;
   }

   public Option<ClassInfo> apply(ClassName a) {
      return this.repository.fetchClass(a);
   }
}
