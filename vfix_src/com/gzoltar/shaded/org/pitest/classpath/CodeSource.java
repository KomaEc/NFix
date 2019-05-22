package com.gzoltar.shaded.org.pitest.classpath;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;
import com.gzoltar.shaded.org.pitest.classinfo.ClassInfoSource;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.classinfo.NameToClassInfo;
import com.gzoltar.shaded.org.pitest.classinfo.Repository;
import com.gzoltar.shaded.org.pitest.classinfo.TestToClassMapper;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.testapi.TestClassIdentifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CodeSource implements ClassInfoSource {
   private final ProjectClassPaths classPath;
   private final Repository classRepository;
   private final TestClassIdentifier testIdentifier;

   public CodeSource(ProjectClassPaths classPath, TestClassIdentifier testIdentifier) {
      this(classPath, new Repository(new ClassPathByteArraySource(classPath.getClassPath())), testIdentifier);
   }

   CodeSource(ProjectClassPaths classPath, Repository classRepository, TestClassIdentifier testIdentifier) {
      this.classPath = classPath;
      this.classRepository = classRepository;
      this.testIdentifier = testIdentifier;
   }

   public Collection<ClassInfo> getCode() {
      return FCollection.flatMap(this.classPath.code(), this.nameToClassInfo()).filter(Prelude.not(this.isWithinATestClass()));
   }

   public Set<ClassName> getCodeUnderTestNames() {
      Set<ClassName> codeClasses = new HashSet();
      FCollection.mapTo(this.getCode(), ClassInfo.toClassName(), codeClasses);
      return codeClasses;
   }

   public List<ClassInfo> getTests() {
      return FCollection.flatMap(this.classPath.test(), this.nameToClassInfo()).filter(Prelude.and(this.isWithinATestClass(), this.isIncludedClass(), Prelude.not(ClassInfo.matchIfAbstract())));
   }

   public ClassPath getClassPath() {
      return this.classPath.getClassPath();
   }

   public ProjectClassPaths getProjectPaths() {
      return this.classPath;
   }

   public Option<ClassName> findTestee(String className) {
      TestToClassMapper mapper = new TestToClassMapper(this.classRepository);
      return mapper.findTestee(className);
   }

   public Collection<ClassInfo> getClassInfo(Collection<ClassName> classes) {
      return FCollection.flatMap(classes, this.nameToClassInfo());
   }

   public Option<byte[]> fetchClassBytes(ClassName clazz) {
      return this.classRepository.querySource(clazz);
   }

   public Option<ClassInfo> fetchClass(ClassName clazz) {
      return this.classRepository.fetchClass(clazz);
   }

   private F<ClassName, Option<ClassInfo>> nameToClassInfo() {
      return new NameToClassInfo(this.classRepository);
   }

   private F<ClassInfo, Boolean> isWithinATestClass() {
      return new F<ClassInfo, Boolean>() {
         public Boolean apply(ClassInfo a) {
            return CodeSource.this.testIdentifier.isATestClass(a);
         }
      };
   }

   private F<ClassInfo, Boolean> isIncludedClass() {
      return new F<ClassInfo, Boolean>() {
         public Boolean apply(ClassInfo a) {
            return CodeSource.this.testIdentifier.isIncluded(a);
         }
      };
   }
}
