package com.gzoltar.shaded.org.pitest.classinfo;

import com.gzoltar.shaded.org.pitest.functional.Option;

class DefaultClassPointer implements ClassPointer {
   private final ClassInfo clazz;

   DefaultClassPointer(ClassInfo clazz) {
      this.clazz = clazz;
   }

   public Option<ClassInfo> fetch() {
      return Option.some(this.clazz);
   }
}
