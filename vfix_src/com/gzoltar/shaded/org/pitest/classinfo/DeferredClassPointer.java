package com.gzoltar.shaded.org.pitest.classinfo;

import com.gzoltar.shaded.org.pitest.functional.Option;

class DeferredClassPointer implements ClassPointer {
   private final Repository repository;
   private final ClassName name;

   DeferredClassPointer(Repository repository, ClassName name) {
      this.repository = repository;
      this.name = name;
   }

   public Option<ClassInfo> fetch() {
      return this.repository.fetchClass(this.name);
   }
}
