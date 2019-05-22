package com.gzoltar.shaded.org.pitest.classpath;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.functional.FCollection;

public class ProjectClassPaths {
   private final ClassPath classPath;
   private final ClassFilter classFilter;
   private final PathFilter pathFilter;

   public ProjectClassPaths(ClassPath classPath, ClassFilter classFilter, PathFilter pathFilter) {
      this.classPath = classPath;
      this.classFilter = classFilter;
      this.pathFilter = pathFilter;
   }

   public Iterable<ClassName> code() {
      return FCollection.filter(this.classPath.getComponent(this.pathFilter.getCodeFilter()).findClasses(this.classFilter.getCode()), this.classFilter.getCode()).map(ClassName.stringToClassName());
   }

   public Iterable<ClassName> test() {
      return FCollection.filter(this.classPath.getComponent(this.pathFilter.getTestFilter()).findClasses(this.classFilter.getTest()), this.classFilter.getTest()).map(ClassName.stringToClassName());
   }

   public ClassPath getClassPath() {
      return this.classPath;
   }

   public ClassFilter getFilter() {
      return this.classFilter;
   }
}
